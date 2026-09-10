package com.sqvizers.forgeborncore.common.entity.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class BrawlerSkeleton extends Skeleton {

    public BrawlerSkeleton(EntityType<? extends Skeleton> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide && this.getTarget() != null) {
            double distanceSq = this.distanceToSqr(this.getTarget());

            // If target is closer than 5 blocks (25 squared) and we are holding a bow
            if (distanceSq < 25.0D && this.getMainHandItem().is(Items.BOW)) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_AXE));
                this.reassessWeaponGoal(); // Triggers vanilla logic to swap to Melee Attack AI
            }
            // If target moves further than 7 blocks (49 squared) and we are holding an axe
            else if (distanceSq >= 49.0D && this.getMainHandItem().is(Items.STONE_AXE)) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
                this.reassessWeaponGoal(); // Triggers vanilla logic to swap back to Bow AI
            }
        }
    }
}