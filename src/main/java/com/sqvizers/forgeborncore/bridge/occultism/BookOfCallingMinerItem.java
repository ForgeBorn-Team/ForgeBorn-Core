package com.sqvizers.forgeborncore.bridge.occultism;

import com.klikli_dev.occultism.common.item.spirit.BookOfCallingItem;
import com.klikli_dev.occultism.common.item.spirit.calling.ItemMode;
import com.klikli_dev.occultism.common.item.spirit.calling.ItemModes;
import com.klikli_dev.occultism.common.entity.spirit.SpiritEntity;
import com.klikli_dev.occultism.util.ItemNBTUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.Arrays;
import java.util.List;

/** Occultism-style calling book whose target predicate accepts only the custom miner job. */
public class BookOfCallingMinerItem extends BookOfCallingItem {
    public BookOfCallingMinerItem(Item.Properties properties) {
        super(properties, "miner", spirit -> spirit.getJob().orElse(null) instanceof MinerSpiritJob);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target,
                                                  InteractionHand hand) {
        if (target instanceof SpiritEntity spirit
                && spirit.isOwnedBy(player)
                && targetSpirit.test(spirit)
                && !spirit.getUUID().equals(ItemNBTUtil.getSpiritEntityUUID(stack))) {
            if (spirit.level().isClientSide()) return InteractionResult.SUCCESS;

            ItemNBTUtil.setSpiritEntityUUID(stack, spirit.getUUID());
            ItemNBTUtil.setBoundSpiritName(stack, spirit.getName().getString());
            stack.set(DataComponents.RARITY, Rarity.RARE);
            player.swing(hand);
            player.setItemInHand(hand, stack);
            player.inventoryMenu.broadcastChanges();
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }

    @Override
    public List<ItemMode> getItemModes() {
        return Arrays.asList(ItemModes.SET_DEPOSIT, ItemModes.SET_BASE);
    }
}
