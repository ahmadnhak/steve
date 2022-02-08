package net.mcreator.stevefabric;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.SharedConstants;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JigsawBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.client.option.ChatVisibility;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.*;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.filter.TextStream;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;




    public  class FakePlayerCustomCode extends ServerPlayerEntity {
        public FakePlayerCustomCode(ServerWorld world, GameProfile name) {
            super(world.getServer(), world, name, new ServerPlayerInteractionManager(world));
            this.networkHandler = new FakePlayerNetHandler(world.getServer(), this);
        }

        @Override
        public Vec3d getPos() {
            return new Vec3d(0, 0, 0);
        }

        @Override
        public BlockPos getSpawnPointPosition() {
            return BlockPos.ORIGIN;
        }

        @Override
        public void sendMessage(Text chatComponent, boolean actionBar) {
        }

        @Override
        public void sendSystemMessage(Text component, UUID senderUUID) {
        }

        @Override
        public void increaseStat(Stat par1StatBase, int par2) {
        }

        //@Override public void openGui(Object mod, int modGuiId, World world, int x, int y, int z){}
        @Override
        public boolean isInvulnerableTo(DamageSource source) {
            return true;
        }

        @Override
        public boolean shouldDamagePlayer(PlayerEntity player) {
            return false;
        }

        @Override
        public void onDeath(DamageSource source) {
            return;
        }

        @Override
        public void tick() {
            return;
        }

        @Override
        public void setClientSettings(ClientSettingsC2SPacket pkt) {
            return;
        }

        @Override
        @Nullable
        public MinecraftServer getServer() {
            return this.world.getServer();
        }


        private static class FakePlayerNetHandler extends ServerPlayNetworkHandler {
            private static final ClientConnection DUMMY_NETWORK_MANAGER = new ClientConnection(NetworkSide.CLIENTBOUND);

            public FakePlayerNetHandler(MinecraftServer server, ServerPlayerEntity player) {
                super(server, DUMMY_NETWORK_MANAGER, player);
            }

            @Override
            public void tick() {
            }

            @Override
            public void syncWithPlayerPosition() {
            }

            @Override
            public void disconnect(Text message) {
            }

            @Override
            public void onPlayerInput(PlayerInputC2SPacket packet) {
            }

            @Override
            public void onVehicleMove(VehicleMoveC2SPacket packet) {
            }

            @Override
            public void onTeleportConfirm(TeleportConfirmC2SPacket packet) {
            }

            @Override
            public void onRecipeBookData(RecipeBookDataC2SPacket packet) {
            }

            @Override
            public void onRecipeCategoryOptions(RecipeCategoryOptionsC2SPacket packet) {
            }

            @Override
            public void onAdvancementTab(AdvancementTabC2SPacket packet) {
            }

            @Override
            public void onRequestCommandCompletions(RequestCommandCompletionsC2SPacket packet) {
            }

            @Override
            public void onUpdateCommandBlock(UpdateCommandBlockC2SPacket packet) {
            }

            @Override
            public void onUpdateCommandBlockMinecart(UpdateCommandBlockMinecartC2SPacket packet) {
            }

            @Override
            public void onPickFromInventory(PickFromInventoryC2SPacket packet) {
            }

            @Override
            public void onRenameItem(RenameItemC2SPacket packet) {
            }

            @Override
            public void onUpdateBeacon(UpdateBeaconC2SPacket packet) {
            }

            @Override
            public void onStructureBlockUpdate(UpdateStructureBlockC2SPacket packet) {
            }

            @Override
            public void onJigsawUpdate(UpdateJigsawC2SPacket packet) {
            }

            @Override
            public void onJigsawGenerating(JigsawGeneratingC2SPacket packet) {
            }

            @Override
            public void onMerchantTradeSelect(SelectMerchantTradeC2SPacket packet) {
            }

            @Override
            public void onBookUpdate(BookUpdateC2SPacket packet) {
            }

            @Override
            public void onQueryEntityNbt(QueryEntityNbtC2SPacket packet) {
            }

            @Override
            public void onQueryBlockNbt(QueryBlockNbtC2SPacket packet) {
            }

            @Override
            public void onPlayerMove(PlayerMoveC2SPacket packet) {
            }

            @Override
            public void requestTeleport(double x, double y, double z, float yaw, float pitch) {
            }

            @Override
            public void requestTeleport(double x, double y, double z, float yaw, float pitch, Set<PlayerPositionLookS2CPacket.Flag> flags) {
            }

            @Override
            public void onPlayerAction(PlayerActionC2SPacket packet) {
            }

            @Override
            public void onPlayerInteractBlock(PlayerInteractBlockC2SPacket packet) {
            }

            @Override
            public void onPlayerInteractItem(PlayerInteractItemC2SPacket packet) {
            }

            @Override
            public void onSpectatorTeleport(SpectatorTeleportC2SPacket packet) {
            }

            @Override
            public void onResourcePackStatus(ResourcePackStatusC2SPacket packet) {
            }

            @Override
            public void onBoatPaddleState(BoatPaddleStateC2SPacket packet) {
            }

            @Override
            public void onDisconnected(Text reason) {
            }

            @Override
            public void sendPacket(Packet<?> packet) {
            }

            @Override
            public void sendPacket(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> listener) {
            }

            @Override
            public void onUpdateSelectedSlot(UpdateSelectedSlotC2SPacket packet) {
            }

            @Override
            public void onGameMessage(ChatMessageC2SPacket packet) {
            }

            @Override
            public void onHandSwing(HandSwingC2SPacket packet) {
            }

            @Override
            public void onClientCommand(ClientCommandC2SPacket packet) {
            }

            @Override
            public void onPlayerInteractEntity(PlayerInteractEntityC2SPacket packet) {
            }

            @Override
            public void onClientStatus(ClientStatusC2SPacket packet) {
            }

            @Override
            public void onCloseHandledScreen(CloseHandledScreenC2SPacket packet) {
            }

            @Override
            public void onClickSlot(ClickSlotC2SPacket packet) {
            }

            @Override
            public void onCraftRequest(CraftRequestC2SPacket packet) {
            }

            @Override
            public void onButtonClick(ButtonClickC2SPacket packet) {
            }

            @Override
            public void onCreativeInventoryAction(CreativeInventoryActionC2SPacket packet) {
            }

            @Override
            public void onConfirmScreenAction(ConfirmScreenActionC2SPacket packet) {
            }

            @Override
            public void onSignUpdate(UpdateSignC2SPacket packet) {
            }

            @Override
            public void onKeepAlive(KeepAliveC2SPacket packet) {
            }

            @Override
            public void onPlayerAbilities(UpdatePlayerAbilitiesC2SPacket packet) {
            }

            @Override
            public void onClientSettings(ClientSettingsC2SPacket packet) {
            }

            @Override
            public void onCustomPayload(CustomPayloadC2SPacket packet) {
            }

            @Override
            public void onUpdateDifficulty(UpdateDifficultyC2SPacket packet) {
            }

            @Override
            public void onUpdateDifficultyLock(UpdateDifficultyLockC2SPacket packet) {
            }

        }
    }

