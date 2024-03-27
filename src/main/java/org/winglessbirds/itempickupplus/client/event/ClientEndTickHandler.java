package org.winglessbirds.itempickupplus.client.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.winglessbirds.itempickupplus.ItemPickupPlus;
import org.winglessbirds.itempickupplus.client.ItemPickupPlusClient;
import org.winglessbirds.itempickupplus.network.CustomPacketInit;

public class ClientEndTickHandler implements ClientTickEvents.EndTick {

    private static boolean hudDisableAck = false;

    @Override
    public void onEndTick(MinecraftClient client) {

        if (client.player == null) {
            return;
        }

        PlayerEntity player = client.player;

        while (ItemPickupPlusClient.keyDropOff.wasPressed()) { // always allow dropping offhand whether mod is on or off

            if (player.getOffHandStack().isEmpty()) {
                continue;
            }
            PacketByteBuf buf = PacketByteBufs.create();
            //ItemPickupPlus.LOG.info("with CTRL pressed!!!");
            buf.writeBoolean(InputUtil.isKeyPressed(client.getWindow().getHandle(), InputUtil.GLFW_KEY_LEFT_CONTROL));
            ClientPlayNetworking.send(CustomPacketInit.pktc2sDropOffID, buf);
            player.swingHand(Hand.OFF_HAND);
        }

        if (!ItemPickupPlus.CFG.modEnabled) {
            return;
        }

        if (ItemPickupPlus.CFG.displayHud) {
            ItemPickupPlusClient.pointedItem = isItemWithinReach(client);
            hudDisableAck = false;
        } else if (!hudDisableAck) {
            ItemPickupPlusClient.pointedItem = null;
            hudDisableAck = true;
        }
        //ItemPickupPlus.LOG.debug("current hit: " + ItemPickupPlusClient.pointedItem);

        if (ItemPickupPlus.CFG.onlyWhenSneaking && !player.isSneaking()) {
            return;
        }

        while (ItemPickupPlusClient.keyPickupMain.wasPressed()) {

            EntityHitResult hit = isItemWithinReach(client);
            if (hit == null) {
                continue;
            }

            sendPickupPacket(hit.getEntity().getId(), false);
        }

        while (ItemPickupPlusClient.keyPickupOff.wasPressed()) {

            EntityHitResult hit = isItemWithinReach(client);
            if (hit == null) {
                continue;
            }

            sendPickupPacket(hit.getEntity().getId(), true);
        }
    }

    private EntityHitResult isItemWithinReach(MinecraftClient client) {

        EntityHitResult hit;
        float range = ItemPickupPlus.CFG.pickupRange < 0.0f ? client.interactionManager.getReachDistance() : ItemPickupPlus.CFG.pickupRange; // if negative range is specified in config use minecraft default instead
        Entity camera = client.cameraEntity;
        Vec3d cameraPos = camera.getCameraPosVec(1.0f);
        Vec3d cameraRot = camera.getRotationVec(1.0f);
        Vec3d cameraDst = cameraPos.add(cameraRot.x * range, cameraRot.y * range, cameraRot.z * range);
        Box box = camera.getBoundingBox().stretch(cameraRot.multiply(range)).expand(1.0d, 1.0d, 1.0d);
        hit = ProjectileUtil.raycast(camera, cameraPos, cameraDst, box, (entityx) -> {
            return entityx instanceof ItemEntity;
        }, cameraDst.squaredDistanceTo(cameraPos));

        HitResult blockHit; // not necessarily blockhit, it's just a hit that is used to determine if there were blocks before the hit
        if (ItemPickupPlus.CFG.pickupRange < client.interactionManager.getReachDistance()) { // don't make an additional raycast if range is the same as vanilla crosshair
            blockHit = client.crosshairTarget;
        } else {
            blockHit = client.cameraEntity.raycast(range, 1.0f, false);
        }

        if (hit != null &&
            blockHit != null &&
            blockHit.getType() == HitResult.Type.BLOCK && // yes, there can't be Type.ENTITY from cameraEntity.raycast, but crosshairTarget can have it
            blockHit.squaredDistanceTo(camera) < hit.squaredDistanceTo(camera)
        ) {
            return null; // don't act like there is no block in the way when there is
        } // only clientside check, would be too costly to raycast serverside + opens up easy way to ddos with raycasts

        return hit; // returns null if no hit!
    }

    private void sendPickupPacket(int id, boolean isOffhand) {

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(isOffhand); // isOffhand
        buf.writeInt(id); // entityId
        ClientPlayNetworking.send(CustomPacketInit.pktc2sPickupID, buf);
        //ItemPickupPlus.LOG.debug("Sent packet! " + CustomPacketInit.pktc2sPickupID + " , " + hit.getEntity().getId());
    }

}
