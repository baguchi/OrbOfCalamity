package baguchi.orb_of_calamity.register;

import baguchi.orb_of_calamity.item.EndBladeItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static baguchi.orb_of_calamity.OrbOfCalamity.MODID;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> END_BLADE = ITEMS.registerItem("end_blade", EndBladeItem::new);

}
