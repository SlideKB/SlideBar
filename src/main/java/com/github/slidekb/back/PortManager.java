package com.github.slidekb.back;

import javafx.collections.ObservableList;
import jssc.SerialPortList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JackSec on 4/28/2017.
 */
public class PortManager {

    private ArrayList<Arduino> arduinos = new ArrayList<>();
    
	private ObservableList<Object> portList;
	
	HashMap<String, Arduino> arduinoHash = new HashMap<String, Arduino>();

    public PortManager(){

    }

    protected boolean addArduino(String Port){
        Arduino temp = new Arduino(Port);
        temp.initialize();
        if (temp.isConnectedAndSlider()){
            arduinos.add(temp);
            arduinoHash.put(temp.getID(), temp);
            if (arduinos.size() == 1){
            	arduinoHash.put("default", temp);
            }
        }
        return temp.isConnectedAndSlider();
    }
    
    public Arduino getArduinoFromID(String ID){
		return arduinoHash.get(ID);
    }
    
    public String[] getAllArduinoID(){
    	String[] temp = new String[arduinos.size()];
    	for (int i = 0; i < arduinos.size(); i++){
    		temp[i] = arduinos.get(i).getID();
    	}
    	return temp;
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
    	System.out.println("Getting port list");
    	return SerialPortList.getPortNames();
    }
}
