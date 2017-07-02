package com.github.slidekb.back.settings;

import java.util.ArrayList;
import java.util.List;

public class PluginSettings {
    String usedSlider;
    List<String> processes;
    List<String> hotkeys;
    boolean alwaysRun;
    List<String> sliderList;

    public List<String> getSliderList() {
        return sliderList;
    }

    public void setSliderList(List<String> list) {
        this.sliderList = list;
    }

    public String getUsedSlider() {
        return usedSlider;
    }

    public void setUsedSlider(String usedSlider, int position) {
        this.usedSlider = usedSlider;
    }

    public List<String> getProcesses() {
        if (processes == null) {
            processes = new ArrayList<>();
        }

        return processes;
    }

    public List<String> getHotkeys() {
        if (hotkeys == null) {
            hotkeys = new ArrayList<>();
        }

        return hotkeys;
    }

    public boolean isAlwaysRun() {
        return alwaysRun;
    }

    public void setAlwaysRun(boolean alwaysRun) {
        this.alwaysRun = alwaysRun;
    }
}
