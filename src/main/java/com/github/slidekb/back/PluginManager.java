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

import java.util.ArrayList;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.CountDownLatch;

import com.github.slidekb.api.PlatformSpecific;
import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.api.Slider;
import com.github.slidekb.util.CurrentWorkingDirectoryClassLoader;
import com.github.slidekb.util.OsHelper;

public class PluginManager {

    private ArrayList<SlideBarPlugin> proci = new ArrayList<>();
    private CountDownLatch pluginsLoaded = new CountDownLatch(1);

    public PluginManager() {

    }
    
    /**
     * calls listFilesForFolder("\\src\\plugins").
     * adds internal process into the list and instances each.
     * 
     * @return true if successful.
     */
    protected boolean loadProcesses() {
        proci.clear();

        ServiceLoader<SlideBarPlugin> loader = ServiceLoader.load(SlideBarPlugin.class, CurrentWorkingDirectoryClassLoader.getCurrentWorkingDirectoryClassLoader());

        for (SlideBarPlugin currentImplementation : loader) {
            PlatformSpecific currentAnnotation = currentImplementation.getClass().getAnnotation(PlatformSpecific.class);

            if (currentAnnotation != null) { // Annotation present -> platform specific plugin
                if (currentAnnotation.value() == OsHelper.getOS()) {
                    currentImplementation.setAlphaKeyManager(MainBack.alphaKeyManager);
                    currentImplementation.setHotKeyManager(MainBack.hotKeyManager);
                    currentImplementation.setSliderManager(MainBack.slideMan);
//                    currentImplementation.setSlider(findSliderById(currentImplementation.currentlyUsedSlider()));
                    proci.add(currentImplementation);
                }
            } else { // No Annotation -> platform independent plugin
                currentImplementation.setAlphaKeyManager(MainBack.alphaKeyManager);
                currentImplementation.setHotKeyManager(MainBack.hotKeyManager);
                currentImplementation.setSliderManager(MainBack.slideMan);
//                currentImplementation.setSlider(findSliderById(currentImplementation.currentlyUsedSlider()));

                proci.add(currentImplementation);
            }
        }

        pluginsLoaded.countDown();
        return true;
    }
    
    public void reloadAllPluginBaseConfigs() {
    	for (SlideBarPlugin p: proci){
    		p.reloadPropFile();
    	}
    }

    public void waitUntilProcessesLoaded() throws InterruptedException {
        pluginsLoaded.await();
    }

    public ArrayList<SlideBarPlugin> getProci() {
        return proci;
    }

    protected void removeProci(boolean RemoveAll) {
        if (RemoveAll) {
            proci.removeAll(proci);
        }
    }
}
