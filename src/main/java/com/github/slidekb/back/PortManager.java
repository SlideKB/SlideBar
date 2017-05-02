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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jssc.SerialPortList;

/**
 * Created by JackSec on 4/28/2017.
 */
public class PortManager {

    Map<String, Arduino> arduinos = new HashMap<String, Arduino>();

    public PortManager() {

    }

    protected boolean addArduino(String port) {
        Arduino currentArduino = new Arduino(port);
        System.out.println("before initialize()");
        currentArduino.initialize();
        System.out.println("after initialize()");

        if (currentArduino.isConnectedAndSlider()) {
            if (arduinos.isEmpty()) {
                arduinos.put("default", currentArduino);
            }

            arduinos.put(currentArduino.getID(), currentArduino);
        }

        return currentArduino.isConnectedAndSlider();
    }

    public Arduino getArduinoFromID(String ID) {
        return arduinos.get(ID);
    }

    public String[] getAllArduinoID() {
        return arduinos //
                .values() // Get the Collection of all values of the Map
                .stream() // Construct a stream from it
                .map(entry -> entry.getID()) // Transform all the entries in the stream
                .toArray(size -> new String[size]); // Collect the entries into an array
    }

    public Map<String, Arduino> getArduinos() {
        return arduinos;
    }

    public void findAndConnect() {
        for (String s : getPortList(0)) {
            addArduino(s);
        }

        // If it still doesn't have a default, no physical Arduinos are connected - use a fake one then
        if (arduinos.isEmpty()) {
            System.out.println("creating fake arduino");
            arduinos.put("default", new FakeArduino("f1n1", "COM69"));
        }
    }

    public static String[] getPortList(int index) {
        System.out.println("Getting port list");
        return SerialPortList.getPortNames();
    }
}
