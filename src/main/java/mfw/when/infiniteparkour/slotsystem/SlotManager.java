package mfw.when.infiniteparkour.slotsystem;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.infparkour.ParkourManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class SlotManager {

    private static final ArrayList<Integer> slotsInUse = new ArrayList<>();
    private static final HashMap<Slot, ParkourManager> parkourMGRs = new HashMap<>();

    private static final World world = Bukkit.getWorld("world");

    public static Slot getNewSlot() {
        for (int i = 1; ; i++) {
            if (!slotsInUse.contains(i)) {
                Slot slot = new Slot(i);
                slotsInUse.add(i);
                return slot;
            }
        }
    }

    public static void resetPlayer(Player player) {
        if (InfiniteParkour.getPlayerParkourManager().containsKey(player)) {

            ParkourManager pm = InfiniteParkour.getPlayerParkourManager().get(player);

            InfiniteParkour.getPlugin().getLogger().info("Cleared Blocks");
            pm.getSlot().getLog().resetBlocks(false);
            pm.getProcess().cancel();
            InfiniteParkour.getPlugin().getLogger().info("cancelled player process");
            SlotManager.getSlotsInUse().remove(Integer.valueOf(pm.getSlot().getSlotNumber()));

            InfiniteParkour.getPlayerParkourManager().remove(player);
        } else {
            InfiniteParkour.getPlugin().getLogger().info("Passed already existing in hashmap check");
        }
    }
    public static void setupSlot(Slot slot) {
        setupFloorRow(world.getBlockAt(0, (int) InfiniteParkour.PARKOUR_HEIGHT, (int) slot.getSlotStartZ()));
        setupFloorRow(world.getBlockAt(-1, (int) InfiniteParkour.PARKOUR_HEIGHT, (int) slot.getSlotStartZ()));
    }

    private static void setupFloorRow(Block startBlock) {
        startBlock.setType(Material.GOLD_BLOCK);
        startBlock.getRelative(0, 0, 1).setType(Material.POLISHED_DIORITE);

        for (int i = 1; i <= 14; i++) {
            startBlock.getRelative(0, 0, i).setType(Material.POLISHED_DIORITE);
        }

        startBlock.getRelative(0, 0, 7).setType(Material.DIAMOND_BLOCK);
        startBlock.getRelative(0, 0, 8).setType(Material.DIAMOND_BLOCK);

        startBlock.getRelative(0, 0, 15).setType(Material.GOLD_BLOCK);

    }

    public static Slot getSlot(int slotNumber) {
        return new Slot(slotNumber);
    }

    public static ArrayList<Integer> getSlotsInUse() {
        return slotsInUse;
    }

    public static HashMap<Slot, ParkourManager> getParkourMGRs() {
        return parkourMGRs;
    }
}
