package mfw.when.infiniteparkour.infparkour;

import mfw.when.infiniteparkour.slotsystem.Slot;
import mfw.when.infiniteparkour.slotsystem.SlotManager;
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

        this.parkourManager = new ParkourManager(player, slot);
        parkourManager.startParkourProcess();

        this.player.teleport(new Location(this.player.getWorld(), 0.5, 101, slot.getMiddleZ()[1], -90f, 0f));
    }

}
