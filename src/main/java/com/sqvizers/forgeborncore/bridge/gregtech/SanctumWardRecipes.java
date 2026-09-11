package com.sqvizers.forgeborncore.bridge.gregtech;

import com.sqvizers.forgeborncore.common.machine.multiblock.ward.SanctumWardMachine;
import com.sqvizers.forgeborncore.common.machine.multiblock.ward.WardMode;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;

public class SanctumWardRecipes {

    private static final int UPKEEP_DURATION = 20 * 30;

    public static void registerSanctumWardRecipes(RecipeOutput provider) {
        upkeep(provider, "hostile", Items.GLOWSTONE_DUST, 4, WardMode.HOSTILE);
        upkeep(provider, "passive", Items.WHEAT, 8, WardMode.PASSIVE);
        upkeep(provider, "neutral", Items.GUNPOWDER, 4, WardMode.NEUTRAL);
        upkeep(provider, "all", Items.ENDER_PEARL, 2, WardMode.ALL);
    }

    private static void upkeep(RecipeOutput provider, String suffix, net.minecraft.world.item.Item catalyst,
                               int count, WardMode mode) {
        FBRecipeTypes.SANCTUM_WARD_RECIPES.recipeBuilder("sustain_sanctum_ward_" + suffix)
                .inputItems(catalyst, count)
                .duration(UPKEEP_DURATION)
                .addData(SanctumWardMachine.WARD_MODE_KEY, mode.name())
                .save(provider);
    }
}
