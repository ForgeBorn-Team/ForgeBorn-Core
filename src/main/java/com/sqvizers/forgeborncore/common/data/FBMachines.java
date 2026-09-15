package com.sqvizers.forgeborncore.common.data;

import com.sqvizers.forgeborncore.ForgeBornCore;
import com.sqvizers.forgeborncore.client.renderer.machine.KineticHatchRender;
import com.sqvizers.forgeborncore.common.machine.kinetic.FBPartAbilities;
import com.sqvizers.forgeborncore.common.machine.kinetic.KineticInputHatchMachine;
import com.sqvizers.forgeborncore.common.machine.kinetic.KineticOutputHatchMachine;

import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.config.ConfigHolder;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static com.gregtechceu.gtceu.api.GTValues.ULV;
import static com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties.IS_FORMED;
import static com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties.IS_STEEL_MACHINE;
import static com.gregtechceu.gtceu.common.data.models.GTMachineModels.createOverlaySteamHullMachineModel;
import static com.sqvizers.forgeborncore.api.registries.FBRegistration.REGISTRATE;

public class FBMachines {

    static {
        REGISTRATE.creativeModeTab(() -> FBCreativeModeTabs.FORGEBORN_CORE);
    }

    public static final MachineDefinition KINETIC_INPUT_HATCH = REGISTRATE
            .machine("kinetic_input_hatch", KineticInputHatchMachine::new)
            .tier(ULV)
            .rotationState(RotationState.ALL)
            .langValue("Kinetic Input Hatch")
            .abilities(FBPartAbilities.KINETIC_INPUT)
            .blockProp(BlockBehaviour.Properties::dynamicShape)
            .blockProp(BlockBehaviour.Properties::noOcclusion)
            .modelProperty(IS_FORMED, false)
            .modelProperty(IS_STEEL_MACHINE, ConfigHolder.INSTANCE.machines.steelSteamMultiblocks)
            .model(createOverlaySteamHullMachineModel(
                    ForgeBornCore.id("block/machine/part/kinetic_input_hatch"))
                    .andThen(builder -> builder.addDynamicRenderer(() -> KineticHatchRender.INSTANCE)))
            .hasBER(true)
            .tooltips(Component.translatable("forgeborncore.machine.kinetic_input_hatch.parallel_tooltip"))
            .register();

    public static final MachineDefinition KINETIC_OUTPUT_HATCH = REGISTRATE
            .machine("kinetic_output_hatch", KineticOutputHatchMachine::new)
            .tier(ULV)
            .rotationState(RotationState.ALL)
            .langValue("Kinetic Output Hatch")
            .abilities(FBPartAbilities.KINETIC_OUTPUT)
            .blockProp(BlockBehaviour.Properties::dynamicShape)
            .blockProp(BlockBehaviour.Properties::noOcclusion)
            .modelProperty(IS_FORMED, false)
            .modelProperty(IS_STEEL_MACHINE, ConfigHolder.INSTANCE.machines.steelSteamMultiblocks)
            .model(createOverlaySteamHullMachineModel(
                    ForgeBornCore.id("block/machine/part/kinetic_output_hatch"))
                    .andThen(builder -> builder.addDynamicRenderer(() -> KineticHatchRender.INSTANCE)))
            .hasBER(true)
            .register();

    public static void init() {}
}
