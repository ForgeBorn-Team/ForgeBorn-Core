package com.sqvizers.forgeborncore.client;

import com.sqvizers.forgeborncore.client.renderer.machine.SanctumWardRender;
import com.sqvizers.forgeborncore.common.data.FBBlocks;
import com.sqvizers.forgeborncore.common.entity.models.SpiritModel;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;

@EventBusSubscriber(modid = "forgeborncore", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> SimpleBlockEntityVisualizer.builder(FBBlocks.KINETIC_INTERFACE_BLOCK_ENTITY.get())
                .factory(SingleAxisRotatingVisual::shaft)
                .skipVanillaRender(blockEntity -> false)
                .apply());
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SpiritModel.LAYER_LOCATION, SpiritModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(ModelResourceLocation.standalone(SanctumWardRender.SPHERE_MODEL_RL));
    }
}
