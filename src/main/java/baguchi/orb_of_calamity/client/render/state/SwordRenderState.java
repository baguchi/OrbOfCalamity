package baguchi.orb_of_calamity.client.render.state;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class SwordRenderState extends EntityRenderState {
    public float xRot;
    public float yRot;
    public ItemStackRenderState sword = new ItemStackRenderState();
}