package org.winglessbirds.itempickupplus.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.hit.EntityHitResult;
import org.lwjgl.glfw.GLFW;
import org.winglessbirds.itempickupplus.client.event.ClientEndTickHandler;
import org.winglessbirds.itempickupplus.client.gui.PickupOverlay;
import org.winglessbirds.itempickupplus.network.CustomPacketInit;

public class ItemPickupPlusClient implements ClientModInitializer {

    public static final MinecraftClient client = MinecraftClient.getInstance();

    // names
    public String nameCategoryPickup = "category.itempickupplus.pickup";
    public String namePickupMain = "key.itempickupplus.pickupmain";
    public String namePickupOff = "key.itempickupplus.pickupoff";

    public String nameCategoryDrop = "category.itempickupplus.drop";
    public String nameDropOff = "key.itempickupplus.dropoff";

    // keybindings
    public static KeyBinding keyPickupMain;
    public static KeyBinding keyPickupOff;
    public static KeyBinding keyDropOff;

    // gui
    public static EntityHitResult pointedItem;

    @Override
    public void onInitializeClient() {
        // register keys
        keyPickupMain = KeyBindingHelper.registerKeyBinding(new KeyBinding(namePickupMain, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, nameCategoryPickup));
        keyPickupOff = KeyBindingHelper.registerKeyBinding(new KeyBinding(namePickupOff, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, nameCategoryPickup));
        keyDropOff = KeyBindingHelper.registerKeyBinding(new KeyBinding(nameDropOff, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_GRAVE_ACCENT, nameCategoryDrop));

        // register client-side events
        ClientTickEvents.END_CLIENT_TICK.register(new ClientEndTickHandler());
        HudRenderCallback.EVENT.register(new PickupOverlay());

        // register client-side packets
        CustomPacketInit.registerS2CPackets();
    }
}
