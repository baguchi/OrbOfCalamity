package baguchi.orb_of_calamity.data;

import baguchi.orb_of_calamity.OrbOfCalamity;
import baguchi.orb_of_calamity.register.ModEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class OrbEntityTags extends EntityTypeTagsProvider {
    public OrbEntityTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, OrbOfCalamity.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(ModEntities.ORB_OF_ENDER.get());
    }
}
