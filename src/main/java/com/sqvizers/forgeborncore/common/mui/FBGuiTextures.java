package com.sqvizers.forgeborncore.common.mui;

import com.gregtechceu.gtceu.GTCEu;

import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;

import brachy.modularui.drawable.ColorType;
import brachy.modularui.drawable.UITexture;
import org.jetbrains.annotations.Nullable;

public interface FBGuiTextures {

    ColorType SPIRIT_FIRE = new ColorType("spiritfire", theme -> 0xFF81DBFF);

    UITexture PROGRESS_FIRE = progressBar("textures/gui/progress_bar/progress_bar_spirit_fire.png", ColorType.DEFAULT);
    public static final ResourceTexture SPIRITFIRE_PROGRESS = new ResourceTexture(
            "forgeborncore:textures/gui/progress_bar/progress_bar_spirit_fire.png");

    static void init() {}

    private static UITexture fullImage(String path) {
        return fullImage(path, (ColorType) null);
    }

    private static UITexture fullImage(String path, @Nullable ColorType colorType) {
        return UITexture.fullImage(GTCEu.id(path), colorType);
    }

    private static UITexture[] slice(String path, int imageWidth, int imageHeight, int sliceWidth, int sliceHeight,
                                     ColorType colorType) {
        if (imageWidth % sliceWidth == 0 && imageHeight % sliceHeight == 0) {
            int countX = imageWidth / sliceWidth;
            int countY = imageHeight / sliceHeight;
            UITexture[] slices = new UITexture[countX * countY];

            for (int indexX = 0; indexX < countX; ++indexX) {
                for (int indexY = 0; indexY < countY; ++indexY) {
                    slices[indexX * countX + indexY] = UITexture.builder().location("gtceu", path)
                            .imageSize(imageWidth, imageHeight).colorType(colorType)
                            .subAreaXYWH(indexX * sliceWidth, indexY * sliceHeight, sliceWidth, sliceHeight).build();
                }
            }

            return slices;
        } else {
            throw new IllegalArgumentException("Slice height and slice width must divide the image evenly!");
        }
    }

    private static UITexture progressBar(String path) {
        return progressBar(path, (ColorType) null);
    }

    private static UITexture progressBar(String path, @Nullable ColorType colorType) {
        return progressBar(path, 20, 40, colorType);
    }

    private static UITexture progressBar(String path, int width, int height) {
        return progressBar(path, width, height, (ColorType) null);
    }

    private static UITexture progressBar(String path, int width, int height, @Nullable ColorType colorType) {
        UITexture.Builder builder = (new UITexture.Builder()).location("gtceu", path).imageSize(width, height)
                .colorType(colorType);
        return builder.build();
    }
}
