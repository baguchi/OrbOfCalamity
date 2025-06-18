package baguchi.orb_of_calamity.entity.goal;

import baguchi.orb_of_calamity.entity.OrbOfEnder;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RandomAttackIdleGoal extends Goal {
    private final OrbOfEnder mob;
    private int cooldown;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;
    public RandomAttackIdleGoal(OrbOfEnder mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }


    @Override
    public boolean canUse() {
        return this.mob.getTarget() != null && this.mob.getAction() == OrbOfEnder.Actions.NORMAL;
    }


    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.cooldown++ > 60) {
            if (this.mob.swordCooldown <= 0) {
                this.mob.setAction(OrbOfEnder.Actions.SHOOT_SWORD);
                this.mob.swordCooldown = this.mob.getRandom().nextInt(400) + 200;
            }
            if(this.mob.wideBeamCooldown <= 0){
                this.mob.setAction(OrbOfEnder.Actions.START_BREATH);
                this.mob.wideBeamCooldown = this.mob.getRandom().nextInt(100) + 100;
                this.mob.teleportTowards(this.mob);
            }

            this.cooldown = 0;
        }
        if (this.mob.getTarget() != null) {
            this.mob.getLookControl().setLookAt(this.mob.getTarget());
        }

        if (++this.strafingTime >= 20) {
            if ((double) this.mob.getRandom().nextFloat() < 0.3D) {
                this.strafingClockwise = !this.strafingClockwise;
            }

            if ((double) this.mob.getRandom().nextFloat() < 0.3D) {
                this.strafingBackwards = !this.strafingBackwards;
            }

            this.strafingTime = 0;
        }
        this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.65F : 0.65F, this.strafingClockwise ? 0.65F : -0.65F);

    }

}