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

import java.util.HashMap;
import java.util.Map;

import jssc.SerialPortList;

/**
 * Created by JackSec on 4/28/2017. <br>
 * The purpose of <b>PortManager</b> is to help with managing everything that is
 * a serial Port. The <code><b>findAndConnect()</b></code> method will find all
 * SlideBars and open their ports. The PortManager keeps a hash map of each
 * connected SlideBar using the SlideBar ID's for the keys. PortManager also
 * creates a fake SlideBar accessible with the key ID of "No Sliders Connected" if no SlideBars
 * are found.
 */
public class PortManager {

    /**
     * Hash map of successfully connected SlideBars (technically they are
     * arduinos)
     */
    Map<String, Arduino> arduinos = new HashMap<String, Arduino>();

    public PortManager() {

    }

    /**
     * Attempts to connect to a SlideBar from given port. If successful, add the
     * SlideBar to the hash map.
     *
     * @param port
     * @return true is successful
     */
    protected boolean addArduino(String port) {
        Arduino currentArduino = new Arduino(port);
        currentArduino.initialize();

        // if PortManager has found a valid SlideBar (an arduino that returns an
        // ID)
        if (currentArduino.isConnectedAndSlider()) {
            arduinos.put(currentArduino.getID(), currentArduino);
        }

        return currentArduino.isConnectedAndSlider();
    }

    /**
     * the Arduino that matches the ID
     * 
     * @Return the Arduino that matches the ID
     */
    public Arduino getArduinoFromID(String ID) {
        return arduinos.get(ID);
    }

    /**
     * not sure what this does TODO pureSpider add doc please
     */
    public String[] getAllArduinoID() {
        return arduinos //
                .values() // Get the Collection of all values of the Map
                .stream() // Construct a stream from it
                .map(entry -> entry.getID()) // Transform all the entries in the
                                             // stream
                .toArray(size -> new String[size]); // Collect the entries into
                                                    // an array
    }

    /**
     * returns the hash map of the connected arduinos
     * 
     * @return Hash map
     */
    public Map<String, Arduino> getArduinos() {
        return arduinos;
    }

    /**
     * For every serial device connected to the computer, attempt to connect to
     * it and add it to the hash map. If no sliders are connected, create a fake
     * arduino and add that instead.
     */
    public void findAndConnect() {
        // for each port that it finds, try and connect.
        for (String s : getPortList(0)) {
            addArduino(s);
        }

        // If it still doesn't have a default, no physical Arduinos are
        // connected then use a fake one
        if (arduinos.isEmpty()) {
            System.out.println("PortManager->findAndConnect()-> creating fake arduino");
            arduinos.put("No Sliders Connected", new FakeArduino("No Sliders Connected", "COM69"));
        }
    }

    /**
     * returns the list of all connected serial devices
     */
    public static String[] getPortList(int index) {
        System.out.println("PortManager->getPortList()-> Getting port list");
        return SerialPortList.getPortNames();
    }
}
