package mfw.when.infiniteparkour.parkour.jumps;

import mfw.when.infiniteparkour.utils.SyncBlockChanger;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Calculates and sets the blocks to its corresponding type to create a neo jump.
 */
public class NeoJump {

    /**
     * Calculates and sets the blocks to create a neo jump based on the given location.
     * @param loc The location from which the ladder jump is created.
     * @return An ArrayList containing the locations of all the blocks affected.
     */
    public static ArrayList<Location> jump(@NotNull Location loc) {
        ArrayList<Location> blockOutput = new ArrayList<>();
        blockOutput.add(new SyncBlockChanger(loc.clone().add(1, 0, 0), Blocks.MOSS_BLOCK, false).run().getLocation());
        blockOutput.add(new SyncBlockChanger(loc.clone().add(2, 0, 0), Blocks.MOSS_BLOCK, false).run().getLocation());
        blockOutput.add(new SyncBlockChanger(loc.clone().add(3, 0, 0), Blocks.MOSS_BLOCK, false).run().getLocation());
        blockOutput.add(new SyncBlockChanger(loc.clone().add(4, 0, 0), Blocks.MOSS_BLOCK, false).run().getLocation());
        blockOutput.add(new SyncBlockChanger(loc.clone().add(5, 0, 0), Blocks.MOSS_BLOCK, false).run().getLocation());
        blockOutput.add(new SyncBlockChanger(loc.clone().add(6, 0, 0), Blocks.MOSS_BLOCK, false).run().getLocation());
        blockOutput.add(new SyncBlockChanger(loc.clone().add(7, 0, 0), Blocks.MOSS_BLOCK, false).run().getLocation());

        blockOutput.add(new SyncBlockChanger(loc.clone().add(4, 1, 0), Blocks.MOSS_BLOCK, false).run().getLocation());
        blockOutput.add(new SyncBlockChanger(loc.clone().add(4, 2, 0), Blocks.MOSS_BLOCK, false).run().getLocation());
        blockOutput.add(new SyncBlockChanger(loc.clone().add(4, 3, 0), Blocks.MOSS_BLOCK, false).run().getLocation());

        return blockOutput;
    }

    /**
     * Gets the location of the new block on which the next jump is based off of.
     * @param loc The location off of which the end block is calculated.
     * @return Location of the end block of the jump.
     */
    public static Location getEndBlock(@NotNull Location loc) {
        return loc.clone().add(7, 0, 0);
    }
}
