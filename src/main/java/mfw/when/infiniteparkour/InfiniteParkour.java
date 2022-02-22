package mfw.when.infiniteparkour;

import mfw.when.infiniteparkour.commands.*;
import mfw.when.infiniteparkour.parkour.JumpCounterSystem;
import mfw.when.infiniteparkour.parkour.ParkourManager;
import mfw.when.infiniteparkour.listeners.PlayerManager;
import mfw.when.infiniteparkour.slotsystem.Slot;
import mfw.when.infiniteparkour.slotsystem.SlotManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public final class InfiniteParkour extends JavaPlugin {

    public static final double PARKOUR_HEIGHT = 100;
    private static final HashMap<Player, Integer> playerJumpCounter = new HashMap<>();
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

    public static HashMap<Player, Integer> getPlayerJumpCounter() {
        return playerJumpCounter;
    }


    @Override
    public void onEnable() {

        plugin = JavaPlugin.getPlugin(InfiniteParkour.class);

        Bukkit.getPluginManager().registerEvents(new PlayerManager(), this);

        Bukkit.getWorld("world").setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        Bukkit.getWorld("world").setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        Bukkit.getWorld("world").setTime(6000);

        getCommand("infiniteparkour").setExecutor(new InfiniteParkourCommand());
        getCommand("test").setExecutor(new TestCommand());
        getCommand("reset").setExecutor(new ResetCommand());
        getCommand("tpparkour").setExecutor(new TPParkourCommand());
        getCommand("setblocknms").setExecutor(new SetBlockNMSCommand());
        getCommand("leaveparkour").setExecutor(new LeaveParkourCommand());

        JumpCounterSystem.start();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Slot slot : SlotManager.getParkourMGRs().keySet()) {
            SlotManager.getParkourMGRs().get(slot).stopParkourProcess(true);
        }
    }
}
