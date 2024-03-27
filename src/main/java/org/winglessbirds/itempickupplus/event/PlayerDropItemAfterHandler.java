package org.winglessbirds.itempickupplus.event;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.winglessbirds.itempickupplus.ItemPickupPlus;
import org.winglessbirds.itempickupplus.util.PlayerDropItemEvents;

public class PlayerDropItemAfterHandler implements PlayerDropItemEvents.After {

    @Override
    public void afterDropItem(ItemStack item, boolean throwRandomly, boolean retainOwnership, @Nullable CallbackInfoReturnable itemEntity) {

        if (!ItemPickupPlus.CFG.modEnabled) {
            return;
        }

        if (!ItemPickupPlus.CFG.noDropDelay) {
            return;
        }

        if (itemEntity == null) {
            return;
        }

        ItemEntity dropped = (ItemEntity) itemEntity.getReturnValue();

        if (dropped == null) {
            return;
        }

        dropped.setPickupDelay(0);
    }
}
