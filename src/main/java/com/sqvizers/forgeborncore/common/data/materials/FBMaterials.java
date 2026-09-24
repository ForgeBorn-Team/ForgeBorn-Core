package com.sqvizers.forgeborncore.common.data.materials;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.sqvizers.forgeborncore.ForgeBornCore;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.ingot;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Lead;
import static com.rekindled.embers.RegistryManager.LEAD_INGOT;
import static vazkii.botania.common.item.BotaniaItems.elementium;
import static vazkii.botania.common.item.BotaniaItems.manaSteel;

public class FBMaterials {
    public static Material MANASTEEL;
    public static Material ELEMENTIUM;
    public static Material TERRASTEEL;
    public static Material GAIA_SPIRIT;
    public static Material GAIA_STEEL;
    public static Material LIVINGWOOD;
    public static Material LIVINGROCK;



    public static Material OTHERROCK;
    public static Material IESNIUM;

    public static Material FROSTSTEEL;
    public static Material RODGORIUM;
    public static Material FORGOTTEN;

    public static Material SOULSTAINED_STEEL;
    public static Material HALLOWED_GOLD;

    public static Material STONE;

    public static Material ANDESITE_ALLOY;

    public static Material TITANIUM_ALLOY;


    public static void register() {
        MANASTEEL = new Material.Builder(ForgeBornCore.id("manasteel"))
                .ingot()
                .iconSet(FBMaterialSet.MANASTEEL)
                .flags(GENERATE_PLATE, GENERATE_RING, GENERATE_ROUND, GENERATE_GEAR, GENERATE_SMALL_GEAR,
                        PHOSPHORESCENT, GENERATE_LONG_ROD,
                        GENERATE_ROD, GENERATE_BOLT_SCREW, GENERATE_FRAME, GENERATE_DENSE, GENERATE_ROTOR,
                        GENERATE_FOIL)
                .buildAndRegister();

        ingot.setIgnored(MANASTEEL, manaSteel.asItem());

        ELEMENTIUM = new Material.Builder(ForgeBornCore.id("elementium"))
                .ingot()
                .iconSet(FBMaterialSet.ELEMENTIUM)
                .flags(GENERATE_PLATE, GENERATE_RING, GENERATE_ROUND, GENERATE_GEAR, GENERATE_SMALL_GEAR,
                        PHOSPHORESCENT, GENERATE_LONG_ROD,
                        GENERATE_ROD, GENERATE_BOLT_SCREW, GENERATE_FRAME, GENERATE_DENSE, GENERATE_ROTOR,
                        GENERATE_FOIL)
                .buildAndRegister();

        ingot.setIgnored(ELEMENTIUM, elementium.asItem());




        ingot.setIgnored(Lead, LEAD_INGOT);
    }
}
