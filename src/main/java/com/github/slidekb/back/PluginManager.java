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
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.ServiceLoader;

import com.github.slidekb.api.PlatformSpecific;
import com.github.slidekb.api.PluginVersion;
import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.api.Slider;
import com.github.slidekb.util.CurrentWorkingDirectoryClassLoader;
import com.github.slidekb.util.Log;
import com.github.slidekb.util.OsHelper;
import com.github.slidekb.util.ResettableCountDownLatch;
import com.github.slidekb.util.SettingsHelper;

public class PluginManager {

    private ArrayList<SlideBarPlugin> proci = new ArrayList<>();
    private ResettableCountDownLatch pluginsLoaded = new ResettableCountDownLatch(1);
    private ServiceLoader<SlideBarPlugin> loader;
    private URLClassLoader currentClassLoader;

    public PluginManager() {

    }

    /**
     * Adds plugins into the list and instances each.
     * 
     * @return true if successful.
     */
    protected boolean loadProcesses(int programVersion) {
        pluginsLoaded.reset();

        unloadPlugins();

        if (currentClassLoader != null) {
            try {
                currentClassLoader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        currentClassLoader = CurrentWorkingDirectoryClassLoader.getCurrentWorkingDirectoryClassLoader();
        loader = ServiceLoader.load(SlideBarPlugin.class, currentClassLoader);

        for (SlideBarPlugin currentImplementation : loader) {
            PluginVersion currentVersion = currentImplementation.getClass().getAnnotation(PluginVersion.class);

            if (currentVersion == null) {
                System.out.println("PluginManager->loadProcesses()-> Found plugin " + currentImplementation.getClass().getCanonicalName() + " but it has no version annotation! Skipping.");
                continue;
            } else if (currentVersion.value() != programVersion) {
                System.out.println("PluginManager->loadProcesses()-> Found plugin " + currentImplementation.getClass().getCanonicalName() + " but its version " + currentVersion.value() + " doesn't match program version " + programVersion + "! Skipping.");
                continue;
            } else {
                PlatformSpecific currentOsAnnotation = currentImplementation.getClass().getAnnotation(PlatformSpecific.class);

                if (currentOsAnnotation != null) { // Annotation present -> platform specific plugin
                    if (currentOsAnnotation.value() == OsHelper.getOS()) {
                        System.out.println("PluginManager->loadProcesses()-> Loading platform dependant plugin " + currentImplementation.getClass().getCanonicalName() + " for platform " + OsHelper.getOS());
                    } else {
                        continue;
                    }
                } else { // No Annotation -> platform independent plugin
                    System.out.println("PluginManager->loadProcesses()-> Loading platform independant plugin " + currentImplementation.getClass().getCanonicalName());
                    Log.logMessage("PluginManager->loadProcesses()-> Loading platform independant plugin " + currentImplementation.getClass().getCanonicalName());
                }
            }

            int totalSliders = currentImplementation.numberOfSlidersRequired();
            Slider usedSlider;

            for (int i = 0; i < totalSliders; i++) {
                String sliderID = SettingsHelper.getUsedSliderAtIndex(currentImplementation.getClass().getCanonicalName(), i);

                if (sliderID == null) {
                    usedSlider = MainBack.getSliderManager().getDefaultSliderByIndex(i);
                } else {
                    usedSlider = MainBack.getSliderManager().getSliderByID(sliderID);

                    if (usedSlider == null) {
                        usedSlider = MainBack.getSliderManager().getDefaultSliderByIndex(i);
                    }
                }

                currentImplementation.setSlider(usedSlider, i);
            }

            currentImplementation.setup();
            proci.add(currentImplementation);
        }

        pluginsLoaded.countDown();
        return true;
    }

    public void waitUntilProcessesLoaded() throws InterruptedException {
        pluginsLoaded.await();
    }

    public ArrayList<SlideBarPlugin> getProci() {
        return proci;
    }

    protected void unloadPlugins() {
        proci.forEach(plugin -> plugin.teardown());
        proci.clear();
    }

    protected void removeProci(boolean RemoveAll) {
        if (RemoveAll) {
            proci.clear();
        }
    }
}
