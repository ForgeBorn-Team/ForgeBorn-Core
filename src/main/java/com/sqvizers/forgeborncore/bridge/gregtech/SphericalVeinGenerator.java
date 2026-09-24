package com.sqvizers.forgeborncore.bridge.gregtech;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.worldgen.GTOreDefinition;
import com.gregtechceu.gtceu.api.data.worldgen.generator.VeinGenerator;
import com.gregtechceu.gtceu.api.data.worldgen.ores.OreBlockPlacer;
import com.gregtechceu.gtceu.api.data.worldgen.ores.OreVeinUtil;
import com.gregtechceu.gtceu.api.registry.GTRegistries;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** A compact, isotropic ore vein made from one solid 3D sphere. */
public final class SphericalVeinGenerator extends VeinGenerator {

    public static final MapCodec<SphericalVeinGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            OreEntry.CODEC.listOf().fieldOf("ores").forGetter(generator -> generator.ores),
            com.mojang.serialization.Codec.intRange(1, 64).fieldOf("radius").forGetter(generator -> generator.radius),
            com.mojang.serialization.Codec.INT.fieldOf("min_y").forGetter(generator -> generator.minY),
            com.mojang.serialization.Codec.INT.fieldOf("max_y").forGetter(generator -> generator.maxY))
            .apply(instance, SphericalVeinGenerator::new));

    private final List<OreEntry> ores;
    private final int radius;
    private final int minY;
    private final int maxY;

    public SphericalVeinGenerator(List<OreEntry> ores, int radius, int minY, int maxY) {
        this.ores = new ArrayList<>(ores);
        this.radius = radius;
        this.minY = minY;
        this.maxY = maxY;
    }

    public static SphericalVeinGenerator fromExisting(VeinGenerator original, GTOreDefinition definition) {
        List<OreEntry> entries = original.getAllEntries().stream()
                .map(entry -> {
                    Material material = entry.mapToMaterial();
                    return material == null ? null : new OreEntry(material, entry.chance());
                })
                .filter(java.util.Objects::nonNull)
                .toList();

        // Keep GT's original size as the input, but make the final body visibly larger
        // than the old compact spheres without allowing huge map-filling deposits.
        int radius = Math.max(12, Math.min(22, (definition.clusterSize().getMaxValue() + 10) / 4));
        int[] yRange = readYRange(definition);
        return new SphericalVeinGenerator(entries, radius, yRange[0], yRange[1]);
    }

    private static int[] readYRange(GTOreDefinition definition) {
        var encoded = net.minecraft.world.level.levelgen.placement.HeightRangePlacement.CODEC.codec()
                .encodeStart(JsonOps.INSTANCE, definition.heightRange()).result().orElse(null);
        if (encoded instanceof com.google.gson.JsonObject object && object.has("height")) {
            var height = object.getAsJsonObject("height");
            var min = height.getAsJsonObject("min_inclusive");
            var max = height.getAsJsonObject("max_inclusive");
            if (min != null && max != null && min.has("absolute") && max.has("absolute")) {
                return new int[] { min.get("absolute").getAsInt(), max.get("absolute").getAsInt() };
            }
        }
        return new int[] { -64, 320 };
    }

    public SphericalVeinGenerator() {
        this(List.of(), 6, -64, 320);
    }

    @Override
    public List<VeinEntry> getAllEntries() {
        return ores.stream()
                .flatMap(ore -> VeinGenerator.mapTarget(Either.right(ore.material), ore.weight))
                .toList();
    }

    @Override
    public Map<BlockPos, OreBlockPlacer> generate(WorldGenLevel level, RandomSource random,
                                                  GTOreDefinition entry, BlockPos origin) {
        Map<BlockPos, OreBlockPlacer> generated = new Object2ObjectOpenHashMap<>();
        int radiusSquared = radius * radius;
        for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-radius, -radius, -radius),
                origin.offset(radius, radius, radius))) {
            int dx = pos.getX() - origin.getX();
            int dy = pos.getY() - origin.getY();
            int dz = pos.getZ() - origin.getZ();
            // GT has already selected the sphere center with the definition's original
            // height_range. Do not apply a second Y filter here: it would change the
            // distribution for relative/triangle/uniform height providers.
            if (dx * dx + dy * dy + dz * dz > radiusSquared || level.isOutsideBuildHeight(pos.getY()))
                continue;

            OreEntry ore = chooseOre(random);
            if (ore == null) continue;
            BlockPos immutablePos = pos.immutable();
            long seed = random.nextLong();
            generated.put(immutablePos,
                    (access, section) -> place(access, section, seed, entry, immutablePos, ore.material));
        }
        return generated;
    }

    private OreEntry chooseOre(RandomSource random) {
        int totalWeight = ores.stream().mapToInt(OreEntry::weight).sum();
        if (totalWeight <= 0) return null;
        int roll = random.nextInt(totalWeight);
        for (OreEntry ore : ores) {
            roll -= ore.weight;
            if (roll < 0) return ore;
        }
        return ores.getLast();
    }

    private static void place(BulkSectionAccess access, LevelChunkSection section, long seed,
                              GTOreDefinition entry, BlockPos pos, Material material) {
        RandomSource random = new net.minecraft.world.level.levelgen.XoroshiroRandomSource(seed);
        int x = SectionPos.sectionRelative(pos.getX());
        int y = SectionPos.sectionRelative(pos.getY());
        int z = SectionPos.sectionRelative(pos.getZ());
        BlockState current = section.getBlockState(x, y, z);
        if (random.nextFloat() > entry.density() ||
                !OreVeinUtil.canPlaceOre(current, access::getBlockState, random, entry, pos))
            return;
        var prefix = ChemicalHelper.getOrePrefix(current);
        if (prefix.isEmpty()) return;
        Block target = ChemicalHelper.getBlock(prefix.get(), material);
        if (target != null) section.setBlockState(x, y, z, target.defaultBlockState(), false);
    }

    @Override
    public SphericalVeinGenerator build() {
        return this;
    }

    @Override
    public SphericalVeinGenerator copy() {
        return new SphericalVeinGenerator(ores, radius, minY, maxY);
    }

    @Override
    public MapCodec<? extends VeinGenerator> codec() {
        return CODEC;
    }

    public static record OreEntry(Material material, int weight) {

        public static final com.mojang.serialization.Codec<OreEntry> CODEC = RecordCodecBuilder
                .create(instance -> instance.group(
                        GTRegistries.MATERIALS.byNameCodec().fieldOf("material").forGetter(OreEntry::material),
                        com.mojang.serialization.Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight")
                                .forGetter(OreEntry::weight))
                        .apply(instance, OreEntry::new));
    }
}
