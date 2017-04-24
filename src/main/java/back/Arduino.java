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

package back;

import gnu.io.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * Created by JackSB on 3/12/2017.
 */
public class Arduino implements SerialPortEventListener {

    Pattern commaPattern = Pattern.compile("<(\\d+)>");

    boolean complete;

    private final String portOverride;

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
    private int reading = 0;

    private int numberOfParts;

    protected boolean runWithArduino = true;

    protected Arduino(String portOverride) {
        this.portOverride = portOverride;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    protected Arduino() {
        this.portOverride = "Auto";
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void initialize(int index) {
        CommPortIdentifier portId = null;

        @SuppressWarnings("unchecked")
        Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();

        if (!getFirstPort(index).equals("none")) {
            while (portEnum.hasMoreElements()) {
                CommPortIdentifier currPortId = portEnum.nextElement();
                if (currPortId.getName().equals(getFirstPort(index))) {
                    System.out.println(currPortId.getName() + " - " + getPortTypeName(currPortId.getPortType()));
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }
        try {
            // open serial port, and use class name for the appName.
            serialPort = (gnu.io.SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE, gnu.io.SerialPort.DATABITS_8, gnu.io.SerialPort.STOPBITS_1, gnu.io.SerialPort.PARITY_NONE);

            // open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();
            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
            Thread.sleep(3000);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        write(2424);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        System.out.println("This 'slider' is currently at: " + reading);
        if (reading == 0) {
            System.out.println("[Not a slider!]");
            serialPort.close();
            initialize(index + 1);
        } else {
            System.out.println("[Found a valid slider!]");
        }
    }

    public static String getFirstPort(int index) {
        @SuppressWarnings("unchecked")
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        int counter = 0;
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            if (getPortTypeName(portIdentifier.getPortType()) == "Serial") {
                if (counter == index) {
                    return portIdentifier.getName();
                } else {
                    counter++;
                }

            }

        }
        return "none";
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

    static String getPortTypeName(int portType) {
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

    // static void listPorts()
    // {
    // java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
    // while ( portEnum.hasMoreElements() )
    // {
    // CommPortIdentifier portIdentifier = portEnum.nextElement();
    //// System.out.println(portIdentifier.getName() + " - " + getPortTypeName(portIdentifier.getPortType()) );
    // }
    // }

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                while (input.ready()) {
                    String inputLine = input.readLine();
                    // System.out.println(inputLine);
                    reading = Integer.parseInt(inputLine);
                }
            } catch (Exception e) {
            }
        }
        // Ignore all the other eventTypes, but you should consider the other ones.
    }

    public int read() {
        return reading;
    }

    public void setReading(int value) {
        reading = value;
    }

    /**
     * This should be called when you stop using the port.
     * This will prevent port locking on platforms like Linux.
     */
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    public void write(int send) {
        if (send == 2000) {

        }
        try {
            output.flush();
            output.write((send + "]").getBytes());
            System.out.println("Writing to arduino: " + send);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * writes to Arduino connected and waits for completion. If the arduino is not connected, the method prints out the
     * console likewise.
     * 
     * @param position
     */
    protected void writeUntilComplete(int position) {
        if (!(position > 0) && !(position < 1024)) {
            System.out.println("Cannot wait for completion for a value that is not in the range 0 < value < 1024");
        } else {
            complete = false;
            write(position);
            Instant start = Instant.now();
            System.out.println(" - waiting for completion");
            do {
            } while (Math.abs(read() - position) > 30 && Duration.between(start, Instant.now()).toMillis() < 1000);
            read();
            System.out.println(" - completed");
        }
    }

    // Methods for dealing with Parts
    protected void createParts(int numberOfParts) {
        if (numberOfParts > 102) {
            System.out.println("Cannot create more than 50 parts.");
        }
        this.numberOfParts = numberOfParts;
        write((2000 + numberOfParts));
    }

    protected int getPartIndex() {
        return getPartIndex(numberOfParts);
    }

    protected int getPartIndex(int numberOfParts) {
        if (numberOfParts > 102) {
            System.out.println("Cannot have more than 102 parts.");
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
            double partSize = (1040.0 / numberOfParts);
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

    public void vibrate(int cycles) {
        if (cycles > 999 || cycles < 0) {
            throw new IllegalArgumentException("cycles should be between 0 and 999");
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

}