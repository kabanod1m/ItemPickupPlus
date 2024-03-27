package org.winglessbirds.itempickupplus;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.winglessbirds.itempickupplus.config.ModConfig;
import org.winglessbirds.itempickupplus.event.ItemPickupBeforeHandler;
import org.winglessbirds.itempickupplus.event.PlayerDropItemAfterHandler;
import org.winglessbirds.itempickupplus.network.CustomPacketInit;
import org.winglessbirds.itempickupplus.util.ItemPickupEvents;
import org.winglessbirds.itempickupplus.util.PlayerDropItemEvents;

public class ItemPickupPlus implements ModInitializer {

    public static final String MODID = "itempickupplus";
    public static final Logger LOG = LoggerFactory.getLogger(MODID);
    public static ModConfig CFG = new ModConfig();

    public static final String pktc2sPickup = "c2s_pickup";
    public static final String pktc2sDropOff = "c2s_dropoff";

    @Override
    public void onInitialize() {
        // config
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        CFG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        // register server-side events
        ItemPickupEvents.BEFORE.register(new ItemPickupBeforeHandler());
        PlayerDropItemEvents.AFTER.register(new PlayerDropItemAfterHandler());

        // register server-side packets
        CustomPacketInit.registerC2SPackets();
    }
}
