package baguchi.orb_of_calamity.data;

import baguchi.orb_of_calamity.register.ModItems;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public class OrbItemModelGenerators extends ItemModelGenerators {
    public OrbItemModelGenerators(ItemModelOutput itemModelOutput, BiConsumer<ResourceLocation, ModelInstance> modelOutput) {
        super(itemModelOutput, modelOutput);
    }

    @Override
    public void run() {
        this.generateFlatItem(ModItems.END_BLADE.asItem(), OrbModelTemplate.BIG_HANDHELD);
    }
}
