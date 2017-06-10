package com.github.slidekb.ifc.types;

public class IfcAssociation {
    public String sliderID;
    public String pluginID;

    public IfcAssociation() {
    }

    public IfcAssociation(String sliderID, String pluginID) {
        this.sliderID = sliderID;
        this.pluginID = pluginID;
    }
}
