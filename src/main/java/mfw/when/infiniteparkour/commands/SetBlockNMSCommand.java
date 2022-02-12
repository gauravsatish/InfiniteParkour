package mfw.when.infiniteparkour.commands;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.utils.SyncBlockChanger;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetBlockNMSCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.isOp()) {
                new SyncBlockChanger(player.getLocation().add(0, 2, 0), Blocks.LAVA, false).run();
                InfiniteParkour.getPlugin().getLogger().info("set block with nms");
            }
        }

        return false;
    }
}
