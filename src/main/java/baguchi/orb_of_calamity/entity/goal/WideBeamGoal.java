package baguchi.orb_of_calamity.entity.goal;

import baguchi.orb_of_calamity.entity.OrbOfEnder;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class WideBeamGoal extends Goal {
    private final OrbOfEnder mob;
    protected final double range;
    private int breathTick;
    public WideBeamGoal(OrbOfEnder mob, double range) {
        this.mob = mob;
        this.range = range;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }


    @Override
    public boolean canUse() {
        return this.mob.getAction() == OrbOfEnder.Actions.START_BREATH;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() || this.breathTick < 240;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.mob.getAction() == OrbOfEnder.Actions.BREATH){
            this.breathTick++;
            doAttack();
        }
    }

    protected void doAttack() {
        Level var3 = this.mob.level();

        if (var3 instanceof ServerLevel serverLevel) {
            List<Entity> entitiesHit = serverLevel.getEntitiesOfClass(Entity.class, getAttackBoundingBox(this.mob));
            for (Entity entity : entitiesHit) {
                if (entity != this.mob) {
                    if (!this.mob.isAlliedTo(entity)) {
                        Vec3 vec3 = entity.getEyePosition();
                        Vec3 yVector = this.mob.calculateViewVector(this.mob.getXRot(), this.mob.getYHeadRot());
                        Vec3 vec32 = vec3.subtract(this.mob.getEyePosition());
                        Vec3 vec33 = (new Vec3(vec32.x, vec32.y, vec32.z)).normalize();
                        double d0 = Math.acos(vec33.dot(yVector));
                        if (resolveAttack(d0, range)) {
                            boolean sight = this.mob.getSensing().hasLineOfSight(entity);

                            Vec3 look = this.mob.getLookAngle().scale(sight ? 0.2F : 0.04F);

                            entity.setDeltaMovement(entity.getDeltaMovement().add(look));
                            if (entity instanceof ServerPlayer serverplayer) {
                                serverplayer.connection.send(new ClientboundSetEntityMotionPacket(serverplayer));
                            }
                        }
                    }
                }
            }
        }

    }

    public AABB getAttackBoundingBox(PathfinderMob attacker) {
        Entity entity = attacker.getVehicle();
        AABB aabb;
        if (entity != null) {
            AABB aabb1 = entity.getBoundingBox();
            AABB aabb2 = attacker.getBoundingBox();
            aabb = new AABB(Math.min(aabb2.minX, aabb1.minX), aabb2.minY, Math.min(aabb2.minZ, aabb1.minZ), Math.max(aabb2.maxX, aabb1.maxX), aabb2.maxY, Math.max(aabb2.maxZ, aabb1.maxZ));
        } else {
            aabb = attacker.getBoundingBox();
        }

        return aabb.inflate(Math.sqrt(2.04F) - (double) 0.6F, 0.0F, Math.sqrt(2.04F) - (double) 0.6F).inflate(30F);
    }

    public boolean resolveAttack(double yRot, double yRotAttackRange) {
        return !(yRot > (((float) Math.PI / 180F) * yRotAttackRange));
    }


    @Override
    public void stop() {
        super.stop();
        this.mob.setAction(OrbOfEnder.Actions.STOP_BREATH);
    }

    @Override
    public void start() {
        super.start();
        this.breathTick = 0;
    }
}