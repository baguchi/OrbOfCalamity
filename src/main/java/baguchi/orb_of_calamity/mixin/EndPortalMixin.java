package baguchi.orb_of_calamity.mixin;

import baguchi.orb_of_calamity.world.ModSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndPortalBlock.class)
public class EndPortalMixin {


    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier p_405056_, CallbackInfo ci) {

        if (level instanceof ServerLevel serverLevel && level.dimension() == Level.END) {
            if (!ModSavedData.get(serverLevel).isDefeated()) {
                if (entity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.sendSystemMessage(Component.translatable("message.orb_of_ender.cannot_escape"), true);
                }
                ci.cancel();
            }
        }
    }
}