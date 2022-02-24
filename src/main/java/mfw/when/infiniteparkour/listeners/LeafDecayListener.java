package mfw.when.infiniteparkour.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.metadata.MetadataValue;

public class LeafDecayListener implements Listener {

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent e) {

        for (MetadataValue value : e.getBlock().getMetadata("no_decay")) {
            if (value.asBoolean()) {
                e.setCancelled(true);
            }
        }
    }
}
