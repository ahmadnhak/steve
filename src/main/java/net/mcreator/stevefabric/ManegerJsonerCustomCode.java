package net.mcreator.stevefabric;

import com.google.gson.*;
import net.minecraft.util.JsonHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class ManegerJsonerCustomCode {

    public static class init{
        private String typeof  ;
        private ArrayList<Double> begingPose = new ArrayList<>() ;
        private Movment movement = new Movment();
        private Anime Animations = new Anime();
        private BreakBlock breakBlock = new BreakBlock();

        public init(String typeof , double x , double y , double z){
            this.typeof=typeof;
            begingPose.add(x);
            begingPose.add(y);
            begingPose.add(z);
        }
    }

    public static class Anime{
        private String state ;
        private String controller;
        private String animeName;
        private Integer repeat;
        private Float speed ;


        public Anime (){
        }

        public Anime (String bolean ,String controller){
            this.state=bolean;
            this.controller=controller;

        }
        public Anime (String bolean ,String controller, String animeName , float speed){
            this.state=bolean;
            this.controller=controller;
            this.animeName=animeName;
            this.speed=speed;
        }
        public Anime(String bolean , String controller,String animeName, int repeat , float speed){
            this.state=bolean;
            this.controller=controller;
            this.animeName=animeName;
            this.repeat=repeat;
            this.speed=speed;
        }

        public String getState(){
            return this.state;
        }

        public String getController (){
            return this.controller;
        }

        public String getAnimeName (){
            return this.animeName;
        }

        public int getRepeat (){
            return this.repeat;
        }
        public float getspeed (){
            return this.speed;
        }
    }

    public static class Movment{
        private Boolean state ;
        private ArrayList<Double> vector;



        public Movment (){

        }
        public Movment (Boolean bolean ){
            this.state=bolean;

        }
        public Movment(Boolean state ,Double x ,Double y , Double z , Double speed ){
            this.state=state;
            ArrayList<Double> arrayList = new ArrayList<>();
            arrayList.add(x);
            arrayList.add(y);
            arrayList.add(z);
            arrayList.add(speed);
            this.vector=arrayList;

        }

        public Boolean getState(){
            return this.state;
        }

        public ArrayList<Double> getVector (){
            return this.vector;
        }
    }

    public static class BreakBlock{
        private String id ;
        private ArrayList<Double> vector;




        public BreakBlock (){

        }
        public BreakBlock ( double x , double y , double z , String id ){
            ArrayList<Double> arrayList =  new ArrayList<>();
            arrayList.add(x);
            arrayList.add(y);
            arrayList.add(z);
            this.vector=arrayList;
            this.id=id;

        }

        public ArrayList<Double> getVector (){
            return this.vector;
        }

        public String getid(){
            return id ;
        }
    }

    public static class Maneger {
        private String sourcstring ;
        public String location;
        public JsonObject jsonObject;
        private Gson gson = new GsonBuilder().setPrettyPrinting().create();

        public ArrayList<String> getfiles(){
            ArrayList<String> secwanse = new ArrayList<>();
            File folder = new File("C:/Users/Ahmad/Desktop/source/sence");
            File[] files = folder.listFiles();
            String s = "";
            for(File file : files){
                s=file.getName();
                int index = s.indexOf('.');
                for (int t = 0 ; t < 5 ; t++) {
                    s = s.substring(0,index ) + s.substring(index+1);
                }
                secwanse.add(s);
            }
            return secwanse;
        }
        public ArrayList<String> getAnimename(){
            JsonerCustomCode jsoner = new JsonerCustomCode();
            jsoner.location="C:/Users/Ahmad/Desktop/source/steve.json";
            try {
                jsoner.readfile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<String> animename = jsoner.finderanimation();
            return animename;
        }

        public Movment getMovement(String time , String charecter) {
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object = object0.getAsJsonObject("movement");
            Movment r = gson.fromJson(object.get(time) , Movment.class);
            return r ;
        }

        public  ArrayList<Integer> finderMovmentTime( String charecter ) {
            ArrayList<Integer> r = new ArrayList<>() ;

            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object2 = (JsonObject)object0.get("movement");
            Set<Map.Entry<String , JsonElement>> entries = object2.entrySet();

            for(Map.Entry<String , JsonElement> entry : entries){
            	if(!entry.getKey().equals("movement_end_tick")){
            		r.add(Integer.parseInt(entry.getKey()));
            	}
                
            }

            return r ;
        }

        public void removMovment(String time , String charecter){
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object = object0.getAsJsonObject("movement");
            object.remove(time);
            object0.add("movement" , object);
            jsonObject.add(charecter , object0);
            sourcstring = gson.toJson(jsonObject);
        }

        public void setMovmentGo(String time , double i , double j , double k , double speed , String charecter){

            Movment movment = new Movment(true, i , j , k , speed );
            JsonElement element = gson.toJsonTree(movment);
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object = object0.getAsJsonObject("movement");
            object.add(time ,element );
            object0.add("movement" , object);
            jsonObject.add(charecter , object0);
            sourcstring = gson.toJson(jsonObject);



        }

        public void setMovmentStop(String time , String charecter ){


            Movment movment = new Movment(false);
            JsonElement element = gson.toJsonTree(movment);
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object = object0.getAsJsonObject("movement");
            object.add(time ,element );
            object0.add("movement" , object);
            jsonObject.add(charecter , object0);
            sourcstring = gson.toJson(jsonObject);
        }



        public  void setBegingPose( double i , double j , double k  , String charecter) {
            ArrayList<Double> beginPose = new ArrayList<>();
            beginPose.add(i);
            beginPose.add(j);
            beginPose.add(k);
            JsonElement element = gson.toJsonTree(beginPose);
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            object0.add("begingPose" , element);
            jsonObject.add(charecter , object0);
            sourcstring = gson.toJson(jsonObject);
        }

        public ArrayList<Double> getbeginPos(String charecter) {
            ArrayList<Double> r = new ArrayList<>() ;
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            String s = gson.toJson(object0.get("begingPose"));
            r = gson.fromJson(s , r.getClass());
            return r ;
        }


        public Anime getAnime(String time , String charecter) {
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object = object0.getAsJsonObject("Animations");
            Anime r = gson.fromJson(object.get(time) , Anime.class);
            return r ;
        }

        public  ArrayList<Integer> finderAnimeTime( String charecter) {
            ArrayList<Integer> r = new ArrayList<>() ;
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object2 = (JsonObject)object0.get("Animations");
            Set<Map.Entry<String , JsonElement>> entries = object2.entrySet();

            for(Map.Entry<String , JsonElement> entry : entries){
                r.add(Integer.parseInt(entry.getKey()));
            }

            return r ;
        }

        public void removAnime(String time , String charecter){
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object = object0.getAsJsonObject("Animations");
            object.remove(time);
            object0.add("Animations" , object);
            jsonObject.add(charecter , object0);
            sourcstring = gson.toJson(jsonObject);
        }

        public void setAnimeRepeat(String time ,String controller, String anime_name ,int repeat , float speed , String charecter){

            Anime anime = new Anime("repeat" ,controller, anime_name , repeat , speed );
            JsonElement element = gson.toJsonTree(anime);
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object = object0.getAsJsonObject("Animations");
            object.add(time ,element );
            object0.add("Animations" , object);
            jsonObject.add(charecter , object0);
            sourcstring = gson.toJson(jsonObject);



        }

        public void setAnimeLoop(String time ,String controller, String anime_name , float speed , String charecter){


            Anime anime = new Anime("loop" , controller ,anime_name , speed );
            JsonElement element = gson.toJsonTree(anime);
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object = object0.getAsJsonObject("Animations");
            object.add(time ,element );
            object0.add("Animations" , object);
            jsonObject.add(charecter , object0);
            sourcstring = gson.toJson(jsonObject);
        }

        public void setAnimestop(String time ,String controller , String charecter){


            Anime anime = new Anime("stop" , controller );
            JsonElement element = gson.toJsonTree(anime);
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object = object0.getAsJsonObject("Animations");
            object.add(time ,element );
            object0.add("Animations" , object);
            jsonObject.add(charecter , object0);
            sourcstring = gson.toJson(jsonObject);
        }

        public void setMovmentEndTick (String tick  , String charecter){
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object1 = object0.get("movement").getAsJsonObject();
            if(!object1.has("movement_end_tick")){
                ArrayList<Integer> movement_end_tick = new ArrayList<>();
                JsonElement jsonElement = gson.toJsonTree(movement_end_tick);
                object1.add( "movement_end_tick" , jsonElement);
            }
            JsonArray jsonArray = object1.get("movement_end_tick").getAsJsonArray();
            jsonArray.add(tick);
            object1.add("movement_end_tick" , jsonArray);
            object0.add("movement" , object1);
            jsonObject.add(charecter , object0);
            sourcstring = gson.toJson(jsonObject);
        }
        public ArrayList<Integer> getMovmentEndTick ( String charecter){
           ArrayList<Integer> r = new ArrayList<>();
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object1 = object0.get("movement").getAsJsonObject();
            if(object1.has("movement_end_tick")){
                JsonArray jsonArray = object1.get("movement_end_tick").getAsJsonArray();
                for(JsonElement entry : jsonArray){
                    r.add(entry.getAsInt());
                }
            }

           return r ;
       }

        public void setBreakBlockEndTick (String tick  , String charecter){
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object1 = object0.get("breakBlock").getAsJsonObject();
            if(!object1.has("breakBlock_end_tick")){
                ArrayList<Integer> movement_end_tick = new ArrayList<>();
                JsonElement jsonElement = gson.toJsonTree(movement_end_tick);
                object1.add( "breakBlock_end_tick" , jsonElement);
            }
            JsonArray jsonArray = object1.get("breakBlock_end_tick").getAsJsonArray();
            jsonArray.add(tick);
            object1.add("breakBlock_end_tick" , jsonArray);
            object0.add("breakBlock" , object1);
            jsonObject.add(charecter , object0);
            sourcstring = gson.toJson(jsonObject);
        }

        public ArrayList<Integer> getBreakBlockEndTick ( String charecter){
            ArrayList<Integer> r = new ArrayList<>();
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object1 = object0.get("breakBlock").getAsJsonObject();
            if(object1.has("breakBlock_end_tick")){
                JsonArray jsonArray = object1.get("breakBlock_end_tick").getAsJsonArray();
                for(JsonElement entry : jsonArray){
                    r.add(entry.getAsInt());
                }
            }

            return r ;
        }

        public void clearEndTicks(String charecter){
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object1 = object0.get("breakBlock").getAsJsonObject();
            JsonObject object2 = object0.get("movement").getAsJsonObject();
            if(object1.has("breakBlock_end_tick")){
                object1.remove("breakBlock_end_tick");
            }
            if(object2.has("movement_end_tick")){
                object1.remove("movement_end_tick");
            }
            object0.add("breakBlock" , object1);
            object0.add("movement" , object2);
            jsonObject.add(charecter , object0);
            sourcstring = gson.toJson(jsonObject);
        }

       public ArrayList<String> getCharecter(){
            ArrayList<String> r = new ArrayList<>();
           Set<Map.Entry<String , JsonElement>> entries = jsonObject.entrySet();
           for(Map.Entry<String , JsonElement> entry : entries){
               r.add(entry.getKey());
           }
           return r ;
       }
       public void makeCharecter(String charecter , String typeof , double x , double y , double z){
            jsonObject.add( charecter, gson.toJsonTree(new ManegerJsonerCustomCode.init(typeof , x , y, z )));
           sourcstring = gson.toJson(jsonObject);
           System.out.println("0");
        }

        public void removCharecter(String charecter){
            jsonObject.remove(charecter);
            sourcstring = gson.toJson(jsonObject);
        }

        public String getTypeof(String charecter){
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonPrimitive jsonPrimitive = object0.getAsJsonPrimitive("typeof");
            return jsonPrimitive.getAsString() ;
        }


        public void setBreakBlock(String time , String charecter, double x , double  y , double z , String id  ){
            BreakBlock breakBlock = new BreakBlock( x ,y , z , id );
            JsonElement element = gson.toJsonTree(breakBlock);
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object = object0.getAsJsonObject("breakBlock");
            object.add(time ,element );
            object0.add("breakBlock" , object);
            jsonObject.add(charecter , object0);
            sourcstring = gson.toJson(jsonObject);
        }

        public BreakBlock getBreakBlock(String time , String charecter ){
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object = object0.getAsJsonObject("breakBlock");
            BreakBlock r = gson.fromJson(object.get(time) , BreakBlock.class);
            return r ;
        }

        public void removeBreakBlock(String time , String charecter  ){
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object = object0.getAsJsonObject("breakBlock");
            object.remove(time);
            object0.add("breakBlock" , object);
            jsonObject.add(charecter , object0);
            sourcstring = gson.toJson(jsonObject);

        }

        public  ArrayList<Integer> finderBreakBlockTime( String charecter) {
            ArrayList<Integer> r = new ArrayList<>() ;
            JsonObject object0 = jsonObject.getAsJsonObject(charecter);
            JsonObject object2 = (JsonObject)object0.get("breakBlock");
            Set<Map.Entry<String , JsonElement>> entries = object2.entrySet();

            for(Map.Entry<String , JsonElement> entry : entries){
            	if(!entry.getKey().equals("breakBlock_end_tick")){
            		r.add(Integer.parseInt(entry.getKey()));
            	}
                
            }

            return r ;
        }




        public  void writeFile() throws IOException {
            JsonParser jsonParser = new JsonParser();
            Files.write(Paths.get(location),gson.toJson(jsonParser.parse(sourcstring)).getBytes());
        }

        public  void  readFile() throws IOException {

            sourcstring = new String(Files.readAllBytes(Paths.get(location)));
            JsonParser parser = new JsonParser() ;
            jsonObject = (JsonObject)parser.parse(sourcstring);


        }





    }
}
