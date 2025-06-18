package baguchi.orb_of_calamity.client;

import baguchi.orb_of_calamity.OrbOfCalamity;
import baguchi.orb_of_calamity.client.model.OrbOfEnderModel;
import baguchi.orb_of_calamity.client.render.OrbOfEnderRender;
import baguchi.orb_of_calamity.register.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = OrbOfCalamity.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegister {
    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.ORB_OF_ENDER.get(), OrbOfEnderRender::new);
     }

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {

        event.registerLayerDefinition(ModModelLayers.ORB_OF_END, OrbOfEnderModel::createBodyLayer);
    }
}
