package com.github.slidekb.ifc.resources;

import javax.ws.rs.Path;

import io.swagger.annotations.Api;

@Api
@Path("api")
public class RootResource {

    @Path("/slider")
    public SliderResource sliders() {
        return new SliderResource();
    }

    @Path("/plugin")
    public PluginResource plugins() {
        return new PluginResource();
    }
}
