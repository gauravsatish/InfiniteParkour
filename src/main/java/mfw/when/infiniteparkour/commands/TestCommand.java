package mfw.when.infiniteparkour.commands;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.parkour.InfiniteParkourInstance;
import mfw.when.infiniteparkour.parkour_rewrite.ParkourManager_REWRITE;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                player.sendMessage(ChatColor.RED + "Nothing to see here");
            } else {
                int repetitions = 0;
                try {
                    repetitions = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid number");
                    return false;
                }

                player.setGameMode(GameMode.SPECTATOR);

                InfiniteParkourInstance instance = new InfiniteParkourInstance(player);
                ParkourManager_REWRITE manager = instance.getParkourManager();
                int finalRepetitions = repetitions;
                new BukkitRunnable() {
                    int counter = 0;

                    @Override
                    public void run() {
                        manager.trigger();

                        if (counter >= finalRepetitions) {
                            cancel();
                        }

                        counter++;
                    }
                }.runTaskTimerAsynchronously(InfiniteParkour.getPlugin(), 20, 2);

            }
        }

        return true;
    }
}
