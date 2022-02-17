package mfw.when.infiniteparkour.slotsystem;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.utils.SyncBlockChanger;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class SlotLog {
    private final ArrayList<Block> blocks = new ArrayList<>();

    public void resetBlocks(boolean onDisable) {
        if (!onDisable) {
            final ArrayList<Block> localBlocks = new ArrayList<>();
            localBlocks.addAll(blocks);

            new BukkitRunnable() {
                int counter = 0;

                @Override
                public void run() {
                    if (counter < localBlocks.size()) {
                        new SyncBlockChanger(localBlocks.get(counter).getLocation(), Blocks.AIR, true).run();
                        counter++;
                    } else {
                        cancel();
                    }
                }
            }.runTaskTimerAsynchronously(InfiniteParkour.getPlugin(), 0, 1);
        } else {
            for (Block block : blocks) {
                block.setType(Material.AIR);
            }
        }

        blocks.clear();
    }

    public void addBlock(Block block) {
        blocks.add(block);
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }
}
