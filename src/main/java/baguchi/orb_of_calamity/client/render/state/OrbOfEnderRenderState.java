package baguchi.orb_of_calamity.client.render.state;

import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class OrbOfEnderRenderState extends ArmedEntityRenderState {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState preSpawnAnimationState = new AnimationState();
    public final AnimationState spawnAnimationState = new AnimationState();
    public final AnimationState swordAttackAnimationState = new AnimationState();
    public final AnimationState preBreathAnimationState = new AnimationState();
    public final AnimationState stopBreathAnimationState = new AnimationState();
    public final AnimationState breathAnimationState = new AnimationState();
    public final AnimationState deathAnimationState = new AnimationState();

}
