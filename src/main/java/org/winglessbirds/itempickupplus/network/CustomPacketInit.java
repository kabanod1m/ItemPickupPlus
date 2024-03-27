package org.winglessbirds.itempickupplus.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import org.winglessbirds.itempickupplus.ItemPickupPlus;
import org.winglessbirds.itempickupplus.network.packet.c2s.play.PlayerDropOffhandItemC2SPacket;
import org.winglessbirds.itempickupplus.network.packet.c2s.play.PlayerPickupItemC2SPacket;

public class CustomPacketInit {
    public static final Identifier pktc2sPickupID = new Identifier(ItemPickupPlus.MODID, ItemPickupPlus.pktc2sPickup);
    public static final Identifier pktc2sDropOffID = new Identifier(ItemPickupPlus.MODID, ItemPickupPlus.pktc2sDropOff);

    public static void registerC2SPackets() {
        //ServerPlayNetworking.registerGlobalReceiver(pktc2sPickupID, )
        ServerPlayNetworking.registerGlobalReceiver(pktc2sDropOffID, PlayerDropOffhandItemC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(pktc2sPickupID, PlayerPickupItemC2SPacket::receive);
    }

    public static void registerS2CPackets() {

    }
}
