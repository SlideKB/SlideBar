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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;
import org.aeonbits.owner.Mutable;
import org.sikuli.script.App;
import org.sikuli.script.Location;
import org.sikuli.script.Mouse;
import org.sikuli.script.Region;

import com.github.slidekb.api.AlphaKeyManager;
import com.github.slidekb.api.BasePluginConfig;
import com.github.slidekb.api.HotKeyManager;
import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.api.Slider;
import com.github.slidekb.api.SliderManager;
import com.github.slidekb.front.ProcessListSelector;
import com.google.auto.service.AutoService;

/**
 * Created by JackSec on 3/24/2017.
 */
@AutoService(SlideBarPlugin.class)
public class Scroller implements SlideBarPlugin {

    Slider slider;
    
    BasePluginConfig baseConfig;

    Region r;

    Robot robot = null;

    int virtualIndex = 12;

    int virtualparts = 25;

    Location prev = null;

    List<String> attachedProcesses = new ArrayList<>();

    ProcessListSelector pls = new ProcessListSelector("Scroller.properties", getLabelName());

	private SliderManager sliderManager;

    public Scroller() {
        loadConfiguration();
    }
    
    public boolean enabled(){
    	return baseConfig.isEnabled();
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String[] getProcessNames() {
//        return attachedProcesses.toArray(new String[attachedProcesses.size()]);
    	return baseConfig.getProcessNames();
    }

    private void Sleeper(int delay) {
        for (int i = 5; i < delay; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reloadPropFile() {
        try {
			baseConfig.reloadConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
    public void run(String process) {
        int slideIndex = slider.getVirtualPartIndex(virtualparts);
        Location current = Mouse.at();

        if (virtualIndex < slideIndex) {
            slider.scrollDown(2);
            virtualIndex++;
            System.out.println(slideIndex);
        }
        if (virtualIndex > slideIndex) {
            slider.scrollUp(2);
            virtualIndex--;
            System.out.println(slideIndex);
        }
        if (slideIndex == virtualparts - 2) {
            slider.scrollDown(1);
            Sleeper(11);
        }
        if (slideIndex == virtualparts - 1) {
            slider.scrollDown(1);
            Sleeper(7);
        }
        if (slideIndex == 1) {
            slider.scrollUp(1);
            Sleeper(11);
        }
        if (slideIndex == 0) {
            slider.scrollUp(1);
            Sleeper(7);
        }
        if (Math.abs(prev.getX() - current.getX()) > 2 || Math.abs(prev.getY() - current.getY()) > 2) {
            if (slideIndex != (virtualparts / 2) && slideIndex != (virtualparts / 2) + 1 && slideIndex != (virtualparts / 2) - 1) {
                slider.writeUntilComplete(512);
                virtualIndex = slider.getVirtualPartIndex(virtualparts);
            }
            prev = current;
        }
    }

    private boolean loadConfiguration() {
    	try {
			baseConfig = new BasePluginConfig(new File(ClassLoader.getSystemClassLoader().getResource(".").getPath() + "\\configs\\Scroller.properties"),"process");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	System.out.println(baseConfig.getSlider1ID());
        return true;
    }

    @Override
    public JFrame getConfigWindow() {
        return baseConfig.displayBaseConfigs();
    }

    @Override
    public void runFirst(String process) {
        slider.removeParts();
        System.out.println("Scroller is running!");
        // s.createParts(50);
        slider.writeUntilComplete(512);
        virtualIndex = slider.getVirtualPartIndex(virtualparts);
        r = App.focusedWindow();
        prev = Mouse.at();
    }

    @Override
    public String getLabelName() {
        return "Scroller";
    }

    @Override
    public JFrame getProcessWindow() {
        return pls.createGUI();
    }

    @Override
    public void setAlphaKeyManager(AlphaKeyManager alphaKeyManager) {
        // NOP
    }

    @Override
    public void setHotKeyManager(HotKeyManager hotKeyManager) {
        // NOP
    }

    @Override
    public void attachToProcess(String processName) {
        attachedProcesses.add(processName);
    }

    @Override
    public void detachFromProcess(String processName) {
        attachedProcesses.remove(processName);
    }

//    @Override
//    public String currentlyUsedSlider() {
//        return baseConfig.getSlider1ID();
//    }

    @Override
    public boolean usesProcessNames() {
        return true;
    }

	@Override
	public void setSliderManager(SliderManager sliderManager) {
		this.sliderManager = sliderManager;
		this.slider = sliderManager.getSliderByID(baseConfig.getSlider1ID());
		this.slider.setReversed(baseConfig.isSlider1Reversed());
	}

	@Override
	public String currentlyUsedSlider() {
		// TODO Auto-generated method stub
		return null;
	}
}
