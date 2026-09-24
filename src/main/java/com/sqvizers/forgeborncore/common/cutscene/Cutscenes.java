package com.sqvizers.forgeborncore.common.cutscene;

import com.sqvizers.forgeborncore.ForgeBornCore;
import com.sqvizers.forgeborncore.client.cutscene.CutsceneManager;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import com.mojang.brigadier.suggestion.SuggestionProvider;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@EventBusSubscriber(modid = ForgeBornCore.MOD_ID)
public final class Cutscenes {

    private Cutscenes() {}

    public static Supplier<Collection<ResourceLocation>> knownCutscenes = List::of;

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_CUTSCENES = SuggestionProviders.register(
            ForgeBornCore.id("cutscenes"),
            (context, builder) -> SharedSuggestionProvider.suggestResource(knownCutscenes.get(), builder));

    public static void play(ServerPlayer player, ResourceLocation id) {
        PacketDistributor.sendToPlayer(player, new PlayCutscenePayload(id));
    }

    public record PlayCutscenePayload(ResourceLocation id) implements CustomPacketPayload {

        public static final Type<PlayCutscenePayload> TYPE = new Type<>(ForgeBornCore.id("play_cutscene"));
        public static final StreamCodec<RegistryFriendlyByteBuf, PlayCutscenePayload> STREAM_CODEC = ResourceLocation.STREAM_CODEC
                .map(PlayCutscenePayload::new, PlayCutscenePayload::id).cast();

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        private static void handle(PlayCutscenePayload payload, IPayloadContext context) {
            CutsceneManager.play(payload.id());
        }
    }

    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToClient(PlayCutscenePayload.TYPE, PlayCutscenePayload.STREAM_CODEC,
                PlayCutscenePayload::handle);
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("cutscene")
                .then(Commands.literal("open")
                        .then(Commands.argument("id", ResourceLocationArgument.id())
                                .suggests(SUGGEST_CUTSCENES)
                                .executes(context -> {
                                    play(context.getSource().getPlayerOrException(),
                                            ResourceLocationArgument.getId(context, "id"));
                                    return 1;
                                })))
                .then(Commands.literal("play")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("id", ResourceLocationArgument.id())
                                        .suggests(SUGGEST_CUTSCENES)
                                        .executes(context -> {
                                            Collection<ServerPlayer> targets = EntityArgument.getPlayers(context,
                                                    "targets");
                                            ResourceLocation id = ResourceLocationArgument.getId(context, "id");
                                            targets.forEach(player -> play(player, id));
                                            context.getSource().sendSuccess(() -> Component.literal(
                                                    "Playing cutscene " + id + " for " + targets.size() +
                                                            " player(s)"),
                                                    true);
                                            return targets.size();
                                        })))));
    }
}
