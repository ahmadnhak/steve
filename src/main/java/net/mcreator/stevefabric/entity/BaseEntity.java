

package net.mcreator.stevefabric.entity;

        import io.netty.buffer.Unpooled;
        import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
        import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
        import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
        import net.mcreator.stevefabric.BreakBlockCustomCode;
        import net.mcreator.stevefabric.FakePlayerFactoryCustomCode;
        import net.mcreator.stevefabric.VariablesCustomCode;
        import net.minecraft.client.network.ClientPlayNetworkHandler;
        import net.minecraft.entity.*;
        import net.minecraft.entity.mob.PathAwareEntity;
        import net.minecraft.entity.passive.TraderLlamaEntity;
        import net.minecraft.entity.passive.WanderingTraderEntity;
        import net.minecraft.inventory.Inventory;
        import net.minecraft.inventory.InventoryChangedListener;
        import net.minecraft.item.Items;
        import net.minecraft.screen.PropertyDelegate;
        import net.minecraft.server.network.ServerPlayNetworkHandler;
        import net.minecraft.server.world.ServerWorld;
        import net.minecraft.util.math.BlockPos;
        import net.minecraft.world.*;
        import net.minecraft.util.registry.Registry;
        import net.minecraft.util.Identifier;
        import net.minecraft.util.Hand;
        import net.minecraft.util.ActionResult;
        import net.minecraft.text.Text;
        import net.minecraft.text.LiteralText;
        import net.minecraft.sound.SoundEvent;
        import net.minecraft.server.network.ServerPlayerEntity;
        import net.minecraft.screen.ScreenHandler;
        import net.minecraft.network.PacketByteBuf;
        import net.minecraft.nbt.NbtList;
        import net.minecraft.nbt.NbtElement;
        import net.minecraft.nbt.NbtCompound;
        import net.minecraft.item.SpawnEggItem;
        import net.minecraft.item.ItemStack;
        import net.minecraft.item.ItemGroup;
        import net.minecraft.inventory.SimpleInventory;
        import net.minecraft.entity.player.PlayerInventory;
        import net.minecraft.entity.player.PlayerEntity;
        import net.minecraft.entity.mob.HostileEntity;
        import net.minecraft.entity.damage.DamageSource;
        import net.minecraft.entity.attribute.EntityAttributes;
        import net.minecraft.enchantment.EnchantmentHelper;

        import net.mcreator.stevefabric.screen.SteveGuiGui;

        import net.mcreator.stevefabric.StevefabricMod;

        import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
        import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
        import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
        import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
        import org.jetbrains.annotations.Nullable;
        import software.bernie.geckolib3.core.IAnimatable;
        import software.bernie.geckolib3.core.PlayState;
        import software.bernie.geckolib3.core.builder.AnimationBuilder;
        import software.bernie.geckolib3.core.controller.AnimationController;
        import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
        import software.bernie.geckolib3.core.manager.AnimationData;
        import software.bernie.geckolib3.core.manager.AnimationFactory;
        import software.bernie.geckolib3.resource.GeckoLibCache;


        import java.util.ArrayList;
        import java.util.Map;
        import java.util.HashMap;


@SuppressWarnings("deprecation")
public class BaseEntity extends HostileEntity implements IAnimatable {

    public static HashMap<String , EntityType<BaseEntity>> Entity = new HashMap<>() ;

    public static void setentityType(String name){
        if(!Entity.containsKey(name)){
            EntityType<BaseEntity> e =  Registry.register(Registry.ENTITY_TYPE, StevefabricMod.id(name),
                    FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, BaseEntity::new).dimensions(EntityDimensions.fixed(0.6f, 1.8f)).trackRangeBlocks(64)
                            .forceTrackedVelocityUpdates(true).trackedUpdateRate(3).build());
            Entity.put(name , e);
        }
    }

    public BaseEntity(EntityType<? extends BaseEntity> entityType, World world) {
        super(entityType, world);
        this.setAiDisabled(false);
        this.experiencePoints = 0;

        setPersistent();
    }



    public static void init(String name) {
        setentityType(name);
        FabricDefaultAttributeRegistry.register(Entity.get(name),
                BaseEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                        .add(EntityAttributes.GENERIC_ARMOR, 0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3));
    }

    @Override
    protected void initGoals() {
        super.initGoals();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return new SoundEvent(new Identifier("entity.generic.hurt"));
    }

    @Override
    protected SoundEvent getDeathSound() {
        return new SoundEvent(new Identifier("entity.generic.death"));
    }


    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData,
                                 @Nullable NbtCompound entityTag) {
        EntityData retval = super.initialize(world, difficulty, spawnReason, entityData, entityTag);
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        Entity entity = this;
        {

            Map<String, Object> $_dependencies = new HashMap<>();

            if(!this.world.isClient){
                spawnLlama((ServerWorld) this.world);
            }
           
        }
        return retval;
    }

    private NbtCompound persistentData;

    public  NbtCompound getNBT(){
        if (persistentData == null)
            persistentData = new NbtCompound();
        return persistentData;

    }

    public final SimpleInventory inventory = new SimpleInventory(2) {
        @Override
        public int getMaxCountPerStack() {
            return 64;
        }

    };
    @Override
    protected void dropInventory() {
        super.dropInventory();
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack itemstack = inventory.getStack(i);

            if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack)) {
                this.dropStack(itemstack);
            }
        }
        VariablesCustomCode.removeCharecter(this.getEntityId());
    }



    public  SimpleInventory getInventory(){
        return inventory;
    }


    private void spawnLlama(ServerWorld world) {
        BlockPos blockPos = this. getNearbySpawnPos(world, this.getBlockPos(), 4);
        if (blockPos != null) {
            TraderLlamaEntity traderLlamaEntity = (TraderLlamaEntity)EntityType.TRADER_LLAMA.spawn(world, (NbtCompound)null, (Text)null, (PlayerEntity)null, blockPos, SpawnReason.EVENT, false, false);
            if (traderLlamaEntity != null) {
                traderLlamaEntity.attachLeash(this, true);
            }
        }
    }

    @Nullable
    private BlockPos getNearbySpawnPos(WorldView world, BlockPos pos, int range) {
        BlockPos blockPos = null;

        for(int i = 0; i < 10; ++i) {
            int j = pos.getX() + this.random.nextInt(range * 2) - range;
            int k = pos.getZ() + this.random.nextInt(range * 2) - range;
            int l = world.getTopY(Heightmap.Type.WORLD_SURFACE, j, k);
            BlockPos blockPos2 = new BlockPos(j, l, k);
            if (SpawnHelper.canSpawn(SpawnRestriction.Location.ON_GROUND, world, blockPos2, EntityType.WANDERING_TRADER)) {
                blockPos = blockPos2;
                break;
            }
        }

        return blockPos;
    }



    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.put("InventoryCustom", inventory.toNbtList());

        NbtList nbtList = new NbtList();

        for(int i = 0 ; i < this.inventory.size(); ++i) {
            ItemStack itemStack = this.inventory.getStack(i);
            if (!itemStack.isEmpty()) {
                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte)i);
                itemStack.writeNbt(nbtCompound);
                nbtList.add(nbtCompound);
            }
        }

        compound.put("Items", nbtList);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        NbtElement inventoryCustom = tag.get("InventoryCustom");
        if (inventoryCustom instanceof NbtCompound){
            inventory.readNbtList((NbtList) inventoryCustom);
        }
        NbtList nbtList = tag.getList("Items", 10);


        for(int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            int j = nbtCompound.getByte("Slot") & 255;
            if (j >=0 && j < this.inventory.size()) {
                this.inventory.setStack(j, ItemStack.fromNbt(nbtCompound));
            }
        }


    }


    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return getEntityId();
        }

        @Override
        public void set(int index, int value) {

        }


        @Override
        public int size() {
            return 1;
        }
    };





    public String charecter;

    @Override
    public ActionResult interactMob(PlayerEntity sourceentity, Hand hand) {
        ItemStack itemstack = sourceentity.getStackInHand(hand);
        ActionResult retval = ActionResult.success(this.world.isClient);

        sourceentity.openHandledScreen(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                buf.writeBlockPos(getBlockPos());

            }

            @Override
            public Text getDisplayName() {
                return new LiteralText(getEntityName());
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                SteveGuiGui.GuiContainerMod guiContainerMod= new SteveGuiGui.GuiContainerMod(syncId, inv, inventory , propertyDelegate);
                return guiContainerMod;
            }
        });
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        Entity entity = this;
        return retval;
    }
    
    public boolean isfreezbreaking = false;
    public boolean isBreakingBlock = false;
    private ArrayList<Double> blockposition = new ArrayList<>();
    private BreakBlockCustomCode breakBlockCustomCode = new BreakBlockCustomCode();
    public void setBreakBlock(double x , double y , double z){
        blockposition.clear();
        blockposition.add(x);
        blockposition.add(y);
        blockposition.add(z);
        isBreakingBlock = true;
    }
    @Override
    public void baseTick() {
        super.baseTick();
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        Entity entity = this;
        {
            if(!entity.world.isClient){
                if(isBreakingBlock){
                    breakBlockCustomCode.breakblock(this , blockposition.get(0), blockposition.get(1) , blockposition.get(2) );
                }
            }
        }
    }
    public void startMoveto(double xd , double yd , double zd , double speed) {

        if(!this.world.isClient){
            PathAwareEntity creature =(PathAwareEntity) this ;
            creature.getNavigation().startMovingTo(xd , yd , zd , speed);
        }


    }

    public void stopMoving() {
        if(!this.world.isClient){
            PathAwareEntity creature =(PathAwareEntity) this ;
            creature.getNavigation().stop();
        }
    }

    private AnimationFactory factory = new AnimationFactory(this);

    public void playRepaetingAnimation(String controller , String animname , int repeat , double speed){


        GeckoLibCache.getInstance().parser.setValue(animname+".speed"  ,speed);
        SynceAnimeSpeed.put(animname,true);
        playState.put(controller , PlayState.CONTINUE);

        if(controller.equals("MovmentAnimations")){
            MovmentAnimations.setAnimation(new AnimationBuilder().addRepeatingAnimation(animname , repeat));
        }
        else if(controller.equals("controller1")){
            controller1.setAnimation(new AnimationBuilder().addRepeatingAnimation(animname , repeat));
        }
        else if(controller.equals("controller2")){
            controller2.setAnimation(new AnimationBuilder().addRepeatingAnimation(animname , repeat));
        }
        else if(controller.equals("controller3")){
            controller3.setAnimation(new AnimationBuilder().addRepeatingAnimation(animname , repeat));
        }
        else if(controller.equals("controller4")){
            controller4.setAnimation(new AnimationBuilder().addRepeatingAnimation(animname , repeat));
        }

    }

    public HashMap<String,Boolean> SynceAnimeSpeed = new HashMap<>();




    public void playLoopAnimation(String controller , String animname , double speed){


        GeckoLibCache.getInstance().parser.setValue(animname+".speed"  ,speed);
        SynceAnimeSpeed.put(animname,true);
        playState.put(controller , PlayState.CONTINUE);

        if(controller.equals("MovmentAnimations")){
            MovmentAnimations.setAnimation(new AnimationBuilder().addAnimation(animname , true));
        }
        else if(controller.equals("controller1")){
            controller1.setAnimation(new AnimationBuilder().addAnimation(animname , true));
        }
        else if(controller.equals("controller2")){
            controller2.setAnimation(new AnimationBuilder().addAnimation(animname , true));
        }
        else if(controller.equals("controller3")){
            controller3.setAnimation(new AnimationBuilder().addAnimation(animname , true));
        }
        else if(controller.equals("controller4")){
            controller4.setAnimation(new AnimationBuilder().addAnimation(animname , true));
        }
    }

    public void stopAnimation (String controller){
        playState.put(controller, PlayState.STOP);
    }

    private HashMap<String , PlayState> playState = new HashMap<String , PlayState>(){{
        put("MovmentAnimations" , PlayState.STOP);
        put("controller1" , PlayState.STOP);
        put("controller2" , PlayState.STOP);
        put("controller3" , PlayState.STOP);
        put("controller4" , PlayState.STOP);
    }};



    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event){
        event.getController().transitionLengthTicks=1;
        event.getController().markNeedsReload();
        return playState.get(event.getController().getName());
    }


    AnimationController MovmentAnimations=new AnimationController(this, "MovmentAnimations" , 0 , this::predicate);
    AnimationController controller1=new AnimationController(this, "controller1" , 0 , this::predicate);
    AnimationController controller2=new AnimationController(this, "controller2" , 0 , this::predicate);
    AnimationController controller3=new AnimationController(this, "controller3" , 0 , this::predicate);
    AnimationController controller4=new AnimationController(this, "controller4" , 0 , this::predicate);


    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(MovmentAnimations);
        animationData.addAnimationController(controller1);
        animationData.addAnimationController(controller2);
        animationData.addAnimationController(controller3);
        animationData.addAnimationController(controller4);


    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }


}

