package mfw.when.infiniteparkour.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.parkour.InfiniteParkourInstance;
import mfw.when.infiniteparkour.parkour.ParkourManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@CommandAlias("test")
@Description("Test command for operators")
public class TestCommand extends BaseCommand {

    @Default
    public void onTest(Player player, String[] args) {
        if (player.isOp()) {
            int repetitions = 0;
            try {
                repetitions = Integer.parseInt(args[0]);
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "Invalid number");
                return;
            }

            player.setGameMode(GameMode.SPECTATOR);

            InfiniteParkourInstance instance = new InfiniteParkourInstance(player);
            ParkourManager manager = instance.getParkourManager();
            int finalRepetitions = repetitions;
            new BukkitRunnable() {
                int counter = 0;

                @Override
                public void run() {
                    manager.trigger();

                    if (counter >= finalRepetitions) cancel();
                    counter++;
                }
            }.runTaskTimerAsynchronously(InfiniteParkour.getPlugin(), 20, 2);
        } else {
            player.sendMessage(ChatColor.RED + "Insufficient permissions.");
        }
    }
    
    @Subcommand("invsize")
    public void invSize(Player player, String[] args) {
        try {
            player.openInventory(Bukkit.createInventory(player, Integer.parseInt(args[0]), "Test Inventory"));
        } catch (Exception e) {
            player.sendMessage("Invalid inventory size");
        }
    }

}
