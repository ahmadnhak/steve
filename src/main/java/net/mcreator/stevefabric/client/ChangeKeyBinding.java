
package net.mcreator.stevefabric.client;

import net.mcreator.stevefabric.VariablesCustomCode;
import net.mcreator.stevefabric.entity.BaseEntity;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.option.KeyBinding;

public class ChangeKeyBinding extends KeyBinding {
	
    public ChangeKeyBinding() {
        super("key.mcreator.change", GLFW.GLFW_KEY_C, "key.categories.misc");
    }

    public void keyPressed(PlayerEntity entity) {
        World world = entity.world;
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
      
        changeAction(entity);
        setPressed(false);
    }

    public void keyReleased(PlayerEntity entity) {

        World world = entity.world;
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
    }

    public void changeAction(PlayerEntity playerEntity){
        BaseEntity baseEntity= VariablesCustomCode.clientCharecter.get(VariablesCustomCode.curentCharecter);
        if(baseEntity!=null){
            if(baseEntity.getNBT().getInt("change") ==8){
                baseEntity.getNBT().putInt("change" , 0);

                if(playerEntity.world.isClient){

                    playerEntity.sendMessage(new LiteralText("xscal"), true);
                }
            }
            else if(baseEntity.getNBT().getInt("change")==0){
                baseEntity.getNBT().putInt("change" , 1 );

                if(playerEntity.world.isClient){
                    playerEntity.sendMessage(new LiteralText("yscal"), true);
                }
            }
            else if(baseEntity.getNBT().getInt("change")==1){
                baseEntity.getNBT().putInt("change" , 2 );

                if(playerEntity.world.isClient){
                    playerEntity.sendMessage(new LiteralText("zscal"), true);
                }
            }
            else if(baseEntity.getNBT().getInt("change")==2){
                baseEntity.getNBT().putInt("change" , 3 );

                if(playerEntity.world.isClient){
                    playerEntity.sendMessage(new LiteralText("itemrotationx"), true);
                }
            }
            else if(baseEntity.getNBT().getInt("change")==3){
                baseEntity.getNBT().putInt("change" , 4 );

                if(playerEntity.world.isClient){
                    playerEntity.sendMessage(new LiteralText("itemrotationy"), true);
                }
            }
            else if(baseEntity.getNBT().getInt("change")==4){
                baseEntity.getNBT().putInt("change" , 5 );

                if(playerEntity.world.isClient){
                    playerEntity.sendMessage(new LiteralText("itemrotationz"), true);
                }
            }
            else if(baseEntity.getNBT().getInt("change")==5){
                baseEntity.getNBT().putInt("change" , 6 );

                if(playerEntity.world.isClient){
                    playerEntity.sendMessage(new LiteralText("itempositionx"), true);
                }
            }
            else if(baseEntity.getNBT().getInt("change")==6){
                baseEntity.getNBT().putInt("change" , 7 );

                if(playerEntity.world.isClient){
                    playerEntity.sendMessage(new LiteralText("itempositiony"), true);
                }
            }
            else if(baseEntity.getNBT().getInt("change")==7){
                baseEntity.getNBT().putInt("change" , 8 );

                if(playerEntity.world.isClient){
                    playerEntity.sendMessage(new LiteralText("itempositionz"), true);
                }
            }
        }


    }



}

