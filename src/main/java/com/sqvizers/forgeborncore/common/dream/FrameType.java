package com.sqvizers.forgeborncore.common.dream;

import com.sqvizers.forgeborncore.ForgeBornCore;

import net.minecraft.resources.ResourceLocation;

public enum FrameType {

    REGULAR("regular_upgrade_icon"),
    MILESTONE("milestone_upgrade_icon"),
    QOL("qol_upgrade_icon");

    private final ResourceLocation texture;

    FrameType(String texture) {
        this.texture = ForgeBornCore.id("textures/gui/upgrade/" + texture + ".png");
    }

    public ResourceLocation texture() {
        return texture;
    }
}
