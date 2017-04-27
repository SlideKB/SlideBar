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

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;
import org.aeonbits.owner.Mutable;

import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.back.HotKeyManager;
import com.github.slidekb.back.Slider;
import com.google.auto.service.AutoService;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by JackSB on 4/22/2017.
 */
@AutoService(SlideBarPlugin.class)
public class Volume implements SlideBarPlugin {

    HotKeyManager HKM = new HotKeyManager();

    Slider slide = new Slider();

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

    @Override
    public String[] getProcessNames() {
        String[] hotKey2 = { "Alt", "0" };
        String[] list = { Arrays.toString(hotKey2) };
        return list;
    }

    private boolean loadConfiguration() {
        cfg = ConfigFactory.create(ThisConfig.class);
        HKM.addKey("0");
        return true;
    }

    public JFrame getConfigWindow() {
        return null;
    }

    @Config.Sources({ "classpath:configs/Volume.properties" })
    private interface ThisConfig extends Accessible, Mutable {

    }

    public int getVolume() {
        String Volume = "0";
        try {
            Runtime.getRuntime().exec("src/ahk_scripts/AudioToFile.exe", null, new File("src/ahk_scripts/"));
        } catch (IOException e) {

        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        File f = new File("src/ahk_scripts/AudioLevel.txt");
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
        int index = slide.getPartIndex(101);
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
            Runtime.getRuntime().exec("src/ahk_scripts/AHKvolumeexes/" + value + ".exe", null, new File("src/ahk_scripts/AHKvolumeexes/"));
            System.out.println("it ran");
        } catch (IOException e) {
            System.out.println("no file found");
        }
    }

    @Override
    public void run(String process) {
        readValues();
    }

    private void writeValues() {
        slide.goToPartComplete(100 - getVolume(), 101);
        for (int i = 0; i < PartLatch.length; i++) {
            PartLatch[i] = true;
        }
    }

    @Override
    public void runFirst(String process) {
        writeValues();
    }

    @Override
    public String getLabelName() {
        return "Volume";
    }

    @Override
    public JFrame getProcessWindow() {
        return null;
    }

    @Override
    public void reloadPropFile() {

    }
}
