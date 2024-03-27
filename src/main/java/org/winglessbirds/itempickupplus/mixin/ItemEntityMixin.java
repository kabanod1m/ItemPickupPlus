package org.winglessbirds.itempickupplus.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.winglessbirds.itempickupplus.util.ItemPickupEvents;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Inject(method = "onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At("HEAD"), cancellable = true)
    private void inject_head_onPlayerCollision(final PlayerEntity player, final CallbackInfo ci) {
        boolean result = ItemPickupEvents.BEFORE.invoker().beforeItemPickup(player);

        if (!result) {
            ci.cancel();
        }
    }

}
