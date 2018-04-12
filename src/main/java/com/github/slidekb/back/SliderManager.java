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
import java.util.HashMap;
import java.util.Map;

import com.github.slidekb.api.Slider;

/**
 * @author JackSB The purpose of SliderManager is to manage each instance of the
 *         slider classes. The SliderManager is available to each plugin for
 *         easy access to the default slider, the next default slider, the next
 *         default slider and finally the next default slider (4 sliders
 *         supported). The SliderManager also has a hash map with keys that are
 *         the SlideBar ID.
 */
public class SliderManager {

    public SliderManager() {

    }

    /**
     * hash map of sliders
     */
    public Map<String, Slider> sliders = new HashMap<>();

    /**
     * Add each connected SlideBar to the hash of sliders and set each slider to
     * default starting from the first slider and going in order of port value
     * (first slider is default, second slider is default2, third slider is
     * default3, and forth slider is default4)
     */
    public void hashTheSlideBars() {
        MainBack.portMan.getArduinos().forEach((id, arduino) -> sliders.put(id, new SliderImpl(arduino)));
        // TODO remove use of arraylist (don't know how I would do that though)
        ArrayList<Arduino> tempArduinos = new ArrayList<>();
        MainBack.portMan.getArduinos().forEach((String, arduino) -> {
            tempArduinos.add(arduino);
        });
        int count = 1;
        for (Arduino a : tempArduinos) {
            if (count != 1) {
                sliders.put("default" + count++, new SliderImpl(a));
            } else {
                sliders.put("default", new SliderImpl(a));
                count++;
            }
        }
        // print out the hash map keys for the sliders including default sliders
        sliders.forEach((String, arduino) -> System.out.println("SliderManager->hashTheSlideBars()-> Slider added to hash: " + String + " => " + arduino.getID()));
    }

    /**
     * To be used by the UI to configure which sliders are in what
     * default
     *
     * @param ID
     * @return true if successful
     */
    public boolean setDefaultSlider1(String ID) {
        Slider temp = getSliderByID(ID);
        if (temp != null) {
            sliders.put("default", getSliderByID(ID));
            return true;
        } else {
            // TODO I don't know what we should do if there isn't a slider
            // connected
        }
        return false;
    }

    /**
     * To be used by the UI to configure which sliders are in what
     * default
     *
     * @param ID
     * @return true if successful
     */
    public boolean setDefaultSlider2(String ID) {
        Slider temp = getSliderByID(ID);
        if (temp != null) {
            sliders.put("default2", getSliderByID(ID));
            return true;
        } else {
            // TODO I don't know what we should do if there isn't a slider
            // connected
        }
        return false;
    }

    /**
     * To be used by the UI to configure which sliders are in what
     * default
     *
     * @param ID
     * @return true if successful
     */
    public boolean setDefaultSlider3(String ID) {
        Slider temp = getSliderByID(ID);
        if (temp != null) {
            sliders.put("default3", getSliderByID(ID));
            return true;
        } else {
            // TODO I don't know what we should do if there isn't a slider
            // connected
        }
        return false;
    }

    /**
     * To be used by the UI to configure which sliders are in what
     * default
     *
     * @param ID
     * @return true if successful
     */
    public boolean setDefaultSlider4(String ID) {
        Slider temp = getSliderByID(ID);
        if (temp != null) {
            sliders.put("default4", getSliderByID(ID));
            return true;
        } else {
            // TODO I don't know what we should do if there isn't a slider
            // connected
        }
        return false;
    }

    /**
     * closes all connections, and TODO remove the sliders from the hashmap.
     */
    public void closeAll() {
        try {
            MainBack.portMan.getArduinos().forEach((String, arduino) -> arduino.close());
        } catch (Exception e) {

        }
        System.out.println("closing success");
        sliders.forEach((String, Slider) -> sliders.remove(Slider));
    }

    public Slider getSliderByID(String ID) {
        return sliders.get(ID);
    }

    public Slider getDefaultSlider() {
        return sliders.get("default");
    }

    public Slider getDefaultSlider1() {
        return getDefaultSlider();
    }

    public Slider getDefaultSlider2() {
        return sliders.get("default2");
    }

    public Slider getDefaultSlider3() {
        return sliders.get("default3");
    }

    public Slider getDefaultSlider4() {
        return sliders.get("default4");
    }

    public Slider getDefaultSliderByIndex(int index) {
        if (index == 0) {
            return getDefaultSlider();
        }
        if (index == 1) {
            return getDefaultSlider2();
        }
        if (index == 2) {
            return getDefaultSlider3();
        }
        if (index == 4) {
            return getDefaultSlider4();
        }
        return getDefaultSlider();
    }

    public String[] getSliderIDList() {
        ArrayList<String> temp = new ArrayList<String>();
        sliders.forEach((String, arduino) -> temp.add(String));
        if (temp.contains("default")) {
            temp.remove("default");
        }
        if (temp.contains("default2")) {
            temp.remove("default2");
        }
        if (temp.contains("default3")) {
            temp.remove("default3");
        }
        if (temp.contains("default4")) {
            temp.remove("default4");
        }
        return temp.toArray(new String[temp.size()]);
    }

}
