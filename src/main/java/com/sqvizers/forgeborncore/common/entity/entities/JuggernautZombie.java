package com.sqvizers.forgeborncore.common.entity.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class JuggernautZombie extends Zombie {

    public JuggernautZombie(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    // You will pass this method into your Registrate builder using .attributes()
    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D) // Double the normal health
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D) // 100% knockback resistance
                .add(Attributes.MOVEMENT_SPEED, 0.23D) // Standard zombie speed
                .add(Attributes.ATTACK_DAMAGE, 5.0D); // Slightly higher damage
    }
}