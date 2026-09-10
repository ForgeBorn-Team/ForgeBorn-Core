package com.sqvizers.forgeborncore.common.data.item.item_properties;

import com.sqvizers.forgeborncore.common.data.item.FBItems;
import com.sqvizers.forgeborncore.common.entity.FBEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class ThrownLoyalAxe extends AbstractArrow {
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownLoyalAxe.class, EntityDataSerializers.BOOLEAN);
    private static final float MAX_DISTANCE_SQUARED = 100.0F; // 10 blocks squared

    private Vec3 spawnPos;
    private boolean isReturning = false;
    private final Set<Integer> piercedEntityIds = new HashSet<>();
    public int clientSideReturnTickCount;

    public ThrownLoyalAxe(EntityType<? extends ThrownLoyalAxe> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownLoyalAxe(Level level, LivingEntity shooter, ItemStack pickupItemStack) {
        super(FBEntityTypes.LOYAL_AXE.get(), shooter, level, pickupItemStack, null);
        this.entityData.set(ID_FOIL, pickupItemStack.hasFoil());
        this.spawnPos = this.position();
    }

    public ThrownLoyalAxe(Level level, double x, double y, double z, ItemStack pickupItemStack) {
        super(FBEntityTypes.LOYAL_AXE.get(), x, y, z, level, pickupItemStack, pickupItemStack);
        this.entityData.set(ID_FOIL, pickupItemStack.hasFoil());
        this.spawnPos = new Vec3(x, y, z);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_FOIL, false);
    }

    @Override
    public void tick() {
        if (this.spawnPos == null) {
            this.spawnPos = this.position();
        }

        // Trigger return if traveled 10 blocks or hit ground
        if (!this.isReturning) {
            if (this.inGround || this.position().distanceToSqr(this.spawnPos) >= MAX_DISTANCE_SQUARED) {
                this.isReturning = true;
                this.inGround = false;
            }
        }

        Entity owner = this.getOwner();
        if (this.isReturning && owner != null) {
            if (!this.isAcceptableReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }
                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 returnDir = owner.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + returnDir.y * 0.015, this.getZ());

                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                this.setDeltaMovement(this.getDeltaMovement().scale(0.85).add(returnDir.normalize().scale(0.75)));
                if (this.clientSideReturnTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }
                ++this.clientSideReturnTickCount;
            }
        }

        super.tick();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();

        // Avoid hitting the same entity twice during piercing
        if (this.piercedEntityIds.contains(entity.getId()) || entity == this.getOwner()) {
            return;
        }

        this.piercedEntityIds.add(entity.getId());
        float damage = 8.0F;
        Entity owner = this.getOwner();
        DamageSource damageSource = this.damageSources().trident(this, owner == null ? this : owner);

        Level level = this.level();
        if (level instanceof ServerLevel serverLevel) {
            damage = EnchantmentHelper.modifyDamage(serverLevel, this.getWeaponItem(), entity, damageSource, damage);
        }

        if (entity.hurt(damageSource, damage)) {
            if (level instanceof ServerLevel serverLevel) {
                EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, entity, damageSource, this.getWeaponItem());
            }
            if (entity instanceof LivingEntity livingEntity) {
                this.doKnockback(livingEntity, damageSource);
                this.doPostHurtEffects(livingEntity);
            }
        }

        this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
        // Motion is NOT reduced/stopped so the axe pierces through
    }

    @Override
    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) {
            // Because pickup is DISALLOWED, we don't give them the item.
            // We just play the catch sound and delete the entity.
            if (!this.level().isClientSide) {
                this.playSound(SoundEvents.TRIDENT_RETURN, 1.0F, 1.0F);
                this.discard(); // Deletes the thrown axe entity instantly
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.isReturning = true; // Turn around immediately on block impact
    }

    private boolean isAcceptableReturnOwner() {
        Entity owner = this.getOwner();
        return owner != null && owner.isAlive() && (!(owner instanceof ServerPlayer) || !owner.isSpectator());
    }

    @Override
    public ItemStack getWeaponItem() {
        return this.getPickupItemStackOrigin();
    }

    @Override
    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || (this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem()));
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(FBItems.LOYAL_AXE.get());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.isReturning = compound.getBoolean("IsReturning");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("IsReturning", this.isReturning);
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
}