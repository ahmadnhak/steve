
package net.mcreator.stevefabric;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.mcreator.stevefabric.entity.BaseEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Function;


public class VariablesCustomCode {
	public static boolean animationReloader=false;
	public static HashMap<String , BaseEntity> clientCharecter = new HashMap<>();
	public static HashMap<String , BaseEntity> serverCharecter = new HashMap<>();
	public static HashMap<Integer , String> charectersName = new HashMap<>();
	public static String curentCharecter = "god" ;
	public static String curentTime = "0" ;




	public static void setNewCharecter( String name ,BaseEntity baseEntity , ServerPlayerEntity playerEntity){

		serverCharecter.put(name , baseEntity);
		charectersName.put(baseEntity.getEntityId() , name );
		PacketByteBuf packetByteBuf = PacketByteBufs.create();
		packetByteBuf.writeInt(baseEntity.getEntityId());
		packetByteBuf.writeString(name);
		ServerPlayNetworking.send(playerEntity , StevefabricMod.id("makenewcharecter") ,packetByteBuf );



	}

	public static void setCurentCharecter (String name){
		curentCharecter=name;
	}

	public  static  void  makeNewCharecterClient(){
		ClientPlayNetworking.registerGlobalReceiver(StevefabricMod.id("makenewcharecter") , ((client, handler, buf, responseSender) -> {
			int id = buf.readInt();
			String name	= buf.readString();
			client.execute(()->{
				clientCharecter.put(name , (BaseEntity) client.world.getEntityById(id));
			});
		}));
	}

	public static void removeCharecter(int id){
		if(!charectersName.isEmpty()){
			if(charectersName.get(id).equals(curentCharecter)){
				curentCharecter="god";
			}
			clientCharecter.remove(charectersName.get(id));
			serverCharecter.remove(charectersName.get(id));
			charectersName.remove(id);
			ManegerCustomCode.getManeger().removCharecter(charectersName.get(id));
		}
		

	}

	public static void unloadCharecters(){
		clientCharecter.clear();
		serverCharecter.clear();
		charectersName.clear();
		curentCharecter="god";


	}


	public static BaseEntity getEntityAt (ServerWorld world ,  EntityType<BaseEntity> entityType  , double x , double y , double z , int rad ){
		return ((BaseEntity) world
				.getEntitiesByType(entityType,
						new Box(x - (rad / 2d), y - (rad / 2d), z - (rad / 2d), x + (rad / 2d), y + (rad / 2d), z + (rad / 2d)), null)
				.stream().sorted(new Object() {
					Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
						return Comparator.comparing((Function<Entity, Double>) (_entcnd -> _entcnd.squaredDistanceTo(_x, _y, _z)));
					}
				}.compareDistOf(x, y, z)).findFirst().orElse(null));
	}



}
