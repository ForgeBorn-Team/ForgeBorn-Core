package com.sqvizers.forgeborncore.common.entity.entities;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SniperSkeleton extends Skeleton {

    public SniperSkeleton(EntityType<? extends Skeleton> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // Runs away if a player gets within 12 blocks. Walks at 1.2x speed, sprints at 1.5x speed.
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 12.0F, 1.2D, 1.5D));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float pullProgress) {
        // 1. Get the bow the skeleton is holding
        ItemStack weapon = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW));

        // 2. Get the arrow based on the weapon
        ItemStack ammo = this.getProjectile(weapon);

        // 3. Pass the weapon as the 4th argument here
        AbstractArrow arrow = ProjectileUtil.getMobArrow(this, ammo, pullProgress, weapon);

        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3D) - arrow.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);

        // The last parameter here is inaccuracy. We hardcode it to 0.0F for sniper precision.
        arrow.shoot(d0, d1 + d3 * 0.2D, d2, 1.6F, 0.0F);

        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(arrow);
    }
}