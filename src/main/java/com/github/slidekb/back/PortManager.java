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

import javafx.collections.ObservableList;
import jssc.SerialPortList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JackSec on 4/28/2017.
 */
public class PortManager {

    private ObservableList<Object> portList;

    HashMap<String, Arduino> arduinoHash = new HashMap<String, Arduino>();

    public PortManager() {

    }

    protected boolean addArduino(String Port) {
        Arduino temp = new Arduino(Port);
        temp.initialize();
        if (temp.isConnectedAndSlider()) {
            arduinoHash.put(temp.getID(), temp);
            if (arduinoHash.size() == 1) {
                arduinoHash.put("default", temp);
            }
        }
        return temp.isConnectedAndSlider();
    }

    public Arduino getArduinoFromID(String ID) {
        return arduinoHash.get(ID);
    }

    public String[] getAllArduinoID() {
        return arduinoHash //
                .values() // Get the Collection of all values of the Map
                .stream() // Construct a stream from it
                .map(entry -> entry.getID()) // Transform all the entries in the stream
                .toArray(size -> new String[size]); // Collect the entries into an array
    }

    public Arduino[] getArduinos() {
        return arduinoHash.values().toArray(new Arduino[arduinoHash.size()]);
    }

    public void findAndConnect() {
        for (String s : getPortList(0)) {
            addArduino(s);
        }
    }

    public static String[] getPortList(int index) {
        System.out.println("Getting port list");
        return SerialPortList.getPortNames();
    }
}
