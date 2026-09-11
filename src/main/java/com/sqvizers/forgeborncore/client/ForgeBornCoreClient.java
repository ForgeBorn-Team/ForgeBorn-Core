package com.sqvizers.forgeborncore.client;

import com.sqvizers.forgeborncore.ForgeBornCore;
import com.sqvizers.forgeborncore.client.renderer.machine.SanctumWardRender;

import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderManager;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = ForgeBornCore.MOD_ID, dist = Dist.CLIENT)
public class ForgeBornCoreClient {

    public ForgeBornCoreClient(IEventBus modBus, ModContainer modContainer) {
        DynamicRenderManager.register(ForgeBornCore.id("sanctum_ward"), SanctumWardRender.TYPE);
    }
}
