
package net.mcreator.stevefabric.client;

import net.mcreator.stevefabric.VariablesCustomCode;
import net.mcreator.stevefabric.entity.BaseEntity;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.option.KeyBinding;

public class MinusKeyBinding extends KeyBinding {
	
	
        
    public MinusKeyBinding() {
        super("key.mcreator.minus", GLFW.GLFW_KEY_X, "key.categories.misc");
    }

    public void keyPressed(PlayerEntity entity) {
        World world = entity.world;
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        
        	minuseAction(entity);
        	setPressed(false);
        	
    }

    public void keyReleased(PlayerEntity entity) {
    	
        World world = entity.world;
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
    }

    public void minuseAction(PlayerEntity playerEntity){
        BaseEntity baseEntity= VariablesCustomCode.clientCharecter.get(VariablesCustomCode.curentCharecter);
        if(baseEntity!=null){
            if(baseEntity.getNBT().getInt("change")==0){
                baseEntity.getNBT().putFloat("xscal" , baseEntity.getNBT().getFloat("xscal" )-0.1f) ;
                if(playerEntity.world.isClient){
                    playerEntity.sendMessage(new LiteralText("xscal"+Float.toString(baseEntity.getNBT().getFloat("xscal")) ), true);
                }
            }
            else if(baseEntity.getNBT().getInt("change")==1){
                baseEntity.getNBT().putFloat("yscal" , baseEntity.getNBT().getFloat("yscal" )-0.1f) ;
                if(playerEntity.world.isClient){
                    playerEntity.sendMessage(new LiteralText("yscal"+Float.toString(baseEntity.getNBT().getFloat("yscal")) ), true);
                }
            }
            else if(baseEntity.getNBT().getInt("change")==2){
                baseEntity.getNBT().putFloat("zscal" , baseEntity.getNBT().getFloat("zscal" )-0.1f) ;
                if(playerEntity.world.isClient){
                    playerEntity.sendMessage(new LiteralText("zscal"+Float.toString(baseEntity.getNBT().getFloat("zscal")) ), true);
                }
            }
            else if(baseEntity.getNBT().getInt("change")==3){
                baseEntity.getNBT().putFloat("itemrotationx" , baseEntity.getNBT().getFloat("itemrotationx" )-0.087266462f) ;
                if(playerEntity.world.isClient){
                    playerEntity.sendMessage(new LiteralText("itemrotationx"+Float.toString(baseEntity.getNBT().getFloat("itemrotationx")) ), true);
                }
            }
            else if(baseEntity.getNBT().getInt("change")==4){
                baseEntity.getNBT().putFloat("itemrotationy" , baseEntity.getNBT().getFloat("itemrotationy" )-0.087266462f) ;
                if(playerEntity.world.isClient){
                    playerEntity.sendMessage(new LiteralText("itemrotationy"+Float.toString(baseEntity.getNBT().getFloat("itemrotationy")) ), true);
                }
            }
            else if(baseEntity.getNBT().getInt("change")==5){
                baseEntity.getNBT().putFloat("itemrotationz" , baseEntity.getNBT().getFloat("itemrotationz" )-0.087266462f) ;
                if(playerEntity.world.isClient){
                    playerEntity.sendMessage(new LiteralText("itemrotationz"+Float.toString(baseEntity.getNBT().getFloat("itemrotationz")) ), true);
                }
            }
            else if(baseEntity.getNBT().getInt("change")==6){
                baseEntity.getNBT().putFloat("itempositionx" , baseEntity.getNBT().getFloat("itempositionx" )-1) ;
                if(playerEntity.world.isClient){
                    playerEntity.sendMessage(new LiteralText("itempositionx"+Float.toString(baseEntity.getNBT().getFloat("itempositionx")) ), true);
                }
            }
            else if(baseEntity.getNBT().getInt("change")==7){
                baseEntity.getNBT().putFloat("itempositiony" , baseEntity.getNBT().getFloat("itempositiony" )-1) ;
                if(playerEntity.world.isClient){
                    playerEntity.sendMessage(new LiteralText("itempositiony"+Float.toString(baseEntity.getNBT().getFloat("itempositiony")) ), true);
                }
            }
            else if(baseEntity.getNBT().getInt("change")==8){
                baseEntity.getNBT().putFloat("itempositionz" , baseEntity.getNBT().getFloat("itempositionz" )-1) ;
                if(playerEntity.world.isClient){
                    playerEntity.sendMessage(new LiteralText("itempositionz"+Float.toString(baseEntity.getNBT().getFloat("itempositionz")) ), true);
                }
            }
        }



    }
}
