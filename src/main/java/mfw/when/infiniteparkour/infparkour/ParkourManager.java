package mfw.when.infiniteparkour.infparkour;

import mfw.when.infiniteparkour.InfiniteParkour;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class ParkourManager {

    private final Random random = new Random();

    public ParkourManager(Player player) {
        Location loc = player.getLocation().add(0, -1, 0);
//        for (int i = 1; i <= 20; i++) {
//            Block nextBlock = getNextBlock(loc);
//            nextBlock.setType(Material.GOLD_BLOCK);
//            loc = nextBlock.getLocation();
//        }

        final Location[] finalLoc = {loc};
        new BukkitRunnable() {
            int counter = 0;
            Block nextBlock = loc.getBlock();

            @Override
            public void run() {
                Bukkit.getScheduler().runTask(InfiniteParkour.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        nextBlock.setType(Material.EMERALD_BLOCK);
                        nextBlock = getNextBlock(finalLoc[0]);
                        nextBlock.setType(Material.GOLD_BLOCK);
                        finalLoc[0] = nextBlock.getLocation();
                        counter++;
                        if (counter > 20) {
                            cancel();
                        }
                    }
                });
            }
        }.runTaskTimerAsynchronously(InfiniteParkour.getPlugin(), 10, 20);
    }

    private Block getNextBlock(Location loc) {
        int height = generateHeight();
        int forwardLength = generateForwardLength();
        int offset = generateSidewaysOffset(height);

        return loc.add(forwardLength, height, offset).getBlock();
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
