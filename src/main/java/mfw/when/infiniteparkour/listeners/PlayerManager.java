package mfw.when.infiniteparkour.listeners;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.slotsystem.SlotManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerManager implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().sendTitle(ChatColor.GOLD + "/infiniteparkour", ChatColor.ITALIC + "do it", 5, 40, 10);
        e.getPlayer().setFoodLevel(20);
        e.getPlayer().setHealth(20);
        Location spawnLocation = e.getPlayer().getWorld().getSpawnLocation();
        spawnLocation.setYaw(-180f);
        spawnLocation.setPitch(0f);

        InfiniteParkour.getPlayerJumpCounter().put(e.getPlayer(), 0);
    }

    @EventHandler
    public void onPlayerHungerDeplete(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        SlotManager.resetPlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerTakeDamage(EntityDamageEvent e) {
        if (e.getEntityType().equals(EntityType.PLAYER)) {
            if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                e.setCancelled(true);
            }
        }
    }
}
