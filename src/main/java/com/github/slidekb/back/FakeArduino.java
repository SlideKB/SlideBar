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

public class FakeArduino extends Arduino {

    private String givenID;

    FakeArduino(String givenID, String portName) {
        super(portName);
        isFakeArduino = true;
        this.givenID = givenID;
    }

    @Override
    public void initialize() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ID = givenID;
        System.out.println("[connecting to FAKE arduino]");
        System.out.println("Port: " + portName);
        System.out.println("ID: " + ID);
        connectedAndSlider = true;
    }

    @Override
    public int read() {
        System.out.println(reading);
        return reading;
    }

    @Override
    public void writeUntilComplete(int send) {
        try {
            Thread.sleep(Math.abs(send - reading));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        write(send);
    }

    @Override
    public void write(int send) {
        System.out.println("Writing to Fake arduino: " + send);
        reading = send;
    }
}
