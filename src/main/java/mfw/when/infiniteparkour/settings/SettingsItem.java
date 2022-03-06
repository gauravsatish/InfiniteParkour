package mfw.when.infiniteparkour.settings;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SettingsItem {
    public static final Material MATERIAL = Material.REDSTONE_BLOCK;
    public static final String DISPLAY_NAME = ChatColor.RED + "Parkour Settings";
    public static final String[] LORE = new String[]{" ", ChatColor.GRAY + "Right-click to open the parkour settings menu"};
    public static final ItemStack item = SettingsMenu.makeItem(MATERIAL, DISPLAY_NAME, LORE);

    public static ItemStack getItem() {
        return item;
    }
}
