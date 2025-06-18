package baguchi.orb_of_calamity.register;

import baguchi.orb_of_calamity.OrbOfCalamity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, OrbOfCalamity.MODID);


    public static final DeferredHolder<SoundEvent, SoundEvent> ORB_OF_ENDER_ROAR = register("entity.orb_of_ender.roar");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String sound) {
        ResourceLocation name = ResourceLocation.fromNamespaceAndPath(OrbOfCalamity.MODID, sound);
        return SOUND_EVENTS.register(sound, () -> SoundEvent.createVariableRangeEvent(name));
    }
}