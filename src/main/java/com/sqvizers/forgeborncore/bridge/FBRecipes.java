package com.sqvizers.forgeborncore.bridge;

import net.minecraft.data.recipes.RecipeOutput;

import static com.sqvizers.forgeborncore.bridge.occultism.SpiritfireFurnaceRecipes.registerSpiritFireFurnaceRecipes;

public class FBRecipes {

    public static void init(RecipeOutput provider) {
        registerSpiritFireFurnaceRecipes(provider);
    }
}
