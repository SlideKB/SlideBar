package com.github.slidekb.ifc.types;

public class IfcPlugin {
    public String name;
    public String currentlyUsedSlider;

    public IfcPlugin() {
    }

    public IfcPlugin(String name, String currentlyUsedSlider) {
        this.name = name;
        this.currentlyUsedSlider = currentlyUsedSlider;
    }
}
