package com.sqvizers.forgeborncore.common.machine.multiblock.ward;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;

public final class SanctumWardEventHandler {

    private SanctumWardEventHandler() {}

    public static void register() {
        NeoForge.EVENT_BUS.addListener(SanctumWardEventHandler::onPositionCheck);
        NeoForge.EVENT_BUS.addListener(SanctumWardEventHandler::onJoinLevel);
    }

    private static void onPositionCheck(MobSpawnEvent.PositionCheck event) {
        
        Mob mob = event.getEntity();

        Level level = event.getLevel().getLevel();
        if (MobWardRegistry.isWarded(mob, level, event.getX(), event.getY(), event.getZ())) {
            event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
        }
    }

    private static void onJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || event.loadedFromDisk()) return;

        Entity entity = event.getEntity();
        if (!(entity instanceof Mob mob)) return;

        Level level = event.getLevel();
        if (MobWardRegistry.isWarded(mob, level, entity.getX(), entity.getY(), entity.getZ())) {
            event.setCanceled(true);
        }
    }
}
