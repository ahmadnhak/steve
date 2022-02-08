package net.mcreator.stevefabric.client;

import net.mcreator.stevefabric.ManegerCustomCode;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class RePlayKeyBinding extends KeyBinding {
    public RePlayKeyBinding() {
        super("key.mcreator.replay", GLFW.GLFW_KEY_O, "key.categories.misc");
    }

    public void keyPressed(PlayerEntity entity) {

        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        if(ManegerCustomCode.serverReset==false && ManegerCustomCode.clientreset==false){
            ManegerCustomCode.serverReset=true;
            ManegerCustomCode.clientreset=true;
            entity.sendMessage(new LiteralText("reset play") , true );
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
