/*
 *    MCreator note:
 *
 *    If you lock base mod element files, you can edit this file and the proxy files
 *    and they won't get overwritten. If you change your mod package or modid, you
 *    need to apply these changes to this file MANUALLY.
 *
 *
 *    If you do not lock base mod element files in Workspace settings, this file
 *    will be REGENERATED on each build.
 *
 */
package net.mcreator.stevefabric;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.mcreator.stevefabric.client.gui.screen.ManegGuiWindow;
import net.mcreator.stevefabric.entity.BaseEntity;
import net.mcreator.stevefabric.screen.ManegGui;
import net.mcreator.stevefabric.server.BreakBlockCommand;
import net.mcreator.stevefabric.server.ManegerWriterCommand;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraft.util.Identifier;
import net.minecraft.screen.ScreenHandlerType;

import net.mcreator.stevefabric.screen.SteveGuiGui;


import net.mcreator.stevefabric.client.gui.screen.SteveGuiGuiWindow;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.api.ModInitializer;

import software.bernie.geckolib3.GeckoLib;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class StevefabricMod implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final ArrayList<String> carecters = new ArrayList<>();
	public static final ScreenHandlerType<SteveGuiGui.GuiContainerMod> SteveGuiScreenType = ScreenHandlerRegistry.registerExtended(id("steve_gui"),
			SteveGuiGui.GuiContainerMod::new);

	public static final ScreenHandlerType<ManegGui.GuiContainerMod> ManegScreenType = ScreenHandlerRegistry.registerExtended(id("maneg_gui"),
			ManegGui.GuiContainerMod::new);
	@Override
	public void onInitialize() {
		LOGGER.info("Initializing StevefabricMod");


		SteveGuiGuiWindow.screenInit();
		ManegGuiWindow.screenInit();


		try{
			String sourcstring = new String(Files.readAllBytes(Paths.get("C:\\Users\\Ahmad\\Desktop\\source\\charecters.json")));
			JsonParser parser = new JsonParser() ;
			JsonObject jsonObject = (JsonObject)parser.parse(sourcstring);
			JsonArray jsonArray = jsonObject.getAsJsonArray("charecters");
			ArrayList<String> arraylist = new ArrayList<>();
			Gson gson = new Gson();
			arraylist = gson.fromJson(jsonArray , arraylist.getClass());

			for(String s : arraylist){
				carecters.add(s);
			}
		}
		catch (java.io.IOException e){
			e.printStackTrace();
		}


		for(String name : carecters ){
			BaseEntity.init(name);
		}
		
		
		GeckoLib.initialize();
		ServerTickEvents.END_SERVER_TICK.register((server )->{
			server.execute(()->{
				ManegerCustomCode.serverManeger(server);
			});
		});
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			ManegerWriterCommand.register(dispatcher);
			BreakBlockCommand.register(dispatcher);
			
		});
	}

	public static final Identifier id(String s) {
		return new Identifier("stevefabric", s);
	}
}
