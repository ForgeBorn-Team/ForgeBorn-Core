package com.sqvizers.forgeborncore;

import com.sqvizers.forgeborncore.api.registries.FBRegistration;
import com.sqvizers.forgeborncore.bridge.gregtech.FBRecipeTypes;
import com.sqvizers.forgeborncore.common.data.FBBlocks;
import com.sqvizers.forgeborncore.common.data.FBCreativeModeTabs;
import com.sqvizers.forgeborncore.common.data.FBDataGen;
import com.sqvizers.forgeborncore.common.data.FBMachines;
import com.sqvizers.forgeborncore.common.data.item.FBItems;
import com.sqvizers.forgeborncore.common.data.materials.FBMaterialSet;
import com.sqvizers.forgeborncore.common.data.materials.FBOres;
import com.sqvizers.forgeborncore.common.data.materials.FBProgressionMaterials;
import com.sqvizers.forgeborncore.common.entity.FBEntityTypes;
import com.sqvizers.forgeborncore.common.entity.entities.SpiritEntity;
import com.sqvizers.forgeborncore.common.machine.MultiblockInit;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.event.PostMaterialEvent;
import com.gregtechceu.gtceu.common.block.CoilBlock;

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
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
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
        FBRegistration.REGISTRATE.registerEventListeners(modBus);
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
        FBProgressionMaterials.register();
        FBMaterialSet.init();
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
        event.enqueueWork(() -> {});
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
