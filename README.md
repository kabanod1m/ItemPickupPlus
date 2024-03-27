# ItemPickupPlus
ItemPickupPlus is a configurable Fabric Minecraft mod that makes you pick up items from the ground manually, instead of picking them up automatically in a radius.

### Installation
Requires Minecraft 1.20.1, [Fabric Loader](https://fabricmc.net/) >=0.14.25, [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) and [Cloth Config API](https://www.curseforge.com/minecraft/mc-mods/cloth-config).

### Configuration
The mod can be configured from within the file ".minecraft/config/itempickupplus.json5" or using modmenu.
* Tooltip with controls can be shown when you look at an item, can be moved
* (NOT IMPLEMENTED YET)Tooltip can be displayed on top of the item in the world instead of drawing it on the interface
* You can bind (or unbind) offhand pick up key, if unbinded, it's not going to show up on the tooltip
* You can bind a key to drop items from your offhand
* You can only allow items to be picked up while sneaking
* Pick up delay upon dropping an item can be disabled since you pick items up manually anyway
* You can only allow items to be picked up when the hand is empty or contains the same type of item that is not a full stack yet
* It is possible to configure maximum pick up range
* Items cannot be picked up through walls (but this check is only performed on the client)

### License
Minepickup is licensed under MIT.