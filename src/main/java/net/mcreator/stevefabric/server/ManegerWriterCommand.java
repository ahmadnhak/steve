package net.mcreator.stevefabric.server;

import com.google.gson.Gson;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.mcreator.stevefabric.JsonerCustomCode;
import net.mcreator.stevefabric.ManegerCustomCode;
import net.mcreator.stevefabric.StevefabricMod;
import net.mcreator.stevefabric.VariablesCustomCode;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class ManegerWriterCommand {


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        final LiteralArgumentBuilder<ServerCommandSource> literalargumentbuilder = CommandManager.literal("Maneger").requires((p_198491_0_) -> {
            return p_198491_0_.hasPermissionLevel(1);
        });

        literalargumentbuilder.then(CommandManager.argument("name of pos", StringArgumentType.greedyString()).executes(ManegerWriterCommand::execute));


        dispatcher.register(literalargumentbuilder);
    }

    private static int execute(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerWorld world = ctx.getSource().getWorld();
        double x = ctx.getSource().getPosition().getX();
        double y = ctx.getSource().getPosition().getY();
        double z = ctx.getSource().getPosition().getZ();
        ServerPlayerEntity PlayerEntity = ctx.getSource().getPlayer();
        HashMap<String, String> cmdparams = new HashMap<>();
        int[] index = {-1};
        Arrays.stream(ctx.getInput().split("\\s+")).forEach(param -> {
            if (index[0] >= 0)
                cmdparams.put(Integer.toString(index[0]), param);
            index[0]++;
        });
        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        NbtCompound nbtCompound = new NbtCompound();
        for (String s:cmdparams.keySet()) {
            nbtCompound.putString(s , cmdparams.get(s));
        }
        packetByteBuf.writeNbt(nbtCompound);
      
        ServerPlayNetworking.send(PlayerEntity , StevefabricMod.id("manegerwritecommend"),packetByteBuf );
        


        return 0;
    }

    public static void mangerWriterClient(){
        ClientPlayNetworking.registerGlobalReceiver(StevefabricMod.id("manegerwritecommend"), ((client, handler, buf, responseSender) -> {
            NbtCompound nbtCompound = buf.readNbt();
            HashMap<String , String> param = new HashMap<>();
            for (String s : nbtCompound.getKeys()) {
                param.put(s , nbtCompound.getString(s));
            }
            client.execute(()->{
                String time = ManegerCustomCode.getCurentClientTime().toString();
                HitResult hit = client.crosshairTarget;
                if(hit.getType().equals(HitResult.Type.BLOCK)){
                    BlockHitResult blockHit = (BlockHitResult) hit ;
                    BlockPos blockPos = blockHit.getBlockPos();
                    ManegerCustomCode.posSuggester.put(param.get("0") , blockPos);
                    ManegerCustomCode.posSuggesterkeys.add(param.get("0"));
                    

                }


            });


        }));
    }
}
