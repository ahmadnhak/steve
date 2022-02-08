
package net.mcreator.stevefabric.screen;

import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerEntity;

import net.mcreator.stevefabric.StevefabricMod;

import net.fabricmc.fabric.api.networking.v1.PacketSender;

import java.util.function.Supplier;
import java.util.Map;
import java.util.HashMap;

import io.netty.buffer.Unpooled;

public class ManegGui {
	public static HashMap guistate = new HashMap();
	public static class GuiContainerMod extends ScreenHandler implements Supplier<Map<Integer, Slot>> {
		public World world;
		public PlayerEntity entity;
		public int x, y, z = 0;
		private Map<Integer, Slot> customSlots = new HashMap<>();
		private boolean bound = false;
		private final Inventory inventory;
		public GuiContainerMod(int id, PlayerInventory inv, PacketByteBuf data) {
			this(id, inv, new SimpleInventory(0));
			BlockPos pos;
			if (data != null) {
				pos = data.readBlockPos();
				this.x = pos.getX();
				this.y = pos.getY();
				this.z = pos.getZ();
			}
		}

		public GuiContainerMod(int id, PlayerInventory inv, Inventory inventory) {
			super(StevefabricMod.ManegScreenType, id);
			this.entity = inv.player;
			this.world = inv.player.world;
			this.inventory = inventory;
		}

		public Map<Integer, Slot> get() {
			return customSlots;
		}

		@Override
		public boolean canUse(PlayerEntity player) {
			return true;
		}
	}

	public static class ButtonPressedMessage extends PacketByteBuf {
		public ButtonPressedMessage(int buttonID, int x, int y, int z) {
			super(Unpooled.buffer());
			writeInt(buttonID);
			writeInt(x);
			writeInt(y);
			writeInt(z);
		}

		public static void apply(MinecraftServer server, ServerPlayerEntity entity, ServerPlayNetworkHandler handler, PacketByteBuf buf,
				PacketSender responseSender) {
			int buttonID = buf.readInt();
			double x = buf.readInt();
			double y = buf.readInt();
			double z = buf.readInt();
			server.execute(() -> {
				World world = entity.getServerWorld();
			});
		}
	}

	public static class GUISlotChangedMessage extends PacketByteBuf {
		public GUISlotChangedMessage(int slotID, int x, int y, int z, int changeType, int meta) {
			super(Unpooled.buffer());
			writeInt(slotID);
			writeInt(x);
			writeInt(y);
			writeInt(z);
			writeInt(changeType);
			writeInt(meta);
		}

		public static void apply(MinecraftServer server, ServerPlayerEntity entity, ServerPlayNetworkHandler handler, PacketByteBuf buf,
				PacketSender responseSender) {
			int slotID = buf.readInt();
			double x = buf.readInt();
			double y = buf.readInt();
			double z = buf.readInt();
			int changeType = buf.readInt();
			int meta = buf.readInt();
			server.execute(() -> {
				World world = entity.getServerWorld();
			});
		}
	}
}
