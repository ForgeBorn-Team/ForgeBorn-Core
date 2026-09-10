package com.sqvizers.forgeborncore.common.dream;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

import javax.annotation.Nullable;

public record UpgradeNode(
                          String id,
                          @Nullable String parentId,
                          float x,
                          float y,
                          String title,
                          String description,
                          FrameType frame,
                          ResourceLocation icon,
                          int cost,
                          Consumer<ServerPlayer> onUnlock) {

    public boolean isRoot() {
        return parentId == null;
    }
}
