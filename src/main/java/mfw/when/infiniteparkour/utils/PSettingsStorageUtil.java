package mfw.when.infiniteparkour.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.models.PSettingsEntry;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PSettingsStorageUtil {

    private static ArrayList<PSettingsEntry> entries = new ArrayList<>();

    public static PSettingsEntry addEntry(String playerUUID) {
        PSettingsEntry entry = new PSettingsEntry(playerUUID, false, false);
        entries.add(entry);
        return entry;
    }

    @Nullable
    public static PSettingsEntry findEntry(String playerUUID) {
        for (PSettingsEntry e : entries) {
            if (e.getPlayerID().equals(playerUUID)) {
                return e;
            }
        }

        return null;
    }

    public static void saveEntries() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(InfiniteParkour.getPlugin().getDataFolder(), InfiniteParkour.PLAYER_SETTINGS_FILE_NAME);

        try {
            Writer writer = new FileWriter(file, false);
            gson.toJson(entries, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteEntry(String playerUUID) {
        entries.remove(findEntry(playerUUID));
    }

    public static PSettingsEntry updateEntry(String playerUUId, boolean ladderJumpsEnabled, boolean neoJumpsEnabled) {
        PSettingsEntry entry = findEntry(playerUUId);
        if (entry != null) {
            entry.setLadderJumpsEnabled(ladderJumpsEnabled);
            entry.setNeoJumpsEnabled(neoJumpsEnabled);
        }
        return entry;
    }

    public static List<PSettingsEntry> getEntries() {
        return entries;
    }

    public static void loadEntries() {
        entries.clear();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(InfiniteParkour.getPlugin().getDataFolder(), InfiniteParkour.PLAYER_SETTINGS_FILE_NAME);
        if (file.exists()) {
            Reader reader = null;
            try {
                reader = new FileReader(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            PSettingsEntry[] e = gson.fromJson(reader, PSettingsEntry[].class);
            entries = new ArrayList<>(Arrays.asList(e));
        }
    }

    public static BukkitTask startSaveTask() {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(InfiniteParkour.getPlugin(), PSettingsStorageUtil::saveEntries, 0, 20);
    }
}
