package mfw.when.infiniteparkour.parkour;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.slotsystem.Slot;
import mfw.when.infiniteparkour.slotsystem.SlotManager;
import mfw.when.infiniteparkour.utils.SyncPlayerTeleport;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Creates a new "instance" of the parkour which does the following:
 * <ul>
 *     <li>Finds an empty slot and sets it up</li>
 *     <li>Sets the player gamemode to Adventure</li>
 *     <li>Creates a ParkourManager which handles all of the parkour stuff</li>
 *     <li>Sets the player's location to the spawn point of the parkour</li>
 *     <li>Starts the parkour</li>
 * </ul>
 */
public class InfiniteParkourInstance {
    private final Player player;
    private final Slot slot;
    private final ParkourManager parkourManager;

    public InfiniteParkourInstance(@NotNull Player player) {
        this.player = player;

        slot = SlotManager.getNewSlot();
        SlotManager.setupSlot(slot);

        if (!player.isOp()) this.player.setGameMode(GameMode.ADVENTURE);

        this.parkourManager = new ParkourManager(this.player, slot);
        this.slot.attachParkourMGR(parkourManager);
        new SyncPlayerTeleport(player, new Location(player.getWorld(), 0.5, InfiniteParkour.PARKOUR_HEIGHT + 1, slot.getMiddleZ()[1], -90f, 0f)).run();
        parkourManager.start();
    }

    /**
     * Gets the player associated with this instance
     * @return The player associated with this instance
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the slot associated with this instance
     * @return The slot associated with this instance
     */
    public Slot getSlot() {
        return slot;
    }

    /**
     * Gets the parkour manager associated with this instance
     * @return The parkour manager associated with this instance
     */
    public ParkourManager getParkourManager() {
        return parkourManager;
    }
}
