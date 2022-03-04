package mfw.when.infiniteparkour;

import co.aikar.commands.PaperCommandManager;
import mfw.when.infiniteparkour.listeners.LeafDecayListener;
import mfw.when.infiniteparkour.listeners.PlayerManager;
import mfw.when.infiniteparkour.commands.ParkourCommand;
import mfw.when.infiniteparkour.commands.ResetCommand;
import mfw.when.infiniteparkour.commands.TPParkourCommand;
import mfw.when.infiniteparkour.commands.TestCommand;
import mfw.when.infiniteparkour.parkour.JumpCounterSystem;
import mfw.when.infiniteparkour.parkour.ParkourManager;
import mfw.when.infiniteparkour.slotsystem.Slot;
import mfw.when.infiniteparkour.slotsystem.SlotManager;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class InfiniteParkour extends JavaPlugin {

    private PaperCommandManager commandManager;

    public static final double PARKOUR_HEIGHT = 100;
    private static final HashMap<Player, Integer> playerJumpCounter = new HashMap<>();
    private static final HashMap<Player, ParkourManager> playerParkourManager = new HashMap<>();
    private static Plugin plugin;

    public static Plugin getPlugin() {
        return plugin;
    }

    public static HashMap<Player, ParkourManager> getPlayerParkourManager() {
        return playerParkourManager;
    }

    public static HashMap<Player, Integer> getPlayerJumpCounter() {
        return playerJumpCounter;
    }


    @Override
    public void onEnable() {

        plugin = JavaPlugin.getPlugin(InfiniteParkour.class);

        this.commandManager = new PaperCommandManager(this);

        Bukkit.getPluginManager().registerEvents(new PlayerManager(), this);
        Bukkit.getPluginManager().registerEvents(new LeafDecayListener(), this);
        InfiniteParkour.getPlugin().getLogger().info("registered event");

        Bukkit.getWorld("world").setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        Bukkit.getWorld("world").setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        Bukkit.getWorld("world").setTime(6000);

        commandManager.registerCommand(new ParkourCommand());
        commandManager.registerCommand(new TestCommand());
        commandManager.registerCommand(new ResetCommand());
        commandManager.registerCommand(new TPParkourCommand());

        JumpCounterSystem.start();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Slot slot : SlotManager.getParkourMGRs().keySet()) {
            SlotManager.getParkourMGRs().get(slot).stop(false, true);
        }
    }

    public PaperCommandManager getCommandManager() {
        return commandManager;
    }
}
