package com.sqvizers.forgeborncore.common.data.kinetic;

import com.sqvizers.forgeborncore.common.machine.kinetic.KineticHatchPartMachine;
import com.sqvizers.forgeborncore.common.machine.kinetic.KineticInputHatchMachine;
import com.sqvizers.forgeborncore.common.machine.kinetic.KineticOutputHatchMachine;

import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import org.jetbrains.annotations.Nullable;

public class KineticInterfaceBlockEntity extends GeneratingKineticBlockEntity {

    // Maximum output speed
    public static final float MAX_RPM = 256.0f;

    private float workingSpeed;
    private float workingCapacityPerRPM;

    public KineticInterfaceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public Direction.Axis getRotationAxis() {
        return getBlockState().getValue(RotatedPillarKineticBlock.AXIS);
    }

    public boolean isLinkedTo(KineticHatchPartMachine hatch) {
        return getLinkedHatch() == hatch;
    }

    public void setOutputSU(float su, float capacityPerRPM) {
        if (!Float.isFinite(su) || !Float.isFinite(capacityPerRPM) || su <= 0.0f || capacityPerRPM <= 0.0f ||
                !(getLinkedHatch() instanceof KineticOutputHatchMachine)) {
            stopOutput();
            return;
        }

        workingCapacityPerRPM = capacityPerRPM;
        workingSpeed = Math.min(MAX_RPM, su / capacityPerRPM);
        updateGeneratedRotation();
        setChanged();
    }

    public void stopOutput() {
        if (workingSpeed == 0.0f && workingCapacityPerRPM == 0.0f) return;

        workingSpeed = 0.0f;
        workingCapacityPerRPM = 0.0f;
        updateGeneratedRotation();
        setChanged();
    }

    @Override
    public float getGeneratedSpeed() {
        return getLinkedHatch() instanceof KineticOutputHatchMachine ? workingSpeed : 0.0f;
    }

    @Override
    public float calculateStressApplied() {
        float impact = getLinkedHatch() instanceof KineticInputHatchMachine ?
                KineticInputHatchMachine.STRESS_IMPACT : 0.0f;
        lastStressApplied = impact;
        return impact;
    }

    @Override
    public float calculateAddedStressCapacity() {
        float capacity = getLinkedHatch() instanceof KineticOutputHatchMachine ? workingCapacityPerRPM : 0.0f;
        lastCapacityProvided = capacity;
        return capacity;
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide) return;

        var linkedHatch = getLinkedHatch();
        if (!(linkedHatch instanceof KineticOutputHatchMachine) && workingSpeed != 0.0f) {
            stopOutput();
        }

        float currentImpact = linkedHatch instanceof KineticInputHatchMachine ?
                KineticInputHatchMachine.STRESS_IMPACT : 0.0f;
        if (lastStressApplied != currentImpact) {
            lastStressApplied = currentImpact;
            if (hasNetwork()) getOrCreateNetwork().updateStressFor(this, currentImpact);
        }
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.putFloat("WorkingSpeed", workingSpeed);
        tag.putFloat("WorkingCapacityPerRPM", workingCapacityPerRPM);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        workingSpeed = tag.getFloat("WorkingSpeed");
        workingCapacityPerRPM = tag.getFloat("WorkingCapacityPerRPM");
    }

    private @Nullable KineticHatchPartMachine getLinkedHatch() {
        if (level == null) return null;

        KineticHatchPartMachine result = null;
        for (Direction direction : Direction.values()) {
            if (direction.getAxis() != getRotationAxis()) continue;

            var machine = MetaMachine.getMachine(level, worldPosition.relative(direction));
            if (!(machine instanceof KineticHatchPartMachine hatch) ||
                    hatch.getFrontFacing() != direction.getOpposite()) {
                continue;
            }

            if (result != null) return null;
            result = hatch;
        }
        return result;
    }
}
