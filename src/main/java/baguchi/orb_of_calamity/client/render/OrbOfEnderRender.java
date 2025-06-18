package baguchi.orb_of_calamity.client.render;

import baguchi.orb_of_calamity.OrbOfCalamity;
import baguchi.orb_of_calamity.client.ModModelLayers;
import baguchi.orb_of_calamity.client.model.OrbOfEnderModel;
import baguchi.orb_of_calamity.client.render.state.OrbOfEnderRenderState;
import baguchi.orb_of_calamity.entity.OrbOfEnder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class OrbOfEnderRender extends MobRenderer<OrbOfEnder, OrbOfEnderRenderState, OrbOfEnderModel<OrbOfEnderRenderState>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(OrbOfCalamity.MODID, "textures/entity/orb_of_ender/orb_of_ender.png");
    private static final RenderType GLOW = RenderType.eyes(ResourceLocation.fromNamespaceAndPath(OrbOfCalamity.MODID, "textures/entity/orb_of_ender/orb_of_ender_eye.png"));

    private static final float HALF_SQRT_3 = (float) (Math.sqrt(140.0) / 2.0);

    public OrbOfEnderRender(EntityRendererProvider.Context p_173952_) {
        super(p_173952_, new OrbOfEnderModel<>(p_173952_.bakeLayer(ModModelLayers.ORB_OF_END)), 0.5F);
        this.addLayer(new EyesLayer<>(this) {
            @Override
            public RenderType renderType() {
                return GLOW;
            }
        });
        this.addLayer(new ItemInHandLayer(this));
    }

    @Override
    public OrbOfEnderRenderState createRenderState() {
        return new OrbOfEnderRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(OrbOfEnderRenderState orbOfEnderRenderState) {
        return TEXTURE;
    }

    @Override
    public void render(OrbOfEnderRenderState p_361886_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_) {
        super.render(p_361886_, p_115311_, p_115312_, p_115313_);
        if (p_361886_.breathAnimationState.isStarted()) {
            p_115311_.pushPose();
            p_115311_.scale(-1.0F, -1.0F, 1.0F);
            p_115311_.translate(0.0F, -1.501F - (2 / 16F), -(2 / 16F));
            this.getModel().Body.translateAndRotate(p_115311_);
            this.getModel().orb.translateAndRotate(p_115311_);
            renderEyeRays(p_361886_, p_115311_, 0.8F, p_115312_.getBuffer(RenderType.dragonRays()));
            renderEyeRays(p_361886_, p_115311_, 0.8F, p_115312_.getBuffer(RenderType.dragonRaysDepth()));
            p_115311_.popPose();
        }

    }

    public void translate(ModelPart part, PoseStack p_104300_) {
        p_104300_.translate(part.x / 16.0F, part.y / 16.0F, part.z / 16.0F);
    }


    private static void renderEyeRays(OrbOfEnderRenderState seekerRenderState, PoseStack p_352922_, float p_352903_, VertexConsumer p_352894_) {
        p_352922_.pushPose();
        int i = ARGB.colorFromFloat(1.0F - p_352903_, 1.0F, 1.0F, 1.0F);
        Vector3f vector3f = new Vector3f();
        Vector3f vector3f1 = new Vector3f();
        Vector3f vector3f2 = new Vector3f();
        Vector3f vector3f3 = new Vector3f();
        Quaternionf quaternionf = new Quaternionf();
        quaternionf.rotationYXZ((float) ((seekerRenderState.bodyRot - 180.0F) * (Math.PI / 180F)), (float) ((-90) * (Math.PI / 180F)), 0);
        p_352922_.mulPose(quaternionf);
        float f1 = 16F;
        float f2 = p_352903_;
        vector3f1.set(-HALF_SQRT_3 * f2, f1, -f2);
        vector3f2.set(HALF_SQRT_3 * f2, f1, -f2);
        vector3f3.set(0.0F, f1, f2);
        PoseStack.Pose posestack$pose = p_352922_.last();
        p_352894_.addVertex(posestack$pose, vector3f).setColor(i);
        p_352894_.addVertex(posestack$pose, vector3f1).setColor(16711935);
        p_352894_.addVertex(posestack$pose, vector3f2).setColor(16711935);
        p_352894_.addVertex(posestack$pose, vector3f).setColor(i);
        p_352894_.addVertex(posestack$pose, vector3f2).setColor(16711935);
        p_352894_.addVertex(posestack$pose, vector3f3).setColor(16711935);
        p_352894_.addVertex(posestack$pose, vector3f).setColor(i);
        p_352894_.addVertex(posestack$pose, vector3f3).setColor(16711935);
        p_352894_.addVertex(posestack$pose, vector3f1).setColor(16711935);


        p_352922_.popPose();
    }

    @Override
    protected float getFlipDegrees() {
        return 0;
    }


    @Override
    public void extractRenderState(OrbOfEnder p_362733_, OrbOfEnderRenderState p_360515_, float p_361157_) {
        super.extractRenderState(p_362733_, p_360515_, p_361157_);
        p_360515_.breathAnimationState.copyFrom(p_362733_.breathAnimationState);
        p_360515_.preBreathAnimationState.copyFrom(p_362733_.preBreathAnimationState);
        p_360515_.stopBreathAnimationState.copyFrom(p_362733_.stopBreathAnimationState);
        p_360515_.spawnAnimationState.copyFrom(p_362733_.spawnAnimationState);
        p_360515_.swordAttackAnimationState.copyFrom(p_362733_.swordAttackAnimationState);
        p_360515_.idleAnimationState.copyFrom(p_362733_.idleAnimationState);

    }
}
