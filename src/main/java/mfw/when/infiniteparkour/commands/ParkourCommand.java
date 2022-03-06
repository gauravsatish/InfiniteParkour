package mfw.when.infiniteparkour.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.parkour.InfiniteParkourInstance;
import mfw.when.infiniteparkour.slotsystem.SlotManager;
import mfw.when.infiniteparkour.utils.SyncPlayerTeleport;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("parkour")
public class ParkourCommand extends BaseCommand {

    @Subcommand("start")
    @Default
    @Description("Starts the parkour")
    public void start(Player player) {
        if (player.isOp()) {
            new InfiniteParkourInstance(player);
        } else {
            player.sendMessage("Insufficient permissions");
        }
    }

    @Subcommand("stop")
    @Description("Stops the parkour")
    public void stop(Player player) {
        if (player.isOp()) {
            new SyncPlayerTeleport(player, new Location(player.getWorld(), 0.5, InfiniteParkour.PARKOUR_HEIGHT + 1, InfiniteParkour.getSessions().get(player).getSlot().getMiddleZ()[1], -90f, 0f)).run();
            SlotManager.resetPlayer(player);
        }
    }
}
