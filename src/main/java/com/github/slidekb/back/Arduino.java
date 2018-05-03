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

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.github.slidekb.front.MainFront;
import com.github.slidekb.util.Log;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 * Created by JackSB on 3/12/2017.
 */
public class Arduino implements SerialPortEventListener {

    boolean complete;

    SerialPort serialPort;

    Robot robot;

    /**
     * A BufferedReader which will be fed by a InputStreamReader
     * converting the bytes into characters
     * making the displayed results codepage independent
     */
    private BufferedReader input;
    /**
     * The output stream to the port
     */
    private OutputStream output;
    /**
     * Milliseconds to block while waiting for port open
     */
    private static final int TIME_OUT = 1000;
    /**
     * Default bits per second for COM port.
     */
    private static final int DATA_RATE = 115200;
    /**
     * current value of the arduino
     **/
    protected int reading = -1;

    protected int numberOfParts;

    protected String portName;

    protected String ID;

    protected boolean connectedAndSlider;

    protected boolean isFakeArduino;

    private boolean reversed;

    /**
     * sets up slider/arduino but does not connect.
     * 
     * @param Port
     */
    public Arduino(String Port) {
        portName = Port;
        connectedAndSlider = false;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
     * returns true if the slider/arduino is connected
     * 
     * @return
     */
    protected boolean isConnectedAndSlider() {
        return connectedAndSlider;
    }

    /**
     * returns slider/arduino port currently connected to
     * 
     * @return
     */
    public String getPortName() {
        return portName;
    }

    /**
     * returns slider/arduino ID
     * 
     * @return
     */
    public String getID() {
        return ID;
    }

    // TODO add Mac OS X support
    /**
     * attempts to connect to slider/arduino
     */
    public void initialize() {
        connectedAndSlider = false;
        SerialPort serialPort = new SerialPort(portName);
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            ID = serialPort.readString(1, 5000);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
            serialPort.writeBytes("2424]".getBytes());
            ID = serialPort.readString();
            serialPort.writeBytes("6003]".getBytes());
            ID = serialPort.readString(6, 5000).trim();

            serialPort.addEventListener((SerialPortEvent serialPortEvent) -> {

                try {
                    String st = "";
                    st = serialPort.readString(serialPortEvent.getEventValue());
                    st = st.trim();
                    if (st.length() != 0) {
                        try {
                            reading = Integer.parseInt(st);
//                            System.out.println("Arduino->SerialEvent-> ID: " + ID + " Reading: " + read());
                            MainFront.updateSliderInfo(ID, read());
                        } catch (Exception e) {
                            // do nothing
                        }
                    }
                } catch (SerialPortException ex) {
                    System.out.println("Arduino->SerialPortEvent serialPortEvent->failed to readString()");
                    ex.printStackTrace();
                }

            });

            this.serialPort = serialPort;

        } catch (SerialPortException ex) {
            System.out.println("SerialPortException: " + ex.toString());
        } catch (SerialPortTimeoutException ex) {
            System.out.println("SerialPortTimeoutException: " + ex.toString());
        }
        if (ID == null) {
            System.out.println("Arduino->initialize()-> Not a slider at " + portName + "");
            connectedAndSlider = false;
        } else {
            System.out.println("Arduino->initialize()-> Found a valid slider with port " + portName + " and ID: " + ID);
            Log.logMessage("Arduino->initialize()-> Found a valid slider with port " + portName + " and ID: " + ID);
            connectedAndSlider = true;
        }
    }

    /**
     * returns slider position
     * 
     * @return
     */
    public int read() {
        if (reversed) {
            return 1022 - reading;
        }
        return reading;

    }

    /**
     * sets the slider position internally in this class. (not physically)
     * 
     * @param value
     */
    public void setReading(int value) {
        reading = value;
    }

    /**
     * This should be called when you stop using the port.
     * This will prevent port locking on platforms like Linux.
     */
    public synchronized void close() {
        if (isFakeArduino) {
            System.out.println("Closing Fake port");
            return;
        }
        if (serialPort != null) {
            try {
                serialPort.removeEventListener();
            } catch (SerialPortException e1) {
                e1.printStackTrace();
            }
            try {
                serialPort.closePort();
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * sets the slider to a position physically
     * 
     * @param send
     */
    public void write(int send) {
        if (send >= 0 && send <= 1024 && reversed) {
            send = 1024 - send;
        }
        String toSend = send + "]";
        try {
            serialPort.writeString(toSend);
        } catch (SerialPortException e) {
            System.out.println("could not write(" + send + ")");
            e.printStackTrace();
        }
    }

    /**
     * writes to Arduino to a position and waits for completion. If the arduino is not connected, the method prints out
     * the
     * console likewise.
     * 
     * @param position
     */
    public void writeUntilComplete(int position) {
        if (!(position > 0) && !(position < 1024)) {
            System.out.println("Arduino->writeUntilComplete()-> Cannot wait for completion for a value that is not in the range 0 < value < 1024");
        } else {
            complete = false;
            write(position);
            Instant start = Instant.now();
            System.out.println("Arduino->writeUntilComplete()-> waiting for completion");
            do {
            } while (Math.abs(read() - position) > 30 && Duration.between(start, Instant.now()).toMillis() < 1000);
            read();
            System.out.println("Arduino->writeUntilComplete()-> completed");
        }
    }

    // Methods for dealing with Parts
    protected void createParts(int numberOfParts) {
        if (numberOfParts > 102) {
            System.out.println("Arduino->createParts()-> Cannot create more than 50 parts.");
        }
        this.numberOfParts = numberOfParts;
        write((2000 + numberOfParts));
    }

    protected int getPartIndex() {
        return getPartIndex(numberOfParts);
    }

    protected int getPartIndex(int numberOfParts) {
        if (numberOfParts > 102) {
            System.out.println("Arduino->getPartIndex()-> Cannot have more than 102 parts.");
        }
        if (numberOfParts != 0) {
            double partSize = (1040.0 / numberOfParts);
            return (int) (reading / partSize);
            // (int) (((1024.0 / numberOfParts) * index) + ((1024.0 / numberOfParts) / 2));
        }
        return -1;
    }

    protected void goToPart(int index) {
        goToPart(index, numberOfParts);
    }

    protected void goToPart(int index, int numberOfParts) {
        if (numberOfParts > index && index > 0) {
            double partSize = (1024.0 / numberOfParts);
            write((int) ((index * partSize) + (partSize / 2)));
        } else {
            throw new IndexOutOfBoundsException();
        }

    }

    protected void goToPartComplete(int index) {
        goToPartComplete(index, numberOfParts);
    }

    protected void goToPartComplete(int index, int numberOfParts) {
        if (numberOfParts > index && index >= 0) {
            double partSize = (1040.0 / numberOfParts);
            writeUntilComplete((int) ((index * partSize) + (partSize / 2)));
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    protected void removeParts() {
        numberOfParts = 0;
        write(2000);
    }

    protected int NumberOfParts() {
        return numberOfParts;
    }

    // Methods for simple writing to arduino.
    protected void shiftRight(int distance) {
        reading = reading + distance;
        reading = Math.min(1023, reading);
        reading = Math.max(0, reading);
        write(reading);
    }

    protected void shiftLeft(int distance) {
        shiftRight(distance * -1);
    }

    public void bumpRight(int milliseconds) {
        write(3000 + milliseconds);
    }

    public void bumpLeft(int milliseconds) {
        write(4000 + milliseconds);
    }

    /**
     * vibrates the slider/arduino for cycle times
     * 
     * @param cycles
     */
    public void vibrate(int cycles) {
        if (cycles > 999 || cycles < 0) {
            throw new IllegalArgumentException("Arduino->Vibrate()-> cycles should be between 0 and 999");
        }
        write(6000 + cycles);
    }

    public void scrollDown(int amount) {
        // if (amount > 499 || amount < 0){
        // throw new IllegalArgumentException("amount should be between 0 and 499");
        // }
        // write(5500+amount);
        robot.mouseWheel(amount);
    }

    public void scrollUp(int amount) {
        // if (amount > 499 || amount < 0){
        // throw new IllegalArgumentException("amount should be between 0 and 499");
        // }
        // write(5000+amount);
        robot.mouseWheel(amount * -1);
    }

    @Override
    public void serialEvent(SerialPortEvent arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * reversed the slider read and write positions
     * 
     * @param reversed
     */
    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

}