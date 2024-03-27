package org.winglessbirds.itempickupplus.network.packet.c2s;

import org.winglessbirds.itempickupplus.ItemPickupPlus;

public class BadPacketNotify {

    private static final String errorString = ": Bad packet encountered! Hacker alert? From: playerName , UUID: ";

    public static void notify(String packetName, String playerName, String playerUuid) {
        ItemPickupPlus.LOG.warn(packetName + errorString + playerName + " , " + playerUuid);
    }

}
