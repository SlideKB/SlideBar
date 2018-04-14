package com.github.slidekb.plugins;

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
public class TypeWriter implements SlideBarPlugin {

    private Slider slider;

    private KeyHook kh;

    private int previousAmount;

    private String previous;

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String getLabelName() {
        return "TypeWriter";
    }

    @Override
    public void run() {
        String[] keys = kh.getAlphaKeys();
        if (keys.length > 0) {
            String key = keys[keys.length - 1];
            if (!key.equals(previous)) {
                if (!key.equals("Enter") && !key.equals("Backspace")) {
                    slider.bumpLeft(10);
                    previous = key;
                } else {
                    if (key.equals("Backspace")) {
                        slider.bumpRight(10);
                        previous = key;
                    } else {
                        slider.writeUntilComplete(1010);
                        previous = key;
                    }
                }

            }
        } else {
            previous = "";
        }
    }

    @Override
    public void runFirst() {
        slider.writeUntilComplete(1022);
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
        kh = new KeyHook();
        GlobalScreen.addNativeKeyListener(kh);
        GlobalScreen.addNativeMouseWheelListener(kh);
        GlobalScreen.addNativeMouseListener(kh);
        System.out.println("TypeWriter->setup()-> KeyHook setup");

        kh.addValidAlphaKey("Space");
        kh.addValidAlphaKey("Enter");
        kh.addValidAlphaKey("Backspace");
    }

    @Override
    public void teardown() {
        // TODO Auto-generated method stub

    }

}
