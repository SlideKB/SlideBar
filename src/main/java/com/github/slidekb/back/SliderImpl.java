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

    public SliderImpl() {

    }

    public void bumpRight(int milliseconds) {
        MainBack.arduino.bumpRight(milliseconds);
    }

    public void bumpLeft(int milliseconds) {
        MainBack.arduino.bumpLeft(milliseconds);
    }

    public void writeUntilComplete(int given) {
        MainBack.arduino.writeUntilComplete(given);
    }

    public void writeUntilComplete(double given) {
        MainBack.arduino.writeUntilComplete((int) given);
    }

    public void writeUntilComplete(String given) {
        MainBack.arduino.writeUntilComplete(Integer.parseInt(given));
    }

    public void write(int given) {
        MainBack.arduino.write(given);
    }

    public void goToPart(int index) {
        MainBack.arduino.goToPart(index);
    }

    public void goToPartComplete(int index) {
        MainBack.arduino.goToPartComplete(index);
    }

    public void goToPartComplete(int index, int numberOfParts) {
        MainBack.arduino.goToPartComplete(index, numberOfParts);
    }

    public int readPart(int numberOfParts) {
        return MainBack.arduino.getPartIndex(numberOfParts);
    }

    public int read() {
        return MainBack.arduino.read();
    }

    public void createParts(int numberOfParts) {
        MainBack.arduino.createParts(numberOfParts);
    }

    public int getPartIndex() {
        return MainBack.arduino.getPartIndex();
    }

    public int getPartIndex(int numberOfParts) {
        return MainBack.arduino.getPartIndex(numberOfParts);
    }

    public void removeParts() {
        MainBack.arduino.removeParts();
    }

    public void shiftRight(int distance) {
        MainBack.arduino.shiftRight(distance);
    }

    public void shiftLeft(int distance) {
        MainBack.arduino.shiftLeft(distance);
    }

    public void vibrate(int amount) {
        MainBack.arduino.vibrate(amount);
    }

    public void scrollUp(int amount) {
        MainBack.arduino.scrollUp(amount);
    }

    public void scrollDown(int amount) {
        MainBack.arduino.scrollDown(amount);
    }

    public int getVirtualPartIndex(int parts) {
        return MainBack.arduino.getPartIndex(parts);
    }
}
