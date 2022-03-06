package mfw.when.infiniteparkour.settings;

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
    private Inventory inv;
    private Player owner;
    private boolean ladderJumps;
    private boolean neoJumps;

    public PlayerSettings(@NotNull Player player) {
        this.owner = player;
        this.ladderJumps = false;
        this.neoJumps = false;

        updateOptions();
        this.inv = Bukkit.createInventory(null, INV_SIZE, TITLE);
        for (int i = 0; i < INV_SIZE; i++) {
            this.inv.setItem(i, options.getOrDefault(i, FILLER_GLASS));
        }
        updateSettings();
    }

    private static String getStateString(boolean state) {
        return state ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled";
    }

    public void openMenu() {
        owner.openInventory(inv);
    }

    public void updateSettings() {
        updateOptions();
        inv.setItem(11, options.getOrDefault(11, new ItemStack(Material.AIR)));
        inv.setItem(15, options.getOrDefault(15, new ItemStack(Material.AIR)));
    }

    private void updateOptions() {
        options.clear();
        options.put(11, makeItem(Material.LADDER, ChatColor.GOLD + "Ladder Jumps",
                ChatColor.GRAY + "Enable or disable ladder jumps",
                " ",
                ChatColor.GOLD + "" + ChatColor.BOLD + "State: " + ChatColor.RESET + "" + getStateString(ladderJumps)));
        options.put(15, makeItem(Material.COBBLESTONE_WALL, ChatColor.GOLD + "Neo Jumps",
                ChatColor.GRAY + "Enable or disable neo jumps",
                " ",
                ChatColor.GOLD + "" + ChatColor.BOLD + "State: " + ChatColor.RESET + "" + getStateString(neoJumps)));
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

    public boolean isLadderJumps() {
        return ladderJumps;
    }

    public void setLadderJumps(boolean ladderJumps) {
        this.ladderJumps = ladderJumps;
    }

    public boolean isNeoJumps() {
        return neoJumps;
    }

    public void setNeoJumps(boolean neoJumps) {
        this.neoJumps = neoJumps;
    }
}
