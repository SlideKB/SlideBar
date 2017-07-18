/**
 Copyright 2017 John Kester (Jack Kester)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.github.slidekb.plugins;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;
import org.aeonbits.owner.Mutable;

import com.github.slidekb.api.PluginVersion;
import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.api.Slider;
import com.google.auto.service.AutoService;

/**
 * Created by JackSB on 3/12/2017.
 */
@PluginVersion(1)
@AutoService(SlideBarPlugin.class)
public class AltProcess implements SlideBarPlugin {

    Slider slider;

    String previous = "";

    Robot rob = null;

    private int virtualIndex = 1;

    ThisConfig cfg;

    public AltProcess() {
        loadConfiguration();
    }

    @Override
    public int getPriority() {
        return -1;
    }

    private boolean loadConfiguration() {
        cfg = ConfigFactory.create(ThisConfig.class);

        return true;
    }

    @Config.Sources({ "classpath:configs/AltProcess.properties" })
    private interface ThisConfig extends Accessible, Mutable {
        @DefaultValue("7")
        int numberOfParts();

        @DefaultValue("0")
        int StartingPart();

        @DefaultValue("default")
        String SliderID();

    }

    @Override
    public void run() {
        int slideIndex = slider.getPartIndex();
        // System.out.println(slideIndex);

        if (virtualIndex < slideIndex) {
            
            rob.keyPress(KeyEvent.VK_SHIFT);
            rob.keyPress(KeyEvent.VK_TAB);
            rob.keyRelease(KeyEvent.VK_TAB);
            rob.keyRelease(KeyEvent.VK_SHIFT);
            virtualIndex++;
        }
        if (virtualIndex > slideIndex) {
            // System.out.println(virtualIndex + " - 1");

            rob.keyPress(KeyEvent.VK_TAB);
            rob.keyRelease(KeyEvent.VK_TAB);
            virtualIndex--;
        }
    }

    @Override
    public void runFirst() {
        virtualIndex = cfg.StartingPart();
        slider.createParts(cfg.numberOfParts());
        slider.goToPartComplete(cfg.StartingPart());
        slider.removeParts();
        slider.createParts(cfg.numberOfParts());
    }

    @Override
    public String getLabelName() {
        return "Alt+Slide";
    }

    @Override
    public void setSlider(Slider slider, int position) {
        this.slider = slider;
    }

    @Override
    public int numberOfSlidersRequired() {
        return 1;
    }

    @Override
    public void setup() {
        try {
            rob = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void teardown() {
    }
}
