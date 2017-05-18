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
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;
import org.aeonbits.owner.Mutable;
import org.sikuli.script.App;
import org.sikuli.script.Location;
import org.sikuli.script.Mouse;
import org.sikuli.script.Region;
import org.aeonbits.owner.Config.DefaultValue;

import com.github.slidekb.api.AlphaKeyManager;
import com.github.slidekb.api.HotKeyManager;
import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.api.Slider;
import com.github.slidekb.api.SliderManager;
import com.github.slidekb.back.MainBack;
import com.github.slidekb.front.ProcessListSelector;
import com.google.auto.service.AutoService;


@AutoService(SlideBarPlugin.class)
public class VSCode implements SlideBarPlugin {
	
	Robot robot = null;
	
	Region r;
	
	Location prev = null;

    int virtualIndex = 12;

    int virtualparts = 25;
    
    int verticalVirtualParts = 7;
    
    int verticalVirtualIndex = 3;
    
    ThisConfig cfg;
    
    ProcessListSelector pls = new ProcessListSelector("VSCode.properties", getLabelName());
	
	List<String> attachedProcesses = new ArrayList<>();
	
	private Slider slider;
	
	private Slider slider2;

	private SliderManager sliderManager;
	
	
	@Config.Sources({ "classpath:configs/Slider.properties" })
    private interface ThisConfig extends Accessible, Mutable {

        @DefaultValue("Code.exe, code.exe")
        String[] processList();

        @DefaultValue("default")
        String SliderID();
        
        @DefaultValue("default2")
        String SliderID2();
    }
	
	@Override
    public void reloadPropFile() {
        try {
            FileInputStream in = new FileInputStream(ClassLoader.getSystemClassLoader().getResource(".").getPath() + "\\configs\\VSCode.properties");
            cfg.load(in);
            in.close();

            attachedProcesses = new ArrayList<>(Arrays.asList(cfg.processList()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private boolean loadConfiguration() {
        cfg = ConfigFactory.create(ThisConfig.class);
        attachedProcesses = new ArrayList<>(Arrays.asList(cfg.processList()));
        return true;
    }
	
	public VSCode(){
		slider2 = MainBack.sliders.get("m1n2");
		loadConfiguration();
		try {
			robot = new Robot();
		} catch (Exception e){
			
		}
		
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public String[] getProcessNames() {
		return attachedProcesses.toArray(new String[attachedProcesses.size()]);
	}

	@Override
	public boolean usesProcessNames() {
		return true;
	}

	@Override
	public String currentlyUsedSlider() {
		return cfg.SliderID();
	}

	@Override
	public void attachToProcess(String processName) {
		attachedProcesses.add(processName);
		
	}

	@Override
	public void detachFromProcess(String processName) {
		attachedProcesses.remove(processName);
		
	}

	@Override
	public void run(String process) {
		int slideIndex = slider.getPartIndex();
		int verticalSlideIndex = slider2.getVirtualPartIndex(verticalVirtualParts);
		Location current = Mouse.at();
//         System.out.println(slideIndex);

        if (virtualIndex < slideIndex) {
            robot.keyPress(KeyEvent.VK_LEFT);
            robot.keyRelease(KeyEvent.VK_LEFT);
            virtualIndex++;
        }
        if (virtualIndex > slideIndex) {
//             System.out.println(virtualIndex + " - 1");
            robot.keyPress(KeyEvent.VK_RIGHT);
            robot.keyRelease(KeyEvent.VK_RIGHT);
            virtualIndex--;
        }
        
        
        if (verticalVirtualIndex < verticalSlideIndex) {
        	robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
            verticalVirtualIndex++;
            System.out.println(verticalSlideIndex);
        }
        if (verticalVirtualIndex > verticalSlideIndex) {
        	robot.keyPress(KeyEvent.VK_UP);
            robot.keyRelease(KeyEvent.VK_UP);
            verticalVirtualIndex--;
            System.out.println(verticalSlideIndex);
        }
        
        if (verticalSlideIndex == verticalVirtualParts - 1) {
        	robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
            Sleeper(10);
        }
        
        if (verticalSlideIndex == 0) {
        	robot.keyPress(KeyEvent.VK_UP);
            robot.keyRelease(KeyEvent.VK_UP);
            Sleeper(10);
        }
        if (Math.abs(prev.getX() - current.getX()) > 2 || Math.abs(prev.getY() - current.getY()) > 2) {
            if (slideIndex != (13 / 2) && slideIndex != (13 / 2) + 1 && slideIndex != (13 / 2) - 1) {
                slider.removeParts();
            	slider.writeUntilComplete(512);
            	slider.createParts(13);
                
                virtualIndex = slider.getPartIndex();
                
            }
            if (verticalSlideIndex != (verticalVirtualParts / 2) && verticalSlideIndex != (verticalVirtualParts / 2) + 1 && verticalSlideIndex != (verticalVirtualParts / 2) - 1) {
            	slider2.writeUntilComplete(500);
                verticalVirtualIndex = slider2.getVirtualPartIndex(verticalVirtualParts);
            }
            prev = current;
            
            
        }
		
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
	public JFrame getConfigWindow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void runFirst(String process) {
		slider.removeParts();
		slider2.removeParts();
		System.out.println("VSCode is running");
		slider.writeUntilComplete(512);
		slider2.writeUntilComplete(512);
		slider.createParts(13);
		virtualIndex = slider.getPartIndex();
		verticalVirtualIndex = slider2.getVirtualPartIndex(verticalVirtualParts);
		r = App.focusedWindow();
		prev = Mouse.at();
	}

	@Override
	public String getLabelName() {
		return "VSCode";
	}

	@Override
	public JFrame getProcessWindow() {
		return pls.createGUI();
	}

	@Override
	public void setAlphaKeyManager(AlphaKeyManager alphaKeyManager) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHotKeyManager(HotKeyManager hotKeyManager) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSliderManager(SliderManager sliderManager) {
		this.sliderManager = sliderManager;
		this.slider = this.sliderManager.getSliderByID("default");
	}

}
