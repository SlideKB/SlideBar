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
public class Scroller implements SlideBarPlugin {

	private Slider slider;
	private Robot rob;
	private int virtualIndex = 12;
	private int virtualparts = 25;
	private Location prev;
	private Object r;
	private KeyHook kh;
	private int wheelDelta;

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
		try {
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        kh = new KeyHook();
//        GlobalScreen.addNativeKeyListener(kh);
        GlobalScreen.addNativeMouseWheelListener(kh);
//        GlobalScreen.addNativeMouseListener(kh);
        System.out.println("Scroller->setup()-> KeyHook setup");
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
            kh.setWheelDelta(0);
            wheelDelta = 0;
            virtualIndex++;
//	            System.out.println("Scroller->run()-> slideIndex " + slideIndex);
        }
        if (virtualIndex > slideIndex) {
            slider.scrollUp(2);
            kh.setWheelDelta(0);
            wheelDelta = 0;
            virtualIndex--;
//	            System.out.println("Scroller->run()-> slideIndex " + slideIndex);
        }
        if (slideIndex == virtualparts - 2) {
            slider.scrollDown(1);
            Sleeper(11);
            kh.setWheelDelta(0);
            wheelDelta = 0;
        }
        if (slideIndex == virtualparts - 1) {
            slider.scrollDown(1);
            Sleeper(7);
            kh.setWheelDelta(0);
            wheelDelta = 0;
        }
        if (slideIndex == 1) {
            slider.scrollUp(1);
            Sleeper(11);
            kh.setWheelDelta(0);
            wheelDelta = 0;
        }
        if (slideIndex == 0) {
            slider.scrollUp(1);
            Sleeper(7);
            kh.setWheelDelta(0);
            wheelDelta = 0;
        }
        if (Math.abs(prev.getX() - current.getX()) > 2 || Math.abs(prev.getY() - current.getY()) > 2) {
            if (slideIndex != (virtualparts / 2) && slideIndex != (virtualparts / 2) + 1 && slideIndex != (virtualparts / 2) - 1) {
                slider.writeUntilComplete(500);
                kh.setWheelDelta(0);
                wheelDelta = 0;
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
            kh.setWheelDelta(0);
            wheelDelta = 0;
        }
        wheelDelta = wheelDelta + kh.getWheelDelta();
        System.out.println(wheelDelta);
        if (wheelDelta != 0 & wheelDelta != -6 & wheelDelta != 6) {
        	System.out.println("moving slider");
        	if (slideIndex == 0 || slideIndex == 2 || slideIndex == virtualparts - 1 || slideIndex == virtualparts - 3){
        		slider.writeUntilComplete(500);
                kh.setWheelDelta(0);
                wheelDelta = 0;
        	} else {
        		if (wheelDelta > 0) {
    				slider.bumpRight(wheelDelta);
    			} else {
    				slider.bumpLeft(wheelDelta*-1);
    			}
        	}
			kh.setWheelDelta(0);
			wheelDelta = 0;
        }
        slideIndex = slider.getVirtualPartIndex(virtualparts);
        kh.setWheelDelta(0);
	}
	
//	public void run() {
//		int wheelDelta = kh.getWheelDelta();
//		if (wheelDelta == 0) {
//			int slideIndex = slider.getVirtualPartIndex(virtualparts);
//	        Location current = Mouse.at();
//
//	        if (virtualIndex < slideIndex) {
//	            kh.setWheelDelta(0);
//	            slider.scrollDown(2);
//	            kh.setWheelDelta(0);
//	            virtualIndex++;
////	            System.out.println("Scroller->run()-> slideIndex " + slideIndex);
//	        }
//	        if (virtualIndex > slideIndex) {
//	            kh.setWheelDelta(0);
//	            slider.scrollUp(2);
//	            kh.setWheelDelta(0);
//	            virtualIndex--;
////	            System.out.println("Scroller->run()-> slideIndex " + slideIndex);
//	        }
//	        if (slideIndex == virtualparts - 2) {
//	            kh.setWheelDelta(0);
//	            slider.scrollDown(1);
//	            kh.setWheelDelta(0);
//	            Sleeper(11);
//	            kh.setWheelDelta(0);
//	        }
//	        if (slideIndex == virtualparts - 1) {
//	            kh.setWheelDelta(0);
//	            slider.scrollDown(1);
//	            kh.setWheelDelta(0);
//	            Sleeper(7);
//	            kh.setWheelDelta(0);
//	        }
//	        if (slideIndex == 1) {
//	            kh.setWheelDelta(0);
//	            slider.scrollUp(1);
//	            kh.setWheelDelta(0);
//	            Sleeper(11);
//	            kh.setWheelDelta(0);
//	        }
//	        if (slideIndex == 0) {
//	            kh.setWheelDelta(0);
//	            slider.scrollUp(1);
//	            kh.setWheelDelta(0);
//	            Sleeper(7);
//	            kh.setWheelDelta(0);
//	        }
//	        if (Math.abs(prev.getX() - current.getX()) > 2 || Math.abs(prev.getY() - current.getY()) > 2) {
//	            if (slideIndex != (virtualparts / 2) && slideIndex != (virtualparts / 2) + 1 && slideIndex != (virtualparts / 2) - 1) {
//		            kh.setWheelDelta(0);
//	                slider.writeUntilComplete(500);
//	                kh.setWheelDelta(0);
//	                virtualIndex = slider.getVirtualPartIndex(virtualparts);
//	            }
//	            prev = current;
//	            kh.setWheelDelta(0);
//	            try {
//	    			Thread.sleep(100);
//	    		} catch (InterruptedException e) {
//	    			// TODO Auto-generated catch block
//	    			e.printStackTrace();
//	    		}
//	            virtualIndex = slider.getVirtualPartIndex(virtualparts);
//	        }
//	        slideIndex = slider.getVirtualPartIndex(virtualparts);
//	        kh.setWheelDelta(0);
//		} else {
//			System.out.println("moving slider");
//			if (wheelDelta > 0) {
//				slider.bumpRight(wheelDelta);
//			} else {
//				slider.bumpLeft(wheelDelta*-1);
//			}
//			
//			kh.setWheelDelta(0);
//		}
//	}

	@Override
	public void runFirst() {
		virtualIndex = slider.getVirtualPartIndex(virtualparts);
        r = App.focusedWindow();
        prev = Mouse.at();
        kh.setWheelDelta(0);
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
