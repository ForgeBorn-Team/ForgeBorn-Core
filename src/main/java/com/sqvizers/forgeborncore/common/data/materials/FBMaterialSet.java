package com.sqvizers.forgeborncore.common.data.materials;

import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconType;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet.METALLIC;

public class FBMaterialSet {

    public static final MaterialIconSet SPIRIT_STEEL = new MaterialIconSet("spirit_steel",
            METALLIC);

    //Later add reinforced plate
    public static final MaterialIconType NANITES = new MaterialIconType("nanites");

    public static void init() {}

}
