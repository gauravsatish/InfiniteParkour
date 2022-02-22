package mfw.when.infiniteparkour.commands;

import mfw.when.infiniteparkour.InfiniteParkour;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPParkourCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                player.teleport(new Location(player.getWorld(), 0, InfiniteParkour.PARKOUR_HEIGHT + 1, 40 + 0.5, -90f, 0f));
            }
        }

        return false;
    }
}
