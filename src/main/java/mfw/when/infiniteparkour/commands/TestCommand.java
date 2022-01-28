package mfw.when.infiniteparkour.commands;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.infparkour.ParkourManager;
import mfw.when.infiniteparkour.slotsystem.Slot;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                if (args.length == 1) {
                    if (args[0].equals("slotspacing")) {
                        for (int i = 1; i <= 3; i++) {
                            Slot slot1 = new Slot(i);
                            Location loc1 = player.getLocation();
                            loc1.setZ(slot1.getSlotStartZ());
                            Location loc2 = player.getLocation();
                            loc2.setZ(slot1.getSlotEndZ());

                            loc1.getBlock().setType(Material.GOLD_BLOCK);
                            loc2.getBlock().setType(Material.GOLD_BLOCK);
                        }
                    } else if (args[0].equals("parkour")) {
                        new ParkourManager(player);
                    } else {
                        player.sendMessage(ChatColor.RED + "Invalid Option!");
                        player.sendMessage(ChatColor.RED + "Available Options" + ChatColor.GOLD + ":");
                        for (String option : InfiniteParkour.testCommandOptions) {
                            player.sendMessage(ChatColor.GOLD + " - " + ChatColor.RED + option);
                        }
                    }
                } else if (args.length == 2) {
                    if (args[0].equals("tickspeed")) {
                        int tickSpeed;

                        try {
                            tickSpeed = Integer.parseInt(args[1]);

                            new BukkitRunnable() {
                                int iterationCount = 1;

                                @Override
                                public void run() {
                                    if (iterationCount > 24) {
                                        cancel();
                                    }

                                    player.sendMessage(ChatColor.GREEN + "Iteration run (" + iterationCount + ")");
                                    iterationCount++;
                                }
                            }.runTaskTimerAsynchronously(InfiniteParkour.getPlugin(), 10, tickSpeed);

                        } catch (Exception e) {
                            player.sendMessage(ChatColor.RED + "Please set a valid amount of tickSpeed to wait before running again");
                            return false;
                        }


                    }
                }
            }
        }

        return false;
    }
}
