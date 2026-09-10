package com.sqvizers.forgeborncore.common.machine.multiblock;

import com.sqvizers.forgeborncore.ForgeBornCore;
import com.sqvizers.forgeborncore.bridge.gregtech.FBRecipeTypes;

import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.multiblock.Predicates;
import com.gregtechceu.gtceu.api.multiblock.pattern.MultiblockPatternBuilder;
import com.gregtechceu.gtceu.api.multiblock.util.RelativeDirection;

import net.minecraft.network.chat.Component;

import com.klikli_dev.occultism.registry.OccultismBlocks;

import static com.gregtechceu.gtceu.api.multiblock.Predicates.*;
import static com.sqvizers.forgeborncore.api.registries.FBRegistration.REGISTRATE;
import static com.sqvizers.forgeborncore.common.data.datagen.FBMachineModels.createSeparateControllerCasingMachineModel;

public class SpiritfireFurnace {

    public static final MultiblockMachineDefinition SPIRITFIRE_FURNACE = REGISTRATE
            .multiblock("spiritfire_furnace",
                    com.sqvizers.forgeborncore.api.machine.multiblock.primitive.SpiritfireFurnace::new)
            .langValue("Spiritfire Furnace")
            .rotationState(RotationState.ALL)
            .recipeType(FBRecipeTypes.SPIRITFIRE_RECIPES)
            .appearanceBlock(OccultismBlocks.POLISHED_OTHERSTONE)
            .tooltips(Component.translatable("forgeborncore.machine.spirit_furnace.tooltip.0"))
            // spotless: off
            .pattern(definition -> MultiblockPatternBuilder
                    .start(RelativeDirection.FRONT, RelativeDirection.UP, RelativeDirection.LEFT)
                    .slice(" XXX ", " XXX ", " XXX ", "     ", "     ")
                    .slice("XXXXX", "XVVVX", "XVVVX", " EXE ", "  X  ")
                    .slice("XXXXX", "XVVVX", "XVVVX", " XVX ", " XVX ")
                    .slice("XXXXX", "XVVVX", "XVVVX", " EXE ", "  X  ")
                    .slice(" XXX ", " XCX ", " XXX ", "     ", "     ")
                    .where('C', controller(blocks(definition.getBlock())))
                    .where('E', blocks(OccultismBlocks.POLISHED_OTHERSTONE_SLAB.get()))
                    .where('X', blocks(OccultismBlocks.POLISHED_OTHERSTONE.get()))
                    .where('V', Predicates.air())
                    .where(' ', any())
                    .build())
            // spotless: on
            .model(createSeparateControllerCasingMachineModel(
                    ForgeBornCore.id("block/otherstone_polished"),
                    ForgeBornCore.id("block/otherstone_polished"),
                    ForgeBornCore.id("block/multiblock/spiritfire_furnace")))
            .register();

    public static void init() {}
}
