package baguchi.orb_of_calamity.entity.goal;

import baguchi.orb_of_calamity.entity.OrbOfEnder;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class AwakeGoal extends Goal {
    private final OrbOfEnder mob;

    public AwakeGoal(OrbOfEnder mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }


    @Override
    public boolean canUse() {
        return this.mob.getTarget() != null && this.mob.distanceTo(this.mob.getTarget()) > 8 && this.mob.getAction() == OrbOfEnder.Actions.PRE_AWAKEN;
    }


    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse();
    }

    @Override
    public void start() {
        super.start();
        this.mob.setAction(OrbOfEnder.Actions.AWAKEN);
    }

}