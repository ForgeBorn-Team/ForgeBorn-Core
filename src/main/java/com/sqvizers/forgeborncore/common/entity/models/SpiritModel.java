package com.sqvizers.forgeborncore.common.entity.models;

import com.sqvizers.forgeborncore.common.entity.entities.SpiritEntity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class SpiritModel extends EntityModel<SpiritEntity> {

    // 1.21+ API for ResourceLocation
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("forgeborncore", "spirit"), "main");

    private final ModelPart spirit;
    private final ModelPart torso;
    private final ModelPart cape;
    private final ModelPart head;
    private final ModelPart head_main;
    private final ModelPart horns;
    private final ModelPart arms;

    // Fixed constructor name matching class name
    public SpiritModel(ModelPart root) {
        this.spirit = root.getChild("spirit");
        this.torso = this.spirit.getChild("torso");
        this.cape = this.torso.getChild("cape");
        this.head = this.spirit.getChild("head");
        this.head_main = this.head.getChild("head_main");
        this.horns = this.head.getChild("horns");
        this.arms = this.spirit.getChild("arms");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition spirit = partdefinition.addOrReplaceChild("spirit", CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition torso = spirit.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F,
                -21.0F, -2.0F, 8.0F, 21.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cape = torso.addOrReplaceChild("cape", CubeListBuilder.create().texOffs(24, 16).addBox(-8.0F,
                -21.0F, 2.1F, 16.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = spirit.addOrReplaceChild("head", CubeListBuilder.create(),
                PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition head_main = head.addOrReplaceChild("head_main", CubeListBuilder.create().texOffs(24, 0).addBox(
                -4.0F, -5.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition horns = head.addOrReplaceChild("horns", CubeListBuilder.create().texOffs(32, 27).addBox(-11.0F,
                -14.0F, 1.0F, 8.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = horns
                .addOrReplaceChild("cube_r1",
                        CubeListBuilder.create().texOffs(32, 36).addBox(-4.0F, -4.5F, 0.0F, 8.0F, 9.0F, 0.0F,
                                new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(7.0F, -9.5F, 0.0F, 0.0F, 0.3491F, 0.0F));

        PartDefinition arms = spirit.addOrReplaceChild("arms", CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r2 = arms.addOrReplaceChild("cube_r2",
                CubeListBuilder.create().texOffs(16, 27)
                        .addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 25).addBox(-14.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(6.0F, -19.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(SpiritEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
    }

    // 1.21+ signature uses packed int color
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
                               int color) {
        spirit.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
