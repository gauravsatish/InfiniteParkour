package mfw.when.infiniteparkour.commands;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.slotsystem.SlotManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ResetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.isOp()) {
                if (args.length == 1) {
                    if (args[0].equals("slots")) {
                        player.sendMessage(ChatColor.GREEN + "Slots reset!");
                        SlotManager.getSlotsInUse().clear();
//                    } else if (args[0].equals("parkour")){
//                        SlotManager.getPlayerSlotHashMap().get(player).resetBlocks();
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Invalid Option");
                }
            } else {
                InfiniteParkour.getPlugin().getLogger().info("Slots reset");
            }
        }

        return false;
    }
}
