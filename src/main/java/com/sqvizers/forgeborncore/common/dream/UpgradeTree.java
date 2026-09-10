package com.sqvizers.forgeborncore.common.dream;

import com.sqvizers.forgeborncore.ForgeBornCore;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class UpgradeTree {

    private static final ResourceLocation MELEE_ICON = icon("melee_upgrade");
    private static final ResourceLocation MINING_ICON = icon("mining_upgrade");
    private static final ResourceLocation MOVEMENT_ICON = icon("movement_upgrade");
    private static final ResourceLocation DASH_ICON = icon("dash_upgrade");
    private static final Map<String, UpgradeNode> NODES = new LinkedHashMap<>();

    static {
        register(new UpgradeNode("soul", null, 0, 0, "Soul", "The beginning point of the dream.", FrameType.QOL,
                icon("qol_upgrade_icon"), 0, player -> {}));
        register(new UpgradeNode("dmg1", "soul", -230, -130, "+1 Damage", "Your attacks deal one additional damage.",
                FrameType.REGULAR, MELEE_ICON, 1,
                player -> addModifier(player, Attributes.ATTACK_DAMAGE, "dream_damage_1", 1.0D)));
        register(new UpgradeNode("speed1", "soul", 230, -130, "+0.2 Movement Speed",
                "Your movement speed is increased by 0.2.", FrameType.REGULAR, MOVEMENT_ICON, 1,
                player -> addModifier(player, Attributes.MOVEMENT_SPEED, "dream_movement_speed_1", 0.2D)));
        register(new UpgradeNode("harvest1", "soul", -270, 130, "+0.2 Harvesting Speed",
                "Your mining efficiency is increased by 0.2.", FrameType.QOL, MINING_ICON, 1,
                player -> addModifier(player, Attributes.MINING_EFFICIENCY, "dream_mining_efficiency_1", 0.2D)));
        register(new UpgradeNode("dash", "speed1", 390, 30, "Swift Step", "Gain another 0.1 movement speed.",
                FrameType.QOL, DASH_ICON, 1,
                player -> addModifier(player, Attributes.MOVEMENT_SPEED, "dream_movement_speed_2", 0.1D)));
        register(new UpgradeNode("mining2", "harvest1", -420, 35, "Deep Delving", "Gain another 0.2 harvesting speed.",
                FrameType.REGULAR, MINING_ICON, 1,
                player -> addModifier(player, Attributes.MINING_EFFICIENCY, "dream_mining_efficiency_2", 0.2D)));
        register(new UpgradeNode("milestone", "dmg1", 0, 235, "Awakened Soul", "Gain one more attack damage.",
                FrameType.MILESTONE, MELEE_ICON, 1,
                player -> addModifier(player, Attributes.ATTACK_DAMAGE, "dream_damage_2", 1.0D)));
    }

    private UpgradeTree() {}

    public static void register(UpgradeNode node) {
        if (NODES.putIfAbsent(node.id(), node) != null) {
            throw new IllegalArgumentException("Duplicate dream upgrade id: " + node.id());
        }
    }

    public static Collection<UpgradeNode> all() {
        return Collections.unmodifiableCollection(NODES.values());
    }

    public static UpgradeNode get(String id) {
        return NODES.get(id);
    }

    private static ResourceLocation icon(String name) {
        return ForgeBornCore.id("textures/gui/upgrade/" + name + ".png");
    }

    private static void addModifier(ServerPlayer player, Holder<Attribute> attribute, String id, double amount) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null) {
            instance.addOrReplacePermanentModifier(new AttributeModifier(ForgeBornCore.id(id), amount,
                    AttributeModifier.Operation.ADD_VALUE));
        }
    }
}
