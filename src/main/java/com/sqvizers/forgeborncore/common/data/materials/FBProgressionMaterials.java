package com.sqvizers.forgeborncore.common.data.materials;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.sqvizers.forgeborncore.ForgeBornCore;

public class FBProgressionMaterials {

    public static Material SPIRIT_STEEL;

    public static void register() {
        SPIRIT_STEEL = new Material.Builder(ForgeBornCore.id("spirit_steel"))
                .ingot()
                .color(0x00FFFF)
                .iconSet(FBMaterialSet.SPIRIT_STEEL)
                .buildAndRegister();
    }

}
