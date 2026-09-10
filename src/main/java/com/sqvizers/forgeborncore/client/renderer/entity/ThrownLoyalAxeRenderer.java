package com.sqvizers.forgeborncore.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.sqvizers.forgeborncore.common.data.item.FBItems;
import com.sqvizers.forgeborncore.common.data.item.item_properties.ThrownLoyalAxe;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ThrownLoyalAxeRenderer extends EntityRenderer<ThrownLoyalAxe> {
    private final ItemRenderer itemRenderer;

    public ThrownLoyalAxeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ThrownLoyalAxe entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // Direction orientation
        poseStack.mulPose(Axis.YP.rotationDegrees(entityYaw - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getXRot()));

        // Spin animation
        float spinTicks = entity.tickCount + partialTicks;
        poseStack.mulPose(Axis.XP.rotationDegrees(spinTicks * 45.0F));

        ItemStack itemStack = entity.getWeaponItem();
        if (itemStack.isEmpty()) {
            itemStack = new ItemStack(FBItems.LOYAL_AXE.get());
        }

        BakedModel bakedModel = this.itemRenderer.getModel(itemStack, entity.level(), null, entity.getId());
        this.itemRenderer.render(itemStack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, false, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, bakedModel);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownLoyalAxe entity) {
        return null;
    }
}