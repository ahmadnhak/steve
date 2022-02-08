package net.mcreator.stevefabric.server;



        import com.mojang.brigadier.builder.LiteralArgumentBuilder;
        import com.mojang.brigadier.suggestion.Suggestion;
        import com.mojang.brigadier.suggestion.SuggestionProvider;
        import com.mojang.brigadier.suggestion.Suggestions;
        import com.mojang.brigadier.suggestion.SuggestionsBuilder;
        import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
        import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
        import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
        import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
        import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
        import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
        import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
        import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
        import net.mcreator.stevefabric.*;
        import net.mcreator.stevefabric.screen.ManegGui;
        import net.minecraft.block.BlockState;
        import net.minecraft.client.MinecraftClient;
        import net.minecraft.client.world.ClientWorld;
        import net.minecraft.command.CommandSource;
        import net.minecraft.entity.player.PlayerEntity;
        import net.minecraft.entity.player.PlayerInventory;
        import net.minecraft.inventory.SimpleInventory;
        import net.minecraft.item.ItemStack;
        import net.minecraft.nbt.NbtCompound;
        import net.minecraft.network.PacketByteBuf;
        import net.minecraft.screen.ScreenHandler;
        import net.minecraft.server.network.ServerPlayerEntity;
        import net.minecraft.server.world.ServerWorld;
        import net.minecraft.server.command.ServerCommandSource;
        import net.minecraft.server.command.CommandManager;
        import net.minecraft.entity.Entity;

        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Arrays;
        import java.util.Locale;
        import java.util.concurrent.CompletableFuture;
        import java.util.stream.Stream;

        import com.mojang.brigadier.context.CommandContext;
        import com.mojang.brigadier.arguments.StringArgumentType;
        import com.mojang.brigadier.CommandDispatcher;
        import net.minecraft.text.LiteralText;
        import net.minecraft.text.Text;
        import net.minecraft.util.Identifier;
        import net.minecraft.util.hit.BlockHitResult;
        import net.minecraft.util.hit.HitResult;
        import net.minecraft.util.math.BlockPos;
        import software.bernie.geckolib3.resource.GeckoLibCache;

        import javax.xml.crypto.Data;

public class BreakBlockCommand {
    private static ServerWorld world;
    private static BreakBlockCustomCode breakBlockCustomCode = new BreakBlockCustomCode();
    private static ServerPlayerEntity serverPlayerEntity;

    
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        final LiteralArgumentBuilder<ServerCommandSource> literalargumentbuilder = CommandManager.literal("breakblock").requires((p_198491_0_) -> {
            return p_198491_0_.hasPermissionLevel(1);
        });

        literalargumentbuilder.executes(BreakBlockCommand::execute);

        dispatcher.register(literalargumentbuilder);


    }

    private static int execute(CommandContext<ServerCommandSource> ctx) {
         world = ctx.getSource().getWorld();
        double x = ctx.getSource().getPosition().getX();
        double y = ctx.getSource().getPosition().getY();
        double z = ctx.getSource().getPosition().getZ();
        serverPlayerEntity = (ServerPlayerEntity) ctx.getSource().getEntity();
        HashMap<String, String> cmdparams = new HashMap<>();
        int[] index = {-1};
        Arrays.stream(ctx.getInput().split("\\s+")).forEach(param -> {
            if (index[0] >= 0)
                cmdparams.put(Integer.toString(index[0]), param);
            index[0]++;
        });
        BlockPos blockPos = new BlockPos(x , y , z);
        serverPlayerEntity.openHandledScreen(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                buf.writeBlockPos(blockPos);

            }

            @Override
            public Text getDisplayName() {
                return new LiteralText("maneg");
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {

                ManegGui.GuiContainerMod guiContainerMod= new ManegGui.GuiContainerMod(syncId, inv, new SimpleInventory(1) );
                return guiContainerMod;
            }
        });
        //PacketByteBuf packetByteBuf = PacketByteBufs.create();
        //ServerPlayNetworking.send(serverPlayerEntity , StevefabricMod.id("breakblockcommand"),packetByteBuf );


        return 0;
    }



    
  

    public static void hit (){

        ClientPlayNetworking.registerGlobalReceiver(StevefabricMod.id("breakblockcommand"), ((client, handler, buf, responseSender) -> {
            client.execute(()->{
                HitResult hit = client.crosshairTarget;
        		if(hit.getType().equals(HitResult.Type.BLOCK)){
         		     BlockHitResult blockHit = (BlockHitResult) hit ;
                    BlockPos blockPos = blockHit.getBlockPos();
                    if(!VariablesCustomCode.curentCharecter.equals("god")){
                        VariablesCustomCode.serverCharecter.get(VariablesCustomCode.curentCharecter).setBreakBlock(blockPos.getX()  ,  blockPos.getY() , blockPos.getZ());
                    }
        		}
            });
        }));
    }




}
