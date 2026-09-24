package com.sqvizers.forgeborncore.common.data;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.common.mui.GTSingleblockMachinePanels;
import com.sqvizers.forgeborncore.ForgeBornCore;
import com.sqvizers.forgeborncore.bridge.gregtech.FBRecipeTypes;
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
import static com.gregtechceu.gtceu.common.data.machines.GTMachineUtils.*;
import static com.gregtechceu.gtceu.common.data.models.GTMachineModels.createOverlaySteamHullMachineModel;
import static com.sqvizers.forgeborncore.api.registries.FBRegistration.REGISTRATE;
import static com.sqvizers.forgeborncore.common.data.FBMachinesUtils.registerTieredMachines;

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

    public static final MachineDefinition[] PEELING_CHAMBER = registerTieredMachines("peeling_chamber",
            (holder, tier) -> new SimpleTieredMachine(holder, tier, defaultTankSizeFunction),
            (tier, builder) -> builder
                    .recipeType(FBRecipeTypes.PEELING_CHAMBER)
                    .ui(GTSingleblockMachinePanels.GENERAL_MACHINE)
                    .tooltipBuilder((stack, list) -> {
                        list.add(Component.translatable("forgeborncore.peeling_chamber.desc"));
                    })
                    .tooltips(
                            workableTiered(tier, GTValues.V[tier], GTValues.V[tier] * 64, FBRecipeTypes.PEELING_CHAMBER,
                                    defaultTankSizeFunction.applyAsInt(tier), true))
                    .workableTieredHullModel(ForgeBornCore.id("block/overlay/machine/peeling_chamber"))
                    .register(),
            ELECTRIC_TIERS);

    public static final MachineDefinition[] SORTING_MACHINE = registerTieredMachines("sorting_machine",
            (holder, tier) -> new SimpleTieredMachine(holder, tier, defaultTankSizeFunction),
            (tier, builder) -> builder
                    .recipeType(FBRecipeTypes.SORTING)
                    .ui(GTSingleblockMachinePanels.GENERAL_MACHINE)
                    .tooltipBuilder((stack, list) -> {
                        list.add(Component.translatable("forgeborncore.sorting_machine.desc"));
                    })
                    .tooltips(
                            workableTiered(tier, GTValues.V[tier], GTValues.V[tier] * 64, FBRecipeTypes.SORTING,
                                    defaultTankSizeFunction.applyAsInt(tier), true))
                    .workableTieredHullModel(ForgeBornCore.id("block/overlay/machine/sorting_machine"))
                    .register(),
            ELECTRIC_TIERS);

    public static final MachineDefinition[] MALAXATOR = registerTieredMachines("malaxator",
            (holder, tier) -> new SimpleTieredMachine(holder, tier, defaultTankSizeFunction),
            (tier, builder) -> builder
                    .recipeType(FBRecipeTypes.MALAXATOR)
                    .ui(GTSingleblockMachinePanels.GENERAL_MACHINE)
                    .tooltipBuilder((stack, list) -> {
                        list.add(Component.translatable("forgeborncore.malaxator.desc"));
                    })
                    .tooltips(
                            workableTiered(tier, GTValues.V[tier], GTValues.V[tier] * 64, FBRecipeTypes.MALAXATOR,
                                    defaultTankSizeFunction.applyAsInt(tier), true))
                    .workableTieredHullModel(ForgeBornCore.id("block/overlay/machine/malaxator"))
                    .register(),
            ELECTRIC_TIERS);

    public static final MachineDefinition[] SQUEEZER = registerTieredMachines("squeezer",
            (holder, tier) -> new SimpleTieredMachine(holder, tier, defaultTankSizeFunction),
            (tier, builder) -> builder
                    .recipeType(FBRecipeTypes.SQUEEZER)
                    .ui(GTSingleblockMachinePanels.GENERAL_MACHINE)
                    .tooltipBuilder((stack, list) -> {
                        list.add(Component.translatable("forgeborncore.squeezer.desc"));
                    })
                    .tooltips(
                            workableTiered(tier, GTValues.V[tier], GTValues.V[tier] * 64, FBRecipeTypes.SQUEEZER,
                                    defaultTankSizeFunction.applyAsInt(tier), true))
                    .workableTieredHullModel(ForgeBornCore.id("block/overlay/machine/squeezer"))
                    .register(),
            ELECTRIC_TIERS);

    public static final MachineDefinition[] SLICER = registerTieredMachines("slicer",
            (holder, tier) -> new SimpleTieredMachine(holder, tier, defaultTankSizeFunction),
            (tier, builder) -> builder
                    .recipeType(FBRecipeTypes.SLICER)
                    .ui(GTSingleblockMachinePanels.GENERAL_MACHINE)
                    .tooltipBuilder((stack, list) -> {
                        list.add(Component.translatable("forgeborncore.slicer.desc"));
                    })
                    .tooltips(
                            workableTiered(tier, GTValues.V[tier], GTValues.V[tier] * 64, FBRecipeTypes.SLICER,
                                    defaultTankSizeFunction.applyAsInt(tier), true))
                    .workableTieredHullModel(ForgeBornCore.id("block/overlay/machine/slicer"))
                    .register(),
            ELECTRIC_TIERS);

    public static final MachineDefinition[] LOOM = registerTieredMachines("loom",
            (holder, tier) -> new SimpleTieredMachine(holder, tier, defaultTankSizeFunction),
            (tier, builder) -> builder
                    .recipeType(FBRecipeTypes.LOOM)
                    .ui(GTSingleblockMachinePanels.GENERAL_MACHINE)
                    .tooltipBuilder((stack, list) -> {
                        list.add(Component.translatable("forgeborncore.loom.desc"));
                    })
                    .tooltips(
                            workableTiered(tier, GTValues.V[tier], GTValues.V[tier] * 64, FBRecipeTypes.LOOM,
                                    defaultTankSizeFunction.applyAsInt(tier), true))
                    .workableTieredHullModel(ForgeBornCore.id("block/overlay/machine/loom"))
                    .register(),
            ELECTRIC_TIERS);

    public static final MachineDefinition[] DEHYDRATOR = registerTieredMachines("dehydrator",
            (holder, tier) -> new SimpleTieredMachine(holder, tier, defaultTankSizeFunction),
            (tier, builder) -> builder
                    .recipeType(FBRecipeTypes.DEHYDRATOR)
                    .ui(GTSingleblockMachinePanels.GENERAL_MACHINE)
                    .tooltipBuilder((stack, list) -> {
                        list.add(Component.translatable("forgeborncore.dehydrator.desc"));
                    })
                    .tooltips(
                            workableTiered(tier, GTValues.V[tier], GTValues.V[tier] * 64, FBRecipeTypes.DEHYDRATOR,
                                    defaultTankSizeFunction.applyAsInt(tier), true))
                    .workableTieredHullModel(ForgeBornCore.id("block/overlay/machine/dehydrator"))
                    .register(),
            ELECTRIC_TIERS);

    public static void init() {}
}
