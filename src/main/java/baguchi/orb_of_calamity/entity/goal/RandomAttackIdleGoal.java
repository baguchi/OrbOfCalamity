package baguchi.orb_of_calamity.entity.goal;

import baguchi.orb_of_calamity.entity.OrbOfEnder;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RandomAttackIdleGoal extends Goal {
    private final OrbOfEnder mob;
    private int cooldown;
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
        if(this.cooldown++ > 100){
            if(this.mob.wideBeamCooldown <= 0){
                this.mob.setAction(OrbOfEnder.Actions.START_BREATH);
                this.mob.wideBeamCooldown = this.mob.getRandom().nextInt(200) + 200;
                this.mob.teleportTowards(this.mob);
            }
            if(this.mob.swordCooldown <= 0){
                //this.mob.setAction(OrbOfEnder.Actions.SHOOT_SWORD);
                this.mob.swordCooldown = this.mob.getRandom().nextInt(400) + 400;
            }
            this.cooldown = 0;
        }
    }

}