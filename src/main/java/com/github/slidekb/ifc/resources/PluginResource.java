package com.github.slidekb.ifc.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.github.slidekb.back.MainBack;
import com.github.slidekb.ifc.types.IfcAssociation;
import com.github.slidekb.ifc.types.IfcPlugin;

public class PluginResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<IfcPlugin> getAllPlugins() {
        List<IfcPlugin> plugins = new ArrayList<>();

        return plugins;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("associate")
    public void associatePlugin(IfcAssociation association) {
        System.out.println(association);
    }
}
