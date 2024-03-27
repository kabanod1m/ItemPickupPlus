package org.winglessbirds.itempickupplus.network.packet.c2s.play;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.winglessbirds.itempickupplus.ItemPickupPlus;
import org.winglessbirds.itempickupplus.network.packet.c2s.BadPacketNotify;
import org.winglessbirds.itempickupplus.util.PlayerDropItemEvents;

public class PlayerDropOffhandItemC2SPacket {

    private static final String packetName = ItemPickupPlus.pktc2sDropOff;

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        //ItemPickupPlus.LOG.debug("packet #2 received!");

        // always allow dropping offhand whether mod is on or off

        // get data from packet
        boolean entireStack;
        try {
            entireStack = buf.getBoolean(0);
        } catch (IndexOutOfBoundsException e) {
            BadPacketNotify.notify(packetName, player.getName().toString(), player.getUuid().toString());
            return;
        }

        server.executeSync(() -> {
            // determine amount
            ItemStack stack = entireStack ? player.getOffHandStack().copy() : player.getOffHandStack().copyWithCount(1);

            // determine position of ItemEntity
            double d = player.getEyeY() - 0.30000001192092896;
            ItemEntity itemEntity = new ItemEntity(player.getWorld(), player.getX(), d, player.getZ(), stack);

            // determine attributes of ItemEntity
            itemEntity.setPickupDelay(40);
            itemEntity.setThrower(player.getUuid());

            // determine velocity of ItemEntity
            Random random = player.getRandom();
            float g = MathHelper.sin(player.getPitch() * 0.017453292f);
            float h = MathHelper.cos(player.getPitch() * 0.017453292f);
            float i = MathHelper.sin(player.getYaw() * 0.017453292f);
            float j = MathHelper.cos(player.getYaw() * 0.017453292f);
            float k = random.nextFloat() * 6.2831855f;
            float l = 0.02f * random.nextFloat();
            itemEntity.setVelocity((double) (-i * h * 0.3F) + Math.cos((double) k) * (double) l, (double) (-g * 0.3F + 0.1F + (random.nextFloat() - random.nextFloat()) * 0.1F), (double) (j * h * 0.3F) + Math.sin((double) k) * (double) l);

            // spawn ItemEntity
            player.getWorld().spawnEntity(itemEntity);

            // callback PlayerDropItem event and remove item from player's inventory
            PlayerDropItemEvents.AFTER.invoker().afterDropItem(
                    stack.isEmpty() ? ItemStack.EMPTY : player.getInventory().removeStack(PlayerInventory.OFF_HAND_SLOT, entireStack ? stack.getCount() : 1),
                    false,
                    true,
                    new CallbackInfoReturnable("dropItem", true, itemEntity));

            // handle stats (not a mod bug, increasing global dropped items by just 1 instead of the correct amount is vanilla behavior)
            player.incrementStat(Stats.DROP);
            player.increaseStat(Stats.DROPPED.getOrCreateStat(stack.getItem()), stack.getCount());
        });
    }

}
