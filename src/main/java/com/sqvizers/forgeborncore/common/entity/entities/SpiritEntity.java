package com.sqvizers.forgeborncore.common.entity.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class SpiritEntity extends Monster {

    private AttackPhase attackPhase = AttackPhase.HOVER;
    private int timeUntilSwoop = 0;
    private Vec3 hoverPosition = Vec3.ZERO;

    public SpiritEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SpiritMoveControl(this);
        // Allows phasing through blocks completely
        this.noPhysics = true;
    }

    // --- Attributes ---
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 14.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FLYING_SPEED, 0.2D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.FOLLOW_RANGE, 64.0D); // Tracks from very far away
    }

    // --- Sounds ---
    @Override
    protected SoundEvent getAmbientSound() {
        // TODO: Replace with your actual sound registry object (e.g., FBSounds.SPIRIT_WHISPER.get())
        return null;
    }

    // --- Tick Logic ---
    @Override
    public void tick() {
        this.noPhysics = true; // Ensure physics stay off
        this.setNoGravity(true); // Don't fall down
        super.tick();

        // Despawn logic during the day
        if (!this.level().isClientSide && this.isAlive()) {
            if (this.level().isDay() && this.level().canSeeSky(this.blockPosition())) {
                // You can use this.discard() to poof instantly, or this.kill() for death animation
                this.discard();
            }
        }
    }

    // --- AI Goals ---
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SpiritHoverGoal());
        this.goalSelector.addGoal(2, new SpiritSwoopGoal());
        // Head tracking
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 32.0F));

        // Target tracking
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    // --- Enums & AI Classes ---
    enum AttackPhase {
        HOVER,
        SWOOP
    }

    // 1. Hover Goal (Keeps distance, picks a random spot above the player)
    class SpiritHoverGoal extends Goal {

        public SpiritHoverGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return getTarget() != null && attackPhase == AttackPhase.HOVER;
        }

        @Override
        public void start() {
            timeUntilSwoop = 60 + random.nextInt(60); // Wait 3 to 6 seconds before swooping
            updateHoverPosition();
        }

        @Override
        public void tick() {
            if (timeUntilSwoop > 0) {
                timeUntilSwoop--;
            } else {
                attackPhase = AttackPhase.SWOOP;
            }

            LivingEntity target = getTarget();
            if (target != null) {
                // Periodically update the hover spot so it circles/drifts
                if (random.nextInt(40) == 0 || hoverPosition.distanceTo(position()) < 1.0) {
                    updateHoverPosition();
                }
                moveControl.setWantedPosition(hoverPosition.x, hoverPosition.y, hoverPosition.z, 1.0D);
            }
        }

        private void updateHoverPosition() {
            LivingEntity target = getTarget();
            if (target != null) {
                hoverPosition = target.position().add(
                        (random.nextDouble() - 0.5) * 16.0, // X distance
                        5.0 + random.nextDouble() * 5.0,    // Y distance (5 to 10 blocks above)
                        (random.nextDouble() - 0.5) * 16.0  // Z distance
                );
            }
        }
    }

    // 2. Swoop Goal (Dives straight at the player to attack)
    class SpiritSwoopGoal extends Goal {

        public SpiritSwoopGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return getTarget() != null && attackPhase == AttackPhase.SWOOP;
        }

        @Override
        public void tick() {
            LivingEntity target = getTarget();
            if (target != null) {
                // Move aggressively towards player core
                moveControl.setWantedPosition(target.getX(), target.getY() + target.getEyeHeight() / 2.0, target.getZ(),
                        1.5D);

                // If collision boxes touch/overlap, attack!
                if (getBoundingBox().inflate(0.5).intersects(target.getBoundingBox())) {
                    doHurtTarget(target);
                    attackPhase = AttackPhase.HOVER; // Retreat back to hover phase
                    timeUntilSwoop = 60 + random.nextInt(60); // Reset timer
                }
            }
        }
    }

    // 3. Custom Move Control (Allows 3D movement through walls without pathfinding)
    class SpiritMoveControl extends MoveControl {

        public SpiritMoveControl(SpiritEntity spirit) {
            super(spirit);
        }

        @Override
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                Vec3 direction = new Vec3(this.wantedX - mob.getX(), this.wantedY - mob.getY(),
                        this.wantedZ - mob.getZ());
                double distance = direction.length();

                if (distance < 0.1) {
                    mob.setDeltaMovement(mob.getDeltaMovement().scale(0.8)); // Slow down when reached
                    return;
                }

                direction = direction.normalize();
                double speed = this.speedModifier * mob.getAttributeValue(Attributes.FLYING_SPEED);

                // Add smooth acceleration
                mob.setDeltaMovement(mob.getDeltaMovement().add(direction.scale(speed * 0.1)));

                // Face the direction of movement smoothly
                double dX = this.wantedX - mob.getX();
                double dZ = this.wantedZ - mob.getZ();
                mob.setYRot(-((float) Mth.atan2(dX, dZ)) * (180F / (float) Math.PI));
                mob.yBodyRot = mob.getYRot();
            }
        }
    }

    // --- Spawn Rules (Underground & Dark) ---
    public static boolean checkSpiritSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor level,
                                                MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        // Y < 60 ensures it spawns underground
        // Light level must be 0 (pitch black)
        return pos.getY() < 40 && level.getRawBrightness(pos, 0) == 0 &&
                checkMonsterSpawnRules(type, level, spawnType, pos, random);
    }
}
