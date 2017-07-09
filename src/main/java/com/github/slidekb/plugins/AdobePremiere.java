package com.github.slidekb.plugins;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.Arrays;

import com.github.slidekb.api.PluginVersion;
import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.api.Slider;
import com.github.slidekb.util.KeyHook;
import com.google.auto.service.AutoService;

@PluginVersion(1)
@AutoService(SlideBarPlugin.class)
public class AdobePremiere implements SlideBarPlugin {
	
	private KeyHook HKM = new KeyHook();
	private Robot robot;
	private Slider slider;
	
	
	@Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String getLabelName() {
        return "Adobe Premiere";
    }

    @Override
    public void run() {
    	
    }

    @Override
    public void runFirst() {
    	slider.writeUntilComplete(512);
    }

    @Override
    public void setSlider(Slider slider, int position) {
    	if (position == 0) {
    		this.slider = slider;
    	}
    }

    @Override
    public int numberOfSlidersRequired() {
        return 1;
    }

    @Override
    public void setup() {
    	try {
            robot = new Robot();
        } catch (AWTException e) {
            System.out.println("Robot could not be created...");
        }
    }
}
