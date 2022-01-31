package mfw.when.infiniteparkour.infparkour;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.slotsystem.Slot;
import mfw.when.infiniteparkour.slotsystem.SlotManager;
import mfw.when.infiniteparkour.utils.SyncBlockChanger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class ParkourManager {

    private final Material TARGET_BLOCK_MATERIAL = Material.EMERALD_BLOCK;
    private final Material SECOND_BLOCK_MATERIAL = Material.WAXED_COPPER_BLOCK;

    private final Random random = new Random();
    private final Player player;
    private final Slot slot;
    private Block targetBlock;
    private Block secondBlock;
    private BukkitTask process;

    public ParkourManager(Player player, Slot slot) {
        this.player = player;
        this.slot = slot;

        SlotManager.resetPlayer(player);

        InfiniteParkour.getPlayerParkourManager().put(player, this);
        InfiniteParkour.getPlugin().getLogger().info("added to hashmap");
    }

    public Slot getSlot() {
        return slot;
    }

    public BukkitTask getProcess() {
        return process;
    }

    public void startParkourProcess() {

        targetBlock = getNextBlock(new Location(player.getWorld(), 0, InfiniteParkour.PARKOUR_HEIGHT, slot.getMiddleZ()[0]));
        targetBlock.setType(TARGET_BLOCK_MATERIAL);
        secondBlock = getNextBlock(targetBlock.getLocation());
        secondBlock.setType(SECOND_BLOCK_MATERIAL);

//        process = new BukkitRunnable() {
//            @Override
//            public void run() {
//                if (player.getLocation().add(0, -1, 0).getBlock().getType().equals(Material.EMERALD_BLOCK)) {
//                    new SyncBlockChanger(targetBlock, Material.GOLD_BLOCK).run();
//                    targetBlock = getNextBlock(targetBlock.getLocation());
//                    new SyncBlockChanger(targetBlock, Material.EMERALD_BLOCK).run();
//                }
//            }
//        }.runTaskTimerAsynchronously(InfiniteParkour.getPlugin(), 0, 5);

        process = new BukkitRunnable() {
            @Override
            public void run() {
                if (player.getLocation().add(0, -1, 0).getBlock().getType().equals(Material.EMERALD_BLOCK)) {
                    new SyncBlockChanger(targetBlock, Material.GOLD_BLOCK).run();
                    new SyncBlockChanger(secondBlock, Material.EMERALD_BLOCK).run();
                    targetBlock = secondBlock;
                    secondBlock = getNextBlock(secondBlock.getLocation());
                    new SyncBlockChanger(secondBlock, Material.WAXED_COPPER_BLOCK).run();

                }
            }
        }.runTaskTimerAsynchronously(InfiniteParkour.getPlugin(), 0, 1);
    }

    private Block getNextBlock(Location loc) {
        int height = generateHeight();
        int forwardLength = generateForwardLength();
        int offset = generateSidewaysOffset(height);

        Block block = loc.add(forwardLength, height, offset).getBlock();
        slot.getLog().addBlock(block);

        return block;
    }

    private int generateSidewaysOffset(int height) {
        int offset = random.nextInt(7) - 3;

//        if (height == 1 && (offset == 3 || offset == -3)) {
//            if (offset == 3) {
//                offset = 2;
//            } else {
//                offset = -2;
//            }
//        }
        return offset;
    }

    private int generateForwardLength() {
        int fdLength = 2 + random.nextInt(2);

        if (fdLength == 2) {
            int increaseChance = random.nextInt(100);
            if (increaseChance >= 30) {
                fdLength = 3;
            }
        }
        return fdLength;
    }

    private int generateHeight() {
        int height = random.nextInt(3) - 1;

        if (height == -1) {
            int decreaseChance = random.nextInt(100);
            if (decreaseChance >= 40) {
                height = 0;
            }
        }
        return height;
    }
}
