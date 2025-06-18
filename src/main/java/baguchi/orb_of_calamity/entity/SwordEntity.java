package baguchi.orb_of_calamity.entity;

import baguchi.orb_of_calamity.register.ModEntities;
import baguchi.orb_of_calamity.register.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SwordEntity extends AbstractHurtingProjectile implements ItemSupplier {
    public SwordEntity(EntityType<? extends SwordEntity> p_37006_, Level p_37007_) {
        super(p_37006_, p_37007_);
    }

    public SwordEntity(EntityType<? extends SwordEntity> p_36990_, double p_36991_, double p_36992_, double p_36993_, Vec3 p_347521_, Level p_36997_) {
        super(p_36990_, p_36991_, p_36992_, p_36993_, p_347521_, p_36997_);
    }

    public SwordEntity(EntityType<? extends SwordEntity> p_36999_, LivingEntity p_37000_, Vec3 p_347671_, Level p_37004_) {
        super(p_36999_, p_37000_, p_347671_, p_37004_);
    }

    public SwordEntity(LivingEntity breeze, Level level) {
        this(ModEntities.SWORD.get(), level, breeze, breeze.getX(), breeze.getEyeY(), breeze.getZ());
    }

    public SwordEntity(EntityType<? extends SwordEntity> entityType, Level level, Entity owner, double x, double y, double z) {
        super(entityType, x, y, z, level);
        this.setOwner(owner);
        this.accelerationPower = (double) 0.0F;
    }

    public int noMoveTick;


    @Override
    public void tick() {
        if (noMoveTick >= 20) {
            super.tick();
        } else {
            Vec3 vec3 = this.position();

            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            this.setPos(vec3);
        }
        ++this.noMoveTick;
    }

    @Override
    protected void onHitEntity(EntityHitResult p_326121_) {
        super.onHitEntity(p_326121_);
        Level var3 = this.level();
        if (var3 instanceof ServerLevel serverlevel) {
            Entity var5 = this.getOwner();
            LivingEntity var10000;
            if (var5 instanceof LivingEntity livingentity) {
                var10000 = livingentity;
            } else {
                var10000 = null;
            }

            LivingEntity livingentity2 = var10000;
            Entity entity = p_326121_.getEntity();
            if (livingentity2 != null) {
                livingentity2.setLastHurtMob(entity);
            }

            DamageSource damagesource = this.damageSources().mobProjectile(this, livingentity2);
            if (entity.hurtServer(serverlevel, damagesource, 12.0F) && entity instanceof LivingEntity livingentity1) {
                EnchantmentHelper.doPostAttackEffects(serverlevel, livingentity1, damagesource);
            }

        }

    }

    @Override
    protected void onHit(HitResult p_326337_) {
        super.onHit(p_326337_);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput p_421866_) {
        super.addAdditionalSaveData(p_421866_);
        p_421866_.putInt("no_move_tick", this.noMoveTick);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput p_421853_) {
        super.readAdditionalSaveData(p_421853_);
        this.noMoveTick = p_421853_.getInt("no_move_tick").orElse(0);
    }

    @Override
    public ItemStack getItem() {
        return ModItems.END_BLADE.asItem().getDefaultInstance();
    }

    @Override
    protected float getInertia() {
        return 1.0F;
    }

    @Override
    protected float getLiquidInertia() {
        return this.getInertia();
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

}
