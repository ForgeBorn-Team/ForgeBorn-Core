package com.sqvizers.forgeborncore.bridge;

import com.gregtechceu.gtceu.api.GTValues;
import com.hollingsworth.arsnouveau.ArsNouveau;
import net.minecraft.data.recipes.RecipeOutput;

import static com.gregtechceu.gtceu.common.data.GTMaterials.Water;
import static com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry.SOURCE_BERRY_FOOD;
import static com.sqvizers.forgeborncore.bridge.gregtech.FBRecipeTypes.PEELING_CHAMBER;
import static com.sqvizers.forgeborncore.bridge.occultism.SpiritfireFurnaceRecipes.registerSpiritFireFurnaceRecipes;

public class FBRecipes {





    public static void init(RecipeOutput provider) {

        registerSpiritFireFurnaceRecipes(provider);
        registerPeelingRecipes(provider);
    }

    private static void registerPeelingRecipes(RecipeOutput provider) {
        PEELING_CHAMBER.recipeBuilder("sourceberry_peeling")
                .inputItems(SOURCE_BERRY_FOOD, 4)
                .inputFluids(Water.getFluid(500))
                .duration(40)
                .EUt(GTValues.VA[GTValues.LV])
                .save(provider);
    }
}
