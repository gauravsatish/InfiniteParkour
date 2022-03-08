package mfw.when.infiniteparkour.parkour;

import mfw.when.infiniteparkour.InfiniteParkour;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class JumpCounterSystem {

    private static final ArrayList<Player> players = new ArrayList<>();
    private static BukkitTask process;

    public static void start() {
        process = Bukkit.getScheduler().runTaskTimerAsynchronously(InfiniteParkour.getPlugin(), () -> {
            for (Player player : players) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, getMessage(player));
            }
        }, 0, 10);
    }

    public static void addPlayer(Player player) {
        players.add(player);
    }

    public static void removePlayer(Player player) {
        players.remove(player);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
    }

    public static void update(Player player) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, getMessage(player));
    }

    public static void stop() {
        process.cancel();
    }

    private static TextComponent getMessage(Player player) {
        return new TextComponent(ChatColor.DARK_AQUA + "Score: " + ChatColor.BLACK +
                InfiniteParkour.getPlayerScores().get(player) + " jumps");
    }
}
