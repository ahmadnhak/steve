package net.mcreator.stevefabric;




import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import net.mcreator.stevefabric.entity.BaseEntity;
import net.mcreator.stevefabric.server.BreakBlockCommand;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EfficiencyEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.*;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;



public class BreakBlockCustomCode {



    public  BlockPos blockpos;
    public  BlockState blockState ;
    public  ServerPlayerEntity playerEntity;
    public  ServerWorld serverworld ;
    public  ItemStack iteminhand ;
    public  int count = 1 ;
    public  int tickcount = 0 ;
    public boolean action = false ;
    public GameProfile username ;


    public void breakblock (BaseEntity serverBaseEntity, double x , double y , double z  ){

        this.serverworld=(ServerWorld) serverBaseEntity.world;
        this.blockpos = new BlockPos(x , y , z) ;
        if(username==null){
        	username = new GameProfile(null , serverBaseEntity.charecter);
        }
         
       
        playerEntity =  FakePlayerFactoryCustomCode.get(serverworld ,  username);
        

        iteminhand = serverBaseEntity.getInventory().getStack(0) ;



        blockState = serverworld.getBlockState(blockpos) ;

        if(serverworld!=null && blockpos!=null){
            blockState=serverworld.getBlockState(blockpos);
        }



        if (  blockState.getBlock()!=Blocks.AIR && blockState!=null && blockpos!=null && playerEntity!=null ){
            serverworld.setBlockBreakingInfo(playerEntity.getEntityId() , blockpos , count);

            int i = 1 ;
            if (breaktime(iteminhand , blockState , serverBaseEntity)<9 ){
                i = 1 ;
            }
            else{
                i =(int) Math.floor(breaktime(iteminhand , blockState , serverBaseEntity)/9) ;
            }
            tickcount++;
            if ((tickcount%i)==0 ){
                if (count<9){
                    count++ ;
                }
                else {
                    if(iteminhand.getItem().isSuitableFor(blockState) || playerEntity.canHarvest(blockState)  ){
                        blockState.getBlock().afterBreak(serverworld , playerEntity , blockpos , blockState , null , iteminhand);
                    }
                    serverworld.breakBlock(blockpos, false) ;
                    serverworld.setBlockBreakingInfo(playerEntity.getEntityId() , blockpos , -1) ;
                    serverBaseEntity.isBreakingBlock=false;
                   
                    tickcount=0;
                    count=0 ;
                }

            }
        }

    }

   

    public  int breaktime(ItemStack iteminhand  , BlockState blockState , BaseEntity serverBaseEntity){

        float speedMultiplier = 1f ;
        boolean check = false ;
        if( iteminhand.isSuitableFor(blockState)){
            check=true;
        }

        if (check ){
            speedMultiplier = iteminhand.getMiningSpeedMultiplier(blockState);

            float efficiencylevel = (float) (EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, (iteminhand)));
            if(iteminhand.hasEnchantments()) {

                speedMultiplier+=((efficiencylevel*efficiencylevel)+1);
            }
        }
        if(serverBaseEntity.isInsideWaterOrBubbleColumn()){
            speedMultiplier /= 5f ;
        }
        float damage = speedMultiplier/(blockState.getHardness(serverworld, blockpos));
        if(iteminhand.isSuitableFor(blockState) || playerEntity.canHarvest(blockState)){
            damage/=30f;
        }
        else {
            damage/=100f;
        }
        if(damage>1){
            return 0;
        }
        int ticks=((int)Math.floor(1/damage))+1;
        return (ticks*2);
    }
}
