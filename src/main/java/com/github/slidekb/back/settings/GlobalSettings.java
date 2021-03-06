package com.github.slidekb.back.settings;

import java.util.HashMap;
import java.util.Map;

public class GlobalSettings {
    private Map<String, SliderSettings> sliders;
    private Map<String, PluginSettings> plugins;

    public Map<String, SliderSettings> getSliders() {
        if (sliders == null) {
            sliders = new HashMap<>();
        }

        return sliders;
    }

    public Map<String, PluginSettings> getPlugins() {
        if (plugins == null) {
            plugins = new HashMap<>();
        }

        return plugins;
    }
}
