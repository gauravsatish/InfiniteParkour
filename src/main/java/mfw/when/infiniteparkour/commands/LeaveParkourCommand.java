package mfw.when.infiniteparkour.commands;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.slotsystem.SlotManager;
import mfw.when.infiniteparkour.utils.SyncPlayerTeleport;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveParkourCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.isOp()) {
                new SyncPlayerTeleport(player, new Location(player.getWorld(), 0.5, InfiniteParkour.PARKOUR_HEIGHT + 1, InfiniteParkour.getPlayerParkourManager().get(player).getSlot().getMiddleZ()[1], -90f, 0f)).run();
                SlotManager.resetPlayer(player);
            }
        }
        return false;
    }
}
