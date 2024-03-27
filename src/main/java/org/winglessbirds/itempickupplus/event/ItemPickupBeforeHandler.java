package org.winglessbirds.itempickupplus.event;

import net.minecraft.entity.player.PlayerEntity;
import org.winglessbirds.itempickupplus.ItemPickupPlus;
import org.winglessbirds.itempickupplus.util.ItemPickupEvents;

public class ItemPickupBeforeHandler implements ItemPickupEvents.Before {
    @Override
    public boolean beforeItemPickup(PlayerEntity player) {
        if (ItemPickupPlus.CFG.modEnabled) {
            return false;
        }

        return true;
    }
}
