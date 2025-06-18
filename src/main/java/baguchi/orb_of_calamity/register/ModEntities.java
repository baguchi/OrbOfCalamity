package baguchi.orb_of_calamity.register;

import baguchi.orb_of_calamity.OrbOfCalamity;
import baguchi.orb_of_calamity.entity.OrbOfEnder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = OrbOfCalamity.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES_REGISTRY = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, OrbOfCalamity.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<OrbOfEnder>> ORB_OF_ENDER = ENTITIES_REGISTRY.register("orb_of_ender", () -> EntityType.Builder.of(OrbOfEnder::new, MobCategory.CREATURE).sized(0.65F, 2.45F).eyeHeight(2.45F * 0.95F).build(prefix("orb_of_ender")));

    private static ResourceKey<EntityType<?>> prefix(String path) {
        return ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(OrbOfCalamity.MODID, path));
    }

    @SubscribeEvent
    public static void registerEntity(EntityAttributeCreationEvent event) {
        event.put(ORB_OF_ENDER.get(), OrbOfEnder.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacement(RegisterSpawnPlacementsEvent event) {
        event.register(ORB_OF_ENDER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, OrbOfEnder::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
   }
}