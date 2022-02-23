package mfw.when.infiniteparkour.slotsystem;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.parkour.JumpCounterSystem;
import mfw.when.infiniteparkour.parkour.ParkourManager;
import mfw.when.infiniteparkour.utils.SyncPlayerTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SlotManager {

    private static final HashMap<Integer, Slot> slotHashMap = new HashMap<>();
    private static final HashMap<Slot, ParkourManager> parkourMGRs = new HashMap<>();

    private static final World world = Bukkit.getWorld("world");

    public static Slot getNewSlot() {
        for (int i = 1; ; i++) {
            if (!slotHashMap.containsKey(i)) {
                Slot slot = new Slot(i);
                slotHashMap.put(i, slot);
                return slot;
            }
        }
    }

    public static void resetPlayer(Player player) {

        if (InfiniteParkour.getPlayerParkourManager().containsKey(player)) {

            ParkourManager pm = InfiniteParkour.getPlayerParkourManager().get(player);

            pm.getSlot().getLog().resetBlocks(false);
            pm.getProcess().cancel();
            SlotManager.getSlotHashMap().remove(pm.getSlot().getSlotNumber());

            InfiniteParkour.getPlayerParkourManager().remove(player);
            JumpCounterSystem.removePlayer(player);
        }
    }

    public static void setupSlot(Slot slot) {
        setupFloorRow(world.getBlockAt(0, (int) InfiniteParkour.PARKOUR_HEIGHT, (int) slot.getMinZ()));
        setupFloorRow(world.getBlockAt(-1, (int) InfiniteParkour.PARKOUR_HEIGHT, (int) slot.getMinZ()));
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
        return slotHashMap.getOrDefault(slotNumber, null);
    }

    public static HashMap<Slot, ParkourManager> getParkourMGRs() {
        return parkourMGRs;
    }

    public static HashMap<Integer, Slot> getSlotHashMap() {
        return slotHashMap;
    }
}
