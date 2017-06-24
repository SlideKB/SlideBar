package com.github.slidekb.ifc.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.github.slidekb.back.MainBack;
import com.github.slidekb.ifc.types.IfcSlider;

public class SliderResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<IfcSlider> getAllSliders() {
        List<IfcSlider> sliders = new ArrayList<>();

        MainBack.getSliderManager().sliders.forEach((id, slider) -> {
            sliders.add(new IfcSlider(slider.getID()));
        });

        return sliders;
    }
}
