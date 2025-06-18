package baguchi.orb_of_calamity.data;

import baguchi.orb_of_calamity.OrbOfCalamity;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class OrbModelTemplate {
    public static final ModelTemplate BIG_HANDHELD = create("item/big_handheld", TextureSlot.LAYER0);

    public static ModelTemplate create(String p_386521_, TextureSlot... p_388561_) {
        return new ModelTemplate(Optional.of(ResourceLocation.fromNamespaceAndPath(OrbOfCalamity.MODID, p_386521_)), Optional.empty(), p_388561_);
    }

    public static ModelTemplate createDefault(String p_386521_, TextureSlot... p_388561_) {
        return new ModelTemplate(Optional.of(ResourceLocation.withDefaultNamespace(p_386521_).withPrefix("block/")), Optional.empty(), p_388561_);
    }
}
