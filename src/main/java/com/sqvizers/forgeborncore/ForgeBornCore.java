package com.sqvizers.forgeborncore;

import com.sqvizers.forgeborncore.api.registries.FBRegistration;
import com.sqvizers.forgeborncore.bridge.gregtech.FBRecipeTypes;
import com.sqvizers.forgeborncore.bridge.gregtech.GTVeinRewriteListener;
import com.sqvizers.forgeborncore.common.data.FBBlocks;
import com.sqvizers.forgeborncore.common.data.FBCreativeModeTabs;
import com.sqvizers.forgeborncore.common.data.FBDataGen;
import com.sqvizers.forgeborncore.common.data.FBItems;
import com.sqvizers.forgeborncore.common.data.FBMachines;
import com.sqvizers.forgeborncore.common.data.materials.*;
import com.sqvizers.forgeborncore.common.entity.FBEntityTypes;
import com.sqvizers.forgeborncore.common.entity.entities.SpiritEntity;
import com.sqvizers.forgeborncore.common.machine.MultiblockInit;
import com.sqvizers.forgeborncore.bridge.occultism.OccultismMinerIntegration;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.event.PostMaterialEvent;
import com.gregtechceu.gtceu.common.block.CoilBlock;
import com.gregtechceu.gtceu.config.ConfigHolder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.world.chunk.RegisterTicketControllersEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// The value here should match an entry in the META-INF/neoforge.mods.toml file

@Mod(ForgeBornCore.MOD_ID)
public class ForgeBornCore {

    // GTCEu 8.0 registers all content during a single RegisterEvent; guard so it only runs once.
    // Content registration mirrors GTCEu's CommonProxy#onRegister ordering (elements -> materials -> tag prefixes
    // -> recipe caps, conditions, and types -> blocks -> items -> machines -> sounds).
    private static boolean didRunRegistration = false;
    public static final String MOD_ID = "forgeborncore", NAME = "ForgeBornCore";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public ForgeBornCore(IEventBus modBus, ModContainer modContainer) {
        modBus.register(this);
        modBus.addListener(OccultismMinerIntegration::registerJob);
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(OccultismMinerIntegration.class);
        FBRegistration.REGISTRATE.registerEventListeners(modBus);
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(this::registerReloadListeners);
    }

    private void registerReloadListeners(AddReloadListenerEvent event) {
        event.addListener(GTVeinRewriteListener.INSTANCE);
    }

    @SubscribeEvent
    public void registerTicketControllers(RegisterTicketControllersEvent event) {}

    @SubscribeEvent
    public void onRegister(RegisterEvent event) {
        if (didRunRegistration) return;
        didRunRegistration = true;
        FBCreativeModeTabs.init();
        FBEntityTypes.init();
        FBOres.register();
        FBMaterialSet.init();
        FBProgressionMaterials.register();
        FBMaterials.register();
        PlantLineMaterials.register();
        FBItems.init();
        FBBlocks.init();
        FBRecipeTypes.init();
        FBMachines.init();
        MultiblockInit.init();
        FBDataGen.init();
    }

    @SubscribeEvent
    public void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(FBEntityTypes.SPIRIT.get(), SpiritEntity.createAttributes().build());
    }

    @SubscribeEvent
    public void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(
                FBEntityTypes.SPIRIT.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                SpiritEntity::checkSpiritSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    @SubscribeEvent
    public void modifyExistingMaterials(PostMaterialEvent event) {}

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // GTCEu places one candidate vein per grid intersection. Eight chunks gives the pack
            // noticeably rarer deposits while still allowing the random offset to vary their centers.
            ConfigHolder.INSTANCE.worldgen.oreVeins.oreVeinGridSize = Math.max(
                    ConfigHolder.INSTANCE.worldgen.oreVeins.oreVeinGridSize, 8);
        });
    }

    @SubscribeEvent
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        GTCEuAPI.HEATING_COILS.remove(CoilBlock.CoilType.RTMALLOY);
        GTCEuAPI.HEATING_COILS.remove(CoilBlock.CoilType.HSSG);
        GTCEuAPI.HEATING_COILS.remove(CoilBlock.CoilType.NAQUADAH);
        GTCEuAPI.HEATING_COILS.remove(CoilBlock.CoilType.TRINIUM);
        GTCEuAPI.HEATING_COILS.remove(CoilBlock.CoilType.TRITANIUM);
    }

    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event) {}
}
