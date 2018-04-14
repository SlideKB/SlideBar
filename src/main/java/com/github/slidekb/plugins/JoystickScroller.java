package com.github.slidekb.plugins;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.sikuli.script.App;
import org.sikuli.script.Location;
import org.sikuli.script.Mouse;

import com.github.slidekb.api.PluginVersion;
import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.api.Slider;
import com.github.slidekb.util.KeyHook;
import com.google.auto.service.AutoService;

@PluginVersion(1)
@AutoService(SlideBarPlugin.class)
public class JoystickScroller implements SlideBarPlugin {

	private Slider slider;
	private Object virtualIndex = 12;
	private int virtualparts = 25;

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public String getLabelName() {
		return "Joystick Scroller";
	}

	@Override
	public void setup() {
		
	}

	@Override
	public void teardown() {
		
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
	public void run() {
		int slideIndex = slider.getVirtualPartIndex(virtualparts);
		if (slideIndex > 12) {
			System.out.println("greater than 12");
			slider.scrollDown(1);
            Sleeper(7);
			slider.write(512);
		}
		if (slideIndex < 12) {
			System.out.println("less than 12");
			slider.scrollUp(1);
            Sleeper(7);
			slider.write(512);
		}
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void runFirst() {
		virtualIndex = slider.getVirtualPartIndex(virtualparts );
	}

	@Override
	public void setSlider(Slider slider, int index) {
		if (index == 0){
			this.slider = slider;
		}
	}

	@Override
	public int numberOfSlidersRequired() {
		return 1;
	}

}
