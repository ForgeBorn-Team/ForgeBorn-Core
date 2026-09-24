package com.sqvizers.forgeborncore.common.data.materials;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.sqvizers.forgeborncore.ForgeBornCore;

public class PlantLineMaterials {

    public static Material SOURCEBERRY_JUICE;

    public static void register() {
        SOURCEBERRY_JUICE = new Material.Builder(ForgeBornCore.id("sourceberry_juice"))
                .liquid(293)
                .color(0xFF4500)
                .secondaryColor(0xA020F0)
                .iconSet(MaterialIconSet.DULL)
                .buildAndRegister();
    }
}
