package org.winglessbirds.itempickupplus.network.packet.c2s.play;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import org.winglessbirds.itempickupplus.ItemPickupPlus;
import org.winglessbirds.itempickupplus.network.packet.c2s.BadPacketNotify;

public class PlayerPickupItemC2SPacket { // PacketBuf structure: boolean isOffhand, int entityId

    private static final String packetName = ItemPickupPlus.pktc2sPickup;

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        // determine max pick up range
        float cfgrange = ItemPickupPlus.CFG.pickupRange;
        double rangeSq = cfgrange < 0.0f ? ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE : cfgrange * cfgrange;

        // get data from packet
        boolean isOffhand;
        int entityId;
        try {
            isOffhand = buf.getBoolean(0);
            entityId = buf.getInt(1);
        } catch (IndexOutOfBoundsException e) {
            BadPacketNotify.notify(packetName, player.getName().toString(), player.getUuid().toString());
            return;
        }

        server.executeSync(() -> {
            Entity entity = player.getWorld().getEntityById(entityId);
            if (!(entity instanceof ItemEntity)) { // also checks if the entity was found or not (if null return)
                BadPacketNotify.notify(packetName, player.getName().toString(), player.getUuid().toString());
                return;
            }
            ItemEntity item = (ItemEntity) entity;
            double dist = entity.squaredDistanceTo(player.getCameraEntity());
            if (dist > rangeSq) { // do distance check separately as discrepancies less than ~0.5 of a block are not always necessarily cheating and do not need a warning
                if (dist - rangeSq > 3.5d) {
                    //ItemPickupPlus.LOG.debug("delta: " + (dist - rangeSq));
                    BadPacketNotify.notify(packetName, player.getName().toString(), player.getUuid().toString());
                }
                return;
            }

            ItemStack stack = item.getStack();
            int amount = stack.getCount();
            if (!ItemPickupPlus.CFG.onlyEmptyHand) {
                // vanilla pick up logic
                if (!item.cannotPickup() &&
                        (item.getOwner() == null || item.getOwner().getUuid().equals(player.getUuid())) &&
                        ((!isOffhand && player.getInventory().insertStack(stack)) ||
                                (isOffhand && player.getInventory().insertStack(PlayerInventory.OFF_HAND_SLOT, stack)))
                ) {
                    confirmPickUp(player, item, stack, amount);
                }
            } else {
                // hand-based pick up logic
                ItemStack curstack = isOffhand ? player.getOffHandStack() : player.getMainHandStack();
                if ((curstack.isEmpty() || (ItemStack.canCombine(curstack, stack))) &&
                        !item.cannotPickup() &&
                        (item.getOwner() == null || item.getOwner().getUuid().equals(player.getUuid())) &&
                        (!isOffhand && player.getInventory().insertStack(player.getInventory().selectedSlot, stack) ||
                                (isOffhand && player.getInventory().insertStack(PlayerInventory.OFF_HAND_SLOT, stack)))
                ) {
                    confirmPickUp(player, item, stack, amount);
                }
            }
            // common logic (stats)
            player.increaseStat(Stats.PICKED_UP.getOrCreateStat(stack.getItem()), amount);
            player.triggerItemPickedUpByEntityCriteria(item);
        });
    }

    private static void confirmPickUp(PlayerEntity player, ItemEntity item, ItemStack stack, int amount) {
        player.sendPickup(item, amount);
        if (stack.isEmpty()) {
            item.discard();
            stack.setCount(amount);
        }
    }

}
