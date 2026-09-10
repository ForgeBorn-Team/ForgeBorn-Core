package com.sqvizers.forgeborncore.common.data;

import com.sqvizers.forgeborncore.ForgeBornCore;
import com.sqvizers.forgeborncore.common.data.item.FBItems;

import net.minecraft.world.item.CreativeModeTab;

import com.tterrag.registrate.util.entry.RegistryEntry;

import static com.sqvizers.forgeborncore.api.registries.FBRegistration.REGISTRATE;

public class FBCreativeModeTabs {

    // 1.21 Registrate auto-populates the default tab from the mod's registered entries via its own
    // BuildCreativeModeTabContentsEvent handler, so we no longer attach a GTCEu RegistrateDisplayItemsGenerator
    // here. Doing both added every entry twice (e.g. the CoilBlocks), which is a hard "already exists" crash
    // whenever the tab (re)builds. Registrate's auto-add is now the single source of truth for this tab.
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> FORGEBORN_CORE = REGISTRATE.defaultCreativeTab(
            ForgeBornCore.MOD_ID,
            builder -> builder
                    .title(REGISTRATE.addLang("itemGroup", ForgeBornCore.id("creative_tab"), "ForgeBorn Core"))
                    .icon(FBItems.DULL::asStack)
                    .build())
            .register();

    public static void init() {}
}
