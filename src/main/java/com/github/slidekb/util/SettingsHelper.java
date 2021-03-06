package com.github.slidekb.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.github.slidekb.back.settings.GlobalSettings;
import com.github.slidekb.back.settings.PluginSettings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SettingsHelper {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static File settingsFile = new File("settings.json");
    private static GlobalSettings settings = null;

    static {
        try {
            if (!settingsFile.createNewFile()) {
                try (Reader reader = new FileReader(settingsFile)) {
                    settings = gson.fromJson(reader, GlobalSettings.class);
                }
            }

            if (settings == null) {
                settings = new GlobalSettings();
            }
        } catch (IOException e) {
            // If the file cannot be parsed, crash the app
            throw new RuntimeException(e);
        }
    }

    public static void addProcess(String pluginID, String processName) {
        if (processName != null && !processName.isEmpty()) {
            settings.getPlugins().computeIfAbsent(pluginID, key -> new PluginSettings()).getProcesses().add(processName);
            save();
        }
    }

    public static void removeProcess(String pluginID, String processName) {
        Optional.ofNullable(settings.getPlugins().get(pluginID)).ifPresent(pluginSettings -> pluginSettings.getProcesses().remove(processName));
        save();
    }

    public static List<String> listProcesses(String pluginID) {
        if (settings.getPlugins().containsKey(pluginID)) {
            return settings.getPlugins().get(pluginID).getProcesses();
        }

        return new ArrayList<>();
    }

    public static void addHotkey(String pluginID, String hotkey) {
        if (hotkey != null && !hotkey.isEmpty()) {
            settings.getPlugins().computeIfAbsent(pluginID, key -> new PluginSettings()).getHotkeys().add(hotkey);
            save();
        }
    }

    public static void removeHotkey(String pluginID, String hotkey) {
        Optional.ofNullable(settings.getPlugins().get(pluginID)).ifPresent(pluginSettings -> pluginSettings.getHotkeys().remove(hotkey));
        save();
    }

    public static List<String> listHotkeys(String pluginID) {
        if (settings.getPlugins().containsKey(pluginID)) {
            return settings.getPlugins().get(pluginID).getHotkeys();
        }

        return new ArrayList<>();
    }

    public static boolean isAlwaysRun(String pluginID) {
        if (settings.getPlugins().containsKey(pluginID)) {
            return settings.getPlugins().get(pluginID).isAlwaysRun();
        }

        return false;
    }

    public static void setAlwaysRun(String pluginID, boolean alwaysRun) {
        settings.getPlugins().computeIfAbsent(pluginID, key -> new PluginSettings()).setAlwaysRun(alwaysRun);
        save();
    }

    public static boolean isReversed(String pluginID) {
        if (settings.getPlugins().containsKey(pluginID)) {
            return settings.getPlugins().get(pluginID).isReversed();
        }

        return false;
    }

    public static void setReversed(String pluginID, boolean reversed) {
        settings.getPlugins().computeIfAbsent(pluginID, key -> new PluginSettings()).setReversed(reversed);
        save();
    }

    @Nullable
    public static String getUsedSliderAtIndex(String pluginID, int index) {
        if (settings.getPlugins().containsKey(pluginID)) {
            return settings.getPlugins().get(pluginID).getSliderAtIndex(index);
        }
        return null;
    }

    public static void setUsedSliderAtIndex(String pluginID, int index, String sliderID) {
        settings.getPlugins().computeIfAbsent(pluginID, key -> new PluginSettings()).setSliderAtIndex(index, sliderID);
        save();
    }

    public static boolean isPluginKnown(String pluginID) {
        return settings.getPlugins().containsKey(pluginID);
    }

    private static void save() {
        try {
            try (Writer writer = new FileWriter(settingsFile)) {
                gson.toJson(settings, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
