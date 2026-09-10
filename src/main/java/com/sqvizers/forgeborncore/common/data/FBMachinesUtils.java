package com.sqvizers.forgeborncore.common.data;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.BlockEntityCreationInfo;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.*;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;
import com.gregtechceu.gtceu.common.mui.GTSingleblockMachinePanels;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.network.chat.Component;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import java.util.Locale;
import java.util.function.BiFunction;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.GTValues.V;
import static com.gregtechceu.gtceu.api.multiblock.Predicates.*;
import static com.gregtechceu.gtceu.common.data.machines.GTMachineUtils.workableTiered;
import static com.gregtechceu.gtceu.common.data.models.GTMachineModels.OVERLAY_FLUID_HATCH_HALF_PX_TEX;
import static com.gregtechceu.gtceu.common.data.models.GTMachineModels.OVERLAY_FLUID_HATCH_TEX;
import static com.gregtechceu.gtceu.utils.FormattingUtil.toEnglishName;
import static com.sqvizers.forgeborncore.api.registries.FBRegistration.REGISTRATE;

public class FBMachinesUtils {

    public static MachineDefinition[] registerSimpleGenerator(String name,
                                                              GTRecipeType recipeType,
                                                              Int2IntFunction tankScalingFunction,
                                                              float hazardStrengthPerOperation,
                                                              int... tiers) {
        return FBMachinesUtils.registerTieredMachines(name,
                (holder, tier) -> new SimpleGeneratorMachine(holder, tier, hazardStrengthPerOperation * tier,
                        tankScalingFunction),
                (tier, builder) -> builder
                        .langValue("%s %s Generator %s".formatted(VLVH[tier], toEnglishName(name), VLVT[tier]))
                        // TODO(8.0.0): SimpleGeneratorMachine.EDITABLE_UI_CREATOR + MachineBuilder.editableUI were
                        // removed in the GTCEu UI rewrite (stock GTMachines comments out the same call). Recipe
                        // type/tier wiring below is unaffected; restore the editable XEI UI once MUI2 ports it.
                        // .editableUI(SimpleGeneratorMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id(name), recipeType))
                        .rotationState(RotationState.ALL)
                        .recipeType(recipeType)
                        .ui(GTSingleblockMachinePanels.GENERAL_MACHINE)
                        .recipeModifier(SimpleGeneratorMachine::recipeModifier, true)
                        .addOutputLimit(ItemRecipeCapability.CAP, 0)
                        .addOutputLimit(FluidRecipeCapability.CAP, 0)
                        .simpleGeneratorModel(GTCEu.id("block/generators/" + name))
                        .tooltips(workableTiered(tier, GTValues.V[tier], GTValues.V[tier] * 64, recipeType,
                                tankScalingFunction.applyAsInt(tier), false))
                        .register(),
                tiers);
    }

    public static MachineDefinition[] registerTieredSingleBlockMachines(String name,
                                                                        BiFunction<BlockEntityCreationInfo, Integer, MetaMachine> factory,
                                                                        BiFunction<Integer, MachineBuilder<MachineDefinition, MetaMachine, ?>, MachineDefinition> builder,
                                                                        int... tiers) {
        return registerTieredMachines(name, factory, builder, tiers);
    }

    public static MachineDefinition[] registerTieredMachines(String name,
                                                             BiFunction<BlockEntityCreationInfo, Integer, MetaMachine> factory,
                                                             BiFunction<Integer, MachineBuilder<MachineDefinition, MetaMachine, ?>, MachineDefinition> builder,
                                                             int... tiers) {
        MachineDefinition[] definitions = new MachineDefinition[GTValues.TIER_COUNT];
        for (int tier : tiers) {
            var register = REGISTRATE
                    .machine(GTValues.VN[tier].toLowerCase(Locale.ROOT) + "_" + name,
                            info -> factory.apply(info, tier))
                    .tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }
        return definitions;
    }

    public static Pair<MachineDefinition, MachineDefinition> registerSteamMachines(String name,
                                                                                   BiFunction<BlockEntityCreationInfo, Boolean, MetaMachine> factory,
                                                                                   BiFunction<Boolean, MachineBuilder<MachineDefinition, MetaMachine, ?>, MachineDefinition> builder) {
        MachineDefinition lowTier = builder.apply(false,
                REGISTRATE.machine("lp_%s".formatted(name), info -> factory.apply(info, false))
                        .langValue("I DO NOT EXIST")
                        .tier(0));
        MachineDefinition highTier = builder.apply(true,
                REGISTRATE.machine("hp_%s".formatted(name), info -> factory.apply(info, true))
                        .langValue("High Pressure " + FormattingUtil.toEnglishName(name))
                        .tier(1));
        return Pair.of(lowTier, highTier);
    }

    /** Fluid hatch helper that uses our mod's REGISTRATE (keeps datagen happy). */
    public static MachineDefinition[] registerFluidHatches(String name, String displayName, String tooltip,
                                                           IO io, int initialCapacity, int slots,
                                                           int[] tiers, PartAbility... abilities) {
        final String pipeOverlay;
        if (slots >= 9) {
            pipeOverlay = "overlay_pipe_9x";
        } else if (slots >= 4) {
            pipeOverlay = "overlay_pipe_4x";
        } else {
            pipeOverlay = null;
        }
        final String ioOverlay = io == IO.OUT ? "overlay_pipe_out_emissive" : "overlay_pipe_in_emissive";
        final String emissiveOverlay = slots > 4 ? OVERLAY_FLUID_HATCH_HALF_PX_TEX : OVERLAY_FLUID_HATCH_TEX;

        return registerTieredMachines(
                name,
                (holder, tier) -> new FluidHatchPartMachine(holder, tier, io, initialCapacity, slots),
                (tier, builder) -> builder
                        .langValue(VNF[tier] + ' ' + displayName)
                        .rotationState(RotationState.ALL)
                        .colorOverlayTieredHullModel(ioOverlay, pipeOverlay, emissiveOverlay)
                        .abilities(abilities)
                        .modelProperty(GTMachineModelProperties.IS_FORMED, false)
                        .tooltips(Component.translatable("gtceu.machine." + tooltip + ".tooltip"))
                        .allowCoverOnFront(true)
                        .tooltips(
                                slots == 1 ? Component.translatable(
                                        "gtceu.universal.tooltip.fluid_storage_capacity",
                                        FormattingUtil.formatNumbers(
                                                FluidHatchPartMachine.getTankCapacity(initialCapacity, tier))) :
                                        Component.translatable(
                                                "gtceu.universal.tooltip.fluid_storage_capacity_mult",
                                                slots,
                                                FormattingUtil.formatNumbers(
                                                        FluidHatchPartMachine.getTankCapacity(initialCapacity, tier))))
                        .register(),
                tiers);
    }
}
