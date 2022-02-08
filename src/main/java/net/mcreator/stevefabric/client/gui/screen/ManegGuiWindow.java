
package net.mcreator.stevefabric.client.gui.screen;

import com.google.gson.Gson;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.SingleRedirectModifier;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.mcreator.stevefabric.ManegerCustomCode;
import net.mcreator.stevefabric.StevefabricMod;
import net.mcreator.stevefabric.VariablesCustomCode;
import net.mcreator.stevefabric.server.ManegerWriterCommand;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.SelectMerchantTradeC2SPacket;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.CharacterVisitor;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;
import net.minecraft.util.Identifier;
import net.minecraft.text.Text;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.MinecraftClient;

import net.mcreator.stevefabric.screen.ManegGui;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

import com.mojang.blaze3d.systems.RenderSystem;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ManegGuiWindow extends HandledScreen<ManegGui.GuiContainerMod> {
	private World world;
	private int positionX, positionY, positionZ;
	private PlayerEntity entity;
    private static final Identifier TEXTURE = new Identifier("stevefabric:textures/maneg.png");
	private static final ManegGuiWindow.WidgetButtonPage[] scroolBottons = new ManegGuiWindow.WidgetButtonPage[7];
	private static final ButtonWidget[] initialButton = new ButtonWidget[2]  ;
	private static int indexStartOffset = 0;
	private static int selectedIndex;
	private static boolean scrolling;
	private static boolean initialed = false ;
	private static Arguments scrollstat ;
	private static int  scrolstatSize ;
	private static ArrayList<String> param = new ArrayList<>();
	private static TextFieldWidget textField  ;
	private static String wijbot0Stat = "";
	private static HashMap<String , Barinfo> barinfos = new HashMap<>();
    private static int choosedtimeY;
    public static boolean ontimerun = false ;



	public ManegGuiWindow(ManegGui.GuiContainerMod container, PlayerInventory inventory, Text text) {
		super(container, inventory, text);
		this.world = container.world;
		this.positionX = container.x;
		this.positionY = container.y;
		this.positionZ = container.z;
		this.entity = container.entity;
		this.backgroundWidth =  377 ;
		this.backgroundHeight = 194 ;
	}

	private void syncButtonIndex(ManegGuiWindow.WidgetButtonPage buttonPage,  MinecraftClient client) {
		param.add(buttonPage.getMessage().getString());

		if(scrollstat!=null){
			if(scrollstat.stage==1){
				if(scrollstat.nextkey==null){

					this.scrollstat = Suggest.suggests.get(buttonPage.getMessage().getString());
				}
				else {

					this.scrollstat = Suggest.suggests.get(scrollstat.nextkey);
				}
				
				if(scrollstat.stage==3){

					execute();
				}
			}
			else if(scrollstat.stage==2){

				execute();
			}
			
		}
		else{
			this.scrollstat = Suggest.suggests.get(buttonPage.getMessage().getString());
		}
	}

	private void execute(){
		initialButton[0].setMessage(new LiteralText("Menue"));
		this.scrollstat=null;
		for(int l = 0; l < 7; ++l) {
            this.scroolBottons[l].active=true;
			this.scroolBottons[l].visible=false;
		}
		PacketByteBuf packetByteBuf = PacketByteBufs.create();
		NbtCompound nbtCompound = new NbtCompound();
		for (int s = 0 ;  s<param.size() ; s++) {
			nbtCompound.putString(Integer.toString(s)  , param.get(s));
		}
		packetByteBuf.writeNbt(nbtCompound);
		if(param.get(0).equals("charecter")) {
			if (param.get(1).equals("charecterManeger")) {
				if(param.get(2).equals("breakBlock")){
					if(param.get(3).equals("setBreakBlock")){
						BlockPos blockPos = ManegerCustomCode.posSuggester.get(param.get(4));
						BlockState blockState = client.world.getBlockState(blockPos) ;
						Identifier id = Registry.BLOCK.getId(blockState.getBlock());
						String idString =id.getNamespace()+":"+id.getPath();
						ManegerCustomCode.getManeger().setBreakBlock(VariablesCustomCode.curentTime ,VariablesCustomCode.curentCharecter , blockPos.getX()  ,  blockPos.getY() , blockPos.getZ() , idString);
					}
					else if(param.get(3).equals("removeBreakBlock")){
						ManegerCustomCode.getManeger().removeBreakBlock(VariablesCustomCode.curentTime ,VariablesCustomCode.curentCharecter);
					}
				}
			}
		}
		ClientPlayNetworking.send( StevefabricMod.id("managgui"),packetByteBuf );
		param.clear();
	}



	@Override
	public void init(MinecraftClient client, int width, int height) {
		super.init(client, width, height);

		if(!ontimerun){
            client.keyboard.setRepeatEvents(true);
            this.client.getTextureManager().bindTexture(TEXTURE);
            int i = (this.width - this.backgroundWidth) / 2;
            int j = (this.height - this.backgroundHeight) / 2;
            int k = j + 11;
            initialButton[0] = this.addButton(new ButtonWidget(i+4 , j+152 , 45 , 18 ,new LiteralText("Menue")   , buttonWidget ->{
                wijbot0Stat=buttonWidget.getMessage().getString();
                if(wijbot0Stat.equals("Cancle")){
               
                    this.scrollstat=null;
                    param.clear();
                    buttonWidget.setMessage(new LiteralText("Menue"));
                    
                    for(int l = 0; l < 7; ++l) {
                   		this.scroolBottons[l].active=true;
						this.scroolBottons[l].visible=false;
                    }
                }
            } ));

            initialButton[1] = this.addButton(new ButtonWidget(i+50 , j+152 , 45 , 18 ,new LiteralText("set")   , buttonWidget ->{
                param.add(textField.getText());
                
                if(scrollstat.stage==1){
                    if(scrollstat.nextkey!=null){
                        scrollstat=Suggest.suggests.get(scrollstat.nextkey);
                    }
                }
                else if(scrollstat.stage==2){
                    execute();
                }
                buttonWidget.active=false;
				textField.visible=false;

            } ));

            initialButton[1].active=false;



            barinfos.put("Movements" , new Barinfo(false , ManegerCustomCode.getCurentClientTime() , new HashMap<Integer , Integer>()));
            barinfos.put("Animatios" , new Barinfo(false , ManegerCustomCode.getCurentClientTime(),new HashMap<Integer , Integer>()));
            barinfos.put("Breakblocks" , new Barinfo(false , ManegerCustomCode.getCurentClientTime(),new HashMap<Integer , Integer>()));



            textField = new TextFieldWidget(this.textRenderer, i+4 , j+172 , 120, 18 , LiteralText.EMPTY);
            textField.setMaxLength(32767);
            this.children.add(this.textField);
            textField.visible=false;

            for(int l = 0; l < 7; ++l) {
                this.scroolBottons[l] = this.addButton(new ManegGuiWindow.WidgetButtonPage(i + 5, k, l, (button) -> {
                    if (button instanceof ManegGuiWindow.WidgetButtonPage) {
                        this.selectedIndex = ((ManegGuiWindow.WidgetButtonPage)button).getIndex() + this.indexStartOffset;
                        this.syncButtonIndex((WidgetButtonPage) button,  client);
                    }
                }));
                k+= 20;
            }
        }
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, partialTicks);
		this.client.getTextureManager().bindTexture(TEXTURE);
		textField.render(matrices, mouseX, mouseY, partialTicks);
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;


        barmaneger(matrices , i , j , mouseX , mouseY);
		if(scrollstat!=null){
			scrollManeger(matrices , i , j , scrollstat , mouseX , mouseY , partialTicks );

            
		}
		else if( wijbot0Stat.equals("Menue") ){
			if(!ManegerCustomCode.initialed){

                Suggest.preinitial();
				scrolstatSize=1;
				this.renderScrollbar(matrices , i , j);

				ArrayList<String> arrayList = new ArrayList<>();
				arrayList.add("initial");
				Iterator<String> stringIterator = arrayList.iterator() ;

				ManegGuiWindow.WidgetButtonPage[] var19 = this.scroolBottons;
				int var20 = var19.length;

				for(int var21 = 0; var21 < var20; ++var21) {
					ManegGuiWindow.WidgetButtonPage widgetButtonPage = var19[var21];
					widgetButtonPage.visible = widgetButtonPage.index < 1;
					if(stringIterator.hasNext()){
						widgetButtonPage.setMessage(new LiteralText(stringIterator.next()));
					}
				}
				
			}
			else {
				
				this.client.getTextureManager().bindTexture(TEXTURE);
				scrolstatSize=2;
				this.renderScrollbar(matrices , i , j);

				ArrayList<String> arrayList = new ArrayList<>();
				arrayList.add("initial");
				arrayList.add("charecter");
				Suggest.initial();
				Iterator<String> stringIterator = arrayList.iterator() ;

				ManegGuiWindow.WidgetButtonPage[] var19 = this.scroolBottons;
				int var20 = var19.length;

				for(int var21 = 0; var21 < var20; ++var21) {
					ManegGuiWindow.WidgetButtonPage widgetButtonPage = var19[var21];
					widgetButtonPage.visible = widgetButtonPage.index < 2;
					if(stringIterator.hasNext()){
						widgetButtonPage.setMessage(new LiteralText(stringIterator.next()));
					}
				}


			}
			initialButton[0].setMessage( new LiteralText("Cancle"));

		}

		
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}


	public void scrollManeger(MatrixStack matrices, int i, int j , Arguments arguments ,  int mouseX, int mouseY, float partialTicks){
		
		if(arguments.canAdNewArg){
			textField.visible=true;
			textField.setText(arguments.textfiled);
			textField.render(matrices, mouseX, mouseY, partialTicks);
			initialButton[1].active=true;
		}
		else {
			textField.visible=false;
			//textField.setText("");
			initialButton[1].active=false;
		}
		ArrayList<String> arrayList = new ArrayList<>();
		int s = 0 ;
		if(arguments.list==null){
			scrolstatSize=0;
		}else {
			this.renderScrollbar(matrices , i , j);
			scrolstatSize=arguments. list.size();
			for(int m = indexStartOffset ; m<arguments.list.size() && m<indexStartOffset+7 ; m++){
				arrayList.add(arguments.list.get(m));
				s++;
			}
		}

		Iterator<String> stringIterator = arrayList.iterator() ;

		ManegGuiWindow.WidgetButtonPage[] var19 = this.scroolBottons;
		int var20 = var19.length;

		for(int var21 = 0; var21 < var20; ++var21) {
			ManegGuiWindow.WidgetButtonPage widgetButtonPage = var19[var21];
			widgetButtonPage.visible = widgetButtonPage.index < s;
			if(!arguments.isscroolkeysActive){
				widgetButtonPage.active=false;
			}
			else {
				widgetButtonPage.active=true;
			}

			if(stringIterator.hasNext()){
				widgetButtonPage.setMessage(new LiteralText(stringIterator.next()));
			}
		}

	}


	public void barmaneger(MatrixStack matrices, int i, int j , int mouseX, int mouseY){
        if(!VariablesCustomCode.curentCharecter.equals("god") && ManegerCustomCode.initialed){
            if(!ManegerCustomCode.getManeger().finderMovmentTime(VariablesCustomCode.curentCharecter).isEmpty()){
                drawTexture(matrices, i+126 , j+23 , this.getZOffset() ,  12.0F, 202.0F, 237, 2, 256, 512);
                barinfos.get("Movements").isactive=true;
                int beginvalue= barinfos.get("Movements").beginvalue;
                ArrayList<Integer> movtimes= ManegerCustomCode.getManeger().finderMovmentTime(VariablesCustomCode.curentCharecter);
                for (int p : movtimes ){
                    if(p<=beginvalue+60 && p>=beginvalue){
                        drawTexture(matrices, i+125+((p-beginvalue)*4) , j+23 , this.getZOffset() ,  12.0F, 199.0F, 3, 3, 256, 512);

                    }
                }
                ArrayList<Integer> endticks = ManegerCustomCode.getManeger().getMovmentEndTick(VariablesCustomCode.curentCharecter);
				for (int p : endticks ){
					if(p<=beginvalue+60 && p>=beginvalue){
						drawTexture(matrices, i+125+((p-beginvalue)*4) , j+23 , this.getZOffset() ,  21.0F, 199.0F, 3, 3, 256, 512);

					}
				}
                int curenttime = ManegerCustomCode.getCurentClientTime();
                if(curenttime<=beginvalue+60 && curenttime>=beginvalue){
                    drawTexture(matrices, i+126+((curenttime-beginvalue)*4)-1 , j+20 , this.getZOffset() ,  15.0F, 199.0F, 3, 3, 256, 512);
                }
            }
            else {
				barinfos.get("Movements").isactive=false;
			}
            if(!ManegerCustomCode.getManeger().finderAnimeTime(VariablesCustomCode.curentCharecter).isEmpty()){
                drawTexture(matrices, i+126 , j+73 , this.getZOffset() ,  12.0F, 202.0F, 237, 2, 256, 512);
				barinfos.get("Animatios").isactive=true;
                int beginvalue= barinfos.get("Animatios").beginvalue;
                ArrayList<Integer> movtimes= ManegerCustomCode.getManeger().finderAnimeTime(VariablesCustomCode.curentCharecter);
                for (int p : movtimes ){
                    if(p<=beginvalue+60 && p>=beginvalue){
                        drawTexture(matrices, i+125+((p-beginvalue)*4) , j+73 , this.getZOffset() ,  12.0F, 199.0F, 3, 3, 256, 512);
                        //barinfos.get("Animatios").curentreddotspos.put(i+126+((p-beginvalue)*4) , p);
                    }
                }
                int curenttime = ManegerCustomCode.getCurentClientTime();
                if(curenttime<=beginvalue+60 && curenttime>=beginvalue){
                    drawTexture(matrices, i+126+((curenttime-beginvalue)*4)-1 , j+70 , this.getZOffset() ,  15.0F, 199.0F, 3, 3, 256, 512);
                }
            }else {
				barinfos.get("Animatios").isactive=false;
			}
            if(!ManegerCustomCode.getManeger().finderBreakBlockTime(VariablesCustomCode.curentCharecter).isEmpty()){
                drawTexture(matrices, i+126 , j+123 , this.getZOffset() ,  12.0F, 202.0F, 237, 2, 256, 512);
				barinfos.get("Breakblocks").isactive=true;
                int beginvalue= barinfos.get("Breakblocks").beginvalue;
                ArrayList<Integer> movtimes= ManegerCustomCode.getManeger().finderBreakBlockTime(VariablesCustomCode.curentCharecter);
                for (int p : movtimes ){
                    if(p<=beginvalue+60 && p>=beginvalue){
                        drawTexture(matrices, i+125+((p-beginvalue)*4 ) , j+123 , this.getZOffset() ,  12.0F, 199.0F, 3, 3, 256, 512);

                    }
                }
				ArrayList<Integer> endticks = ManegerCustomCode.getManeger().getBreakBlockEndTick(VariablesCustomCode.curentCharecter);
				for (int p : endticks ){
					if(p<=beginvalue+60 && p>=beginvalue){
						drawTexture(matrices, i+125+((p-beginvalue)*4) , j+23 , this.getZOffset() ,  21.0F, 199.0F, 3, 3, 256, 512);

					}
				}
                int curenttime = ManegerCustomCode.getCurentClientTime();
                if(curenttime<=beginvalue+60 && curenttime>=beginvalue){
                    drawTexture(matrices, i+126+((curenttime-beginvalue)*4)-1 , j+120 , this.getZOffset() ,  15.0F, 199.0F, 3, 3, 256, 512);
                }
            }
            else {
				barinfos.get("Breakblocks").isactive=false;
			}



            int beginvalue= barinfos.get("Movements").beginvalue;
            int curentime = Integer.parseInt(VariablesCustomCode.curentTime);


            if(choosedtimeY==73){
				beginvalue= barinfos.get("Animatios").beginvalue;
			}
			else if(choosedtimeY==123){
				beginvalue= barinfos.get("Breakblocks").beginvalue;
			}


            if(curentime<=beginvalue+60 && curentime>=beginvalue && choosedtimeY!=0){
                drawTexture(matrices , i+125+( (curentime-beginvalue)*4 ) , j+choosedtimeY , this.getZOffset() , 18F , 199F , 3 ,3  , 256 ,512);
            }


        }
    }

	@Override
	protected void drawBackground(MatrixStack ms, float partialTicks, int gx, int gy) {
		RenderSystem.color4f(1, 1, 1, 1);
		MinecraftClient.getInstance().getTextureManager().bindTexture(TEXTURE);
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		drawTexture(ms, i, j, this.getZOffset() , 0, 0, this.backgroundWidth, this.backgroundHeight, 256 , 512);
		
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.client.player.closeScreen();
			return true;
		}
		if(textField!=null){
			if (textField.isFocused())
				return textField.keyPressed(key, b, c);
		}
		
		return super.keyPressed(key, b, c);
	}

	@Override
	public void tick() {
		super.tick();
		textField.tick();
	}

	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		if(!VariablesCustomCode.curentCharecter.equals("god") && ManegerCustomCode.initialed){
			this.textRenderer.draw(matrices , new LiteralText(VariablesCustomCode.curentCharecter) , 138 , 2 ,4210752);

			if(!ManegerCustomCode.getManeger().finderMovmentTime(VariablesCustomCode.curentCharecter).isEmpty()){
				this.textRenderer.draw(matrices , new LiteralText("Movements") , 126 , 15 ,4210752);
                this.textRenderer.draw(matrices , new LiteralText(Integer.toString(barinfos.get("Movements").beginvalue) ) , 102 , 23 ,4210752);
			}
			else {
				barinfos.get("Movements").isactive=false;
			}
			if(!ManegerCustomCode.getManeger().finderAnimeTime(VariablesCustomCode.curentCharecter).isEmpty()){
				this.textRenderer.draw(matrices , new LiteralText("Animatios") , 126 , 55 ,4210752);
                this.textRenderer.draw(matrices , new LiteralText(Integer.toString(barinfos.get("Animatios").beginvalue) ) , 102 , 73 ,4210752);

			}else {
				barinfos.get("Animatios").isactive=false;
			}
			if(!ManegerCustomCode.getManeger().finderBreakBlockTime(VariablesCustomCode.curentCharecter).isEmpty()){
				this.textRenderer.draw(matrices , new LiteralText("Breakblocks") , 126 , 105 ,4210752);
                this.textRenderer.draw(matrices , new LiteralText(Integer.toString(barinfos.get("Breakblocks").beginvalue) ) , 102 , 123 ,4210752);
			}
			else {
				barinfos.get("Breakblocks").isactive=false;
			}

			int i = (this.width - this.backgroundWidth) / 2;
			int j = (this.height - this.backgroundHeight) / 2;
			if(j+22<mouseY && mouseY<j+27 && mouseX>i+125 && mouseX<i+364 && barinfos.get("Movements").isactive ){
                int choosedtime =Math.round(((float) (mouseX-i)-126)/4);

                this.textRenderer.draw(matrices ,Integer.toString(choosedtime+barinfos.get("Movements").beginvalue) , 126+(choosedtime*4) , 30 , 4210752 );


				/*for(int pos : barinfos.get("Movements").curentreddotspos.keySet() ){
					if(mouseX>pos-1  && mouseX<pos+1){
						Text text = new LiteralText(Integer.toString(barinfos.get("Movements").curentreddotspos.get(pos)));
						this.textRenderer.draw(matrices ,text , pos-i-2 , 26 , 4210752 );
					}
				}*/
			}
			else if(j+72<mouseY && mouseY<j+77 && mouseX>i+125 && mouseX<i+364 && barinfos.get("Animatios").isactive ){
                int choosedtime =Math.round(((float) (mouseX-i)-126)/4);
                this.textRenderer.draw(matrices ,Integer.toString(choosedtime+barinfos.get("Animatios").beginvalue) , 126+(choosedtime*4) , 80 , 4210752 );

				/*for(int pos : barinfos.get("Animatios").curentreddotspos.keySet() ){
					if(mouseX>pos-1  && mouseX<pos+1){
						Text text = new LiteralText(Integer.toString(barinfos.get("Animatios").curentreddotspos.get(pos)));
						this.textRenderer.draw(matrices ,text , pos-i-2 , 76 , 4210752 );
					}
				}*/
			}
			else if(j+122<mouseY && mouseY<j+127 && mouseX>i+125 && mouseX<i+364 && barinfos.get("Breakblocks").isactive){
                int choosedtime =Math.round(((float) (mouseX-i)-126)/4);
                this.textRenderer.draw(matrices ,Integer.toString(choosedtime+barinfos.get("Breakblocks").beginvalue) , 126+(choosedtime*4) , 130 , 4210752 );

                /*for(int pos : barinfos.get("Breakblocks").curentreddotspos.keySet() ){
					if(mouseX>pos-1  && mouseX<pos+1){
						Text text = new LiteralText(Integer.toString(barinfos.get("Breakblocks").curentreddotspos.get(pos)));
						this.textRenderer.draw(matrices ,text , pos-i-2 , 126 , 4210752 );
					}
				}*/
			}
		}
	}

	@Override
	public void onClose() {
		super.onClose();
		MinecraftClient.getInstance().keyboard.setRepeatEvents(false);
	}

	private boolean canScroll(int listSize) {
		return listSize > 7;
	}

	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		int i = scrolstatSize;
		if (this.canScroll(i) && 11<mouseY && mouseY<158 && mouseX>5 && mouseX<101 ) {
			int j = i - 7;
			this.indexStartOffset = (int)((double)this.indexStartOffset - amount);
			this.indexStartOffset = MathHelper.clamp(this.indexStartOffset, 0, j);
		}

		int a = (this.width - this.backgroundWidth) / 2;
		int b = (this.height - this.backgroundHeight) / 2;
		int c = 1;
		if(this.hasShiftDown()){
			c=10;
		}

		if(barinfos.get("Movements").isactive && b+15<mouseY && mouseY<b+60 && mouseX>a+125 && mouseX<a+364  ){
            barinfos.get("Movements").beginvalue-=(amount*c);
            //barinfos.get("Movements").curentreddotspos.clear();
            if(barinfos.get("Movements").beginvalue<0)
                barinfos.get("Movements").beginvalue=0;

		}
		else if(barinfos.get("Animatios").isactive && b+65<mouseY && mouseY<b+110 && mouseX>a+125 && mouseX<a+364  ){
            barinfos.get("Animatios").beginvalue-=(amount*c);
           // barinfos.get("Animatios").curentreddotspos.clear();
            if(barinfos.get("Animatios").beginvalue<0)
                barinfos.get("Animatios").beginvalue=0;

		}
		else if(barinfos.get("Breakblocks").isactive && b+115<mouseY && mouseY<b+170 && mouseX>a+125 && mouseX<a+364  ){
            barinfos.get("Breakblocks").beginvalue-=(amount*c);
           // barinfos.get("Breakblocks").curentreddotspos.clear();
            if(barinfos.get("Breakblocks").beginvalue<0)
                barinfos.get("Breakblocks").beginvalue=0;
		}


		return true;
	}

	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		int i = scrolstatSize;
		if (this.scrolling) {
			int j = this.y + 18;
			int k = j + 139;
			int l = i - 7;
			float f = ((float)mouseY - (float)j - 13.5F) / ((float)(k - j) - 27.0F);
			f = f * (float)l + 0.5F;
			this.indexStartOffset = MathHelper.clamp((int)f, 0, l);
			return true;
		} else {
			return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
		}
	}

	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		this.scrolling = false;
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		if (this.canScroll(scrolstatSize) && mouseX > (double)(i + 94) && mouseX < (double)(i + 94 + 6) && mouseY > (double)(j + 18) && mouseY <= (double)(j + 18 + 139 + 1)) {
			this.scrolling = true;
		}

        if(j+22<mouseY && mouseY<j+27 && mouseX>i+124 && mouseX<i+364 && barinfos.get("Movements").isactive ){
            int choosedtime =Math.round(((float) (mouseX-i)-125)/4)+barinfos.get("Movements").beginvalue;
            VariablesCustomCode.curentTime= Integer.toString(choosedtime);
            choosedtimeY=23;
        }
        else if(j+72<mouseY && mouseY<j+77 && mouseX>i+124 && mouseX<i+364 && barinfos.get("Animatios").isactive ){
			int choosedtime =Math.round(((float) (mouseX-i)-126)/4)+barinfos.get("Animatios").beginvalue;
			VariablesCustomCode.curentTime= Integer.toString(choosedtime);
            choosedtimeY=73;


        }
        else if(j+122<mouseY && mouseY<j+127 && mouseX>i+124 && mouseX<i+364 && barinfos.get("Breakblocks").isactive){
			int choosedtime =Math.round(((float) (mouseX-i-125))/4)+barinfos.get("Breakblocks").beginvalue;
			VariablesCustomCode.curentTime= Integer.toString(choosedtime);
            choosedtimeY=123;


        }

		return super.mouseClicked(mouseX, mouseY, button);
	}

	private void renderScrollbar(MatrixStack matrices , int x, int y) {
		int i = scrolstatSize + 1 - 7;
		if (i > 1) {
			int j = 139 - (27 + (i - 1) * 139 / i);
			int k = 1 + j / i + 139 / i;

			int m = Math.min(113, this.indexStartOffset * k);
			if (this.indexStartOffset == i - 1) {
				m = 113;
			}

			drawTexture(matrices,  x+94, y+11+m, this.getZOffset(), 0.0F, 199.0F, 6, 27, 256, 512);
		} else {
			drawTexture(matrices,  x+94,  y+11, this.getZOffset(), 6.0F, 199.0F, 6, 27, 256, 512);
		}

	}

	public static void screenInit() {
		ServerPlayNetworking.registerGlobalReceiver(StevefabricMod.id("managgui") , (server, PlayerEntity, handler1, buf, responseSender) -> {
			NbtCompound nbtCompound = buf.readNbt();
			ArrayList<String> param = new ArrayList<>();
			for (String s : nbtCompound.getKeys()) {
				param.add(nbtCompound.getString(s));
			}

			ServerWorld world = PlayerEntity.getServerWorld();
			server.execute(()->{
				if (param.get(0).equals("initial")){
					if (param.get(1).equals("makeNewSecwanse")){
						ManegerCustomCode.makeNewSence(param.get(2) );

					}
					else if(param.get(1).equals("loadSence")){
						ManegerCustomCode.loadSence(param.get(2) , world ,PlayerEntity ) ;
					}
					else if(param.get(1).equals("saveChanges")){
						try {
							ManegerCustomCode.getManeger().writeFile();

						}
						catch (IOException e){
							e.printStackTrace();
						}
					}
					else if(param.get(1).equals("makeEndTicks")){
						ManegerCustomCode.MakeEndTicks();
					}


				}
				else if(param.get(0).equals("charecter")){
					if(param.get(1).equals("makeNewCharecter")){
						ManegerCustomCode.makeNewCharecter(param.get(2) , param.get(3) , param.get(4) , world , PlayerEntity);
					}
					else if(param.get(1).equals("removeCharecter")){
						ManegerCustomCode.removeCharecter(param.get(2));

					}
					else if(param.get(1).equals("choosCharecter")){
						VariablesCustomCode.curentCharecter= param.get(2) ;

					}

					else if(param.get(1).equals("charecterManeger")){

						if (param.get(2).equals("movement")){
							if(param.get(3).equals("go")){
								BlockPos pos = ManegerCustomCode.posSuggester.get(param.get(5));
								ManegerCustomCode.getManeger().setMovmentGo(VariablesCustomCode.curentTime , pos.getX() , pos.getY() , pos.getZ() , Double.parseDouble(param.get(4)) , VariablesCustomCode.curentCharecter );

							}
							else if (param.get(3).equals("stop")){
								ManegerCustomCode.getManeger().setMovmentStop(VariablesCustomCode.curentTime , VariablesCustomCode.curentCharecter);
							}
							else if (param.get(3).equals("removeMovement")){
								ManegerCustomCode.getManeger().removMovment(VariablesCustomCode.curentTime , VariablesCustomCode.curentCharecter);
							}
						}
						else if(param.get(2).equals("anime")){
							if(param.get(3).equals("ReapAnim")){
								ManegerCustomCode.getManeger().setAnimeRepeat(VariablesCustomCode.curentTime, param.get(4) , param.get(5),Integer.parseInt(param.get(7)) ,Float.parseFloat(param.get(6)) , VariablesCustomCode.curentCharecter);
							}
							else if (param.get(3).equals("LoopAnim")){
								ManegerCustomCode.getManeger().setAnimeLoop(VariablesCustomCode.curentTime, param.get(4) , param.get(5) ,Float.parseFloat(param.get(6)) , VariablesCustomCode.curentCharecter);
							}
							else if (param.get(3).equals("stopAnim")){
								ManegerCustomCode.getManeger().setAnimestop(VariablesCustomCode.curentTime, param.get(4) ,VariablesCustomCode.curentCharecter);
							}
							else if (param.get(3).equals("removeAnim")){
									ManegerCustomCode.getManeger().removAnime(VariablesCustomCode.curentTime , VariablesCustomCode.curentCharecter);
							}
						}
					}
				}



			});
		});
	}

	@Environment(EnvType.CLIENT)
	class WidgetButtonPage extends ButtonWidget {
		final int index;

		public WidgetButtonPage(int x, int y, int index, PressAction onPress) {
			super(x, y, 89, 20, LiteralText.EMPTY, onPress);
			this.index = index;
			this.visible = false;
		}

		public int getIndex() {
			return this.index;
		}


	}



	public static class Suggest{
		public static HashMap<String, Arguments> suggests= new HashMap<>();

		public static void preinitial(){
            String[] s = {"loadSence" , "makeNewSecwanse" };

            suggests.put("initial" , new Arguments("initial" , new  ArrayList<String>(  Arrays.asList(s.clone())) , 1 , null  , false , null , true));

            suggests.put("loadSence" ,new Arguments("loadSence" ,  ManegerCustomCode.getManeger().getfiles() ,2 , null   ,  false , null , true) );

            suggests.put("makeNewSecwanse" ,new Arguments("makeNewSecwanse" ,  ManegerCustomCode.getManeger().getfiles() ,2 , null , true , "file name" , false));

        }

		public static void initial (){
		    suggests.clear();
			String[] s = {"loadSence" , "makeNewSecwanse" , "saveChanges" , "makeEndTicks" };

			suggests.put("initial" , new Arguments("initial" , new  ArrayList<String>(  Arrays.asList(s.clone())) , 1 , null  , false , null , true));

			suggests.put("loadSence" ,new Arguments("loadSence" ,  ManegerCustomCode.getManeger().getfiles() ,2 , null   ,  false , null , true) );

			suggests.put("makeNewSecwanse" ,new Arguments("makeNewSecwanse" ,  ManegerCustomCode.getManeger().getfiles() ,2 , null , true , "file name" , false));

			suggests.put("saveChanges" ,new Arguments("saveChanges" , null ,3 , null   , false , null , false));

			suggests.put("makeEndTicks" ,new Arguments("makeEndTicks" , null ,3 , null   , false , null , false));

			String[] s0 = {"makeNewCharecter" , "removeCharecter"  , "choosCharecter" , "charecterManeger"};
			suggests.put("charecter" ,new Arguments("charecter" , new  ArrayList<String>( Arrays.asList(s0.clone())) ,1 , null   , false , null , true));

			suggests.put("makeNewCharecter" ,new Arguments("makeNewCharecter" ,  StevefabricMod.carecters ,1 , "charectername"   , false , null , true));
			suggests.put("charectername" ,new Arguments("charectername" , ManegerCustomCode.getManeger().getCharecter() ,1 , "begingpos"   , true , "new name" , false));
			suggests.put("begingpos" ,new Arguments("begingpos" , ManegerCustomCode.posSuggesterkeys ,2 , null   , false , null , true));

			suggests.put("removeCharecter" ,new Arguments("removeCharecter" , ManegerCustomCode.getManeger().getCharecter() ,2 , null   , false , null , true));

			suggests.put("choosCharecter" ,new Arguments("choosCharecter" , ManegerCustomCode.getManeger().getCharecter() ,2 , null  , false , null , true));

			String[] s1 = {"movement" , "anime" , "breakBlock"};
			suggests.put("charecterManeger" ,new Arguments("charecterManeger" , new  ArrayList<String>( Arrays.asList(s1.clone())) ,1 , null   , false , null , true));


			String[] s2 = {"go" , "stop" ,"removeMovement" };
			suggests.put("movement" , new Arguments("movement" , new  ArrayList<String>( Arrays.asList(s2.clone())) , 1 , null  , false , null , true));

			suggests.put("go" , new Arguments("go" , null , 1 , "gopos"  , true , "speed" , false));
			suggests.put("gopos" , new Arguments("gopos" , ManegerCustomCode.posSuggesterkeys , 2 , null  , false , null , true));

			suggests.put("stop" , new Arguments("stop" , null , 3 , null  , false , null, true));

			suggests.put("removeMovement" , new Arguments("removeMovement" , null , 3 , null  , false , null, true));


			String[] s3 = {"ReapAnim" , "LoopAnim" , "stopAnim" , "removeAnim"};
			suggests.put("anime" , new Arguments("anime" , new  ArrayList<String>( Arrays.asList(s3.clone())) , 1 , null  , false , null , true));

			String[] s4 = {"MovmentAnimations" , "controller1" , "controller2" , "controller3" ,"controller4"};
			suggests.put("ReapAnim" , new Arguments("ReapAnim" , new  ArrayList<String>( Arrays.asList(s4.clone())) , 1 , "repanimname" , false , null , true));
			suggests.put("repanimname" , new Arguments("repanimname" , ManegerCustomCode.getManeger().getAnimename() , 1 , "repanimspeed" , false , null , true));
			suggests.put("repanimspeed" , new Arguments("repanimspeed" , null , 1 , "repanimRepeat" , true , "animspeed" , false));
			suggests.put("repanimRepeat" , new Arguments("repanimRepeat" , null , 2 , null , true , "animrepea" , false));

			suggests.put("LoopAnim" , new Arguments("LoopAnim" , new  ArrayList<String>( Arrays.asList(s4.clone())) , 1 , "lopanimname" , false , null , true));
			suggests.put("lopanimname" , new Arguments("lopanimname" , ManegerCustomCode.getManeger().getAnimename() , 1 , "lopanimspeed" , false , null , true));
			suggests.put("lopanimspeed" , new Arguments("lopanimspeed" , null , 2 , null , true , "animspeed" , false));

			suggests.put("stopAnim" , new Arguments("stopAnim" , new  ArrayList<String>( Arrays.asList(s4.clone())) , 2 , null , false , null , true));

			suggests.put("removeAnim" , new Arguments("removeAnim" , null , 3 , null , false , null , false));

			String[] s5 = {"setBreakBlock" , "removeBreakBlock"};
			suggests.put("breakBlock" , new Arguments("breakBlock" , new  ArrayList<String>( Arrays.asList(s5.clone())) , 1 , null , false , null , true));

			suggests.put("setBreakBlock" , new Arguments("setBreakBlock" , ManegerCustomCode.posSuggesterkeys , 2 , null , false , null , true));

			suggests.put("removeBreakBlock" , new Arguments("removeBreakBlock" , null , 3 , null , false , null , false));

		}




	}




	public static class Arguments{
		public String key;
		public ArrayList<String> list;
		public int stage;
		public String nextkey;
		public boolean canAdNewArg;
		public String textfiled ;
		public boolean isscroolkeysActive ;
		//public Action action;

		public Arguments(String key ,@Nullable ArrayList<String> list  , int stage , @Nullable String nextkey  , boolean canAdNewArg , @Nullable String textfiled , boolean isscroolkeysActive ) {
			this.key = key;
			this.list=list;
			this.stage=stage;
			this.nextkey=nextkey;
			this.canAdNewArg=canAdNewArg;
			this.isscroolkeysActive=isscroolkeysActive;
			//this.action=action;
		}

	}

	public static class Barinfo{
		public boolean isactive ;
		public int beginvalue ;
		public HashMap<Integer , Integer> curentreddotspos ;

		public Barinfo( boolean isactive , int beginvalue , HashMap<Integer , Integer> curentreddotspos  ){
			this.isactive=isactive;
			this.beginvalue=beginvalue;
			this.curentreddotspos=curentreddotspos;
		}

	}





	public interface Action {
		void onPress();
	}



}
