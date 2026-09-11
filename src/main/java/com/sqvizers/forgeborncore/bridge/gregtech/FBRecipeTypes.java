package com.sqvizers.forgeborncore.bridge.gregtech;

import com.sqvizers.forgeborncore.ForgeBornCore;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;
import com.gregtechceu.gtceu.common.mui.GTGuiTextures;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

public class FBRecipeTypes {

    public final static GTRecipeType SPIRITFIRE_RECIPES = register(ForgeBornCore.id("spiritfire_recipes"), MULTIBLOCK)
            .setMaxIOSize(1, 1, 0, 0).setEUIO(IO.IN)
            .UI(builder -> builder.setProgressBar(GTGuiTextures.PROGRESS_ARROW)
                    .setItemSlotOverlay(IO.IN, 0, GTGuiTextures.FURNACE_OVERLAY_1))
            .setSound(GTSoundEntries.FURNACE);

    public final static GTRecipeType SANCTUM_WARD_RECIPES = register(ForgeBornCore.id("sanctum_ward_recipes"),
            MULTIBLOCK)
            .setMaxIOSize(4, 0, 1, 0).setEUIO(IO.IN)
            .UI(builder -> builder.setProgressBar(GTGuiTextures.PROGRESS_ARROW))
            .setSound(GTSoundEntries.FURNACE);

    public static void init() {
        for (GTRecipeType type : new GTRecipeType[] {
                SPIRITFIRE_RECIPES, SANCTUM_WARD_RECIPES }) {
            type.setEUIO(IO.IN);
        }
    }
}
