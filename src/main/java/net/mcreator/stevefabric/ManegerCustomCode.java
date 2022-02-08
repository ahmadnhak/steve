package net.mcreator.stevefabric;

import com.google.gson.*;
import it.unimi.dsi.fastutil.ints.IntComparator;
import net.mcreator.stevefabric.entity.BaseEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import software.bernie.geckolib3.resource.GeckoLibCache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class ManegerCustomCode {
    private static int serverTickCounter=0;
    private static int clientTickCounter=0;
    private static ManegerJsonerCustomCode.Maneger maneger = new ManegerJsonerCustomCode.Maneger();
    public static boolean initialed=false;
    public static String serverPlayState="";
    public static String clientPlayState="";
    public static boolean serverReset = false;
    public static boolean clientreset = false;
    private static String sence;
    private static boolean senceSeted=false;
    public static boolean endtickSyner=false;
    public static boolean isprepared=false;
    private static boolean makeendtick = false;
    public static Order order = new Order();
    public static ArrayList<String> charecters ;
    private static ClientWorld clientWorld;
    private static ServerWorld serverWorld ;
    public static HashMap<String , BlockPos > posSuggester = new HashMap<>();
    public static ArrayList<String> posSuggesterkeys = new ArrayList<>() ;
    private static boolean ismovementseted = false ;
    private static boolean isbreakblockseted = false ;


    public static void clientManeger(ClientWorld world){
        if(!isprepared){
            clientWorld=world;
        }
        if(isprepared){
            
            if(clientPlayState.equals("play")){
                for(String s : charecters){
                    if(order.getNextAnimetime(s)==clientTickCounter){
                        AnimeHandler(s);
                    }
                }

                clientTickCounter++;

            }

            if(clientreset){
                clientPlayState="stop";
                clientTickCounter=0;
                for(String s : charecters){
                    VariablesCustomCode.clientCharecter.get(s).stopAnimation("MovmentAnimations");
                    VariablesCustomCode.clientCharecter.get(s).stopAnimation("controller1");
                    VariablesCustomCode.clientCharecter.get(s).stopAnimation("controller2");
                    VariablesCustomCode.clientCharecter.get(s).stopAnimation("controller3");
                    VariablesCustomCode.clientCharecter.get(s).stopAnimation("controller4");
                   // replaceBlocks(s);
                }

                clientreset=false;
                prepareToPlay();
            }

            else if(clientPlayState.equals("stop")){
                for(String s : charecters){
                    VariablesCustomCode.clientCharecter.get(s).stopAnimation("MovmentAnimations");
                    VariablesCustomCode.clientCharecter.get(s).stopAnimation("controller1");
                    VariablesCustomCode.clientCharecter.get(s).stopAnimation("controller2");
                    VariablesCustomCode.clientCharecter.get(s).stopAnimation("controller3");
                    VariablesCustomCode.clientCharecter.get(s).stopAnimation("controller4");
                }
                clientPlayState="";

            }
        }


    }

    public static void serverManeger(MinecraftServer server){
        if(!isprepared){
            if(server.getCurrentPlayerCount()>0){
                serverWorld=server.getPlayerManager().getPlayerList().get(0).getServerWorld();
            }

        }
        if(isprepared){
            if(serverPlayState.equals("play")){
                for(String s : charecters){
                    if(VariablesCustomCode.serverCharecter.get(s).isfreezbreaking){
                        VariablesCustomCode.serverCharecter.get(s).isfreezbreaking=false;
                        VariablesCustomCode.serverCharecter.get(s).isBreakingBlock=true;
                    }
                    if(order.getNextMovementtime(s)==serverTickCounter){
                        MovmentHandler(s);
                        ismovementseted=true;

                    }

                    if (order.getNextBreakBlocktime(s)== serverTickCounter){
                        BreakBlockHandler(s);
                        isbreakblockseted=true;
                    }

                    if( makeendtick ){
                        if(ismovementseted){
                            if( VariablesCustomCode.serverCharecter.get(s).getNavigation().getCurrentPath().isFinished()){
                                maneger.setMovmentEndTick( Integer.toString(serverTickCounter)  , s);
                                ismovementseted=false;
                            }
                        }
                        if(isbreakblockseted){
                            if(!VariablesCustomCode.serverCharecter.get(s).isBreakingBlock){
                                maneger.setBreakBlockEndTick(Integer.toString(serverTickCounter) , s);
                                isbreakblockseted=false;
                            }
                        }
                    }
                }
                serverTickCounter++;
            }



            if(serverReset){
                serverPlayState="stop";
                serverTickCounter=0;
                
                for(String s : charecters){
                    ArrayList<Double> beginpos = maneger.getbeginPos(s);
                    VariablesCustomCode.serverCharecter.get(s).teleport(beginpos.get(0) , beginpos.get(1),beginpos.get(2));
                    replaceBlocks(s);
                    serverReset=false;
                    VariablesCustomCode.serverCharecter.get(s).isBreakingBlock=false;
             	    VariablesCustomCode.serverCharecter.get(s).isfreezbreaking=false;
                }
                prepareToPlay();
            }

           
            else if (serverPlayState.equals("stop")){
                makeendtick=false;
                for(String s : charecters){
                    VariablesCustomCode.serverCharecter.get(s).stopMoving();
                    if(VariablesCustomCode.serverCharecter.get(s).isBreakingBlock){
                        VariablesCustomCode.serverCharecter.get(s).isBreakingBlock=false;
                        VariablesCustomCode.serverCharecter.get(s).isfreezbreaking=true;
                    }

                }
                serverPlayState="";


            }
        }


        

    }

    public static void MakeEndTicks(){
        if(!VariablesCustomCode.serverCharecter.isEmpty()){
            for( String s :  VariablesCustomCode.serverCharecter.keySet() ){
                maneger.clearEndTicks(VariablesCustomCode.curentCharecter);
            }
            makeendtick=true;
        }


    }



    public static void MovmentHandler( String name){
        String time = Integer.toString( serverTickCounter);

        if(VariablesCustomCode.serverCharecter.containsKey(name)){
            ManegerJsonerCustomCode.Movment movment = maneger.getMovement(time , name);
            if(movment.getState()){
                VariablesCustomCode.serverCharecter.get(name).startMoveto(movment.getVector().get(0) , movment.getVector().get(1) , movment.getVector().get(2) , movment.getVector().get(3));
            }
            else if(!movment.getState()){
                VariablesCustomCode.serverCharecter.get(name).stopMoving();
            }
        }
        order.thisMovementTimePased(name);
    }

    public static void AnimeHandler( String name ){
        String time = Integer.toString( clientTickCounter);
        if(VariablesCustomCode.clientCharecter.containsKey(name)){
            ManegerJsonerCustomCode.Anime anime = maneger.getAnime(time , name);

            if(anime.getState().equals("repeat")){

                VariablesCustomCode.clientCharecter.get(name).playRepaetingAnimation(anime.getController() , anime.getAnimeName(), anime.getRepeat() , anime.getspeed());
            }
            else if(anime.getState().equals("loop")){

                VariablesCustomCode.clientCharecter.get(name).playLoopAnimation(anime.getController() , anime.getAnimeName() , anime.getspeed());
            }
            else if(anime.getState().equals("stop")){
                VariablesCustomCode.clientCharecter.get(name).stopAnimation(anime.getController());
            }
        }
        order.thisAnimeTimePased(name);


    }

    public static void BreakBlockHandler( String name ){
        String time = Integer.toString( serverTickCounter);
       
        if(VariablesCustomCode.serverCharecter.containsKey(name)){
            ManegerJsonerCustomCode.BreakBlock breakBlock = maneger.getBreakBlock(time , name);
            VariablesCustomCode.serverCharecter.get(name).setBreakBlock(breakBlock.getVector().get(0) , breakBlock.getVector().get(1) , breakBlock.getVector().get(2));
        }
        order.thisBreakBlockTimePased(name);


    }

    public static void replaceBlocks(String name){
        for(Integer time : maneger.finderBreakBlockTime(name)){
            ManegerJsonerCustomCode.BreakBlock breakBlock = maneger.getBreakBlock(Integer.toString(time)  , name);
            BlockPos blockPos = new BlockPos(breakBlock.getVector().get(0) , breakBlock.getVector().get(1) , breakBlock.getVector().get(2));
            clientWorld.setBlockState(blockPos, Registry.BLOCK.get( new Identifier(breakBlock.getid())).getDefaultState(), 3);
            serverWorld.setBlockState(blockPos, Registry.BLOCK.get( new Identifier(breakBlock.getid())).getDefaultState(), 3);
        }

    }




    public static ManegerJsonerCustomCode.Maneger getManeger(){
        return maneger;
    }



    public static void ManegerInitializer()  {
        if(senceSeted){
            maneger.location="C:/Users/Ahmad/Desktop/source/sence/"+sence+".json";
            try{
                maneger.readFile();
                initialed=true;
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }



    }

    public static void prepareToPlay(){
        if(initialed){
            order.initial(maneger);
            charecters=maneger.getCharecter();
            isprepared=true;
        }

    }

    public static void loadSence(String secwens , ServerWorld serverWorld , ServerPlayerEntity playerEntity) {
        sence=secwens;
        senceSeted=true;
        isprepared=false;
        ManegerInitializer();

        for (String s : VariablesCustomCode.serverCharecter.keySet()){
            VariablesCustomCode.serverCharecter.get(s).remove();
        }
        VariablesCustomCode.unloadCharecters();
        for(String s : maneger.getCharecter() ){
            BaseEntity baseEntity = spawnBaseEntity(maneger.getTypeof(s) , maneger.getbeginPos(s).get(0),
                    maneger.getbeginPos(s).get(1),
                    maneger.getbeginPos(s).get(2) , serverWorld);

            VariablesCustomCode.setNewCharecter(s , baseEntity , playerEntity );
            baseEntity.charecter=s;
        }
        prepareToPlay();



    }

    public static BaseEntity spawnBaseEntity(String typeof , double x , double y , double z , ServerWorld serverWorld){

        BaseEntity entityToSpawn = new BaseEntity(BaseEntity.Entity.get(typeof), (World) serverWorld);
        entityToSpawn.refreshPositionAndAngles(x, y, z , (float) 0  , (float) 0);
        entityToSpawn.setBodyYaw((float) 0);
        entityToSpawn.setHeadYaw((float) 0);
        if (entityToSpawn instanceof MobEntity)
            ((MobEntity) entityToSpawn).initialize( serverWorld, serverWorld.getLocalDifficulty(entityToSpawn.getBlockPos()),
                    SpawnReason.MOB_SUMMONED,  (EntityData)null,  (NbtCompound)null);
        serverWorld.spawnEntity(entityToSpawn);
        return entityToSpawn ;
    }

    public static void makeNewCharecter(String typeof , String name  , String poskey , ServerWorld serverWorld , ServerPlayerEntity playerEntity){
        if(!VariablesCustomCode.serverCharecter.containsKey(name)){
            double x = posSuggester.get(poskey).getX();
            double y = posSuggester.get(poskey).getY();
            double z = posSuggester.get(poskey).getZ();
            maneger.makeCharecter(name , typeof , x , y , z);
            BaseEntity baseEntity = spawnBaseEntity(typeof,x ,y ,z , serverWorld);
            VariablesCustomCode.setNewCharecter(name , baseEntity , playerEntity );
            baseEntity.charecter=name;
        }
        prepareToPlay();

    }

    public static void removeCharecter(String name){
        if(VariablesCustomCode.serverCharecter.containsKey(name)){
            VariablesCustomCode.removeCharecter(VariablesCustomCode.serverCharecter.get(name).getEntityId());
            VariablesCustomCode.serverCharecter.get(name).remove();
        }
        if(VariablesCustomCode.charectersName.isEmpty()){
        	isprepared=false;
        }
        else{
        	prepareToPlay();
        }
        

    }





    public static void makeNewSence(String secwens) {
        sence=secwens;
        senceSeted=true;
        isprepared=false;
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = new JsonObject();
        try {
            Files.write(Paths.get("C:/Users/Ahmad/Desktop/source/sence/"+sence+".json"),gson.toJson(jsonObject).getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }

        if(!VariablesCustomCode.serverCharecter.isEmpty()){
            for (String s : VariablesCustomCode.serverCharecter.keySet()){
                VariablesCustomCode.serverCharecter.get(s).remove();
            }
            VariablesCustomCode.unloadCharecters();
        }
        ManegerInitializer();


    }

    public static Integer getCurentServerTime(){
        return serverTickCounter;
    }
    public static Integer getCurentClientTime(){
        return clientTickCounter;
    }



    public static class Order {
        public  ArrayList<String> charecters ;
        public  String[] commands = {"Animations" , "movement" , "breakBlock"} ;
        public  ManegerJsonerCustomCode.Maneger maneger ;
        public  HashMap<String , HashMap<String  , ValuHash > > charecterHash = new HashMap<>() ;


        public  void initial (ManegerJsonerCustomCode.Maneger manege){
            maneger = manege ;
            initValus();
        }

        public  void initValus (){
            charecters= maneger.getCharecter();

            for(String ch : charecters){
                HashMap<String  , ValuHash > commandHash = new HashMap<>() ;
                for(String s : commands){
                    ValuHash cc = new ValuHash();
                    if(s.equals("Animations")){
                        cc.put(sort(maneger.finderAnimeTime(ch)) , 0);
                        commandHash.put(s , cc);

                    }
                    else if(s.equals("movement")){
                        cc.put(sort(maneger.finderMovmentTime(ch)) , 0);
                        commandHash.put(s , cc );
                    }
                    else if(s.equals("breakBlock")){
                        cc.put(sort(maneger.finderBreakBlockTime(ch)) , 0);
                        commandHash.put(s , cc );
                    }

                }
                charecterHash.put(ch , commandHash );
            }

        }

        public  ArrayList<Integer> sort (ArrayList<Integer> arrayList){
            Comparator<Integer> comparator = Comparator.naturalOrder();
            arrayList.sort(comparator);

            return arrayList;
        }



        public  void thisAnimeTimePased(String charecter){
            ValuHash valuHash = charecterHash.get(charecter).get("Animations").nextIndex();
            charecterHash.get(charecter).replace("Animations" , valuHash);
        }
        public  int getNextAnimetime(String charecter){
            return charecterHash.get(charecter).get("Animations").getWithIndex();
        }

        public  void thisMovementTimePased(String charecter){
            ValuHash valuHash = charecterHash.get(charecter).get("movement").nextIndex();
            charecterHash.get(charecter).replace("movement" , valuHash);
        }
        public  int getNextMovementtime(String charecter){
            return charecterHash.get(charecter).get("movement").getWithIndex();
        }

        public  void thisBreakBlockTimePased(String charecter){
            ValuHash valuHash = charecterHash.get(charecter).get("breakBlock").nextIndex();
            charecterHash.get(charecter).replace("breakBlock" , valuHash);
        }
        public  int getNextBreakBlocktime(String charecter){
            return charecterHash.get(charecter).get("breakBlock").getWithIndex();
        }




    }

    public static class ValuHash{
        private  ArrayList<Integer>  arrayList ;
        private int index ;

        public void put( ArrayList<Integer> arrayList , int index){
            this.arrayList=arrayList;
            this.index=index;
        }

        public int getWithIndex(){
        	if(arrayList.isEmpty()){
                return -1;
            }
            return arrayList.get(index);
        }

        public ValuHash nextIndex(){
            if( index<arrayList.size()-1){
                index++;
            }
            return this;
        }

    }









}
