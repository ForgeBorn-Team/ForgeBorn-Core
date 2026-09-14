package com.sqvizers.forgeborncore.client.renderer.machine;

import com.sqvizers.forgeborncore.common.machine.kinetic.KineticHatchPartMachine;

import com.gregtechceu.gtceu.client.renderer.machine.DynamicRender;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderType;

import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.theme.Color;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class KineticHatchRender extends DynamicRender<KineticHatchPartMachine, KineticHatchRender> {

    public static final KineticHatchRender INSTANCE = new KineticHatchRender();
    public static final MapCodec<KineticHatchRender> CODEC = MapCodec.unit(KineticHatchRender::new);
    public static final DynamicRenderType<KineticHatchPartMachine, KineticHatchRender> TYPE = new DynamicRenderType<>(
            CODEC);

    @Override
    public DynamicRenderType<KineticHatchPartMachine, KineticHatchRender> getType() {
        return TYPE;
    }

    @Override
    public void render(KineticHatchPartMachine machine, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Direction.Axis axis = machine.getFrontFacing().getAxis();
        var kinetic = machine.getKineticBlockEntity();
        float speed = kinetic == null ? 0.0f : kinetic.getSpeed();
        float angle = AnimationTickHolder.getRenderTime(machine.getLevel()) * speed * 0.3f;
        angle += KineticBlockEntityVisual.rotationOffset(machine.getBlockState(), axis, machine.getBlockPos());
        angle = (angle % 360.0f) * ((float) Math.PI / 180.0f);

        for (Direction direction : Direction.values()) {
            if (direction.getAxis() != axis) continue;

            CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, machine.getBlockState(), direction)
                    .light(packedLight)
                    .color(Color.WHITE)
                    .rotateCentered(angle, Direction.get(Direction.AxisDirection.POSITIVE, axis))
                    .renderInto(poseStack, buffer.getBuffer(RenderType.solid()));
        }
    }
}
