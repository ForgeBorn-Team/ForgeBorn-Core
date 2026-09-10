package com.sqvizers.forgeborncore.client.gui.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.attachments.thirst.ThirstAttachment;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

/**
 * Hides the vanilla health, armor, food, and air (breathing) HUD elements
 * and replaces them with a compact vertical list in the bottom-left corner:
 *
 *   [armor icon] 12
 *   [food icon]  18/20
 *   [heart icon] 16/20
 */
@EventBusSubscriber(modid = "forgeborncore", value = Dist.CLIENT)
public final class CustomHudOverlay {

    // These reuse the same sprite paths vanilla uses internally
    // (textures/gui/sprites/hud/...), so no custom texture is required.
    private static final ResourceLocation HEART_ICON =
            ResourceLocation.withDefaultNamespace("hud/heart/full");
    private static final ResourceLocation ARMOR_ICON =
            ResourceLocation.withDefaultNamespace("hud/armor_full");
    private static final ResourceLocation FOOD_ICON =
            ResourceLocation.withDefaultNamespace("hud/food_full");

    // The vanilla experience orb texture is a raw 64x64 sheet of 16x16 cells
    // (11 orb-value variants + 5 unused slots). Cell (0,0) is the smallest/
    // default orb and reads fine as a generic "XP" icon at any size.
    private static final ResourceLocation XP_ORB_TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/entity/experience_orb.png");
    private static final int XP_ORB_CELL_SIZE = 16;
    private static final int XP_ORB_SHEET_SIZE = 64;

    // LSO's own GUI icon sheet - used so the thirst icon matches LSO's art
    // instead of reusing the hunger sprite. Full hydration droplet icon is
    // at u=9,v=0 (see RenderThirstGui.ThirstEffect#getXTextureOffset with
    // isHalfIcon=false, isContainer=false). Sheet is 256x256.
    private static final ResourceLocation LSO_ICONS =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overlay.png");
    private static final int THIRST_ICON_U = 9;
    private static final int THIRST_ICON_V = 0;

    // The exact GUI layer names LSO registers (see ClientModBusEvents#registerGuiOverlays).
    private static final ResourceLocation LSO_THIRST_LAYER =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "thirst");
    private static final ResourceLocation LSO_HEALTH_OVERHAUL_LAYER =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "health_overhaul");

    // Vanilla's own absorption/shield heart sprite - reused so the golden
    // heart in our list matches what players already recognize.
    private static final ResourceLocation SHIELD_HEART_ICON =
            ResourceLocation.withDefaultNamespace("hud/heart/absorbing_full");

    private static final int ICON_SIZE = 9;
    private static final int LINE_HEIGHT = 12;
    private static final int MARGIN_X = 6;
    private static final int MARGIN_Y = 6;

    private CustomHudOverlay() {}

    // 1) Cancel the vanilla bars so they never render.
    @SubscribeEvent
    public static void onRenderLayer(RenderGuiLayerEvent.Pre event) {
        ResourceLocation name = event.getName();
        if (name.equals(VanillaGuiLayers.PLAYER_HEALTH)
                || name.equals(VanillaGuiLayers.ARMOR_LEVEL)
                || name.equals(VanillaGuiLayers.FOOD_LEVEL)
                || name.equals(VanillaGuiLayers.AIR_LEVEL)
                || name.equals(VanillaGuiLayers.EXPERIENCE_BAR)
                || name.equals(VanillaGuiLayers.EXPERIENCE_LEVEL)
                || name.equals(LSO_THIRST_LAYER)
                || name.equals(LSO_HEALTH_OVERHAUL_LAYER)) {
            event.setCanceled(true);
        }
    }

    // 2) Draw our own list after the vanilla GUI pass.
    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.options.hideGui) {
            return;
        }

        GuiGraphics graphics = event.getGuiGraphics();
        Font font = mc.font;
        int screenHeight = graphics.guiHeight();

        int level = player.experienceLevel;
        int armor = player.getArmorValue();
        int hunger = player.getFoodData().getFoodLevel();
        int maxHunger = 20;
        int thirst = AttachmentUtil.getThirstAttachment(player).getHydrationLevel();
        int maxThirst = ThirstAttachment.MAX_HYDRATION;
        int health = Mth.ceil(player.getHealth());
        int maxHealth = Mth.ceil(player.getMaxHealth());
        float shieldHealth = AttachmentUtil.getHealthAttachment(player).getShieldHealth();
        int shieldHearts = Mth.ceil(shieldHealth / 2.0f); // same conversion LSO itself uses

        // Build from the bottom up so the final visual order (top-to-bottom)
        // is: level, armor, hunger, thirst, health (+ shield hearts).
        int y = screenHeight - MARGIN_Y - LINE_HEIGHT;
        drawHealthRow(graphics, font, MARGIN_X, y, health, maxHealth, shieldHearts);

        y -= LINE_HEIGHT;
        drawThirstRow(graphics, font, thirst, maxThirst, MARGIN_X, y);

        y -= LINE_HEIGHT;
        drawRow(graphics, font, FOOD_ICON, MARGIN_X, y, hunger + "/" + maxHunger);

        y -= LINE_HEIGHT;
        drawRow(graphics, font, ARMOR_ICON, MARGIN_X, y, String.valueOf(armor));

        y -= LINE_HEIGHT;
        drawXpRow(graphics, font, MARGIN_X, y, "Lvl " + level);
    }

    private static void drawRow(GuiGraphics graphics, Font font, ResourceLocation icon,
                                int x, int y, String text) {
        graphics.blitSprite(icon, x, y, ICON_SIZE, ICON_SIZE);
        graphics.drawString(font, text, x + ICON_SIZE + 4, y + 1, 0xFFFFFF, true);
    }

    /**
     * Same as the red heart row, but appends the golden "shield" hearts
     * LSO creates when the player eats a golden apple (vanilla Absorption
     * -> LSO shieldHealth, see CommonNeoForgeEvents around line 421).
     * Only drawn when shieldHearts > 0, right after the red heart count.
     */
    private static void drawHealthRow(GuiGraphics graphics, Font font, int x, int y,
                                      int health, int maxHealth, int shieldHearts) {
        graphics.blitSprite(HEART_ICON, x, y, ICON_SIZE, ICON_SIZE);
        String text = health + "/" + maxHealth;
        graphics.drawString(font, text, x + ICON_SIZE + 4, y + 1, 0xFFFFFF, true);

        if (shieldHearts > 0) {
            int shieldX = x + ICON_SIZE + 4 + font.width(text) + 6;
            graphics.blitSprite(SHIELD_HEART_ICON, shieldX, y, ICON_SIZE, ICON_SIZE);
            graphics.drawString(font, "+" + shieldHearts, shieldX + ICON_SIZE + 3, y + 1, 0xFFD700, true);
        }
    }

    /**
     * Draws the XP orb icon scaled down from its native 16x16 cell (inside
     * the 64x64 vanilla sheet) to match the other 9x9 icons in the list.
     */
    private static void drawXpRow(GuiGraphics graphics, Font font, int x, int y, String text) {
        graphics.blit(XP_ORB_TEXTURE, x, y, ICON_SIZE, ICON_SIZE,
                0f, 0f, XP_ORB_CELL_SIZE, XP_ORB_CELL_SIZE, XP_ORB_SHEET_SIZE, XP_ORB_SHEET_SIZE);
        graphics.drawString(font, text, x + ICON_SIZE + 4, y + 1, 0xFFFFFF, true);
    }

    /**
     * Draws LSO's own hydration droplet icon (not the vanilla food sprite),
     * pulled straight from its overlay.png at the "full" cell.
     */
    private static void drawThirstRow(GuiGraphics graphics, Font font, int thirst, int maxThirst,
                                      int x, int y) {
        graphics.blit(LSO_ICONS, x, y, THIRST_ICON_U, THIRST_ICON_V, ICON_SIZE, ICON_SIZE);
        graphics.drawString(font, thirst + "/" + maxThirst, x + ICON_SIZE + 4, y + 1, 0xFFFFFF, true);
    }
}


