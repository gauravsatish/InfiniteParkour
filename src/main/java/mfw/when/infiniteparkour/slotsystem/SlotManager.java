package mfw.when.infiniteparkour.slotsystem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SlotManager {

    private static final ArrayList<Integer> slotsInUse = new ArrayList<>();

    private static final World world = Bukkit.getWorld("world");

    public static Slot getNewSlot() {
        for (int i = 1; ; i++) {
            if (!slotsInUse.contains(i)) {
                slotsInUse.add(i);
                return new Slot(i);
            }
        }
    }

    public static void setupSlot(Slot slot) {
        setupFloorRow(world.getBlockAt(0, 100, (int) slot.getSlotStartZ()));
        setupFloorRow(world.getBlockAt(-1, 100, (int) slot.getSlotStartZ()));

    }

    private static void setupFloorRow(Block startBlock) {
        startBlock.setType(Material.GOLD_BLOCK);
        startBlock.getRelative(0, 0, 1).setType(Material.POLISHED_DIORITE);

        for (int i = 1; i <= 14; i++) {
            startBlock.getRelative(0, 0, i).setType(Material.POLISHED_DIORITE);
        }

        startBlock.getRelative(0, 0, 7).setType(Material.EMERALD_BLOCK);
        startBlock.getRelative(0, 0, 8).setType(Material.EMERALD_BLOCK);

        startBlock.getRelative(0, 0, 15).setType(Material.GOLD_BLOCK);

    }

    public static void resetSlot(Slot slot) {

    }

    public static Slot getSlot(int slotNumber) {
        return new Slot(slotNumber);
    }

    public static ArrayList<Integer> getSlotsInUse() {
        return slotsInUse;
    }
}
