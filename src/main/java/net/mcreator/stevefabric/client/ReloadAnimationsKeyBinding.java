
package net.mcreator.stevefabric.client;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.mcreator.stevefabric.ManegerCustomCode;
import net.mcreator.stevefabric.VariablesCustomCode;
import net.mcreator.stevefabric.screen.ManegGui;
import net.mcreator.stevefabric.screen.SteveGuiGui;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.option.KeyBinding;

import java.nio.channels.NetworkChannel;

public class ReloadAnimationsKeyBinding extends KeyBinding {


	public ReloadAnimationsKeyBinding() {
		super("key.mcreator.reload_animations", GLFW.GLFW_KEY_R, "key.categories.misc");
	}

	public void keyPressed(PlayerEntity entity) {
		World world = entity.world;
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		reloadAnimation();
		setPressed(false);
	}

	public void keyReleased(PlayerEntity entity) {
		World world = entity.world;
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
	}

	public void reloadAnimation(){
		VariablesCustomCode.animationReloader=true;
	}
	


	
}
