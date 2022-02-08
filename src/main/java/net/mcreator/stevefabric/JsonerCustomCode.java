package net.mcreator.stevefabric;


import com.google.gson.*;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;



public class JsonerCustomCode {

    public void backup(String backupname   ) throws IOException {
        FileWriter fileWriter = new FileWriter("C:/Users/Ahmad/Desktop/source/backup/"+backupname+".json");
        fileWriter.write(sourcstring);
        fileWriter.flush();
        fileWriter.close();
    }

    private String sourcstring ;
    public String location;



    public  void chang() throws IOException {
        FileWriter fileWriter = new FileWriter(location);
        fileWriter.write(sourcstring);
        fileWriter.flush();
        fileWriter.close();
    }

    public  void  readfile() throws IOException{
        sourcstring = new String(Files.readAllBytes(Paths.get(location)));

    }

    public  String insert(String sourc , int index  , String target) {
        int d = (target.length());
        int t = 0;
        int p = index;
        char v;
        while (t <d) {
            v = target.charAt(t);
            t++;
            sourc = sourc.substring(0, p) + v + sourc.substring(p);
            p++;
        }
        return sourc ;
    }

    public  String remove(String sourc , int index  , int strlengh) {
        int d = (strlengh);
        int t = 0;
        while (t < d) {
            sourc = sourc.substring(0, index) + sourc.substring(index+1);
            t++;
        }
        return sourc ;
    }

    public  String replace (String sourc , int index  , int strlen , String neew){
        String s = remove(sourc , index , strlen );
        String r = insert(s , index , neew);
        return r ;
    }

    public  void cor ( String animation , String bone , String action , float i , float j , float k , float time ){

        int f = sourcstring.indexOf(animation);
        int v = sourcstring.indexOf(bone , f);
        int a0 = sourcstring.indexOf(action ,v);
        int a1= sourcstring.indexOf(Float.toString(time) , a0);
        int a= sourcstring.indexOf("[" , a1)+1;

        int m =0;
        char o = ',';
        while ( m<3 ){
            char c1 = sourcstring.charAt(a);
            int count =0;
            int charcount = a ;
            while (c1!=o){
                count++;
                charcount++;
                c1= sourcstring.charAt(charcount);

            }
            if (m==0){
                sourcstring = replace(sourcstring , a , count , Float.toString(i));
                a=a+Float.toString(i).length()+1;

            }
            else if (m==1){
                sourcstring = replace(sourcstring , a , count , Float.toString(j));
                a=a+Float.toString(j).length()+1;
                o = ']';
            }
            else if (m==2){
                sourcstring = replace(sourcstring , a , count , Float.toString(k));
            }
            m=m+1;
        }
    }

    public  void length(String animation  , float i)  {

        int f = sourcstring.indexOf(animation);
        int a= sourcstring.indexOf("length" , f)+9;
        char c1 = sourcstring.charAt(a);
        int count =0;
        int charcount = a ;
        while (c1!=','){
            count++;
            charcount++;
            c1= sourcstring.charAt(charcount);
        }
        sourcstring = replace(sourcstring , a , count , Float.toString(i));

    }

    public  void time( String animation , String bon , String action  , float oldtime, float newtime) {

        int f = sourcstring.indexOf(animation);
        int f2= sourcstring.indexOf(bon ,f);
        int f3= sourcstring.indexOf(action , f2);
        int a = sourcstring.indexOf(Float.toString(oldtime),f3);

        char c1 = sourcstring.charAt(a);
        int count =0;
        int charcount = a ;
        while (c1!='\"'){
            count++;
            charcount++;
            c1= sourcstring.charAt(charcount);
        }
        sourcstring = replace(sourcstring , a , count , Float.toString(newtime));





    }



    public void newanim(String anim , float length){
        int f1 = sourcstring.length()-8;
        String animation="animation.model."+anim;

        String s = ",\n" +
                "\t\""+animation+"\": {\n" +
                "      \"animation_length\":"+Float.toString(length)+",\n" +
                "      \"bones\": {\n" +
                "      }\n" +
                "    }";
        sourcstring = insert(sourcstring , f1 , s );
    }

    public  void setanim ( String animation , String bon , String action ,  float time  , float x , float y , float z) {
        String pos = "";


        if(finderbon(animation).contains(bon)){
            if (finderaction(animation , bon).contains(action)) {
                int f1 = sourcstring.indexOf(animation);
                int f2 = sourcstring.indexOf(bon , f1);
                int f3 = sourcstring.indexOf(action , f2);
                ArrayList<Float> lasttime = findertime( animation , bon , action );
                int f4 = sourcstring.indexOf(Float.toString(lasttime.get(lasttime.size()-1)) , f3);
                int f5 = sourcstring.indexOf('}',f4)+1;
                pos = ",\n"+
                        " 			\""+Float.toString(time)+"\": {\n"+
                        "              \"vector\": ["+Float.toString(x)+","+Float.toString(y)+","+Float.toString(z)+"]\n"+
                        "            }";
                sourcstring = insert(sourcstring , f5 , pos );
            }
            else {
                int f1 = sourcstring.indexOf(animation);
                int f2 = sourcstring.indexOf(bon , f1);
                ArrayList<String> lastaction = finderaction( animation , bon  );
                int f3 = sourcstring.indexOf(lastaction.get(lastaction.size()-1) , f2);
                ArrayList<Float> lasttime = findertime( animation , bon , lastaction.get(lastaction.size()-1) );
                int f4 = sourcstring.indexOf(Float.toString(lasttime.get(lasttime.size()-1)) , f3);
                int f5= sourcstring.indexOf("}" , f4);
                int f6 = sourcstring.indexOf("}" , f5+1)+1;
                pos =",\n" +
                        "\t\t\t\""+action+"\": {\n" +
                        "            \"0.0\": {\n" +
                        "              \"vector\": ["+Float.toString(x)+","+Float.toString(y)+","+Float.toString(z)+"]\n" +
                        "\t\t\t\t}\n" +
                        "            }";

                sourcstring = insert(sourcstring , f6 , pos );
            }
        }
        else {
            int f1 = sourcstring.indexOf(animation);
            ArrayList<String> lastbon = finderbon( animation );
            if(lastbon.isEmpty()){
                int f2 = sourcstring.indexOf("bones" , f1)+9;

                pos ="\n" +
                        "        \""+bon+"\": {\n" +
                        "          \""+action+"\": {\n" +
                        "            \"0.0\": {\n" +
                        "              \"vector\": ["+Float.toString(x)+","+Float.toString(y)+","+Float.toString(z)+"]\n" +
                        "    \t\t}\n" +
                        "          }\n" +
                        "        }";

                sourcstring = insert(sourcstring , f2 , pos );

            }else {
                int f2 = sourcstring.indexOf(lastbon.get(lastbon.size()-1) , f1);
                ArrayList<String> lastaction = finderaction( animation , lastbon.get(lastbon.size()-1)  );
                int f3 = sourcstring.indexOf(lastaction.get(lastaction.size()-1) , f2);
                ArrayList<Float> lasttime = findertime( animation , lastbon.get(lastbon.size()-1) , lastaction.get(lastaction.size()-1) );
                int f4 = sourcstring.indexOf(Float.toString(lasttime.get(lasttime.size()-1)) , f3);
                int f5= sourcstring.indexOf("}" , f4);
                int f6 = sourcstring.indexOf("}" , f5+1);
                int f7 = sourcstring.indexOf("}"  ,f6+1)+1;
                pos =",\n" +
                        "\t\t\""+bon+"\": {\n" +
                        "          \""+action+"\": {\n" +
                        "            \"0.0\": {\n" +
                        "              \"vector\": ["+Float.toString(x)+","+Float.toString(y)+","+Float.toString(z)+"]\n" +
                        "    \t\t}\n" +
                        "          }\n" +
                        "        }";

                sourcstring = insert(sourcstring , f7 , pos );

            }
        }
    }

    public  void clearanim ( String animation   ) {
        ArrayList<String> listbon = bonlist();
        ArrayList<String> listaction = new ArrayList<>();
        listaction.add("rotation");
        listaction.add("position");
        listaction.add("scale");


        for (int i=0 ; i<listbon.size() ; i++){
            for(int j=0 ; j<listaction.size() ; j++){
                ArrayList<Float> times = findertime("animation.model."+animation,listbon.get(i),listaction.get(j));
                for (int k=times.size()-1 ; k!=0 ; k--){

                    int f1 = sourcstring.indexOf(animation);
                    int f2= sourcstring.indexOf(listbon.get(i) ,f1);
                    int f3= sourcstring.indexOf(listaction.get(j) , f2);
                    int f4 = sourcstring.indexOf("\""+Float.toString(times.get(k))+"\"",f3)-2;
                    int f5 = sourcstring.indexOf('}' , f4);
                    sourcstring= remove(sourcstring,f4 , f5 - f4+1 );


                }

            }

        }

    }

    public  float finderlength( String animation) {
        float r = 0 ;
        JsonParser parser = new JsonParser() ;

        Object obj = parser.parse(sourcstring);
        JsonObject object = (JsonObject) obj ;
        JsonObject object2 = (JsonObject)object.get("animations");
        JsonObject object3 = (JsonObject)object2.get(animation);
        JsonPrimitive object4 = (JsonPrimitive)object3.get("animation_length");
        r = (object4.getAsFloat());
        return r ;
    }


    public  ArrayList<Float> findertime( String animation , String bon , String action ) {
        ArrayList<Float> r = new ArrayList<>() ;
        JsonParser parser = new JsonParser() ;

        Object obj = parser.parse(sourcstring);
        JsonObject object = (JsonObject) obj ;
        JsonObject object2 = (JsonObject)object.get("animations");
        JsonObject object3 = (JsonObject)object2.get(animation);
        JsonObject object4 = (JsonObject)object3.get("bones");
        JsonObject object5 = (JsonObject)object4.get(bon);
        JsonObject object6 = (JsonObject)object5.get(action);
        Set<Map.Entry<String , JsonElement>> entries = object6.entrySet();

        for(Map.Entry<String , JsonElement> entry : entries){
            r.add(Float.parseFloat(entry.getKey()));
        }

        return r ;
    }
    public  ArrayList<String> finderanimation() {
        ArrayList<String> r = new ArrayList<>() ;
        JsonParser parser = new JsonParser() ;

        Object obj = parser.parse(sourcstring);
        JsonObject object = (JsonObject) obj ;
        JsonObject object2 = (JsonObject)object.get("animations");
        Set<Map.Entry<String , JsonElement>> entries = object2.entrySet();
        for(Map.Entry<String , JsonElement> entry : entries){
            r.add(entry.getKey());
        }
        return r ;
    }
    public  ArrayList<String> finderbon( String animation  ) {
        ArrayList<String> r = new ArrayList<>() ;
        JsonParser parser = new JsonParser() ;

        Object obj = parser.parse(sourcstring);
        JsonObject object = (JsonObject) obj ;
        JsonObject object2 = (JsonObject)object.get("animations");
        JsonObject object3 = (JsonObject)object2.get(animation);
        JsonObject object4 = (JsonObject)object3.get("bones");
        Set<Map.Entry<String , JsonElement>> entries = object4.entrySet();

        for(Map.Entry<String , JsonElement> entry : entries){
            r.add(entry.getKey());
        }
        return r ;
    }
    public  ArrayList<String> finderaction( String animation , String bon  ) {
        ArrayList<String> r = new ArrayList<>() ;
        JsonParser parser = new JsonParser() ;

        Object obj = parser.parse(sourcstring);
        JsonObject object = (JsonObject) obj ;
        JsonObject object2 = (JsonObject)object.get("animations");
        JsonObject object3 = (JsonObject)object2.get(animation);
        JsonObject object4 = (JsonObject)object3.get("bones");
        JsonObject object5 = (JsonObject)object4.get(bon);
        Set<Map.Entry<String , JsonElement>> entries = object5.entrySet();

        for(Map.Entry<String , JsonElement> entry : entries){
            r.add(entry.getKey());
        }

        return r ;
    }

    public  float findecor( String animation , String bon , String action, float time , char i ) {
        float r = 0 ;
        JsonParser parser = new JsonParser() ;

        Object obj = parser.parse(sourcstring);
        JsonObject object = (JsonObject) obj ;
        JsonObject object2 = (JsonObject)object.get("animations");
        JsonObject object3 = (JsonObject)object2.get(animation);
        JsonObject object4 = (JsonObject)object3.get("bones");
        JsonObject object5 = (JsonObject)object4.get(bon);
        JsonObject object6 = (JsonObject)object5.get(action);
        JsonObject object7 = (JsonObject)object6.get(Float.toString(time));
        JsonArray object8 = (JsonArray)object7.get("vector"); ;







        if (i=='x'){
            r=object8.get(0).getAsFloat();
        }
        else if (i=='y'){
            r=object8.get(1).getAsFloat();
        }
        else if (i=='z'){
            r=object8.get(2).getAsFloat();
        }
        return r ;

    }
    public static ArrayList<String> bonlist(){
        ArrayList<String> list = new ArrayList<>();
        list.add("all");
        list.add("body");
        list.add("mainbody");
        list.add("head");
        list.add("muse");
        list.add("eyeright");
        list.add("bluright");
        list.add("eyeleft");
        list.add("bluleft");
        list.add("eyebroright");
        list.add("eyebroleft");
        list.add("leftArm");
        list.add("leftArmdown");
        list.add("rightArm");
        list.add("rightArmdown");
        list.add("leftLeg");
        list.add("leftLegdown");
        list.add("rightLeg");
        list.add("rightLegdown");
        return list ;

    }


}
