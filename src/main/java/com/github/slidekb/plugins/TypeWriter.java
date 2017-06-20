package com.github.slidekb.plugins;

import javax.swing.JFrame;

import com.github.slidekb.api.AlphaKeyManager;
import com.github.slidekb.api.HotKeyManager;
import com.github.slidekb.api.PluginVersion;
import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.api.Slider;
import com.github.slidekb.api.SliderManager;
import com.google.auto.service.AutoService;

@PluginVersion(1)
@AutoService(SlideBarPlugin.class)
public class TypeWriter implements SlideBarPlugin {

    private SliderManager sliderManager;
    private Slider slider;
    private AlphaKeyManager akm;

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String[] getProcessNames() {
        String[] test = { "notepad.exe" };
        return test;
    }

    @Override
    public boolean usesProcessNames() {
        return true;
    }

    @Override
    public String getLabelName() {
        return "Type Writer";
    }

    @Override
    public void run(String process) {
    }

    @Override
    public void runFirst(String process) {
        slider.writeUntilComplete(1000);
        slider.writeUntilComplete(100);
    }

    @Override
    public JFrame getConfigWindow() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JFrame getProcessWindow() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void reloadPropFile() {
        System.out.println("reloading prop file");
    }

    @Override
    public void setAlphaKeyManager(AlphaKeyManager alphaKeyManager) {
        this.akm = alphaKeyManager;
    }

    @Override
    public void setHotKeyManager(HotKeyManager hotKeyManager) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSliderManager(SliderManager sliderManager) {
        this.sliderManager = sliderManager;
        this.slider = sliderManager.getSliderByID("default");

    }

}
