package com.sqvizers.forgeborncore.bridge.occultism;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import com.klikli_dev.occultism.registry.OccultismBlocks;
import com.klikli_dev.occultism.registry.OccultismItems;

import static com.sqvizers.forgeborncore.bridge.gregtech.FBRecipeTypes.SPIRITFIRE_RECIPES;

public class SpiritfireFurnaceRecipes {

    private static final int PORT_DURATION = 20 * 4;
    // private static final int PORT_EUT = GTValues.VA[GTValues.ULV];

    public static void registerSpiritFireFurnaceRecipes(RecipeOutput provider) {
        item(provider, "port_chalk_black", OccultismItems.CHALK_BLACK_IMPURE.get(), OccultismItems.CHALK_BLACK.get());
        item(provider, "port_chalk_blue", OccultismItems.CHALK_BLUE_IMPURE.get(), OccultismItems.CHALK_BLUE.get());
        item(provider, "port_chalk_brown", OccultismItems.CHALK_BROWN_IMPURE.get(), OccultismItems.CHALK_BROWN.get());
        item(provider, "port_chalk_cyan", OccultismItems.CHALK_CYAN_IMPURE.get(), OccultismItems.CHALK_CYAN.get());
        item(provider, "port_chalk_gold", OccultismItems.CHALK_YELLOW_IMPURE.get(), OccultismItems.CHALK_YELLOW.get());
        item(provider, "port_chalk_gray", OccultismItems.CHALK_GRAY_IMPURE.get(), OccultismItems.CHALK_GRAY.get());
        item(provider, "port_chalk_green", OccultismItems.CHALK_GREEN_IMPURE.get(), OccultismItems.CHALK_GREEN.get());
        item(provider, "port_chalk_light_blue", OccultismItems.CHALK_LIGHT_BLUE_IMPURE.get(),
                OccultismItems.CHALK_LIGHT_BLUE.get());
        item(provider, "port_chalk_light_gray", OccultismItems.CHALK_LIGHT_GRAY_IMPURE.get(),
                OccultismItems.CHALK_LIGHT_GRAY.get());
        item(provider, "port_chalk_lime", OccultismItems.CHALK_LIME_IMPURE.get(), OccultismItems.CHALK_LIME.get());
        item(provider, "port_chalk_magenta", OccultismItems.CHALK_MAGENTA_IMPURE.get(),
                OccultismItems.CHALK_MAGENTA.get());
        item(provider, "port_chalk_orange", OccultismItems.CHALK_ORANGE_IMPURE.get(),
                OccultismItems.CHALK_ORANGE.get());
        item(provider, "port_chalk_pink", OccultismItems.CHALK_PINK_IMPURE.get(), OccultismItems.CHALK_PINK.get());
        item(provider, "port_chalk_purple", OccultismItems.CHALK_PURPLE_IMPURE.get(),
                OccultismItems.CHALK_PURPLE.get());
        item(provider, "port_chalk_red", OccultismItems.CHALK_RED_IMPURE.get(), OccultismItems.CHALK_RED.get());
        item(provider, "port_chalk_white", OccultismItems.CHALK_WHITE_IMPURE.get(), OccultismItems.CHALK_WHITE.get());

        item(provider, "port_otherrock", Items.DIORITE, OccultismBlocks.OTHERROCK.get());
        item(provider, "port_otherstone", Items.ANDESITE, OccultismBlocks.OTHERSTONE.get());
        item(provider, "port_otherworld_sapling_natural", Items.OAK_SAPLING,
                OccultismBlocks.OTHERWORLD_SAPLING_NATURAL.get());
        item(provider, "port_otherworld_essence", OccultismItems.DEMONS_DREAM_ESSENCE.get(),
                OccultismItems.OTHERWORLD_ESSENCE.get());
        item(provider, "port_taboo_book", Items.BOOK, OccultismItems.TABOO_BOOK.get());

        tag(provider, "port_awakened_feather", tag("c", "feathers"), OccultismItems.AWAKENED_FEATHER.get());
        tag(provider, "port_book_of_binding_empty", tag("occultism", "books/books_for_empty"),
                OccultismItems.BOOK_OF_BINDING_EMPTY.get());
        tag(provider, "port_otherflower", tag("minecraft", "flowers"), OccultismBlocks.OTHERFLOWER.get());
        tag(provider, "port_otherworld_ashes", tag("occultism", "otherworld_logs"),
                OccultismItems.OTHERWORLD_ASHES.get());
        tag(provider, "port_purified_ink", tag("c", "dyes/black"), OccultismItems.PURIFIED_INK.get());
        tag(provider, "port_spirit_attuned_gem", tag("c", "gems/diamond"), OccultismItems.SPIRIT_ATTUNED_GEM.get());

        // item(provider, "expandable_example", Items.NETHER_STAR, OccultismItems.SPIRIT_ATTUNED_GEM.get());
    }

    private static void item(RecipeOutput provider, String name, ItemLike input, ItemLike output) {
        SPIRITFIRE_RECIPES.recipeBuilder(name)
                .inputItems(input)
                .outputItems(output)
                .duration(PORT_DURATION)
                // .EUt(PORT_EUT)
                .save(provider);
    }

    private static void tag(RecipeOutput provider, String name, TagKey<Item> input, ItemLike output) {
        SPIRITFIRE_RECIPES.recipeBuilder(name)
                .inputItems(input, 1)
                .outputItems(output)
                .duration(PORT_DURATION)
                // .EUt(PORT_EUT)
                .save(provider);
    }

    private static TagKey<Item> tag(String namespace, String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
    //
}
