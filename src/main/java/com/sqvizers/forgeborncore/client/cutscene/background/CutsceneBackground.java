package com.sqvizers.forgeborncore.client.cutscene.background;

import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
public interface CutsceneBackground {

    void render(GuiGraphics graphics, BackgroundContext context);

    interface Data extends CutsceneBackground {

        String type();
    }

    record BackgroundContext(int width, int height, float time, float alpha, int mouseX, int mouseY) {}
}
