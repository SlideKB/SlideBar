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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import com.github.slidekb.api.AlphaKeyManager;
import com.github.slidekb.api.HotKeyManager;
import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.api.Slider;
import com.github.slidekb.util.NativeUtils;

public class MainBack implements Runnable {

    protected static Arduino[] arduino;

    public static ArrayList<String> prev20List = new ArrayList<String>();

    public static com.github.slidekb.back.PluginManager PM = new PluginManager();

    private static KeyHook x;

    private static boolean started = false;

    private static boolean keyHookRunning = false;

    private static String previous = "";

    static AlphaKeyManager alphaKeyManager = new AlphaKeyManagerImpl();

    static HotKeyManager hotKeyManager = new HotKeyManagerImpl();
    
    static PortManager portMan = new PortManager();

    static Slider slider = new SliderImpl();

    public static void main(String[] args) {
        Thread t = new Thread(new MainBack());
        t.start();
    }

    @Override
    public void run() {
        FirstLoad();
    }

    public static void FirstLoad() {
        loadNativeLibraries();
        setupKeyHook();
        startIt("Auto");
        
        while (started) {
            try {
                Run();
                Thread.sleep(10);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void testVibrate(int amount) {
    	for (Arduino a: arduino){
    		a.vibrate(5);
    	}
    }

    public static boolean isStarted() {
        return started;
    }

    public static boolean startIt(String port) {
        // connect and write to arduino
        if (started == false) {
//        	Arduino fake = new FakeArduino("m1n4", "COM69");
//            fake.write(400);
//            fake.bumpLeft(500);
//            fake.writeUntilComplete(1);
//            fake.writeUntilComplete(1000);
//            fake.writeUntilComplete(1);
//            fake.writeUntilComplete(1000);
//            fake.writeUntilComplete(1);
//            fake.writeUntilComplete(1000);
            System.out.println("starting plugins");
            portMan.findAndConnect();
            System.out.println(portMan.getArduinos().length);
            if (portMan.getArduinos().length != 0) {
                arduino = portMan.getArduinos();
                started = true;
            } else {
                System.out.println("Could not connect to a Slider");
                started = false;
            }
            PM.loadProcesses();
        }
        return started;
    }

    private static void updatePrevList(String given) {
        if (prev20List.contains(given)) {
            prev20List.remove(given);

        }
        prev20List.add(given);
    }

    public static String[] getPrev20() {
        return prev20List.toArray(new String[prev20List.size()]);
    }

    /**
     * Starts the process of reading the active process and choosing which
     * process in the proci list to run
     *
     * @throws Throwable
     */
    public static void Run() throws Throwable {
        int counter = 0;
        String previous = "";
        while (started) {
            boolean exe = true;
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }
            String activeProcess = ActiveProcess.getProcess();
            String hotKeys = Arrays.toString(KeyHook.getHotKeys());
            // System.out.println(ard.read());
            // System.out.println(AP);
            for (SlideBarPlugin p : PM.getProci()) {
                for (String processName : p.getProcessNames()) {
                    if (processName.contentEquals(hotKeys)) {
                        exe = false;
                    }
                }
            }
            if (!exe) {
                if (!previous.equals(hotKeys)) {
                    for (SlideBarPlugin p : PM.getProci()) {
                        for (String processName : p.getProcessNames()) {
                            if (processName.contentEquals(hotKeys)) {
                                System.out.println("process change");
                                previous = processName;
                                p.runFirst(processName);
                            }
                        }
                    }
                } else {
                    for (SlideBarPlugin p : PM.getProci()) {
                        for (String processName : p.getProcessNames()) {
                            if (processName.contentEquals(hotKeys)) {
                                previous = processName;
                                p.run(processName);
                            }
                        }
                    }
                }
            } else {
                if (!previous.equals(activeProcess)) {
                    updatePrevList(activeProcess);
                    for (SlideBarPlugin p : PM.getProci()) {
                        for (String processName : p.getProcessNames()) {
                            if (processName.contentEquals(activeProcess)) {
                                System.out.println("process change");
                                previous = processName;
                                p.runFirst(processName);
                            }
                        }
                    }
                } else {
                    for (SlideBarPlugin p : PM.getProci()) {
                        for (String processName : p.getProcessNames()) {
                            if (processName.contentEquals(activeProcess)) {
                                previous = processName;
                                p.run(processName);

                            }
                        }
                    }
                }
            }

            if (exe) {
                previous = activeProcess;
            } else {
                previous = hotKeys;
            }

        }
    }

    public static void setupKeyHook() {
        // setup keyhook
        try {
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            GlobalScreen.registerNativeHook();
            keyHookRunning = true;
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        x = new KeyHook();
        GlobalScreen.addNativeKeyListener(x);
    }

    public static void loadNativeLibraries() {
        try {
            NativeUtils.loadLibraryFromJar("/util/rxtxParallel.dll");
            NativeUtils.loadLibraryFromJar("/util/rxtxSerial.dll");
        } catch (IOException e) {
            System.out.println("Unable to load rxtx native libraries");
        }
    }

    /**
     * stops the run() function disconnects arduino removes all process from the
     * process list.
     * 
     * @return
     */
    public static Boolean stop() {
        System.out.println("stopping");
        for(Arduino a: arduino){
        	a.close();
        }
        PM.removeProci(true);
        started = false;
        return true;
    }
}
