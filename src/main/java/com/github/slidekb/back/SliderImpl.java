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

import com.github.slidekb.api.Slider;

public class SliderImpl implements Slider {

    int location = 0;

    Arduino arduino = null;

    public SliderImpl(Arduino arduino) {
        this.arduino = arduino;
    }

    public void bumpRight(int milliseconds) {
        arduino.bumpRight(milliseconds);
    }

    public void bumpLeft(int milliseconds) {
        arduino.bumpLeft(milliseconds);
    }

    public void writeUntilComplete(int given) {
        arduino.writeUntilComplete(given);
    }

    public void writeUntilComplete(double given) {
        arduino.writeUntilComplete((int) given);
    }

    public void writeUntilComplete(String given) {
        arduino.writeUntilComplete(Integer.parseInt(given));
    }

    public void write(int given) {
        arduino.write(given);
    }

    public void goToPart(int index) {
        arduino.goToPart(index);
    }

    public void goToPartComplete(int index) {
        arduino.goToPartComplete(index);
    }

    public void goToPartComplete(int index, int numberOfParts) {
        arduino.goToPartComplete(index, numberOfParts);
    }

    public int readPart(int numberOfParts) {
        return arduino.getPartIndex(numberOfParts);
    }

    public int read() {
        return arduino.read();
    }

    public void createParts(int numberOfParts) {
        arduino.createParts(numberOfParts);
    }

    public int getPartIndex() {
        return arduino.getPartIndex();
    }

    public int getPartIndex(int numberOfParts) {
        return arduino.getPartIndex(numberOfParts);
    }

    public void removeParts() {
        arduino.removeParts();
    }

    public void shiftRight(int distance) {
        arduino.shiftRight(distance);
    }

    public void shiftLeft(int distance) {
        arduino.shiftLeft(distance);
    }

    public void vibrate(int amount) {
        arduino.vibrate(amount);
    }

    public void scrollUp(int amount) {
        arduino.scrollUp(amount);
    }

    public void scrollDown(int amount) {
        arduino.scrollDown(amount);
    }

    public int getVirtualPartIndex(int parts) {
        return arduino.getPartIndex(parts);
    }

    @Override
    public void close() {
        arduino.close();
    }
}
