package mfw.when.infiniteparkour.settings;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SettingsMenu {
    public static final String TITLE = ChatColor.DARK_GRAY + "Parkour Settings";
    public static final int INV_SIZE = 27;
    public static final ItemStack FILLER_GLASS = makeItem(Material.GRAY_STAINED_GLASS_PANE, " ");
    public static final Material ladderJumpRepresent = Material.LADDER;
    public static final Material neoJumpRepresent = Material.COBBLESTONE_WALL;

    public static ItemStack makeItem(Material material, String displayName, String... lore) {

        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(displayName);

        itemMeta.setLore(Arrays.asList(lore));
        item.setItemMeta(itemMeta);

        return item;
    }
}
