package mfw.when.infiniteparkour.infparkour;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.slotsystem.Slot;
import mfw.when.infiniteparkour.slotsystem.SlotManager;
import mfw.when.infiniteparkour.utils.SyncPlayerTeleport;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class InfiniteParkourInstance {
    private final Player player;
    private final Slot slot;
    private final ParkourManager parkourManager;

    public InfiniteParkourInstance(Player player) {
        this.player = player;

        slot = SlotManager.getNewSlot();
        SlotManager.setupSlot(slot);

        this.player.setGameMode(GameMode.ADVENTURE);

        this.parkourManager = new ParkourManager(this.player, slot);
        this.slot.attachParkourMGR(parkourManager);
        new SyncPlayerTeleport(player, new Location(player.getWorld(), 0.5, InfiniteParkour.PARKOUR_HEIGHT + 1, slot.getMiddleZ()[1], -90f, 0f)).run();
        parkourManager.startParkourProcess();
    }

}
