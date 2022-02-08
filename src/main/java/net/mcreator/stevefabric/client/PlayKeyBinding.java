package net.mcreator.stevefabric.client;

import net.mcreator.stevefabric.ManegerCustomCode;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class PlayKeyBinding extends KeyBinding {

    public PlayKeyBinding() {
        super("key.mcreator.play", GLFW.GLFW_KEY_P, "key.categories.misc");
    }

    public void keyPressed(PlayerEntity entity) {

        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        if(ManegerCustomCode.serverPlayState.equals("") && ManegerCustomCode.clientPlayState.equals("")  ){
            ManegerCustomCode.serverPlayState="play";
            ManegerCustomCode.clientPlayState="play";
            entity.sendMessage(new LiteralText("play") , true );
        }
        else if(ManegerCustomCode.serverPlayState.equals("play") && ManegerCustomCode.clientPlayState.equals("play") ) {

            ManegerCustomCode.serverPlayState="stop";
            ManegerCustomCode.clientPlayState="stop";
            entity.sendMessage(new LiteralText("stop") , true );
        }
        else if(ManegerCustomCode.serverPlayState.equals("stop") && ManegerCustomCode.clientPlayState.equals("stop") ){
            ManegerCustomCode.serverPlayState="play";
            ManegerCustomCode.clientPlayState="play";
            entity.sendMessage(new LiteralText("play") , true );
        }
        setPressed(false);



    }

    public void keyReleased(PlayerEntity entity) {
        World world = entity.world;
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

    }
}
