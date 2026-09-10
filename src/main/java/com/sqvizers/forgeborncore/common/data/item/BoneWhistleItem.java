package com.sqvizers.forgeborncore.common.data.item;

import com.sqvizers.forgeborncore.common.entity.FBEntityTypes;
import com.sqvizers.forgeborncore.common.entity.entities.SpiritEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class BoneWhistleItem extends Item {

    public BoneWhistleItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        // Gives it the horn blowing hand animation
        return UseAnim.TOOT_HORN;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        // Duration of the use action in ticks (e.g., 40 ticks = 2 seconds)
        return 40;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Play a horn/mystical sound globally or around the player
        level.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.GOAT_HORN_PLAY, // You can swap this for any custom sound event you registered
                SoundSource.PLAYERS,
                1.0F,
                0.8F);

        // Apply a 5-second cooldown (20 ticks * 5 = 100 ticks)
        player.getCooldowns().addCooldown(this, 100);

        // Server-side logic for spawning spirits
        if (!level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level;

            // Determine how many spirits to spawn (e.g., 2 to 4 spirits)
            int spiritCount = 2 + level.random.nextInt(3);

            for (int i = 0; i < spiritCount; i++) {
                SpiritEntity spirit = FBEntityTypes.SPIRIT.get().create(serverLevel);
                if (spirit != null) {
                    // Random offset around the player (within a 6-block radius)
                    double offsetX = (level.random.nextDouble() - 0.5) * 6.0;
                    double offsetZ = (level.random.nextDouble() - 0.5) * 6.0;
                    BlockPos spawnPos = player.blockPosition().offset((int) offsetX, 0, (int) offsetZ);

                    // Find a safe Y coordinate or spawn near the player's height
                    spirit.moveTo(
                            spawnPos.getX() + 0.5,
                            player.getY(),
                            spawnPos.getZ() + 0.5,
                            level.random.nextFloat() * 360.0F,
                            0.0F);

                    // Spawn the entity into the world
                    serverLevel.addFreshEntity(spirit);
                }
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
