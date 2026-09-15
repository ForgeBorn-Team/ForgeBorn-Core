package com.sqvizers.forgeborncore.common.machine.kinetic;

import com.sqvizers.forgeborncore.common.data.kinetic.KineticInterfaceBlockEntity;

import com.gregtechceu.gtceu.api.blockentity.BlockEntityCreationInfo;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class KineticHatchPartMachine extends TieredIOPartMachine
                                              implements IHaveGoggleInformation, IHaveHoveringInformation {

    protected KineticHatchPartMachine(BlockEntityCreationInfo info, IO io) {
        super(info, 0, io);
    }

    public @Nullable KineticInterfaceBlockEntity getKineticInterface() {
        return getKineticInterface(getFrontFacing());
    }

    public @Nullable KineticBlockEntity getKineticBlockEntity() {
        var kineticInterface = getKineticInterface();
        return kineticInterface != null ? kineticInterface : getDirectKineticBlockEntity(getFrontFacing());
    }

    protected @Nullable KineticInterfaceBlockEntity getKineticInterface(Direction facing) {
        var level = getLevel();
        if (level == null) return null;

        var blockEntity = level.getBlockEntity(getBlockPos().relative(facing));
        if (blockEntity instanceof KineticInterfaceBlockEntity kineticInterface &&
                kineticInterface.getRotationAxis() == facing.getAxis() && kineticInterface.isLinkedTo(this)) {
            return kineticInterface;
        }
        return null;
    }

    protected @Nullable KineticBlockEntity getDirectKineticBlockEntity() {
        return getDirectKineticBlockEntity(getFrontFacing());
    }

    protected @Nullable KineticBlockEntity getDirectKineticBlockEntity(Direction facing) {
        var level = getLevel();
        if (level == null) return null;

        var pos = getBlockPos().relative(facing);
        var blockEntity = level.getBlockEntity(pos);
        BlockState state = level.getBlockState(pos);
        if (blockEntity instanceof KineticBlockEntity kinetic && state.getBlock() instanceof IRotate rotatingBlock &&
                !(kinetic instanceof KineticInterfaceBlockEntity) &&
                rotatingBlock.hasShaftTowards(level, pos, state, facing.getOpposite())) {
            return kinetic;
        }
        return null;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        var kineticInterface = getKineticInterface();
        return kineticInterface != null && kineticInterface.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }

    @Override
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        var kinetic = getKineticBlockEntity();
        return kinetic != null && kinetic.addToTooltip(tooltip, isPlayerSneaking);
    }
}
