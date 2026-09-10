package com.sqvizers.forgeborncore;

import com.sqvizers.forgeborncore.api.registries.FBRegistration;
import com.sqvizers.forgeborncore.bridge.occultism.SpiritfireFurnaceRecipes;

import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

import net.minecraft.data.recipes.RecipeOutput;

@GTAddon(ForgeBornCore.MOD_ID)
public class ForgeBornCoreGTAddon implements IGTAddon {

    @Override
    public GTRegistrate getRegistrate() {
        return FBRegistration.REGISTRATE;
    }

    @Override
    public void gtInitComplete() {}

    @Override
    public void addRecipes(RecipeOutput provider) {
        SpiritfireFurnaceRecipes.registerSpiritFireFurnaceRecipes(provider);
    }

    @Override
    public void registerVeinGenerators() {}
}
