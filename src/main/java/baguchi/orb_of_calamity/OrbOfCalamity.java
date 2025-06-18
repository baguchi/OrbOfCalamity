package baguchi.orb_of_calamity;

import baguchi.orb_of_calamity.register.ModEntities;
import baguchi.orb_of_calamity.register.ModItems;
import baguchi.orb_of_calamity.register.ModSounds;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(OrbOfCalamity.MODID)
public class OrbOfCalamity {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "orb_of_calamity";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public OrbOfCalamity(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        ModSounds.SOUND_EVENTS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITIES_REGISTRY.register(modEventBus);
    }


  private void commonSetup(FMLCommonSetupEvent event) {

    }
}
