package mfw.when.infiniteparkour;

import mfw.when.infiniteparkour.commands.InfiniteParkourCommand;
import mfw.when.infiniteparkour.commands.ResetCommand;
import mfw.when.infiniteparkour.commands.TPParkourCommand;
import mfw.when.infiniteparkour.commands.TestCommand;
import mfw.when.infiniteparkour.infparkour.ParkourManager;
import mfw.when.infiniteparkour.listeners.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public final class InfiniteParkour extends JavaPlugin {

    public static final double PARKOUR_HEIGHT = 100;
    private static final HashMap<Player, ParkourManager> playerParkourManager = new HashMap<>();
    private static final HashMap<Player, BukkitTask> velocityTrackerProcesses = new HashMap<>();
    private static Plugin plugin;

    public static Plugin getPlugin() {
        return plugin;
    }

    public static HashMap<Player, ParkourManager> getPlayerParkourManager() {
        return playerParkourManager;
    }

    public static HashMap<Player, BukkitTask> getVelocityTrackerProcesses() {
        return velocityTrackerProcesses;
    }


    @Override
    public void onEnable() {

        plugin = JavaPlugin.getPlugin(InfiniteParkour.class);

        // Plugin startup logic

//        MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");

        Bukkit.getPluginManager().registerEvents(new PlayerManager(), this);

        Bukkit.getWorld("world").setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        Bukkit.getWorld("world").setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        Bukkit.getWorld("world").setTime(6000);

        getCommand("infiniteparkour").setExecutor(new InfiniteParkourCommand());
        getCommand("test").setExecutor(new TestCommand());
        getCommand("reset").setExecutor(new ResetCommand());
        getCommand("tpparkour").setExecutor(new TPParkourCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
