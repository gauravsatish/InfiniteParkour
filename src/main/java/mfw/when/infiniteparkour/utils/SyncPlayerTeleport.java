package mfw.when.infiniteparkour.utils;

import mfw.when.infiniteparkour.InfiniteParkour;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SyncPlayerTeleport {

    private final Player player;
    private final Location location;

    public SyncPlayerTeleport(Player player, Location location) {
        this.player = player;
        this.location = location;
    }

    public void run() {
        Bukkit.getScheduler().runTask(InfiniteParkour.getPlugin(), () -> player.teleport(location));
    }
}
