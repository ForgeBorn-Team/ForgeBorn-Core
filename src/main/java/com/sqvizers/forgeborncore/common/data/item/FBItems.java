package com.sqvizers.forgeborncore.common.data.item;

import com.sqvizers.forgeborncore.api.registries.FBRegistration;
import com.sqvizers.forgeborncore.common.data.FBCreativeModeTabs;

import com.gregtechceu.gtceu.api.item.ComponentItem;

import com.sqvizers.forgeborncore.common.data.item.item_properties.LoyalAxeItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.sqvizers.forgeborncore.api.registries.FBRegistration.REGISTRATE;

public class FBItems {

    static {
        FBRegistration.REGISTRATE.creativeModeTab(() -> FBCreativeModeTabs.FORGEBORN_CORE);
    }

    //Runes
    //Shared interface so the helper method accepts any Rune Enum
    public static class RuneItem extends ComponentItem {
        private final String runeType;
        private final int tier;
        private final ChatFormatting typeColor;

        public RuneItem(Properties properties, String runeType, int tier, ChatFormatting typeColor) {
            super(properties);
            this.runeType = runeType;
            this.tier = tier;
            this.typeColor = typeColor;
        }

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
            super.appendHoverText(stack, context, tooltip, flag);
            // Applies the specific passed color to the Type line
            tooltip.add(Component.literal("Type: " + this.runeType).withStyle(this.typeColor));
            // Keeps the Tier line gray for clean contrast
            tooltip.add(Component.literal("Tier: " + this.tier).withStyle(ChatFormatting.GRAY));
        }
    }

    // 2. Shared Interface
    public interface IRuneSymbol {
        String getId();
        String getDisplayName();
    }

    // 3. Categories
    public enum GoodRune implements IRuneSymbol {
        Water("Wild Waters"),
        Air("Skies"),
        Earth("Earth");
        private final String displayName;
        GoodRune(String displayName) { this.displayName = displayName; }
        @Override public String getId() { return this.name().toLowerCase(Locale.ROOT); }
        @Override public String getDisplayName() { return this.displayName; }
    }

    public enum EvilRune implements IRuneSymbol {
        Fire("Flames"),
        BlueFire("Nether Flames"),
        Wither("Wither");
        private final String displayName;
        EvilRune(String displayName) { this.displayName = displayName; }
        @Override public String getId() { return this.name().toLowerCase(Locale.ROOT); }
        @Override public String getDisplayName() { return this.displayName; }
    }

    public enum SymbioticRune implements IRuneSymbol {
        ElementBending("Element-Bending"),
        Compatability("Compatability");
        private final String displayName;
        SymbioticRune(String displayName) { this.displayName = displayName; }
        @Override public String getId() { return this.name().toLowerCase(Locale.ROOT); }
        @Override public String getDisplayName() { return this.displayName; }
    }

    // 4. Update Maps
    public static final Map<GoodRune, ItemEntry<RuneItem>> GOOD_RUNES = new EnumMap<>(GoodRune.class);
    public static final Map<EvilRune, ItemEntry<RuneItem>> EVIL_RUNES = new EnumMap<>(EvilRune.class);
    public static final Map<SymbioticRune, ItemEntry<RuneItem>> SYMBIOTIC_RUNES = new EnumMap<>(SymbioticRune.class);

    // 5. Register with specific ChatFormatting colors
    static {
        registerRuneCategory("good", "Good", GoodRune.values(), GOOD_RUNES, 1, ChatFormatting.GREEN);
        registerRuneCategory("evil", "Evil", EvilRune.values(), EVIL_RUNES, 1, ChatFormatting.RED);
        registerRuneCategory("symbiotic", "Symbiotic", SymbioticRune.values(), SYMBIOTIC_RUNES, 1, ChatFormatting.LIGHT_PURPLE);
    }

    // 6. Generic helper updated to accept ChatFormatting
    private static <T extends Enum<T> & IRuneSymbol> void registerRuneCategory(
            String categoryId, String categoryName, T[] symbols, Map<T, ItemEntry<RuneItem>> map, int tier, ChatFormatting color) {

        for (T symbol : symbols) {
            // Also updating the registry ID to match the new naming scheme (e.g., rune_1_good_life)
            ItemEntry<RuneItem> entry = REGISTRATE.item("rune_" + tier + "_" + categoryId + "_" + symbol.getId(),
                            p -> new RuneItem(p, categoryName, tier, color))
                    .lang("Rune of " + symbol.getDisplayName())
                    .properties(p -> p.stacksTo(16))
                    .tag()
                    .model((ctx, prov) -> prov.withExistingParent(ctx.getName(), "item/generated")
                            // Dynamically targets "item/rune_1_good_base", "item/rune_2_good_base", etc.
                            .texture("layer0", prov.modLoc("item/rune_" + tier + "_" + categoryId + "_base"))
                            // Targets the specific symbol overlay
                            .texture("layer1", prov.modLoc("item/runes_symbols/rune_" + categoryId + "_" + symbol.getId())))
                    .register();
            map.put(symbol, entry);
        }
    }

    //Regular registry
    public static final ItemEntry<ComponentItem> DULL = REGISTRATE.item("forgeporn", ComponentItem::new)
            .lang("ForgePorn")
            .properties(p -> p.stacksTo(16))
            .tag()
            .defaultModel()
            .register();

    public static final ItemEntry<BoneWhistleItem> DEATH_WHISTLE = REGISTRATE
            .item("death_whistle", BoneWhistleItem::new)
            .lang("Death Whistle")
            .properties(p -> p.stacksTo(1))
            .tag()
            .defaultModel()
            .register();

    public static final ItemEntry<LoyalAxeItem> LOYAL_AXE = REGISTRATE
            .item("loyal_axe", LoyalAxeItem::new)
            .lang("Loyal Axe")
            .properties(p -> p.stacksTo(1).attributes(LoyalAxeItem.createAttributes()))
            .tag(ItemTags.AXES)
            .model((ctx, prov) -> prov.handheld(ctx::getEntry))
            .register();

    public static final ItemEntry<ComponentItem> RUNE_1_GOOD_BASE = REGISTRATE.item("rune_1_good_base", ComponentItem::new)
            .lang("Rune Base")
            .properties(p -> p.stacksTo(64))
            .tag()
            .defaultModel()
            .register();

    public static final ItemEntry<ComponentItem> RUNE_1_EVIL_BASE = REGISTRATE.item("rune_1_evil_base", ComponentItem::new)
            .lang("Rune Base")
            .properties(p -> p.stacksTo(64))
            .tag()
            .defaultModel()
            .register();

    public static final ItemEntry<ComponentItem> RUNE_1_SYMBIOTIC_BASE = REGISTRATE.item("rune_1_symbiotic_base", ComponentItem::new)
            .lang("Rune Base")
            .properties(p -> p.stacksTo(64))
            .tag()
            .defaultModel()
            .register();

    public static final ItemEntry<ComponentItem> SOUL_FRAGMENT_1 = REGISTRATE.item("soul_fragment_1", ComponentItem::new)
            .lang("Fragmentum Animae")
            .properties(p -> p.stacksTo(64))
            .tag()
            .defaultModel()
            .register();

    public static final ItemEntry<ComponentItem> SOUL_FRAGMENT_2 = REGISTRATE.item("soul_fragment_2", ComponentItem::new)
            .lang("Fragmentum Animae")
            .properties(p -> p.stacksTo(64))
            .tag()
            .defaultModel()
            .register();

    public static final ItemEntry<ComponentItem> SOUL_STONE = REGISTRATE.item("soul_stone", ComponentItem::new)
            .lang("Soul-Stone")
            .properties(p -> p.stacksTo(64))
            .tag()
            .defaultModel()
            .register();

    public static final ItemEntry<ComponentItem> SHELL_SPIRIT_STEEL = REGISTRATE.item("shell_spirit_steel", ComponentItem::new)
            .lang("Spirit-Steel Shell")
            .properties(p -> p.stacksTo(64))
            .tag()
            .defaultModel()
            .register();

    public static final ItemEntry<ComponentItem> SOUL_MECHANISM = REGISTRATE.item("soul_mechanism", ComponentItem::new)
            .lang("Attuned Soul Mechanism")
            .properties(p -> p.stacksTo(64))
            .tag()
            .defaultModel()
            .register();


    public static void init() {}
}
