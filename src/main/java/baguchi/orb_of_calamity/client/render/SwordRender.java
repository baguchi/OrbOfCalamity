package baguchi.orb_of_calamity.client.render;

import baguchi.orb_of_calamity.client.render.state.SwordRenderState;
import baguchi.orb_of_calamity.entity.SwordEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;

public class SwordRender extends EntityRenderer<SwordEntity, SwordRenderState> {
    private final ItemModelResolver itemModelResolver;


    public SwordRender(EntityRendererProvider.Context renderManager) {
        super(renderManager);
        this.itemModelResolver = renderManager.getItemModelResolver();
    }

    @Override
    public void render(SwordRenderState renderState, PoseStack stackIn, MultiBufferSource bufferIn, int packedLightIn) {
        stackIn.pushPose();


        stackIn.mulPose(Axis.YP.rotationDegrees(-renderState.yRot));
        stackIn.translate(0.4F, 0.4F, 0.0F);
        stackIn.mulPose(Axis.XP.rotationDegrees(renderState.xRot));
        stackIn.mulPose(Axis.YP.rotationDegrees(-45.0f));
        stackIn.mulPose(Axis.XP.rotationDegrees(90.0f));
        renderState.sword.render(stackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
        stackIn.popPose();
        super.render(renderState, stackIn, bufferIn, packedLightIn);

    }

    @Override
    public SwordRenderState createRenderState() {
        return new SwordRenderState();
    }

    @Override
    public void extractRenderState(SwordEntity p_361771_, SwordRenderState p_364204_, float p_360538_) {
        super.extractRenderState(p_361771_, p_364204_, p_360538_);
        p_364204_.xRot = p_361771_.getXRot(p_360538_);
        p_364204_.yRot = p_361771_.getYRot(p_360538_);
        this.itemModelResolver.updateForNonLiving(p_364204_.sword, p_361771_.getItem(), ItemDisplayContext.GROUND, p_361771_);
    }
}