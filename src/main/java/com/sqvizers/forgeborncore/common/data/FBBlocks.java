package com.sqvizers.forgeborncore.common.data;

import com.sqvizers.forgeborncore.common.data.kinetic.KineticInterfaceBlock;
import com.sqvizers.forgeborncore.common.data.kinetic.KineticInterfaceBlockEntity;

import net.minecraft.world.level.block.SoundType;

import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;

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

    public static void init() {}
}
