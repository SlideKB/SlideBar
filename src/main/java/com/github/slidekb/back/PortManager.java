package com.github.slidekb.back;

import gnu.io.CommPortIdentifier;

import java.util.ArrayList;

/**
 * Created by JackSec on 4/28/2017.
 */
public class PortManager {

    private ArrayList<Arduino> arduinos = new ArrayList<>();

    public PortManager(){

    }

    protected boolean addArduino(String Port){
        Arduino temp = new Arduino(Port);
        temp.initialize();
        if (temp.isConnectedAndSlider()){
            arduinos.add(temp);
        }
        return temp.isConnectedAndSlider();
    }

    public Arduino[] getArduinos(){
        return arduinos.toArray(new Arduino[arduinos.size()]);
    }

    public void findAndConnect(){
        for (String s: getPortList(0)){
            addArduino(s);

        }
    }

    public static String[] getPortList(int index) {
        ArrayList<String> temp = new ArrayList<String>();

        @SuppressWarnings("unchecked")
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();

        int counter = 0;
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            if (getPortTypeName(portIdentifier.getPortType()) == "Serial") {
                if (counter == index) {
                    temp.add(portIdentifier.getName());
                } else {
                    counter++;
                }

            }

        }
        return temp.toArray(new String[temp.size()]);
    }

    private static String getPortTypeName(int portType) {
        switch (portType) {
            case CommPortIdentifier.PORT_I2C:
                return "I2C";
            case CommPortIdentifier.PORT_PARALLEL:
                return "Parallel";
            case CommPortIdentifier.PORT_RAW:
                return "Raw";
            case CommPortIdentifier.PORT_RS485:
                return "RS485";
            case CommPortIdentifier.PORT_SERIAL:
                return "Serial";
            default:
                return "unknown type";
        }
    }
}
