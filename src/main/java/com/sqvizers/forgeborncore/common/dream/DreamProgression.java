package com.sqvizers.forgeborncore.common.dream;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

public final class DreamProgression {

    private static final String ROOT = "forgeborncore_dream";
    private static final String READY = "ready";
    private static final String DREAMING = "dreaming"; // NEW
    private static final String UNLOCKED = "unlocked";

    private DreamProgression() {}

    public static boolean isReady(ServerPlayer player) {
        return player.getPersistentData().getBoolean(ROOT + "_" + READY);
    }

    public static void setReady(ServerPlayer player, boolean ready) {
        player.getPersistentData().putBoolean(ROOT + "_" + READY, ready);
    }

    /**
     * NEW: true from the moment a ready player successfully starts sleeping
     * until they press Leave Bed (see DreamNetwork.LeaveBed / PlayerWakeUpEvent).
     * DreamSleepEvents checks this across all sleeping players in the level to
     * decide whether to block the vanilla auto night-skip.
     */
    public static boolean isDreaming(ServerPlayer player) {
        return player.getPersistentData().getBoolean(ROOT + "_" + DREAMING);
    }

    public static void setDreaming(ServerPlayer player, boolean dreaming) {
        player.getPersistentData().putBoolean(ROOT + "_" + DREAMING, dreaming);
    }

    public static Set<String> unlocked(ServerPlayer player) {
        Set<String> result = new HashSet<>();
        ListTag list = player.getPersistentData().getList(ROOT + "_" + UNLOCKED, 8);
        for (int i = 0; i < list.size(); i++) result.add(list.getString(i));
        return result;
    }

    public static boolean unlock(ServerPlayer player, String id) {
        UpgradeNode definition = UpgradeTree.get(id);
        if (definition == null || definition.isRoot()) return false;
        Set<String> unlocked = unlocked(player);
        boolean parentsAvailable = definition.parentId().equals("soul") || unlocked.contains(definition.parentId());
        if (unlocked.contains(id) || !parentsAvailable) return false;
        unlocked.add(id);
        ListTag list = new ListTag();
        unlocked.forEach(value -> list.add(StringTag.valueOf(value)));
        player.getPersistentData().put(ROOT + "_" + UNLOCKED, list);
        definition.onUnlock().accept(player);
        return true;
    }

    public static CompoundTag snapshot(ServerPlayer player) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        unlocked(player).forEach(value -> list.add(StringTag.valueOf(value)));
        tag.put(UNLOCKED, list);
        return tag;
    }

    public static void reapply(ServerPlayer player) {
        for (String id : unlocked(player)) {
            UpgradeNode definition = UpgradeTree.get(id);
            if (definition != null) definition.onUnlock().accept(player);
        }
    }
}
