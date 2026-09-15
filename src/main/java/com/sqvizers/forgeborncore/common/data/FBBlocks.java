package com.sqvizers.forgeborncore.common.data;

import com.sqvizers.forgeborncore.common.data.kinetic.KineticInterfaceBlock;
import com.sqvizers.forgeborncore.common.data.kinetic.KineticInterfaceBlockEntity;

import net.minecraft.world.level.block.SoundType;

import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.sqvizers.forgeborncore.ForgeBornCore;

import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import org.jetbrains.annotations.NotNull;

import static com.sqvizers.forgeborncore.api.registries.FBRegistration.REGISTRATE;

public class FBBlocks {

    static {
        REGISTRATE.creativeModeTab(() -> FBCreativeModeTabs.FORGEBORN_CORE);
    }

    public static final BlockEntry<KineticInterfaceBlock> KINETIC_INTERFACE = REGISTRATE
            .block("kinetic_interface", KineticInterfaceBlock::new)
            .lang("Kinetic Interface")
            .initialProperties(SharedProperties::stone)
            .properties(properties -> properties.sound(SoundType.METAL).forceSolidOff())
            .blockstate((context, provider) -> {})
            .item()
            .model((context, provider) -> {})
            .build()
            .register();

    public static final BlockEntityEntry<KineticInterfaceBlockEntity> KINETIC_INTERFACE_BLOCK_ENTITY = REGISTRATE
            .blockEntity("kinetic_interface", KineticInterfaceBlockEntity::new)
            .validBlock(KINETIC_INTERFACE)
            .renderer(() -> ShaftRenderer::new)
            .register();

    private FBBlocks() {}

    private static @NotNull BlockEntry<Block> registerSimpleBlock(String name, String id, String texture,
                                                                  NonNullBiFunction<Block, Item.Properties, ? extends BlockItem> func) {
        return REGISTRATE
                .block(id, Block::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false)
                        .strength(5.0f, 6.0f)
                        .requiresCorrectToolForDrops())
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(),
                        prov.models().cubeAll(ctx.getName(), ForgeBornCore.id("block/" + texture))))
                .lang(name)
                .item(func)
                .build()
                .register();
    }

    public static final BlockEntry<Block> ALTAR_UNFINISHED = REGISTRATE
            .block("runic_altar_unfinished", Block::new)
            .initialProperties(() -> Blocks.STONE)
            .blockstate((ctx, prov) -> prov.simpleBlock(
                    ctx.get(),
                    prov.models().getExistingFile(
                            prov.modLoc("block/runic_altar_unfinished"))))
            .item()
            .model((ctx, prov) -> prov.withExistingParent(
                    ctx.getName(),
                    prov.modLoc("block/runic_altar_unfinished")))
            .build()
            .register();

    public static final BlockEntry<Block> ALTAR_UNFINISHED_2 = REGISTRATE
            .block("runic_altar_unfinished_2", Block::new)
            .initialProperties(() -> Blocks.STONE)
            .blockstate((ctx, prov) -> prov.simpleBlock(
                    ctx.get(),
                    prov.models().getExistingFile(
                            prov.modLoc("block/runic_altar_unfinished2"))))
            .item()
            .model((ctx, prov) -> prov.withExistingParent(
                    ctx.getName(),
                    prov.modLoc("block/runic_altar_unfinished2")))
            .build()
            .register();

    // Casings GTM
    public static BlockEntry<Block> MACHINE_CASING_KINETIC = registerSimpleBlock(
            "Kinetic Machine Casing", "machine_casing_kinetic",
            "casings/machine_casing_kinetic", BlockItem::new);
    public static BlockEntry<Block> HEAT_AND_PRESSURE_RESISTANT_MACHINE_CASING = registerSimpleBlock(
            "Heat and Pressure Resistant Casing", "heat_and_pressure_resistant_machine_casing",
            "casings/heat_and_pressure_resistant_machine_casing", BlockItem::new);
    public static BlockEntry<Block> ACACIA_MAGNALIUM_CASING = registerSimpleBlock(
            "Acacia-Magnalium Casing", "acacia_magnalium_casing",
            "casings/acacia_magnalium_casing", BlockItem::new);
    public static BlockEntry<Block> SOLID_WROUGHT_IRON_CASING = registerSimpleBlock(
            "Solid Wrought Iron Casing", "solid_wrought_iron_casing",
            "casings/solid_wrought_iron_casing", BlockItem::new);
    public static BlockEntry<Block> SPRUCE_ZINC_CASING = registerSimpleBlock(
            "Spruce-Zinc Casing", "spruce_zinc_casing",
            "casings/spruce_zinc_casing", BlockItem::new);

    // Casings Botania
    public static BlockEntry<Block> MANASTEEL_FIREBOX = registerSimpleBlock(
            "Manasteel Firebox", "manasteel_firebox",
            "casings/manasteel_firebox", BlockItem::new);
    public static BlockEntry<Block> MANASTEEL_LIVINGROCK_CASING = registerSimpleBlock(
            "Manasteel Plated Livingrock Casing", "manasteel_livingrock_casing",
            "casings/manasteel_livingrock_casing", BlockItem::new);
    public static BlockEntry<Block> MANASTEEL_CASING = registerSimpleBlock(
            "Manasteel Casing", "manasteel_casing",
            "casings/manasteel_casing", BlockItem::new);
    public static BlockEntry<Block> TERRASTEEL_FIREBOX = registerSimpleBlock(
            "Terrasteel Firebox", "terrasteel_firebox",
            "casings/terrasteel_firebox", BlockItem::new);
    public static BlockEntry<Block> TERRASTEEL_LIVINGWOOD_CASING = registerSimpleBlock(
            "Terrasteel Plated Livingwood Casing", "terrasteel_livingwood_casing",
            "casings/terrasteel_livingwood_casing", BlockItem::new);
    /*
     * public static BlockEntry<Block> MANASTEEL_LIVINGWOOD_CASING = registerSimpleBlock(
     * "Manasteel Plated Livingwood Casing", "manasteel_livingwood_casing",
     * "casings/manasteel_livingwood_casing", BlockItem::new);
     */

    public static void init() {}
}
