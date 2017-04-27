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
import com.github.slidekb.back.AlphaKeyManager;
import com.github.slidekb.back.Slider;
import com.github.slidekb.front.ProcessListSelector;
import com.google.auto.service.AutoService;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@AutoService(SlideBarPlugin.class)
public class TypeWriter implements SlideBarPlugin {

    AlphaKeyManager AKM = new AlphaKeyManager();

    Slider slide = new Slider();

    String previous = "";

    ThisConfig cfg;

    ProcessListSelector pls = new ProcessListSelector("TypeWriter.properties", getLabelName());

    public TypeWriter() {
        loadConfiguration();
        AKM.addKey("Enter");
        AKM.addKey("Space");
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public String[] getProcessNames() {
        if (cfg.processList().length != 0) {
            return cfg.processList();
        }
        return new String[] { "none" };
    }

    /**
     * returns the human readable name of this class.
     */
    @Override
    public String getLabelName() {
        return "Type Writer";
    }

    @Override
    public JFrame getProcessWindow() {
        return pls.createGUI();
    }

    @Override
    public void reloadPropFile() {
        try {
            FileInputStream in = new FileInputStream(ClassLoader.getSystemClassLoader().getResource(".").getPath() + "\\configs\\TypeWriter.properties");
            cfg.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean loadConfiguration() {
        cfg = ConfigFactory.create(ThisConfig.class);
        for (String s : cfg.processList()) {
            pls.addToRight(s);
        }
        return true;
    }

    @Config.Sources({ "classpath:configs/TypeWriter.properties" })
    private interface ThisConfig extends Accessible, Mutable {
        String[] processList();
    }

    /**
     * calls writeValues();
     */
    @Override
    public void run(String process) {
        try {
            writeValues();
        } catch (Exception e) {
            System.out.println("Type Writer Process threw an exception");
        }

    }

    /**
     * creates part detector for the run() method. 26 parts for 26 letters in
     * the alphabet.
     */
    @Override
    public void runFirst(String process) {
        slide.write(1010);
    }

    /**
     * Listens to AKM and runs when there is an alpha key pressed. moves arduino
     * to position.
     */
    public void writeValues() {
        String[] keys = AKM.getAlphaKeys();
        if (keys.length > 0) {
            String key = keys[keys.length - 1];
            if (!key.equals(previous)) {
                if (!key.equals("Enter")) {
                    slide.bumpLeft(10);
                    previous = key;
                } else {
                    slide.writeUntilComplete(1010);
                    previous = key;
                }

            }
        } else {
            previous = "";
        }
    }

    /**
     * not used for the type-writer.
     */
    public void readValues() {
    }

    @Override
    public JFrame getConfigWindow() {
        return null;
    }

}
