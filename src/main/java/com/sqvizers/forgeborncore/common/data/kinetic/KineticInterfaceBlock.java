package com.sqvizers.forgeborncore.common.data.kinetic;

import com.sqvizers.forgeborncore.common.data.FBBlocks;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.AbstractShaftBlock;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class KineticInterfaceBlock extends AbstractShaftBlock {

    public KineticInterfaceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return FBBlocks.KINETIC_INTERFACE_BLOCK_ENTITY.get();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AllShapes.SIX_VOXEL_POLE.get(state.getValue(AXIS));
    }

    @Override
    public float getParticleTargetRadius() {
        return 0.35f;
    }

    @Override
    public float getParticleInitialRadius() {
        return 0.125f;
    }
}
