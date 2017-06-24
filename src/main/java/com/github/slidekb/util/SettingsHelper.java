package com.github.slidekb.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.slidekb.back.MainBack;
import com.github.slidekb.back.settings.GlobalSettings;
import com.github.slidekb.back.settings.PluginSettings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SettingsHelper {
    private static GlobalSettings settings = MainBack.getSettings();
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void addProcess(String pluginID, String processName) {
        settings.getPlugins().computeIfAbsent(pluginID, key -> new PluginSettings()).getProcesses().add(processName);
        save();
    }

    public static void removeProcess(String pluginID, String processName) {
        Optional.ofNullable(settings.getPlugins().get(pluginID)).ifPresent(pluginSettings -> pluginSettings.getProcesses().remove(processName));
        save();
    }

    public static List<String> listProcesses(String pluginID) {
        if (settings.getPlugins().containsKey(pluginID)) {
            return settings.getPlugins().get(pluginID).getProcesses();
        }

        // TODO
        // Is it good to return an empty list here?
        // Maybe return null or throw an exception instead?
        return new ArrayList<>();
    }

    public static void addHotkey(String pluginID, String hotkey) {
        settings.getPlugins().computeIfAbsent(pluginID, key -> new PluginSettings()).getHotkeys().add(hotkey);
        save();
    }

    public static void removeHotkey(String pluginID, String hotkey) {
        Optional.ofNullable(settings.getPlugins().get(pluginID)).ifPresent(pluginSettings -> pluginSettings.getHotkeys().remove(hotkey));
        save();
    }

    public static List<String> listHotkeys(String pluginID) {
        if (settings.getPlugins().containsKey(pluginID)) {
            return settings.getPlugins().get(pluginID).getHotkeys();
        }

        // TODO
        // Is it good to return an empty list here?
        // Maybe return null or throw an exception instead?
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

    public static String getUsedSlider(String pluginID) {
        if (settings.getPlugins().containsKey(pluginID)) {
            return settings.getPlugins().get(pluginID).getUsedSlider();
        }

        return null;
    }

    public static void setUsedSlider(String pluginID, String sliderID) {
        settings.getPlugins().computeIfAbsent(pluginID, key -> new PluginSettings()).setUsedSlider(sliderID);
        save();
    }

    private static void save() {
        try {
            File settingsFile = new File("settings.json");
            settingsFile.createNewFile();

            try (Writer writer = new FileWriter(settingsFile)) {
                gson.toJson(settings, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
