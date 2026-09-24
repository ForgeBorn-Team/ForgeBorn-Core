package com.sqvizers.forgeborncore.bridge.occultism;

import com.klikli_dev.occultism.common.entity.job.SpiritJob;
import com.klikli_dev.occultism.common.entity.spirit.SpiritEntity;
import com.klikli_dev.occultism.registry.OccultismItems;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Occultism foliot job with explicit walking, mining, and storage phases. */
public class MinerSpiritJob extends SpiritJob {
    public static final ResourceLocation FACTORY_ID = ResourceLocation.fromNamespaceAndPath("forgeborncore", "miner");
    private static final TagKey<Block> COMMON_ORES = TagKey.create(
            net.minecraft.core.registries.Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("c", "ores"));
    private static final TagKey<Block> FORGE_ORES = TagKey.create(
            net.minecraft.core.registries.Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("forge", "ores"));
    private static final int SEARCH_RADIUS = 8;
    private static final int ORES_PER_TRIP = 64;
    private static final int BREAK_TICKS = 10;

    private BlockPos containerPos;
    private BlockPos workPos;
    private BlockPos currentOre;
    private BlockPos oreApproach;
    private BlockPos breakingOre;
    private int breakingTicks;
    private int minedSinceDeposit;
    private boolean returningToStorage;
    private final List<ItemStack> buffer = new ArrayList<>();

    public MinerSpiritJob(SpiritEntity entity) {
        super(entity);
    }

    @Override
    protected void onInit() {
        entity.setNoGravity(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public BrainActivityGroup<SpiritEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTargetSink(8, 8),
                new MoveToWalkTarget<>()
        );
    }

    @Override
    public void cleanup() {
        clearWalkTarget();
        entity.setDeltaMovement(Vec3.ZERO);
    }

    @Override
    public void update() {
        entity.setNoGravity(false);
        entity.noPhysics = false;
        syncBookTargets();
        if (!(entity.level() instanceof net.minecraft.server.level.ServerLevel level)
                || containerPos == null || workPos == null) return;
        if (!level.hasChunkAt(containerPos) || !level.hasChunkAt(workPos)) return;

        if (breakingOre != null) {
            tickBreaking(level);
            return;
        }

        if (returningToStorage || minedSinceDeposit >= ORES_PER_TRIP
                || (currentOre == null && !buffer.isEmpty() && findOre(level) == null)) {
            returningToStorage = true;
            tickStorage(level);
            return;
        }

        if (currentOre == null || !isOre(level.getBlockState(currentOre))) {
            currentOre = findOre(level);
            oreApproach = currentOre == null ? null : findApproach(level, currentOre);
        }
        if (currentOre == null || oreApproach == null) {
            clearWalkTarget();
            return;
        }

        if (entity.distanceToSqr(Vec3.atCenterOf(oreApproach)) <= 9.0) {
            clearWalkTarget();
            breakingOre = currentOre;
            breakingTicks = 0;
            currentOre = null;
            oreApproach = null;
            return;
        }

        setWalkTarget(oreApproach);
    }

    private void tickBreaking(net.minecraft.server.level.ServerLevel level) {
        BlockState state = level.getBlockState(breakingOre);
        if (!isOre(state)) {
            level.destroyBlockProgress(entity.getId(), breakingOre, -1);
            breakingOre = null;
            return;
        }
        entity.setNoGravity(false);
        entity.setDeltaMovement(Vec3.ZERO);
        level.destroyBlockProgress(entity.getId(), breakingOre, Math.min(9, breakingTicks));
        if (breakingTicks == 0) {
            level.playSound(null, breakingOre, state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        if (breakingTicks++ < BREAK_TICKS - 1) return;

        BlockEntity blockEntity = level.getBlockEntity(breakingOre);
        List<ItemStack> drops = Block.getDrops(state, level, breakingOre, blockEntity, entity,
                new ItemStack(OccultismItems.IESNIUM_PICKAXE.get()));
        for (ItemStack drop : drops) buffer.add(drop.copy());
        level.removeBlock(breakingOre, false);
        level.levelEvent(2001, breakingOre, Block.getId(state));
        level.destroyBlockProgress(entity.getId(), breakingOre, -1);
        breakingOre = null;
        minedSinceDeposit++;
    }

    private void tickStorage(net.minecraft.server.level.ServerLevel level) {
        BlockPos target = containerPos.above();
        if (entity.distanceToSqr(Vec3.atCenterOf(target)) <= 9.0) {
            clearWalkTarget();
            deposit(level);
            return;
        }
        setWalkTarget(target);
    }

    private void syncBookTargets() {
        BlockPos selectedContainer = entity.getDepositPosition().orElse(null);
        BlockPos selectedWork = entity.getWorkAreaPosition().orElse(null);
        if (!Objects.equals(containerPos, selectedContainer) || !Objects.equals(workPos, selectedWork)) {
            containerPos = selectedContainer == null ? null : selectedContainer.immutable();
            workPos = selectedWork == null ? null : selectedWork.immutable();
            currentOre = null;
            oreApproach = null;
            returningToStorage = false;
            resetMovement();
        }
    }

    private void deposit(net.minecraft.server.level.ServerLevel level) {
        for (ItemStack stack : buffer) {
            ItemStack remainder = insert(level, containerPos, stack);
            if (!remainder.isEmpty()) {
                level.addFreshEntity(new net.minecraft.world.entity.item.ItemEntity(
                        level, entity.getX(), entity.getY(), entity.getZ(), remainder));
            }
        }
        buffer.clear();
        minedSinceDeposit = 0;
        returningToStorage = false;
        resetMovement();
    }

    private void setWalkTarget(BlockPos target) {
        BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new BlockPosTracker(target));
        BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(target, 1.0F, 1));
    }

    private void clearWalkTarget() {
        entity.setNoGravity(false);
        entity.getNavigation().stop();
        BrainUtils.clearMemory(entity, MemoryModuleType.LOOK_TARGET);
        BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
        entity.setDeltaMovement(Vec3.ZERO);
    }

    private void resetMovement() {
        clearWalkTarget();
    }

    private BlockPos findOre(net.minecraft.server.level.ServerLevel level) {
        BlockPos best = null;
        double bestDistance = Double.MAX_VALUE;
        for (BlockPos pos : BlockPos.betweenClosed(
                workPos.offset(-SEARCH_RADIUS, -SEARCH_RADIUS, -SEARCH_RADIUS),
                workPos.offset(SEARCH_RADIUS, SEARCH_RADIUS, SEARCH_RADIUS))) {
            if (!isOre(level.getBlockState(pos))) continue;
            double distance = entity.distanceToSqr(Vec3.atCenterOf(pos));
            if (distance < bestDistance) {
                bestDistance = distance;
                best = pos.immutable();
            }
        }
        return best;
    }

    private BlockPos findApproach(net.minecraft.server.level.ServerLevel level, BlockPos ore) {
        BlockPos[] candidates = {ore.above(), ore.north(), ore.south(), ore.east(), ore.west(), ore.below()};
        BlockPos best = null;
        double bestDistance = Double.MAX_VALUE;
        for (BlockPos candidate : candidates) {
            if (!level.getBlockState(candidate).getCollisionShape(level, candidate).isEmpty()) continue;
            double distance = entity.distanceToSqr(Vec3.atCenterOf(candidate));
            if (distance < bestDistance) {
                bestDistance = distance;
                best = candidate;
            }
        }
        return best;
    }

    private boolean isOre(BlockState state) {
        return state.is(COMMON_ORES) || state.is(FORGE_ORES);
    }

    private ItemStack insert(net.minecraft.server.level.ServerLevel level, BlockPos pos, ItemStack stack) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof Container container) {
            ItemStack remaining = stack.copy();
            for (int slot = 0; slot < container.getContainerSize() && !remaining.isEmpty(); slot++) {
                if (!container.canPlaceItem(slot, remaining)) continue;
                ItemStack inSlot = container.getItem(slot);
                if (inSlot.isEmpty()) {
                    container.setItem(slot, remaining.split(Math.min(remaining.getCount(), remaining.getMaxStackSize())));
                } else if (ItemStack.isSameItemSameComponents(inSlot, remaining)
                        && inSlot.getCount() < Math.min(inSlot.getMaxStackSize(), container.getMaxStackSize())) {
                    int amount = Math.min(remaining.getCount(),
                            Math.min(inSlot.getMaxStackSize(), container.getMaxStackSize()) - inSlot.getCount());
                    inSlot.grow(amount);
                    remaining.shrink(amount);
                    container.setChanged();
                }
            }
            return remaining;
        }
        IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
        return handler == null ? stack : ItemHandlerHelper.insertItemStacked(handler, stack, false);
    }

    @Override
    public CompoundTag writeJobToNBT(CompoundTag tag, HolderLookup.Provider registries) {
        super.writeJobToNBT(tag, registries);
        if (containerPos != null) tag.putLong("container", containerPos.asLong());
        if (workPos != null) tag.putLong("work", workPos.asLong());
        return tag;
    }

    @Override
    public void readJobFromNBT(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains("container")) containerPos = BlockPos.of(tag.getLong("container"));
        if (tag.contains("work")) workPos = BlockPos.of(tag.getLong("work"));
    }
}
