package mfw.when.infiniteparkour.parkour.jumps;

import mfw.when.infiniteparkour.utils.SyncBlockChanger;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NeoJump {

    public static ArrayList<Location> jump(Location loc) {
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

    public static Location getEndBlock(@NotNull Location loc) {
        return loc.clone().add(7, 0, 0);
    }
}
