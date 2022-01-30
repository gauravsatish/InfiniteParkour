package mfw.when.infiniteparkour.slotsystem;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;

public class SlotLog {
    private ArrayList<Block> blocks = new ArrayList<>();

    public void resetBlocks() {
        for (Block block : blocks) {
            block.setType(Material.AIR);
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
