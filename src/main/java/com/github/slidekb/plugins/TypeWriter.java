package com.github.slidekb.plugins;

import com.github.slidekb.api.PluginVersion;
import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.api.Slider;
import com.google.auto.service.AutoService;

@PluginVersion(1)
@AutoService(SlideBarPlugin.class)
public class TypeWriter implements SlideBarPlugin {

    private Slider slider;
    private Slider secondSlider;

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String getLabelName() {
        return "Type Writer";
    }

    @Override
    public void run() {
        System.out.println("type writer is running");
    }

    @Override
    public void runFirst() {
        slider.removeParts();
        slider.writeUntilComplete(1000);
        slider.writeUntilComplete(100);

        secondSlider.removeParts();
        secondSlider.writeUntilComplete(1000);
        secondSlider.writeUntilComplete(100);
    }

    @Override
    public void setSlider(Slider slider, int position) {
        if (position == 0) {
            this.slider = slider;
        } else if (position == 1) {
            this.secondSlider = slider;
        }
    }

    @Override
    public int numberOfSlidersRequired() {
        return 2;
    }

    @Override
    public void setup() {
    }

}
