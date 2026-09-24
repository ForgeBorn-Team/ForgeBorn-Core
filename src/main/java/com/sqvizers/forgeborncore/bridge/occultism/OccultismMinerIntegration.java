package com.sqvizers.forgeborncore.bridge.occultism;

import com.klikli_dev.occultism.client.entities.SpiritJobClient;
import com.klikli_dev.occultism.common.entity.job.SpiritJobFactory;
import com.klikli_dev.occultism.common.entity.spirit.SpiritEntity;
import com.klikli_dev.occultism.registry.OccultismSpiritJobs;
import com.sqvizers.forgeborncore.ForgeBornCore;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

public final class OccultismMinerIntegration {
    private OccultismMinerIntegration() {}

    public static void registerJob(RegisterEvent event) {
        if (!event.getRegistryKey().equals(OccultismSpiritJobs.JOBS_KEY)) return;
        event.register(OccultismSpiritJobs.JOBS_KEY, helper -> helper.register(
                ForgeBornCore.id("miner"),
                new SpiritJobFactory(MinerSpiritJob::new, SpiritJobClient.create("miner"))));
    }

    @SubscribeEvent
    public static void preventMinerFallDamage(LivingFallEvent event) {
        if (event.getEntity() instanceof SpiritEntity spirit
                && spirit.getJob().orElse(null) instanceof MinerSpiritJob) {
            event.setCanceled(true);
        }
    }

}
