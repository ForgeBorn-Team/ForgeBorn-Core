package com.sqvizers.forgeborncore.client.renderer.entity;

import com.sqvizers.forgeborncore.common.entity.entities.SpiritEntity;
import com.sqvizers.forgeborncore.common.entity.models.SpiritModel;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SpiritRenderer extends MobRenderer<SpiritEntity, SpiritModel> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("forgeborncore",
            "textures/entity/spirit.png");

    public SpiritRenderer(EntityRendererProvider.Context context) {
        super(context, new SpiritModel(context.bakeLayer(SpiritModel.LAYER_LOCATION)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(SpiritEntity entity) {
        return TEXTURE;
    }

    @Override
    protected RenderType getRenderType(SpiritEntity entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        return RenderType.entityTranslucent(getTextureLocation(entity));
    }
}
