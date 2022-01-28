package mfw.when.infiniteparkour;

import mfw.when.infiniteparkour.commands.InfiniteParkourCommand;
import mfw.when.infiniteparkour.commands.ResetCommand;
import mfw.when.infiniteparkour.commands.TestCommand;
import mfw.when.infiniteparkour.listeners.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class InfiniteParkour extends JavaPlugin {

    public static List<String> testCommandOptions = new ArrayList<>();
    private static Plugin plugin;

    public static Plugin getPlugin() {
        return plugin;
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

        testCommandOptions.add("slotspacing");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
