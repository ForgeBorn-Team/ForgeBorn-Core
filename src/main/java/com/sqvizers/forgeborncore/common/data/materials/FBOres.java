package com.sqvizers.forgeborncore.common.data.materials;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.sqvizers.forgeborncore.ForgeBornCore;

public class FBOres {

    //ForgeBorn

    //GregTech

    //Malum

    //Thaumlarp

    //Spectrum

    //Aether

    //Undergarden
    public static Material CLOGGRUM;

    //Botania


    public static void register() {
        CLOGGRUM = new Material.Builder(ForgeBornCore.id("cloggrum"))
                .ingot()
                .ore()
                .color(0x7A687F)
                .iconSet(MaterialIconSet.SHINY)
                .buildAndRegister();
    }
}
