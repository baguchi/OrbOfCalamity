package baguchi.orb_of_calamity.entity.goal;

import baguchi.orb_of_calamity.entity.OrbOfEnder;
import baguchi.orb_of_calamity.entity.SwordEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

public class SwordGoal extends Goal {
    private final OrbOfEnder mob;
    private int tick;

    public SwordGoal(OrbOfEnder mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }


    @Override
    public boolean canUse() {
        return this.mob.getAction() == OrbOfEnder.Actions.SHOOT_SWORD && this.mob.getTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.tick < 100;
    }

    @Override
    public void tick() {
        super.tick();
        this.tick++;
        if (this.tick % 10 == 0) {
            doAttack();
        }

        if (this.mob.getTarget() != null) {
            this.mob.getLookControl().setLookAt(this.mob.getTarget());
        }
    }

    protected void doAttack() {
        Level var3 = this.mob.level();

        if (this.mob.getTarget() != null) {
            double x = this.mob.getX() + this.mob.getRandom().nextInt(8) - this.mob.getRandom().nextInt(8);
            double y = this.mob.getEyeY() + 4 + this.mob.getRandom().nextInt(8);
            double z = this.mob.getZ() + this.mob.getRandom().nextInt(8) - this.mob.getRandom().nextInt(8);

            if (this.mob.level().getBlockState(new BlockPos((int) x, (int) y, (int) z)).isAir()) {
                LivingEntity livingentity = this.mob.getTarget();
                double d0 = livingentity.getX() - x;
                double d1 = livingentity.getY(livingentity.isPassenger() ? 0.8 : 0.3) - y;
                double d2 = livingentity.getZ() - z;
                Projectile.spawnProjectileUsingShoot(
                        new SwordEntity(this.mob, getServerLevel(this.mob)), getServerLevel(this.mob), ItemStack.EMPTY, d0, d1, d2, 2F, 5 - this.mob.level().getDifficulty().getId() * 4
                ).setPos(x, y, z);

                this.getServerLevel(this.mob)
                        .sendParticles(
                                ParticleTypes.PORTAL,
                                x,
                                y - 0.25,
                                z,
                                8,
                                (this.mob.getRandom().nextDouble() - 0.5) * 2.0,
                                -this.mob.getRandom().nextDouble(),
                                (this.mob.getRandom().nextDouble() - 0.5) * 2.0,
                                1
                        );
                this.mob.level().playSound(this.mob, x, y, z, SoundEvents.TRIDENT_THROW, SoundSource.HOSTILE, 2F, 1F);
            }

        }
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setAction(OrbOfEnder.Actions.NORMAL);
    }

    @Override
    public void start() {
        super.start();
        this.tick = 0;
    }
}