package com.sqvizers.forgeborncore.bridge.gregtech;

import com.gregtechceu.gtceu.api.data.worldgen.GTOreDefinition;
import com.gregtechceu.gtceu.api.data.worldgen.generator.veins.NoopVeinGenerator;
import com.gregtechceu.gtceu.api.registry.GTRegistries;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.resource.ContextAwareReloadListener;

/** Replaces every loaded GT vein generator after GTCEu has finished loading its registry. */
public final class GTVeinRewriteListener extends ContextAwareReloadListener {

    public static final GTVeinRewriteListener INSTANCE = new GTVeinRewriteListener();

    private GTVeinRewriteListener() {}

    protected void apply() {
        var lookup = getRegistryLookup().lookupOrThrow(GTRegistries.Keys.ORE_VEIN);
        int total = 0;
        int rewritten = 0;
        for (var holder : lookup.listElements().toList()) {
            total++;
            GTOreDefinition definition = holder.value();
            if (!definition.canGenerate() || definition.veinGenerator() instanceof NoopVeinGenerator ||
                    definition.veinGenerator() instanceof SphericalVeinGenerator)
                continue;
            definition.veinGenerator(SphericalVeinGenerator.fromExisting(definition.veinGenerator(), definition));
            rewritten++;
        }
        com.sqvizers.forgeborncore.ForgeBornCore.LOGGER.info(
                "GT ore vein generation ready: {} definitions loaded, {} converted at reload time; spherical definitions are kept",
                total, rewritten);
    }

    @Override
    public java.util.concurrent.CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier stage,
                                                               ResourceManager resourceManager,
                                                               ProfilerFiller preparationsProfiler,
                                                               ProfilerFiller reloadProfiler,
                                                               java.util.concurrent.Executor backgroundExecutor,
                                                               java.util.concurrent.Executor gameExecutor) {
        return stage.wait(null).thenRunAsync(this::apply, gameExecutor);
    }
}
