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

import java.awt.Robot;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;
import org.aeonbits.owner.Mutable;

import com.github.slidekb.api.PluginVersion;
import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.api.Slider;
import com.google.auto.service.AutoService;

/**
 * Created by JackSB on 4/22/2017.
 */
@PluginVersion(1)
@AutoService(SlideBarPlugin.class)
public class Volume implements SlideBarPlugin {

    Slider slider;

    String previous = "";

    Robot rob = null;

    boolean[] PartLatch = new boolean[101];

    private int virtualIndex = 1;

    ThisConfig cfg;

    public Volume() {
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

    public JFrame getConfigWindow() {
        return null;
    }

    @Config.Sources({ "classpath:configs/Volume.properties" })
    private interface ThisConfig extends Accessible, Mutable {
        @DefaultValue("default")
        String SliderID();
    }

    public int getVolume() {
        String Volume = "0";
        try {
            Runtime.getRuntime().exec("ahk_scripts/AudioToFile.exe", null, new File("ahk_scripts/"));
        } catch (IOException e) {

        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        File f = new File("ahk_scripts/AudioLevel.txt");
        try {
            Scanner s = new Scanner(f);
            if (s.hasNext()) {
                Volume = s.next();
                Volume = (int) (Double.parseDouble(Volume)) + "";
                s.close();
                // System.out.println(Integer.parseInt(Volume));
                return Integer.parseInt(Volume);
            }
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Integer.parseInt(Volume);
    }

    private void readValues() {
        int index = slider.getPartIndex(101);
        index = 100 - index;
        while (PartLatch[index] == true) {
            for (int i = 0; i < PartLatch.length; i++) {
                PartLatch[i] = true;
            }
            System.out.println("Setting Volume to: " + index / 5 * 5);
            setVolume(index / 5 * 5);
            PartLatch[index] = false;
        }
    }

    public void setVolume(int value) {
        try {
            Runtime.getRuntime().exec("ahk_scripts/AHKvolumeexes/" + value + ".exe", null, new File("ahk_scripts/AHKvolumeexes/"));
            System.out.println("it ran");
        } catch (IOException e) {
            System.out.println("no file found");
        }
    }

    @Override
    public void run() {
        readValues();
    }

    private void writeValues() {
        slider.removeParts();
        int volume = getVolume();
        if (volume < 5) {
            slider.write(1022);
        } else {
            slider.goToPartComplete(100 - getVolume(), 101);
        }
        for (int i = 0; i < PartLatch.length; i++) {
            PartLatch[i] = true;
        }
    }

    @Override
    public void runFirst() {
        writeValues();
    }

    @Override
    public String getLabelName() {
        return "Volume";
    }

    @Override
    public void setSlider(Slider slider, int position) {
        this.slider = slider;
    }

	@Override
	public int numberOfSlidersRequired() {
		// TODO Auto-generated method stub
		return 1;
	}
}
