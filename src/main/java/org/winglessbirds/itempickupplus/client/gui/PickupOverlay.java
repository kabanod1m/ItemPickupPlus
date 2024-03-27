package org.winglessbirds.itempickupplus.client.gui;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.winglessbirds.itempickupplus.ItemPickupPlus;
import org.winglessbirds.itempickupplus.client.ItemPickupPlusClient;

public class PickupOverlay implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, float v) {

        if (!ItemPickupPlus.CFG.displayHud) {
            return;
        }

        if (ItemPickupPlus.CFG.displayHudInWorld) {
            return;
        }

        ItemStack stack;
        try {
            stack = ((ItemEntity)ItemPickupPlusClient.pointedItem.getEntity()).getStack();
        } catch (Exception e) {
            return;
        }

        if (stack.getItem().equals(Registries.ITEM.get(new Identifier("minecraft", "air")))) {
            return; // I'm doing this not because I'm a schizophrenic, but because apparently when you pick up an item it turns into air for some time before disappearing!
        }

        TextRenderer textr = ItemPickupPlusClient.client.textRenderer;
        int w = drawContext.getScaledWindowWidth();
        int h = drawContext.getScaledWindowHeight();
        int w_offset = ItemPickupPlus.CFG.hudOffsetX;
        int h_offset = ItemPickupPlus.CFG.hudOffsetY;
        int white = 0xFFFFFF;

        drawContext.drawItem(
                stack,
                w / 2 - 8 + w_offset,
                h / 2 + 16 + h_offset
        );
        if (ItemPickupPlus.CFG.onlyWhenSneaking && !ItemPickupPlusClient.client.player.isSneaking()) {
            String strKeySneak = ItemPickupPlusClient.client.options.sneakKey.getBoundKeyLocalizedText().getString();
            drawContext.drawCenteredTextWithShadow(
                    textr,
                    "[ " + strKeySneak + " ]",
                    w / 2 + w_offset,
                    h / 2 + 36 + h_offset,
                    white
            );
            return;
        }
        String strKeyPickupMain = ItemPickupPlusClient.keyPickupMain.getBoundKeyLocalizedText().getString();
        String strPickupMain = Text.translatable("gui.itempickupplus.tooltip.pickup_main").getString();
        String strItem = stack.getItem().getName().getString();
        drawContext.drawCenteredTextWithShadow(
                textr,
                "[ " + strKeyPickupMain + " ] " + strPickupMain + " " + strItem,
                w / 2 + w_offset,
                h / 2 + 36 + h_offset,
                white
        );
        if (ItemPickupPlusClient.keyPickupOff.isUnbound()) {
            return;
        }
        String strKeyPickupOff = ItemPickupPlusClient.keyPickupOff.getBoundKeyLocalizedText().getString();
        String strPickupOff = Text.translatable("gui.itempickupplus.tooltip.pickup_off").getString();
        drawContext.drawCenteredTextWithShadow(
                textr,
                "[ " + strKeyPickupOff + " ] " + strPickupOff,
                w / 2 + w_offset,
                h / 2 + 44 + h_offset,
                white
        );
    }

}
