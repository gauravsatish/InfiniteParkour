package mfw.when.infiniteparkour.utils;

import mfw.when.infiniteparkour.InfiniteParkour;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public record SyncPlayerTeleport(Player player, Location location) {

    public void run() {
        Bukkit.getScheduler().runTask(InfiniteParkour.getPlugin(), () -> player.teleport(location));
    }
}
