package mfw.when.infiniteparkour.slotsystem;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.utils.SyncBlockChanger;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;

public class SlotLog {
    private final ArrayList<Block> blocks = new ArrayList<>();

    public void resetBlocks() {
        for (Block block : blocks) {
//            new SyncBlockChanger(block, Blocks.AIR).run();
            new SyncBlockChanger(block.getLocation(), Blocks.a, Material.AIR).run();
        }
        blocks.clear();
        InfiniteParkour.getPlugin().getLogger().info("cleared blocks");
    }

    public void addBlock(Block block) {
        blocks.add(block);
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }
}
