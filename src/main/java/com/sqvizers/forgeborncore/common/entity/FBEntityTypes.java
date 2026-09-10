package com.sqvizers.forgeborncore.common.entity;

import com.sqvizers.forgeborncore.api.registries.FBRegistration;
import com.sqvizers.forgeborncore.client.renderer.entity.SpiritRenderer;
import com.sqvizers.forgeborncore.client.renderer.entity.ThrownLoyalAxeRenderer;
import com.sqvizers.forgeborncore.common.data.item.item_properties.ThrownLoyalAxe;
import com.sqvizers.forgeborncore.common.entity.entities.BrawlerSkeleton;
import com.sqvizers.forgeborncore.common.entity.entities.SpiritEntity;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class FBEntityTypes {

    public static final EntityEntry<SpiritEntity> SPIRIT = FBRegistration.REGISTRATE
            .entity("spirit", SpiritEntity::new, MobCategory.MONSTER)
            .renderer(() -> SpiritRenderer::new)
            .properties(builder -> builder
                    .sized(0.6F, 1.8F)
                    .clientTrackingRange(64)
                    .updateInterval(1))
            .loot((tables, entityType) -> tables.add(entityType, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(Items.BONE))
                    )))
            .register();

    public static final EntityEntry<BrawlerSkeleton> BRAWLER_SKELETON = FBRegistration.REGISTRATE
            .entity("brawler_skeleton", BrawlerSkeleton::new, MobCategory.MONSTER)
            .properties(builder -> builder
                    .sized(0.6F, 1.8F)
                    .clientTrackingRange(64)
                    .updateInterval(1))
            .loot((tables, entityType) -> tables.add(entityType, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(Items.BONE))
                    )))
            .register();

    public static final EntityEntry<ThrownLoyalAxe> LOYAL_AXE = FBRegistration.REGISTRATE
            .<ThrownLoyalAxe>entity("loyal_axe", ThrownLoyalAxe::new, MobCategory.MISC)
            .renderer(() -> ThrownLoyalAxeRenderer::new)
            .properties(builder -> builder
                    .sized(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .clientTrackingRange(64)
                    .updateInterval(1))
            .register();

    public static void init() {}
}