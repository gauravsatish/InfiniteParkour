package mfw.when.infiniteparkour.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import mfw.when.infiniteparkour.slotsystem.SlotManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandAlias("reset")
public class ResetCommand extends BaseCommand {

    @Subcommand("slots")
    public void resetSlots(Player player) {
        if (player.isOp()) {
            player.sendMessage(ChatColor.GREEN + "Slots reset!");
            for (Integer integer : SlotManager.getSlotHashMap().keySet()) {
                SlotManager.getSlotHashMap().get(integer).getLog().resetBlocks(false);
            }
            SlotManager.getSlotHashMap().clear();
        } else {
            player.sendMessage(ChatColor.RED + "Insufficient Permissions");
        }
    }
}
