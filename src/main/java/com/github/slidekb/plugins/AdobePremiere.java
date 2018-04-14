package com.github.slidekb.plugins;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

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

    private int previousSliderIndex;

    private boolean playing = false;
    private boolean toggle;
    private int virtualIndex;

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
        String[] keys = HKM.getHotKeys();
        String key = "";
        if (keys.length > 0) {
            key = keys[keys.length - 1];
            for (String k : keys) {
                if (k.equals("Shift")) {
                    toggle = true;
                }
            }

        } else {
            toggle = false;
        }
        int sliderIndex = slider.getVirtualPartIndex(100);
        if (virtualIndex < sliderIndex) {
            playing = false;
            System.out.println(Arrays.toString(keys));
            if (toggle) {

                // lib.ahkExec(new WString("SendEvent {g}"));

            } else {
                robot.keyPress(KeyEvent.VK_LEFT);
                robot.keyRelease(KeyEvent.VK_LEFT);
            }
            virtualIndex++;
            System.out.println(sliderIndex);
        }
        if (virtualIndex > sliderIndex) {
            playing = false;
            System.out.println(Arrays.toString(keys));
            if (toggle) {

                // lib.ahkExec(new WString("SendEvent {;}"));

            } else {
                robot.keyPress(KeyEvent.VK_RIGHT);
                robot.keyRelease(KeyEvent.VK_RIGHT);
            }
            virtualIndex--;
            System.out.println(sliderIndex);
        }
        if (sliderIndex == 98 && !playing) {
            playing = true;
            robot.keyPress(KeyEvent.VK_J);
            robot.keyRelease(KeyEvent.VK_J);
        }
        if (sliderIndex == 0 && !playing) {
            playing = true;
            robot.keyPress(KeyEvent.VK_L);
            robot.keyRelease(KeyEvent.VK_L);
        }
        if (key.equals("Space")) {
            playing = false;
            runFirst();
        }

    }

    @Override
    public void runFirst() {
        slider.writeUntilComplete(512);
        virtualIndex = slider.getVirtualPartIndex(100);
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
        // setup keyhook
        try {
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        HKM = new KeyHook();
        HKM.addValidHotkey("Space");
        GlobalScreen.addNativeKeyListener(HKM);
        System.out.println("AdobePremeire->setup()-> KeyHook setup");
        try {
            robot = new Robot();
        } catch (AWTException e) {
            System.out.println("Robot could not be created...");
        }
    }

    @Override
    public void teardown() {
        // TODO Auto-generated method stub

    }
}
