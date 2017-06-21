/**
 Copyright 2017 John Kester (Jack Kester)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.github.slidekb.back;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import com.github.slidekb.api.AlphaKeyManager;
import com.github.slidekb.api.HotKeyManager;
import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.back.settings.GlobalSettings;
import com.github.slidekb.back.settings.PluginSettings;
import com.github.slidekb.ifc.resources.RootResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

public class MainBack implements Runnable {

    // TODO decide if this and relating operations on it should be moved to a
    // different class.
    /**
     * The previous 20 unique executables that have been visited. TODO move this
     * to a different class that makes more sense.
     */
    public static ArrayList<String> prev20List = new ArrayList<String>();

    public static PluginManager PM = new PluginManager();

    // TODO rename to a more meaningful name
    private static KeyHook x;

    /**
     * boolean that expresses whether the backend has been started or not / is
     * currently running.
     */
    private static boolean started = false;

    static AlphaKeyManager alphaKeyManager = new AlphaKeyManagerImpl();

    static HotKeyManager hotKeyManager = new HotKeyManagerImpl();

    static PortManager portMan = new PortManager();

    private static SliderManagerImpl slideMan = new SliderManagerImpl();

    private static GlobalSettings settings;

    /**
     * For running without an Interface. creates a new thread and starts it
     * 
     * @param args
     */
    public static void main(String[] args) {
        Thread t = new Thread(new MainBack());
        t.start();
    }

    @Override
    public void run() {
        FirstLoad();
    }

    /**
     * runs when MainBack and MainFront are first loaded. (I think, TODO double
     * check that)
     */
    public static void FirstLoad() {
        setupKeyHook();
        startIt();
        startRestServer();

        while (started) {
            try {
                Run();
                Thread.sleep(10);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static HttpServer startRestServer() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setTitle("SlideBar REST Interface");
        beanConfig.setResourcePackage(RootResource.class.getPackage().getName());
        beanConfig.setSchemes(new String[] { "http" });
        beanConfig.setHost("localhost:5055");
        beanConfig.setScan(true);

        final ResourceConfig rc = new ResourceConfig();
        rc.packages(RootResource.class.getPackage().getName());
        rc.register(LoggingFeature.class);
        rc.register(JacksonFeature.class);
        rc.register(ApiListingResource.class);
        rc.register(SwaggerSerializers.class);
        rc.property(ServerProperties.WADL_FEATURE_DISABLE, true);

        Logger l = Logger.getLogger("org.glassfish.grizzly.http.server.HttpHandler");
        l.setLevel(Level.FINE);
        l.setUseParentHandlers(false);
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.ALL);
        l.addHandler(ch);

        try {
            return GrizzlyHttpServerFactory.createHttpServer(new URI("http://localhost:5055"), rc);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    // TODO move this to a class that makes more sense
    /**
     * vibrates each connected SlideBar
     * 
     * @param amount
     */
    public static void testVibrate(int amount) {
        getSlideMan().sliders.forEach((String, Arduino) -> Arduino.vibrate(5));
    }

    /**
     * Is the backend running?
     * 
     * @return started
     */
    public static boolean isStarted() {
        return started;
    }

    // TODO rename to something more meaningful than StartIt()
    /**
     * Finds and connects to all connected SlideBarsx, adds them all to the
     * slider hash map, creates the defaults in the slider hash map, prints out
     * the number of connected SlideBars, and loads the plugins.
     * 
     * @return
     */
    public static boolean startIt() {
        // connect and write to arduino
        if (started == false) {
            System.out.println("Discovering Arduinos");
            // find and connect to all the SlideBars
            portMan.findAndConnect();
            // Add the SlideBars to the Hash map.
            getSlideMan().hashTheSlideBars();
            // TODO this should be moved to the portManager class
            System.out.println("Number of sliders connected: " + portMan.getArduinos().size());
            started = true;
            PM.loadProcesses(1);

            Gson gson = new GsonBuilder().create();
            try {
                File settingsFile = new File("settings.json");
                settingsFile.createNewFile();

                // try (Writer writer = new FileWriter(settingsFile)) {
                // PluginSettings sObj = new PluginSettings();
                // sObj.setUsedSlider("f1n1");
                // sObj.getProcesses().add("explorer.exe");
                // sObj.getHotkeys().add("alt");
                //
                // GlobalSettings gObj = new GlobalSettings();
                // gObj.getPlugins().put(AltProcess.class.getCanonicalName(), sObj);
                // gObj.getSliders().put("f1n1", new SliderSettings());
                //
                // gson.toJson(gObj, writer);
                // }

                try (Reader reader = new FileReader(settingsFile)) {
                    settings = gson.fromJson(reader, GlobalSettings.class);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return started;
    }

    // TODO can this be moved to a different class?
    private static void updatePrevList(String given) {
        if (prev20List.contains(given)) {
            prev20List.remove(given);
        }
        prev20List.add(given);
    }

    // TODO can this be moved to a different class?
    public static String[] getPrev20() {
        return prev20List.toArray(new String[prev20List.size()]);
    }

    public static SliderManagerImpl getSlideMan() {
        return slideMan;
    }

    public static GlobalSettings getSettings() {
        return settings;
    }

    public static void setSlideMan(SliderManagerImpl slideMan) {
        MainBack.slideMan = slideMan;
    }

    // TODO rename Run() to something other than Run to avoid confusion
    // TODO this is a cluster fuck
    /**
     * Starts the process of reading the active process and choosing which
     * process in the proci list to run
     *
     * @throws Throwable
     */
    public static void Run() throws Throwable {
        boolean changed = false;
        boolean runThisPlugin = false;

        while (started && !PM.getProci().isEmpty()) {
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }

            String activeProcess = ActiveProcess.getProcess();
            String hotKeys = Arrays.toString(KeyHook.getHotKeys());

            String previousActiveProcess = null;
            String previousHotKeys = null;

            if ((previousActiveProcess != activeProcess) || (previousHotKeys != hotKeys)) {
                updatePrevList(activeProcess);
                getSlideMan().sliders.forEach((String, Arduino) -> Arduino.removeParts());

                changed = true;
            }

            for (SlideBarPlugin plugin : PM.getProci()) {
                String pluginID = plugin.getClass().getCanonicalName();

                if (settings.getPlugins().containsKey(pluginID)) {
                    PluginSettings pluginSettings = settings.getPlugins().get(pluginID);

                    runThisPlugin = pluginSettings.isAlwaysRun() || ((pluginSettings.getHotkeys().isEmpty() || pluginSettings.getHotkeys().contains(hotKeys)) && (pluginSettings.getProcesses().isEmpty() || pluginSettings.getProcesses().contains(activeProcess)));

                    // If both lists are empty, only run when the "always run" flag is set
                    if (!pluginSettings.isAlwaysRun() && pluginSettings.getHotkeys().isEmpty() && pluginSettings.getProcesses().isEmpty()) {
                        runThisPlugin = false;
                    }

                    if (runThisPlugin) {
                        if (changed) {
                            plugin.runFirst();
                        } else {
                            plugin.run();
                        }
                    }
                }
            }

            previousActiveProcess = activeProcess;
            previousHotKeys = hotKeys;
        }
    }

    // TODO add Mac OS support
    /**
     * sets up the keyhook.
     */
    public static void setupKeyHook() {
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
        x = new KeyHook();
        GlobalScreen.addNativeKeyListener(x);
    }

    // TODO
    /**
     * TODO move to sliderManager class
     * reconnect to the sliders without reloading plugins. maybe make plugins not run while reconnecting to prevent
     * issues
     * 
     * @return
     */
    public static Boolean reconnect() {
        // TODO

        return true;
    }

    /**
     * stops the run() function disconnects arduino removes all process from the
     * process list.
     * 
     * @return
     */
    public static Boolean stop() {
        System.out.println("stopping");
        getSlideMan().closeAll();
        // TODO decide if this needs to move to the Move this to the SliderManager class
        PM.removeProci(true);
        started = false;
        return true;
    }
}
