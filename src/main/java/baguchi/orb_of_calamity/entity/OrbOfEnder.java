package baguchi.orb_of_calamity.entity;

import baguchi.orb_of_calamity.entity.control.StafeableFlyingMoveControl;
import baguchi.orb_of_calamity.entity.goal.AwakeGoal;
import baguchi.orb_of_calamity.entity.goal.RandomAttackIdleGoal;
import baguchi.orb_of_calamity.entity.goal.SwordGoal;
import baguchi.orb_of_calamity.entity.goal.WideBeamGoal;
import baguchi.orb_of_calamity.register.ModItems;
import baguchi.orb_of_calamity.register.ModSounds;
import baguchi.orb_of_calamity.world.ModSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class OrbOfEnder extends Monster {
    private final ServerBossEvent bossEvent = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS)).setPlayBossMusic(true).setCreateWorldFog(true);

    private static final EntityDataAccessor<String> ACTION = SynchedEntityData.defineId(OrbOfEnder.class, EntityDataSerializers.STRING);
    private int actionTick;
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState preSpawnAnimationState = new AnimationState();
    public final AnimationState spawnAnimationState = new AnimationState();
    public final AnimationState swordAttackAnimationState = new AnimationState();
    public final AnimationState preBreathAnimationState = new AnimationState();
    public final AnimationState stopBreathAnimationState = new AnimationState();
    public final AnimationState breathAnimationState = new AnimationState();
    public final AnimationState deathAnimationState = new AnimationState();
    public int wideBeamCooldown;
    public int swordCooldown;
    public OrbOfEnder(EntityType<? extends OrbOfEnder> p_32485_, Level p_32486_) {
        super(p_32485_, p_32486_);
        this.wideBeamCooldown = p_32486_.getRandom().nextInt(400) + 200;
        this.swordCooldown = p_32486_.getRandom().nextInt(100) + 100;
        this.moveControl = new StafeableFlyingMoveControl(this, 20, true);
        this.setDropChance(EquipmentSlot.MAINHAND, 0F);
        this.xpReward = 500;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 300.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.FLYING_SPEED, 0.15F)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    public void travel(Vec3 p_218382_) {
        this.travelFlying(p_218382_, this.getSpeed());
    }


    @Override
    protected PathNavigation createNavigation(Level p_218342_) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, p_218342_);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setRequiredPathLength(32.0F);
        return flyingpathnavigation;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new AwakeGoal(this));
        this.goalSelector.addGoal(1, new WideBeamGoal(this, 140.0));
        this.goalSelector.addGoal(1, new SwordGoal(this));
        this.goalSelector.addGoal(4, new RandomAttackIdleGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));

    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder p_326499_) {
        super.defineSynchedData(p_326499_);
        p_326499_.define(ACTION, "NORMAL");
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput p_421640_) {
        super.addAdditionalSaveData(p_421640_);
        p_421640_.putInt("wide_beam_cooldown", this.wideBeamCooldown);
        p_421640_.putInt("sword_cooldown", this.swordCooldown);
    }

    @Override
    public boolean hurtServer(ServerLevel p_376221_, DamageSource p_376460_, float p_376610_) {
        if (p_376460_.is(DamageTypes.DROWN)) {
            this.teleport();
        }

        return super.hurtServer(p_376221_, p_376460_, p_376610_);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput p_422339_) {
        super.readAdditionalSaveData(p_422339_);
        this.wideBeamCooldown = p_422339_.getInt("wide_beam_cooldown").orElse(0);
        this.swordCooldown = p_422339_.getInt("sword_cooldown").orElse(100);

    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        }

    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);

    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        this.actionTicks();
        if(this.wideBeamCooldown > 0){
            this.wideBeamCooldown--;
        }
        if(this.swordCooldown > 0){
            this.swordCooldown--;
        }
        if (this.level().isClientSide) {
            if (this.getAction() == Actions.NORMAL) {
                idleAnimationState.startIfStopped(this.tickCount);
            } else {
                idleAnimationState.stop();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte p_70103_1_) {
        if (p_70103_1_ == 7) {
            this.stopAnimations();
            this.deathAnimationState.start(this.tickCount);
        } else {
            super.handleEntityEvent(p_70103_1_);
        }

    }

    @Override
    public void die(DamageSource p_21014_) {
        super.die(p_21014_);
        this.playSound(SoundEvents.ENDERMAN_HURT, 2.0F, 1.0F);

        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, (byte) 7);
        }
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;

        if (this.deathTime == 100 && !this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, (byte) 60);
            this.remove(Entity.RemovalReason.KILLED);
            ModSavedData.get(this.level()).setDefeated(true);
        }
    }

    protected boolean teleport() {
        if (!this.level().isClientSide() && this.isAlive()) {
            double d0 = this.getX() + (this.random.nextDouble() - 0.5) * 64.0;
            double d1 = this.getY() + (this.random.nextInt(64) - 32);
            double d2 = this.getZ() + (this.random.nextDouble() - 0.5) * 64.0;
            return this.teleport(d0, d1, d2);
        } else {
            return false;
        }
    }

    /**
     * Teleport the enderman to another entity
     */
    public boolean teleportTowards(Entity target) {
        Vec3 vec3 = new Vec3(this.getX() - target.getX(), this.getY(0.5) - target.getEyeY(), this.getZ() - target.getZ());
        vec3 = vec3.normalize();
        double d0 = 20;
        double d1 = this.getX() + (this.random.nextDouble() - 0.5) * 10 - vec3.x * 20;
        double d2 = this.getY() + (this.random.nextInt(20) - 10) - vec3.y * 20;
        double d3 = this.getZ() + (this.random.nextDouble() - 0.5) * 10 - vec3.z * 20;
        return this.teleport(d1, d2, d3);
    }

    /**
     * Teleport the enderman
     */
    private boolean teleport(double x, double y, double z) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(x, y, z);

        BlockState blockstate = this.level().getBlockState(blockpos$mutableblockpos);
        boolean flag = blockstate.isAir();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1) {
            net.neoforged.neoforge.event.entity.EntityTeleportEvent.EnderEntity event = net.neoforged.neoforge.event.EventHooks.onEnderTeleport(this, x, y, z);
            if (event.isCanceled()) return false;
            Vec3 vec3 = this.position();
            boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag2) {
                this.level().gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(this));
                if (!this.isSilent()) {
                    this.level().playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                    this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            }

            return flag2;
        } else {
            return false;
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData spawnGroupData) {
        if(spawnReason == EntitySpawnReason.EVENT){
            this.setAction(Actions.PRE_AWAKEN);
        }
        this.populateDefaultEquipmentSlots(random, difficulty);
        return super.finalizeSpawn(level, difficulty, spawnReason, spawnGroupData);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, ModItems.END_BLADE.toStack());
    }

    @Override
    protected void populateDefaultEquipmentEnchantments(ServerLevelAccessor level, RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentEnchantments(level, random, difficulty);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> p_146754_) {
        if (ACTION.equals(p_146754_)) {
            this.actionAnimations(this.getAction());
            if(this.getAction() == Actions.AWAKEN){
                this.playSound(ModSounds.ORB_OF_ENDER_ROAR.value(), 2.0F, 1.0F);
            }
        }

        super.onSyncedDataUpdated(p_146754_);

    }

    public Actions getAction() {
        return Actions.get(this.entityData.get(ACTION));
    }

    public void setAction(Actions action) {
        this.entityData.set(ACTION, action.name());
        this.actionTick = 0;
    }

    public void actionTicks() {
        if (getAction().tick > -1) {
            if (getAction().tick <= this.actionTick) {
                this.actionTick = 0;
                if (getAction() == Actions.START_BREATH) {
                    setAction(Actions.BREATH);
                } else {
                    setAction(Actions.NORMAL);
                }
            } else {
                ++this.actionTick;
            }

        } else {
            this.actionTick = 0;
        }
    }

    public void actionAnimations(Actions actions) {
        switch (actions) {
                case START_BREATH:

                    this.stopAnimations();
                    preBreathAnimationState.start(this.tickCount);
                    break;
                case BREATH:

                    this.stopAnimations();
                    breathAnimationState.startIfStopped(this.tickCount);
                    break;
                case STOP_BREATH:

                    this.stopAnimations();
                    stopBreathAnimationState.start(this.tickCount);
                    break;
                case NORMAL:

                    this.stopAnimations();
                    break;
                case SHOOT_SWORD:

                    this.stopAnimations();
                    swordAttackAnimationState.start(this.tickCount);
                    break;
                case AWAKEN:

                    this.stopAnimations();
                    spawnAnimationState.start(this.tickCount);
                    break;
            case PRE_AWAKEN:

                this.stopAnimations();
                preSpawnAnimationState.start(this.tickCount);
                break;
                default:
                    this.stopAnimations();
                    break;
            }
    }

    public void stopAnimations() {
        swordAttackAnimationState.stop();
        breathAnimationState.stop();
        preBreathAnimationState.stop();
        stopBreathAnimationState.stop();
    }

    private boolean hurtWithCleanWater(ServerLevel level, DamageSource damageSource, AbstractThrownPotion potion, float damageAmount) {
        ItemStack itemstack = potion.getItem();
        PotionContents potioncontents = (PotionContents)itemstack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        return potioncontents.is(Potions.WATER) ? super.hurtServer(level, damageSource, damageAmount) : false;
    }


    public enum Actions {
        NORMAL(true, -1),
        SHOOT_SWORD(true, (int) (-1)),
        START_BREATH(false, (int) (0.6667F * 20)),
        BREATH(true, (int) (-1)),
        STOP_BREATH(false, (int) (20 * 0.5F)),
        AWAKEN(false, 80),
        PRE_AWAKEN(true, -1);

        private final boolean loop;
        private final int tick;

        Actions(boolean loop, int tick) {

            this.loop = loop;
            this.tick = tick;
        }

        public static Actions get(String nameIn) {
            for (Actions role : values()) {
                if (role.name().equals(nameIn))
                    return role;
            }
            return NORMAL;
        }
    }
}
