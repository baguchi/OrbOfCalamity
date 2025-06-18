package baguchi.orb_of_calamity.mixin;

import baguchi.orb_of_calamity.entity.OrbOfEnder;
import baguchi.orb_of_calamity.register.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragon.class)
public abstract class DragonMixin extends Mob {

    @Shadow
    public int dragonDeathTime;

    protected DragonMixin(EntityType<? extends Mob> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
    }

    @Inject(method = "tickDeath", at = @At("HEAD"))
    protected void tickDeath(CallbackInfo ci) {
        if (this.dragonDeathTime == 199) {
            if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
                OrbOfEnder orb = ModEntities.ORB_OF_ENDER.get().create(this.level(), EntitySpawnReason.EVENT);
                orb.snapTo(this.position());
                orb.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(this.blockPosition()), EntitySpawnReason.EVENT, null);
                this.level().addFreshEntity(orb);
            }
        }
    }
}
