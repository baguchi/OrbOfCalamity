package baguchi.orb_of_calamity.data;

import baguchi.orb_of_calamity.OrbOfCalamity;
import baguchi.orb_of_calamity.register.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class OrbItemTags extends ItemTagsProvider {
    public OrbItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, OrbOfCalamity.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ItemTags.SWORDS).add(ModItems.END_BLADE.asItem());
    }
}
