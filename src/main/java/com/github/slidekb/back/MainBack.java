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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.ifc.resources.RootResource;
import com.github.slidekb.util.Log;
import com.github.slidekb.util.SettingsHelper;

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

    public static PluginManager pluginMan = new PluginManager();
    
    // TODO rename to a more meaningful name
    private static KeyHook x;

    /**
     * boolean that expresses whether the backend has been started or not / is
     * currently running.
     */
    private static boolean started = false;

    static AlphaKeyManager alphaKeyManager = new AlphaKeyManager();

    static HotKeyManager hotKeyManager = new HotKeyManager();

    static PortManager portMan = new PortManager();

    private static SliderManager slideMan = new SliderManager();

    // /**
    // * For running without an Interface. creates a new thread and starts it
    // *
    // * @param args
    // */
    // public static void main(String[] args) {
    // Thread t = new Thread(new MainBack());
    // t.start();
    // }

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
//        startRestServer();
        while (true) {
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
        getSliderManager().sliders.forEach((String, Arduino) -> Arduino.vibrate(5));
        getSliderManager().sliders.forEach((String, Arduino) -> Arduino.writeUntilComplete(512));
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
            System.out.println("MainBack->startIt()-> Discovering Arduinos");
            Log.logMessage("MainBack->startIt()-> Discovering Arduinos");
            // find and connect to all the SlideBars
            portMan.findAndConnect();
            // SettingsHelper.setSliderList("com.github.slidekb.plugins.TypeWriter", new String[] {"m1n3", "l1n1"});
            // Add the SlideBars to the Hash map.
            getSliderManager().hashTheSlideBars();
            // TODO this should be moved to the portManager class
            System.out.println("MainBack->startIt()-> Number of sliders connected: " + portMan.getArduinos().size());
            Log.logMessage("MainBack->startIt()-> Number of sliders connected: " + portMan.getArduinos().size());
            pluginMan.loadProcesses(1);
            started = true;
        }
        return started;
    }

    // TODO can this be moved to a different class?
    private static void updatePrevList(String given) {
        prev20List.remove(given);
        prev20List.add(given);
    }

    // TODO can this be moved to a different class?
    public static String[] getPrev20() {
        return prev20List.toArray(new String[prev20List.size()]);
    }

    public static SliderManager getSliderManager() {
        return slideMan;
    }

    public static void setSlideMan(SliderManager slideMan) {
        MainBack.slideMan = slideMan;
    }

    /**
     * Starts the process of reading the active process and choosing which
     * process in the proci list to run
     *
     * @throws Throwable
     */
    public static void Run() throws Throwable {
        boolean activeProcessChanged = false;
        boolean hotKeysChanged = false;
        boolean runThisPlugin = false;
        boolean hotkeysUsed = false;
        String previousActiveProcess = "";
        String previousHotKeys = "";
        String previousPlugin = "";

        List<String> hotkeyList = null;
        List<String> processList = null;
        boolean alwaysRun = false;

        while (started && !pluginMan.getProci().isEmpty()) {
            try {
                Thread.sleep(1);
            } catch (Exception e) {

                System.out.println("MainBack->Run()-> error thrown run 1");
                Log.logMessage("MainBack->Run()-> error thrown run 1");
            }
            
            hotkeysUsed = false;
            String activeProcess = ActiveProcess.getProcess().toLowerCase();

            String[] hotKeysArray = KeyHook.getHotKeys();
            int numHotKeys = hotKeysArray.length;
            String hotKeys = Arrays.toString(hotKeysArray);

            // Use copy of the list to avoid ConcurrentModificationException on plugin reload
            List<SlideBarPlugin> currentPlugins = new ArrayList<>(pluginMan.getProci());

            if (!previousActiveProcess.equals(activeProcess)) {
                updatePrevList(activeProcess);
                previousActiveProcess = activeProcess;
                activeProcessChanged = true;
                if(activeProcess.length() > 0){
                    System.out.println("MainBack->Run()-> PROCESS CHANGED, is now: " + activeProcess);
                    Log.logMessage("MainBack->Run()-> PROCESS CHANGED, is now: " + activeProcess);
                }
            }

            if (!previousHotKeys.equals(hotKeys)) {
                previousHotKeys = hotKeys;
                hotKeysChanged = true;
                if(hotKeys.length() > 0){
                	System.out.println("MainBack->Run()-> HOTKEYS CHANGED, is now: " + hotKeys);
                	Log.logMessage("MainBack->Run()-> HOTKEYS CHANGED, is now: " + hotKeys);
                }
            }
            if (currentPlugins.size() != 0){
            	// First, check if the current hotkeys are used in ANY plugin
                for (SlideBarPlugin plugin : currentPlugins) {
                    String pluginID = plugin.getClass().getCanonicalName();

                    if (SettingsHelper.isPluginKnown(pluginID)) {
                        hotkeyList = SettingsHelper.listHotkeys(pluginID);

                        if (hotkeyList.contains(hotKeys)) {
                            hotkeysUsed = true;
                            break;
                        }
                    }
                }

                for (SlideBarPlugin plugin : currentPlugins) {
                    String pluginID = plugin.getClass().getCanonicalName();

                    if (SettingsHelper.isPluginKnown(pluginID)) {
                        hotkeyList = SettingsHelper.listHotkeys(pluginID);
                        processList = SettingsHelper.listProcesses(pluginID);
                        alwaysRun = SettingsHelper.isAlwaysRun(pluginID);

                        if (alwaysRun) {
                            runThisPlugin = true;
                        } else if (hotkeyList.isEmpty() && processList.isEmpty()) {
                            runThisPlugin = false;
                        } else if (hotkeyList.isEmpty() && numHotKeys > 0 && hotkeysUsed) {
                            runThisPlugin = false;
                        } else if ((hotkeyList.isEmpty() || hotkeyList.contains(hotKeys)) && (processList.isEmpty() || processList.contains(activeProcess))) {
                            runThisPlugin = true;
                        }

                        if (runThisPlugin) {
                            if (previousPlugin != plugin.getClass().getCanonicalName()) {
                                previousPlugin = plugin.getClass().getCanonicalName();
                                getSliderManager().sliders.forEach((String, Arduino) -> Arduino.removeParts());
                            }
                            if (activeProcessChanged && !processList.isEmpty()) {
                                plugin.runFirst();
                                System.out.println("MainBack->Run()-> Running Plugin: " + plugin.getLabelName());
                                Log.logMessage("MainBack->Run()-> Running Plugin: " + plugin.getLabelName());
                            } else if (hotKeysChanged && !hotkeyList.isEmpty()) {
                                plugin.runFirst();
                                System.out.println("MainBack->Run()-> Running Plugin: " + plugin.getLabelName());
                                Log.logMessage("MainBack->Run()-> Running Plugin: " + plugin.getLabelName());
                            } else {
                                plugin.run();
                            }
                        }

                        runThisPlugin = false;
                    }
                }

                activeProcessChanged = false;
                hotKeysChanged = false;
            } else {
            	System.err.println("MainBack->Run()-> No plugins are found to be loaded");
            	Log.logMessage("MainBack->Run()-> No plugins are found to be loaded");
            }
            
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
//            System.err.println(ex.getMessage());
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
    	started=false;
        System.out.println("MainBack->stop()-> stopping");
        Log.logMessage("MainBack->stop()-> stopping");
        getSliderManager().closeAll();
        // TODO decide if this needs to move to the Move this to the SliderManager class
        pluginMan.unloadPlugins();
        started = false;
        return true;
    }
}
