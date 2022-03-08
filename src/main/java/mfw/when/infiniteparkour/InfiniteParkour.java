package mfw.when.infiniteparkour;

import co.aikar.commands.PaperCommandManager;
import mfw.when.infiniteparkour.commands.ParkourCommand;
import mfw.when.infiniteparkour.commands.ResetCommand;
import mfw.when.infiniteparkour.commands.TPParkourCommand;
import mfw.when.infiniteparkour.commands.TestCommand;
import mfw.when.infiniteparkour.listeners.LeafDecayListener;
import mfw.when.infiniteparkour.listeners.PlayerManager;
import mfw.when.infiniteparkour.listeners.SettingsListener;
import mfw.when.infiniteparkour.parkour.JumpCounterSystem;
import mfw.when.infiniteparkour.parkour.ParkourManager;
import mfw.when.infiniteparkour.slotsystem.Slot;
import mfw.when.infiniteparkour.slotsystem.SlotManager;
import mfw.when.infiniteparkour.utils.PSettingsStorageUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public final class InfiniteParkour extends JavaPlugin {

    public static final double PARKOUR_HEIGHT = 100;
    public static final String PLAYER_SETTINGS_FILE_NAME = "player_settings.json";
    private static final HashMap<Player, Integer> playerScores = new HashMap<>();
    private static final HashMap<Player, ParkourManager> sessions = new HashMap<>();
    private static Plugin plugin;
    private PaperCommandManager commandManager;

    public static Plugin getPlugin() {
        return plugin;
    }

    public static HashMap<Player, ParkourManager> getSessions() {
        return sessions;
    }

    public static HashMap<Player, Integer> getPlayerScores() {
        return playerScores;
    }


    @Override
    public void onEnable() {

        plugin = JavaPlugin.getPlugin(InfiniteParkour.class);

        PSettingsStorageUtil.loadEntries();
        PSettingsStorageUtil.startSaveTask();

        File file = new File(InfiniteParkour.getPlugin().getDataFolder(), PLAYER_SETTINGS_FILE_NAME);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.commandManager = new PaperCommandManager(this);

        Bukkit.getPluginManager().registerEvents(new PlayerManager(), this);
        Bukkit.getPluginManager().registerEvents(new LeafDecayListener(), this);
        Bukkit.getPluginManager().registerEvents(new SettingsListener(), this);

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

        PSettingsStorageUtil.saveEntries();
        for (Slot slot : SlotManager.getParkourMGRs().keySet()) {
            SlotManager.getParkourMGRs().get(slot).stop(false, true);
        }
    }

    public PaperCommandManager getCommandManager() {
        return commandManager;
    }
}
