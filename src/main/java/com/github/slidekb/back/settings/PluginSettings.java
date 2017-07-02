package com.github.slidekb.back.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PluginSettings {
    List<String> processes;
    List<String> hotkeys;
    boolean alwaysRun;
    String[] sliderList;

    public String getSliderAtIndex(int index) {
        validateSliderArray();

        return sliderList[index];
    }

    public void setSliderAtIndex(int index, String sliderID) {
        validateSliderArray();

        sliderList[index] = sliderID;
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

    private void validateSliderArray() {
        if (sliderList == null) {
            sliderList = new String[4];
        } else {
            sliderList = Arrays.copyOfRange(sliderList, 0, 4);
        }
    }
}
