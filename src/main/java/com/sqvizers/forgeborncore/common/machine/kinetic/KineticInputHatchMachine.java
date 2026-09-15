package com.sqvizers.forgeborncore.common.machine.kinetic;

import com.gregtechceu.gtceu.api.blockentity.BlockEntityCreationInfo;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.CreateLang;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class KineticInputHatchMachine extends KineticHatchPartMachine {

    // Stress consumed per RPM
    public static final float STRESS_IMPACT = 32.0f;
    // Stress capacity required per parallel
    public static final float SU_PER_PARALLEL = 1_024.0f;
    // Minimum speed required to operate
    public static final float REQUIRED_RPM = 32.0f;
    // Maximum recipe parallel
    public static final int MAX_PARALLELS = 8;

    public KineticInputHatchMachine(BlockEntityCreationInfo info) {
        super(info, IO.IN);
        subscribeServerTick(this::updateDirectStress);
    }

    public float getSpeed() {
        var kinetic = getKineticBlockEntity();
        return kinetic == null ? 0.0f : kinetic.getSpeed();
    }

    public float getAvailableSU() {
        return Math.abs(getSpeed()) * STRESS_IMPACT;
    }

    public int getAvailableParallels() {
        if (!isWorkingEnabled() || Math.abs(getSpeed()) < REQUIRED_RPM) return 0;
        return Math.min(MAX_PARALLELS, (int) Math.floor(getAvailableSU() / SU_PER_PARALLEL));
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (super.addToGoggleTooltip(tooltip, isPlayerSneaking)) return true;

        var kinetic = getDirectKineticBlockEntity();
        if (kinetic == null) return false;

        CreateLang.translate("gui.goggles.kinetic_stats").forGoggles(tooltip);
        CreateLang.translate("tooltip.stressImpact").style(ChatFormatting.GRAY).forGoggles(tooltip);
        CreateLang.number(STRESS_IMPACT * Math.abs(kinetic.getTheoreticalSpeed()))
                .translate("generic.unit.stress")
                .style(ChatFormatting.AQUA)
                .space()
                .add(CreateLang.translate("gui.goggles.at_current_speed").style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);
        return true;
    }

    @Override
    public void onRotated(Direction oldFacing, Direction newFacing) {
        var oldKinetic = getDirectKineticBlockEntity(oldFacing);
        if (oldKinetic != null) updateDirectStress(oldKinetic, getBlockPos());
        super.onRotated(oldFacing, newFacing);
        updateDirectStress();
    }

    @Override
    public void onUnload() {
        var kinetic = getDirectKineticBlockEntity();
        if (kinetic != null) updateDirectStress(kinetic, getBlockPos());
        super.onUnload();
    }

    private void updateDirectStress() {
        var kinetic = getDirectKineticBlockEntity();
        if (kinetic != null) updateDirectStress(kinetic, null);
    }

    private void updateDirectStress(KineticBlockEntity kinetic, @Nullable BlockPos excludedHatch) {
        var level = getLevel();
        if (level == null || level.isClientSide || !kinetic.hasNetwork()) return;

        int attachedInputs = 0;
        for (Direction direction : Direction.values()) {
            var machine = MetaMachine.getMachine(level, kinetic.getBlockPos().relative(direction));
            if (machine instanceof KineticInputHatchMachine input &&
                    !input.getBlockPos().equals(excludedHatch) && input.getDirectKineticBlockEntity() == kinetic) {
                attachedInputs++;
            }
        }

        float stress = kinetic.calculateStressApplied() + attachedInputs * STRESS_IMPACT;
        kinetic.getOrCreateNetwork().updateStressFor(kinetic, stress);
    }
}
