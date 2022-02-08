


package net.mcreator.stevefabric.entity.render;


        import com.google.gson.Gson;
        import com.google.gson.JsonElement;
        import com.google.gson.JsonObject;
        import net.mcreator.stevefabric.StevefabricMod;
        import net.mcreator.stevefabric.VariablesCustomCode;
        import net.mcreator.stevefabric.entity.BaseEntity;
        import net.minecraft.client.MinecraftClient;
        import net.minecraft.client.gl.ShaderParseException;
        import net.minecraft.client.render.RenderLayer;
        import net.minecraft.client.render.VertexConsumer;
        import net.minecraft.client.render.VertexConsumerProvider;
        import net.minecraft.client.render.model.json.JsonUnbakedModel;
        import net.minecraft.client.render.model.json.ModelTransformation;
        import net.minecraft.client.util.math.MatrixStack;
        import net.minecraft.item.ItemStack;
        import net.minecraft.resource.ResourceManager;
        import net.minecraft.util.Identifier;

        import net.minecraft.client.render.entity.EntityRenderDispatcher;



        import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

        import net.minecraft.util.JsonHelper;
        import net.minecraft.util.math.Vec3f;
        import software.bernie.geckolib3.GeckoLib;
        import software.bernie.geckolib3.core.IAnimatable;
        import software.bernie.geckolib3.core.builder.Animation;
        import software.bernie.geckolib3.file.AnimationFileLoader;
        import software.bernie.geckolib3.geo.exception.GeckoLibException;
        import software.bernie.geckolib3.geo.raw.pojo.Converter;
        import software.bernie.geckolib3.geo.raw.pojo.FormatVersion;
        import software.bernie.geckolib3.geo.raw.pojo.RawGeoModel;
        import software.bernie.geckolib3.geo.raw.tree.RawGeometryTree;
        import software.bernie.geckolib3.geo.render.GeoBuilder;
        import software.bernie.geckolib3.geo.render.built.GeoBone;
        import software.bernie.geckolib3.geo.render.built.GeoModel;
        import software.bernie.geckolib3.model.AnimatedGeoModel;
        import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;
        import software.bernie.geckolib3.resource.GeckoLibCache;
        import software.bernie.geckolib3.util.json.JsonAnimationUtils;
        import software.bernie.shadowed.eliotlash.molang.MolangParser;
        import software.bernie.shadowed.eliotlash.molang.expressions.MolangMultiStatement;

        import java.io.*;
        import java.lang.reflect.Field;
        import java.nio.charset.StandardCharsets;
        import java.nio.file.Files;
        import java.nio.file.Paths;
        import java.util.*;


public class BaseEntityRenderer extends GeoEntityRenderer<BaseEntity> {
    private ItemStack itt;
    private VertexConsumerProvider vertexConsumerProvider;
    private BaseEntity baseEntity;
    private ModelBase modelbase;
    private String charecter ;

    public static void clientInit(String name) {
        EntityRendererRegistry.INSTANCE.register(BaseEntity.Entity.get(name), (dispatcher, context) -> new BaseEntityRenderer(dispatcher , new ModelBase(name) , name));
       
    }



    protected BaseEntityRenderer(EntityRenderDispatcher renderManager , ModelBase m , String name) {
        super(renderManager, m);
        modelbase=m;
        this.charecter=name;
        this.shadowRadius = 0.7F;

    }

    public float valus(String key) {
        return baseEntity.getNBT().getFloat(key);


    }



    @Override
    public void renderEarly(BaseEntity animatable, MatrixStack stackIn, float ticks, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        baseEntity = animatable;
        this.vertexConsumerProvider = renderTypeBuffer;
        this.itt =  baseEntity.getInventory().getStack(0);
        modelbase.lenghthChanger(baseEntity);

        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);

    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        if (bone.getName().equals("itemplace"+charecter)) {
            stack.push();
            float xscal = (float) (1f + valus("xscal"));
            float yscal = (float) (1f + valus("yscal"));
            float zscal = (float) (1f + valus("zscal"));
            float itemrotationx = (float) valus("itemrotationx");
            float itemrotationy = (float) valus("itemrotationy");
            float itemrotationz = (float) valus("itemrotationz");
            float itempositionx = (float) valus("itempositionx");
            float itempositiony = (float) valus("itempositiony");
            float itempositionz = (float) valus("itempositionz");


            double itemposx = ((itempositionx) / (16));
            double itemposy = ((itempositiony) / (16));
            double itemposz = ((itempositionz) / (16));
            stack.translate(itemposx, itemposy, itemposz);
            stack.multiply(Vec3f.POSITIVE_Z.getRadialQuaternion(itemrotationz));
            stack.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(itemrotationy));
            stack.multiply(Vec3f.POSITIVE_X.getRadialQuaternion(itemrotationx));
            stack.scale(xscal, yscal, zscal);
            MinecraftClient.getInstance().getItemRenderer().renderItem(this.itt, ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.vertexConsumerProvider);
            stack.pop();
            bufferIn = rtb.getBuffer(RenderLayer.getEntityTranslucentCull(whTexture));
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public RenderLayer getRenderType(BaseEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityCutout(getTextureLocation(animatable));
    }


    public static class ModelBase extends AnimatedGeoModel {
        private String carectername;
        public ModelBase(String name){
            this.carectername=name;
        }


        private  JsonObject jsonofromfile;
        public  boolean loopbydef;

        public  void setanimationfile() {
            
            String location = "C:/Users/Ahmad/Desktop/source/"+carectername+".json";


            try {

                Gson GSON = new Gson();
                InputStreamReader stream = new InputStreamReader(new FileInputStream(location), StandardCharsets.UTF_8);
                Reader reader = new BufferedReader(
                        stream);
                jsonofromfile = JsonHelper.deserialize(GSON, reader, JsonObject.class);
                stream.close();


            } catch (IOException e) {
                e.printStackTrace();
            }


        }




        private  HashMap<String, Animation> animationList1 = new HashMap();


        public  void loadAllAnimations() {
            animationList1.clear();
            Set<Map.Entry<String, JsonElement>> entrySet = JsonAnimationUtils.getAnimations(jsonofromfile);
            for (Map.Entry<String, JsonElement> entry : entrySet) {
                String animationName = entry.getKey();
                Animation animation = null;
                try {
                    animation = JsonAnimationUtils.deserializeJsonToAnimation(
                            JsonAnimationUtils.getAnimation(jsonofromfile, animationName), GeckoLibCache.getInstance().parser);

                    if (loopbydef == true) {
                        animation.loop = true;
                    }
                } catch (ShaderParseException e) {
                    throw new RuntimeException(e);
                }
                animationList1.put(animationName, animation);
            }
        }





        public void lenghthChanger (BaseEntity baseEntity ){
            
            for (String s : baseEntity.SynceAnimeSpeed.keySet()){
                if(baseEntity.SynceAnimeSpeed.get(s)){
                    if(GeckoLibCache.getInstance().parser.variables.containsKey(s+".speed")){
                        double d = GeckoLibCache.getInstance().parser.variables.get(s+".speed").get();
                        double number;
                        if(d!=0){
                            number=1.2/d;
                        }
                        else {
                            number=0;
                        }
                       
                        JsonObject object = jsonofromfile.get("animations").getAsJsonObject();
                        JsonObject object1 = object.get(s).getAsJsonObject();
                        object1.addProperty("animation_length" , number );
                        object.add(s,object1);
                        jsonofromfile.add("animations", object);
                        loadAllAnimations();

                    }
                    baseEntity.SynceAnimeSpeed.put(s,false);
                }
            }

            
            
            
            

        }




        @Override
        public Animation getAnimation(String name, IAnimatable animatable) {
                if(animationList1.isEmpty() || VariablesCustomCode.animationReloader){
                    setanimationfile();
                    loadAllAnimations();
                    VariablesCustomCode.animationReloader=false;
                }

            return animationList1.get(name);

        }



        @Override
        public Identifier getModelLocation(Object t) {
            return new Identifier("stevefabric", "geo/"+carectername+"model.json");
        }

        @Override
        public Identifier getTextureLocation(Object t) {
            return new Identifier("stevefabric", "textures/"+carectername+".png");
        }

        @Override
        public Identifier getAnimationFileLocation(Object t) {
            return new Identifier("stevefabric", "animations/steve.json");
        }


    }

}
