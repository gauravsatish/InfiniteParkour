package mfw.when.infiniteparkour.listeners;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.parkour.ParkourManager;
import mfw.when.infiniteparkour.settings.SettingsItem;
import mfw.when.infiniteparkour.settings.SettingsMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class SettingsListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();
            if (e.getView().getTitle().equals(SettingsMenu.TITLE)) {
                e.setCancelled(true);
                if (e.getCurrentItem() == null) return;
                if (e.getCurrentItem().getType().equals(SettingsMenu.ladderJumpRepresent)) {
                    ParkourManager manager = InfiniteParkour.getSessions().get(player);
                    manager.getSettings().setLadderJumps(!manager.getSettings().isLadderJumps());
                    manager.getSettings().updateSettings();
                } else if (e.getCurrentItem().getType().equals(SettingsMenu.neoJumpRepresent)) {
                    ParkourManager manager = InfiniteParkour.getSessions().get(player);
                    manager.getSettings().setNeoJumps(!manager.getSettings().isNeoJumps());
                    manager.getSettings().updateSettings();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (Objects.requireNonNull(e.getPlayer().getInventory().getItemInMainHand().getItemMeta()).getDisplayName().equals(SettingsItem.DISPLAY_NAME)) {
                InfiniteParkour.getSessions().get(e.getPlayer()).getSettings().openMenu();
                e.setCancelled(true);
            }
        }

    }
}
