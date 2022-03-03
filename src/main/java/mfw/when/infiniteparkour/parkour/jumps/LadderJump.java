package mfw.when.infiniteparkour.parkour.jumps;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.utils.SyncBlockChanger;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Calculates and sets the blocks to its corresponding type to create a ladder jump.
 */
public class LadderJump {

    /**
     * Calculates and sets the blocks to create a ladder jump based on the given location.
     * @param loc The location from which the ladder jump is created.
     * @return An ArrayList containing the locations of all the blocks affected.
     */
    public static ArrayList<Location> jump(@NotNull Location loc) {
        ArrayList<Location> blockOutput = new ArrayList<>();

        Bukkit.getScheduler().runTask(InfiniteParkour.getPlugin(), () -> {
            Block ladder1Block = loc.clone().add(2, 1, 0).getBlock();
            ladder1Block.setType(Material.LADDER);
            Ladder ladder1 = (Ladder) ladder1Block.getBlockData();
            ladder1.setFacing(BlockFace.WEST);
            ladder1Block.setBlockData(ladder1);
            blockOutput.add(ladder1Block.getLocation());
            InfiniteParkour.getPlugin().getLogger().info("set ladder1 to output array");

            Block ladder2Block = loc.clone().add(3, 2, 1).getBlock();
            ladder2Block.setType(Material.LADDER);
            Ladder ladder2 = (Ladder) ladder2Block.getBlockData();
            ladder2.setFacing(BlockFace.SOUTH);
            ladder2Block.setBlockData(ladder2);
            blockOutput.add(ladder2Block.getLocation());
            InfiniteParkour.getPlugin().getLogger().info("set ladder2 to output array");

            Block ladder3block = loc.clone().add(4, 3, 0).getBlock();
            ladder3block.setType(Material.LADDER);
            Ladder ladder3 = (Ladder) ladder3block.getBlockData();
            ladder3.setFacing(BlockFace.EAST);
            ladder3block.setBlockData(ladder3);
            blockOutput.add(ladder3block.getLocation());
            InfiniteParkour.getPlugin().getLogger().info("set ladder3 to output array");
        });

        ArrayList<Block> mainBlocks = new ArrayList<>();
        mainBlocks.add(new SyncBlockChanger(loc.clone().add(3, 1, 0), Blocks.MOSS_BLOCK, false).run());
        mainBlocks.add(new SyncBlockChanger(loc.clone().add(3, 2, 0), Blocks.MOSS_BLOCK, false).run());
        mainBlocks.add(new SyncBlockChanger(loc.clone().add(3, 3, 0), Blocks.MOSS_BLOCK, false).run());
        mainBlocks.add(new SyncBlockChanger(loc.clone().add(3, 4, 0), Blocks.MOSS_BLOCK, false).run());

        for (Block block : mainBlocks) {
            blockOutput.add(block.getLocation());
            block.setMetadata("ladderblock", new FixedMetadataValue(InfiniteParkour.getPlugin(), true));
        }

        return blockOutput;
    }

    /**
     * Gets the location of the new block on which the next jump is based off of.
     * @param loc The location off of which the end block is calculated.
     * @return Location of the end block of the jump.
     */
    public static Location getEndBlock(@NotNull Location loc) {
        return loc.clone().add(3, 4, 0);
    }
}
