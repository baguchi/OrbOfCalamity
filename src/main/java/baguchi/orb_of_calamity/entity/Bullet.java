package baguchi.orb_of_calamity.entity;

import baguchi.orb_of_calamity.register.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

public class Bullet extends Projectile {
    private static final double SPEED = 0.15;

    public Bullet(EntityType<? extends Bullet> p_37319_, Level p_37320_) {
        super(p_37319_, p_37320_);
        this.noPhysics = true;
    }

    public Bullet(Level level, LivingEntity shooter) {
        this(ModEntities.BULLET.get(), level);
        this.setOwner(shooter);
        Vec3 vec3 = shooter.getBoundingBox().getCenter();
        this.snapTo(vec3.x, vec3.y, vec3.z, this.getYRot(), this.getXRot());
    }

    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    protected void addAdditionalSaveData(ValueOutput p_421638_) {
        super.addAdditionalSaveData(p_421638_);

    }

    protected void readAdditionalSaveData(ValueInput p_421868_) {
        super.readAdditionalSaveData(p_421868_);
    }

    protected void defineSynchedData(SynchedEntityData.Builder p_326398_) {
    }

    public void checkDespawn() {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
        }

    }

    protected double getDefaultGravity() {
        return 0.04;
    }

    public void tick() {
        super.tick();
        HitResult hitresult = null;
        if (!this.level().isClientSide) {

            this.applyGravity();


            hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        }

        Vec3 vec31 = this.getDeltaMovement();
        this.setPos(this.position().add(vec31));
        this.applyEffectsFromBlocks();
        if (this.portalProcess != null && this.portalProcess.isInsidePortalThisTick()) {
            this.handlePortal();
        }

        if (hitresult != null && this.isAlive() && hitresult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitresult)) {
            this.hitTargetOrDeflectSelf(hitresult);
        }

        ProjectileUtil.rotateTowardsMovement(this, 0.5F);
        if (getOwner() instanceof OrbOfEnder orb) {
            if (orb.getAction() != OrbOfEnder.Actions.BREATH) {
                this.destroy();
                this.level().explode(this.getOwner(), this.position().x, this.position().y, this.position().z, 1.0F, Level.ExplosionInteraction.NONE);
            }
        }
    }

    @Override
    protected boolean isAffectedByBlocks() {
        return !this.isRemoved();
    }

    @Override
    protected boolean canHitEntity(Entity p_37341_) {
        return super.canHitEntity(p_37341_) && !p_37341_.noPhysics;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < (double) 16384.0F;
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        Entity entity1 = this.getOwner();
        LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity) entity1 : null;
        DamageSource damagesource = this.damageSources().mobProjectile(this, livingentity);
        boolean flag = entity.hurtOrSimulate(damagesource, 4.0F);
        if (flag) {
            Level var8 = this.level();
            if (var8 instanceof ServerLevel) {
                ServerLevel serverlevel = (ServerLevel) var8;
                EnchantmentHelper.doPostAttackEffects(serverlevel, entity, damagesource);
            }
        }

        this.destroy();
    }

    private void destroy() {
        if (!this.level().isClientSide) {
            this.discard();
            ((ServerLevel) this.level()).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 2, 0.2, 0.2, 0.2, (double) 0.0F);
        }
        this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
        this.level().gameEvent(GameEvent.ENTITY_DAMAGE, this.position(), GameEvent.Context.of(this));
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
    }

    public boolean isPickable() {
        return true;
    }

    public boolean hurtClient(DamageSource p_376754_) {
        return true;
    }

    public boolean hurtServer(ServerLevel p_376836_, DamageSource p_376419_, float p_376652_) {
        this.playSound(SoundEvents.SHULKER_BULLET_HURT, 1.0F, 1.0F);
        p_376836_.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 15, 0.2, 0.2, 0.2, (double) 0.0F);
        this.destroy();
        return true;
    }

    public void recreateFromPacket(ClientboundAddEntityPacket p_150185_) {
        super.recreateFromPacket(p_150185_);
        double d0 = p_150185_.getXa();
        double d1 = p_150185_.getYa();
        double d2 = p_150185_.getZa();
        this.setDeltaMovement(d0, d1, d2);
    }
}
