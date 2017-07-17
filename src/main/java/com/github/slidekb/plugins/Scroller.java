package com.github.slidekb.plugins;

import java.awt.AWTException;
import java.awt.Robot;

import org.sikuli.script.App;
import org.sikuli.script.Location;
import org.sikuli.script.Mouse;

import com.github.slidekb.api.PluginVersion;
import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.api.Slider;
import com.google.auto.service.AutoService;

@PluginVersion(1)
@AutoService(SlideBarPlugin.class)
public class Scroller implements SlideBarPlugin {

	private Slider slider;
	private Robot rob;
	private int virtualIndex = 12;
	private int virtualparts = 25;
	private Location prev;
	private Object r;

	@Override
	public int getPriority() {// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLabelName() {
		return "Scroller";
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

	@Override
	public void run() {
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
                slider.writeUntilComplete(500);
                virtualIndex = slider.getVirtualPartIndex(virtualparts);
            }
            prev = current;
            try {
    			Thread.sleep(100);
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            virtualIndex = slider.getVirtualPartIndex(virtualparts);
        }
        slideIndex = slider.getVirtualPartIndex(virtualparts);
        
	}

	@Override
	public void runFirst() {
		virtualIndex = slider.getVirtualPartIndex(virtualparts);
        r = App.focusedWindow();
        prev = Mouse.at();
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
