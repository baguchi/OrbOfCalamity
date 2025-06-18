package baguchi.orb_of_calamity.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.HashMap;
import java.util.Map;

public class ModSavedData extends SavedData {
    public static final Codec<ModSavedData> CODEC = RecordCodecBuilder.create(
            p_400930_ -> p_400930_.group(
                            Codec.BOOL.fieldOf("defeated").forGetter(p_400933_ -> p_400933_.defeated)
                    )
                    .apply(p_400930_, ModSavedData::new)
    );
    private static final String IDENTIFIER = "end_world_data";
    public static final SavedDataType<ModSavedData> TYPE = new SavedDataType<>(
            "end_world_data",
            ModSavedData::new,
            CODEC);

    private boolean defeated;
    private static Map<Level, ModSavedData> dataMap = new HashMap<>();

    public ModSavedData() {
        this(false);
    }

    public ModSavedData(boolean defeated) {
        this.defeated = defeated;
    }

    public static ModSavedData get(Level world) {
        if (world instanceof ServerLevel serverLevel) {
            ServerLevel overworld = world.getServer().getLevel(serverLevel.dimension());
            ModSavedData fromMap = dataMap.get(overworld);
            if (fromMap == null) {
                DimensionDataStorage storage = overworld.getDataStorage();
                ModSavedData data = storage.computeIfAbsent(TYPE);
                if (data != null) {
                    data.setDirty();
                }
                dataMap.put(world, data);
                return data;
            }
            return fromMap;
        }
        return null;
    }

    public void setDefeated(boolean defeated) {
        this.defeated = defeated;
    }

    public boolean isDefeated() {
        return defeated;
    }
}