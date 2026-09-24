package com.sqvizers.forgeborncore.common.data.materials;

import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.sqvizers.forgeborncore.ForgeBornCore;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;

public class FBProgressionMaterials {

    public static Material SPIRIT_STEEL;

    public static Material NEFROUNTIUM;

    public static void register() {
        SPIRIT_STEEL = new Material.Builder(ForgeBornCore.id("spirit_steel"))
                .ingot()
                .color(0x00FFFF)
                .iconSet(FBMaterialSet.SPIRIT_STEEL)
                .buildAndRegister();

        NEFROUNTIUM = new Material.Builder(ForgeBornCore.id("nefrountium"))
                .ingot()
                .color(0x2d3562)
                .iconSet(MaterialIconSet.METALLIC)
                .buildAndRegister();
    }
}
