package org.winglessbirds.itempickupplus.util;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public class ItemPickupEvents {

    public static final Event<Before> BEFORE = EventFactory.createArrayBacked(Before.class,
            (listeners) -> (player) -> {
                for (Before event : listeners) {
                    boolean result = event.beforeItemPickup(player);

                    if (!result) {
                        return false;
                    }
                }

                return true;
            }
    );

    @FunctionalInterface
    public interface Before {
        boolean beforeItemPickup(PlayerEntity player);
    }
}
