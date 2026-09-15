package com.sqvizers.forgeborncore.api.registries;

import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import com.mojang.serialization.Codec;

import java.util.function.Supplier;

public final class ModAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister
            .create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, "forgeborncore");

    /**
     * True once the player has consumed a Synapse Interface. Persists across logout
     * (serialize) and is pushed to the owning client automatically whenever it
     * changes (sync) - setData() already calls syncData() internally, no extra
     * call needed.
     */
    public static final Supplier<AttachmentType<Boolean>> SYNAPSE_UNLOCKED = ATTACHMENT_TYPES.register(
            "synapse_unlocked",
            () -> AttachmentType.builder(() -> Boolean.FALSE)
                    .serialize(Codec.BOOL)
                    .sync(ByteBufCodecs.BOOL)
                    .build());

    private ModAttachments() {}
}
