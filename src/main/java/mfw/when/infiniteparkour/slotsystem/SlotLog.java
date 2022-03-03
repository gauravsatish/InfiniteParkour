package mfw.when.infiniteparkour.slotsystem;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.utils.SyncBlockChanger;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SlotLog {
    private final ArrayList<Block> blocks = new ArrayList<>();

    public void resetBlocks(@NotNull boolean onDisable) {
        if (!onDisable) {
            final ArrayList<Block> localBlocks = new ArrayList<>();
            localBlocks.addAll(blocks);

            new BukkitRunnable() {
                int counter = 0;

                @Override
                public void run() {
                    if (counter < localBlocks.size()) {
                        Block block = localBlocks.get(counter);
                        new SyncBlockChanger(block.getLocation(), Blocks.AIR, true).run().removeMetadata("counter", InfiniteParkour.getPlugin());

                        for (MetadataValue value : block.getMetadata("ladderblock")) {
                            if (value.asBoolean()) {
                                new SyncBlockChanger(block.getLocation().add(0, 0, 1), Blocks.AIR, true).run().removeMetadata("ladderblock", InfiniteParkour.getPlugin());
                                new SyncBlockChanger(block.getLocation().add(0, 0, -1), Blocks.AIR, true).run().removeMetadata("ladderblock", InfiniteParkour.getPlugin());
                                new SyncBlockChanger(block.getLocation().add(1, 0, 0), Blocks.AIR, true).run().removeMetadata("ladderblock", InfiniteParkour.getPlugin());
                                new SyncBlockChanger(block.getLocation().add(-1, 0, 0), Blocks.AIR, true).run().removeMetadata("ladderblock", InfiniteParkour.getPlugin());
                            }
                        }
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

    public void addBlock(@NotNull Block block) {
        blocks.add(block);
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }
}
