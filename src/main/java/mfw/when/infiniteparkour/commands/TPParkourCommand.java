package mfw.when.infiniteparkour.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import mfw.when.infiniteparkour.InfiniteParkour;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("tpparkour")
@Description("Teleport to the parkour spawn")
public class TPParkourCommand extends BaseCommand {
    @Default
    public void onDefault(Player player) {
        if (player.isOp()) {
            player.teleport(new Location(player.getWorld(), 0, InfiniteParkour.PARKOUR_HEIGHT + 1, 40 + 0.5, -90f, 0f));
        }
    }
}
