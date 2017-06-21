package com.github.slidekb.util;

import java.util.Optional;

import com.github.slidekb.back.MainBack;
import com.github.slidekb.back.settings.GlobalSettings;
import com.github.slidekb.back.settings.PluginSettings;

public class SettingsHelper {
    private static GlobalSettings settings = MainBack.getSettings();

    public static void addProcess(String pluginID, String processName) {
        settings.getPlugins().computeIfAbsent(pluginID, key -> new PluginSettings()).getProcesses().add(processName);
    }

    public static void removeProcess(String pluginID, String processName) {
        Optional.ofNullable(settings.getPlugins().get(pluginID)).ifPresent(pluginSettings -> pluginSettings.getProcesses().remove(processName));
    }

    public static void addHotkey(String pluginID, String hotkey) {
        settings.getPlugins().computeIfAbsent(pluginID, key -> new PluginSettings()).getHotkeys().add(hotkey);
    }

    public static void removeHotkey(String pluginID, String hotkey) {
        Optional.ofNullable(settings.getPlugins().get(pluginID)).ifPresent(pluginSettings -> pluginSettings.getHotkeys().remove(hotkey));
    }
}
