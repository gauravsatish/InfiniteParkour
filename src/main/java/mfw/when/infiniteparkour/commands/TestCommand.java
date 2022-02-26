package mfw.when.infiniteparkour.commands;

import mfw.when.infiniteparkour.parkour.JumpGetterIDKWhatToCallThis;
import mfw.when.infiniteparkour.parkour.JumpType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                player.sendMessage(ChatColor.RED + "Nothing to see here");
            } else {
                if (args[0].equals("jumpchance")) {
                    JumpGetterIDKWhatToCallThis wsdf = new JumpGetterIDKWhatToCallThis();
                    wsdf.setCooldown(20);
                    for (int i = 0; i < Integer.parseInt(args[1]); i++) {
                        player.sendMessage(wsdf.getNextJumpType().name());
                        wsdf.decrementCooldown();
                    }
                } else if (args[0].equals("jtvalues")) {
                    for (int i = 1; i <= 10; i++) {
                        for (JumpType jumpType : JumpType.values()) {
                            player.sendMessage(jumpType.name());
                        }
                    }
                }
            }
        }

        return false;
    }
}
