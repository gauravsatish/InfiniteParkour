package mfw.when.infiniteparkour.slotsystem;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.parkour.JumpCounterSystem;
import mfw.when.infiniteparkour.parkour_rewrite.ParkourManager_REWRITE;
import mfw.when.infiniteparkour.utils.SyncBlockChanger;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SlotManager {

    private static final HashMap<Integer, Slot> slotHashMap = new HashMap<>();
    private static final HashMap<Slot, ParkourManager_REWRITE> parkourMGRs = new HashMap<>();

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

            ParkourManager_REWRITE pm = InfiniteParkour.getPlayerParkourManager().get(player);

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

        new SyncBlockChanger(startBlock.getLocation(), Blocks.GOLD_BLOCK, false).run();

        for (int i = 1; i <= 14; i++) {
            new SyncBlockChanger(startBlock.getRelative(0, 0, i).getLocation(), Blocks.POLISHED_DIORITE, false).run();
        }

        new SyncBlockChanger(startBlock.getLocation().add(0, 0, 7), Blocks.DIAMOND_BLOCK, false).run();
        new SyncBlockChanger(startBlock.getLocation().add(0, 0, 8), Blocks.DIAMOND_BLOCK, false).run();
        new SyncBlockChanger(startBlock.getLocation().add(0, 0, 15), Blocks.GOLD_BLOCK, false).run();

    }

    public static Slot getSlot(int slotNumber) {
        return slotHashMap.getOrDefault(slotNumber, null);
    }

    public static HashMap<Slot, ParkourManager_REWRITE> getParkourMGRs() {
        return parkourMGRs;
    }

    public static HashMap<Integer, Slot> getSlotHashMap() {
        return slotHashMap;
    }
}
