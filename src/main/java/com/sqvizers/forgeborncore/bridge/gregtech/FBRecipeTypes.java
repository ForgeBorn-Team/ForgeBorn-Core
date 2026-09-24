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


    public final static GTRecipeType SQUEEZER = register(ForgeBornCore.id("squeezer_recipes"), ELECTRIC)
            .setMaxIOSize(2, 2, 0, 2).setEUIO(IO.IN)
            .UI(builder -> builder.setProgressBar(GTGuiTextures.PROGRESS_ARROW))
            .setSound(GTSoundEntries.FURNACE);

    public final static GTRecipeType MALAXATOR = register(ForgeBornCore.id("malaxator_recipes"), ELECTRIC)
            .setMaxIOSize(2, 2, 0, 2).setEUIO(IO.IN)
            .UI(builder -> builder.setProgressBar(GTGuiTextures.PROGRESS_ARROW))
            .setSound(GTSoundEntries.FURNACE);

    public static final GTRecipeType PEELING_CHAMBER = register(ForgeBornCore.id("peeling_chamber"), ELECTRIC)
            .setSound(GTSoundEntries.CHEMICAL)
            .setMaxIOSize(2, 2, 1, 2)
            .setEUIO(IO.IN)
            .UI(builder -> builder.setProgressBar(GTGuiTextures.PROGRESS_ARROW_MULTIPLE));

    public static final GTRecipeType DEHYDRATOR = register(ForgeBornCore.id("dehydrator"), ELECTRIC)
            .setSound(GTSoundEntries.CHEMICAL)
            .setMaxIOSize(2, 2, 1, 2)
            .setEUIO(IO.IN)
            .UI(builder -> builder.setProgressBar(GTGuiTextures.PROGRESS_ARROW_MULTIPLE));

    public static final GTRecipeType WELDING_MACHINE = register(ForgeBornCore.id("welding_machine"), ELECTRIC)
            .setSound(GTSoundEntries.ARC)
            .setMaxIOSize(2, 2, 1, 2)
            .setEUIO(IO.IN)
            .UI(builder -> builder.setProgressBar(GTGuiTextures.PROGRESS_ARROW_MULTIPLE));

    public static final GTRecipeType DECANTER = register(ForgeBornCore.id("decanter"), ELECTRIC)
            .setSound(GTSoundEntries.MIXER)
            .setMaxIOSize(2, 2, 1, 2)
            .setEUIO(IO.IN)
            .UI(builder -> builder.setProgressBar(GTGuiTextures.PROGRESS_ARROW_MULTIPLE));

    public static final GTRecipeType GRINDER = register(ForgeBornCore.id("grinder"), ELECTRIC)
            .setSound(GTSoundEntries.MACERATOR)
            .setMaxIOSize(2, 2, 1, 2)
            .setEUIO(IO.IN)
            .UI(builder -> builder.setProgressBar(GTGuiTextures.PROGRESS_ARROW_MULTIPLE));

    public static final GTRecipeType SORTING = register(ForgeBornCore.id("sorting"), ELECTRIC)
            .setSound(GTSoundEntries.ASSEMBLER)
            .setMaxIOSize(2, 2, 0, 0)
            .setEUIO(IO.IN)
            .UI(builder -> builder.setProgressBar(GTGuiTextures.PROGRESS_ARROW_MULTIPLE));

    public static final GTRecipeType SLICER = register(ForgeBornCore.id("slicer"), ELECTRIC)
            .setSound(GTSoundEntries.CUT)
            .setMaxIOSize(2, 2, 0, 0)
            .setEUIO(IO.IN)
            .UI(builder -> builder.setProgressBar(GTGuiTextures.PROGRESS_ARROW_MULTIPLE));

    public static final GTRecipeType LOOM = register(ForgeBornCore.id("loom"), ELECTRIC)
            .setSound(GTSoundEntries.ASSEMBLER)
            .setMaxIOSize(2, 2, 0, 0)
            .setEUIO(IO.IN)
            .UI(builder -> builder.setProgressBar(GTGuiTextures.PROGRESS_ARROW_MULTIPLE));

    public static void init() {
        for (GTRecipeType type : new GTRecipeType[] {
                SPIRITFIRE_RECIPES, SANCTUM_WARD_RECIPES }) {
            type.setEUIO(IO.IN);
        }
    }
}
