package mfw.when.infiniteparkour.commands;

import mfw.when.infiniteparkour.infparkour.InfiniteParkourInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InfiniteParkourCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            new InfiniteParkourInstance(player);

        }

        return false;
    }
}
