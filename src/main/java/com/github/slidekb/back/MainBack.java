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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import com.github.slidekb.api.AlphaKeyManager;
import com.github.slidekb.api.HotKeyManager;
import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.api.Slider;

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

	static SliderManagerImpl slideMan = new SliderManagerImpl();

	// TODO remove this
	public static Map<String, Slider> sliders = new HashMap<>();

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
		while (started) {
			try {
				Run();
				Thread.sleep(10);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	// TODO move this to a class that makes more sense
	/**
	 * vibrates each connected SlideBar
	 * 
	 * @param amount
	 */
	public static void testVibrate(int amount) {
		sliders.forEach((String, Arduino) -> Arduino.vibrate(5));
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
	 * Finds and connects to all connected SlideBars, adds them all to the
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
			slideMan.hashTheSlideBars();
			// TODO this should be moved to the portManager class
			System.out.println("Number of sliders connected: " + portMan.getArduinos().size());
			started = true;
			PM.loadProcesses();
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

	//TODO can this be moved to a different class?
	public static String[] getPrev20() {
		return prev20List.toArray(new String[prev20List.size()]);
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

	//TODO add Mac OS support
	/**
	 * sets up the keyhook.
	 *
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

	/**
	 * stops the run() function disconnects arduino removes all process from the
	 * process list.
	 * 
	 * @return
	 */
	public static Boolean stop() {
		System.out.println("stopping");
		// for (Slider s : sliders.values()) {
		// s.close();
		// }
		slideMan.closeAll();
		// TODO Move this to the SliderManager class
		PM.removeProci(true);
		started = false;
		return true;
	}
}
