package mfw.when.infiniteparkour.settings;

import mfw.when.infiniteparkour.models.PSettingsEntry;
import mfw.when.infiniteparkour.utils.PSettingsStorageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class PlayerSettings extends SettingsMenu {
    private final HashMap<Integer, ItemStack> options = new HashMap<>();
    private final Inventory inv;
    private Player owner;
    private boolean ladderJumpsEnabled;
    private boolean neoJumpsEnabled;

    public PlayerSettings(@NotNull Player player) {
        this.owner = player;
        this.inv = Bukkit.createInventory(null, INV_SIZE, TITLE);
        this.loadSettings();
        for (int i = 0; i < INV_SIZE; i++) {
            this.inv.setItem(i, options.getOrDefault(i, FILLER_GLASS));
        }
    }

    private static String getStateString(boolean state) {
        return state ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled";
    }

    public void loadSettings() {
        PSettingsEntry entry = PSettingsStorageUtil.findEntry(owner.getUniqueId().toString());
        this.ladderJumpsEnabled = entry.isLadderJumpsEnabled();
        this.neoJumpsEnabled = entry.isNeoJumpsEnabled();

        updateSettings();
    }

    public void openMenu() {
        owner.openInventory(inv);
    }

    public void updateSettings() {
        updateOptions();
        PSettingsStorageUtil.updateEntry(owner.getUniqueId().toString(), ladderJumpsEnabled, neoJumpsEnabled);
        inv.setItem(11, options.getOrDefault(11, new ItemStack(Material.AIR)));
        inv.setItem(15, options.getOrDefault(15, new ItemStack(Material.AIR)));
    }

    private void updateOptions() {
        options.clear();
        options.put(11, makeItem(Material.LADDER, ChatColor.GOLD + "Ladder Jumps",
                ChatColor.GRAY + "Enable or disable ladder jumps",
                " ",
                ChatColor.GOLD + "" + ChatColor.BOLD + "State: " + ChatColor.RESET + "" + getStateString(ladderJumpsEnabled)));
        options.put(15, makeItem(Material.COBBLESTONE_WALL, ChatColor.GOLD + "Neo Jumps",
                ChatColor.GRAY + "Enable or disable neo jumps",
                " ",
                ChatColor.GOLD + "" + ChatColor.BOLD + "State: " + ChatColor.RESET + "" + getStateString(neoJumpsEnabled)));
    }

    public HashMap<Integer, ItemStack> getOptions() {
        return options;
    }

    public InventoryHolder getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public boolean isLadderJumpsEnabled() {
        return ladderJumpsEnabled;
    }

    public void setLadderJumpsEnabled(boolean ladderJumpsEnabled) {
        this.ladderJumpsEnabled = ladderJumpsEnabled;
    }

    public boolean isNeoJumpsEnabled() {
        return neoJumpsEnabled;
    }

    public void setNeoJumpsEnabled(boolean neoJumpsEnabled) {
        this.neoJumpsEnabled = neoJumpsEnabled;
    }
}
