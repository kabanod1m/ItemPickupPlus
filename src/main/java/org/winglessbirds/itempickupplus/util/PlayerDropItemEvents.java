package org.winglessbirds.itempickupplus.util;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class PlayerDropItemEvents {
    
    public static final Event<After> AFTER = EventFactory.createArrayBacked(After.class,
            (listeners) -> (stack, throwRandomly, retainOwnership, itemEntity) -> {
                for (After event : listeners) {
                    event.afterDropItem(stack, throwRandomly, retainOwnership, itemEntity);
                }
            }
    );

    @FunctionalInterface
    public interface After {
        void afterDropItem(ItemStack item, boolean throwRandomly, boolean retainOwnership, @Nullable CallbackInfoReturnable itemEntity);
    }
}
