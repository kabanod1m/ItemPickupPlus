package org.winglessbirds.itempickupplus.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import org.winglessbirds.itempickupplus.ItemPickupPlus;

@Config(name = ItemPickupPlus.MODID)
@Config.Gui.Background("minecraft:textures/block/dirt.png")
public class ModConfig implements ConfigData {

    @Comment("Enables or disables the mod completely\nDefault picking up key is R, bind in controls")
    public boolean modEnabled = true;

    @Comment("Maximum pick up range for manual picking up; specify negative value to use default block reach")
    public float pickupRange = -1.0f;

    @Comment("Only allows picking up while holding down the sneak key")
    public boolean onlyWhenSneaking = false;

    @Comment("When mod is enabled, allow picking up items back immediately")
    public boolean noDropDelay = false;

    @Comment("Only allow picking up if the hand is empty or has a non-full stack of the same item type")
    public boolean onlyEmptyHand = false;

    @Comment("Display the pointed at item and keybindings under the crosshair")
    public boolean displayHud = true;

    @Comment("Move tooltip horizontally, doesn't affect the tooltip over items")
    public int hudOffsetX = 0;

    @Comment("Move tooltip vertically, doesn't affect the tooltip over items")
    public int hudOffsetY = 0;

    @Comment("When \"Display tooltip\" is enabled, display the tooltip over the physical item instead")
    public boolean displayHudInWorld = false;
}
