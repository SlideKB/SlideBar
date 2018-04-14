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

import java.util.ArrayList;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

public class KeyHook implements NativeKeyListener, NativeMouseWheelListener, NativeMouseInputListener {

    /**
     * list of valid hotKeys.
     */
    private static ArrayList<String> validHotKeys = new ArrayList<String>();

    /**
     * list of valid alphakeys.
     */
    private static ArrayList<String> validAlphaKeys = new ArrayList<String>();

    /**
     * list of pressed hotKeys.
     */
    private static ArrayList<String> pressedHotKeys = new ArrayList<String>();

    /**
     * list of pressed alphakeys.
     */
    private static ArrayList<String> pressedAlphaKeys = new ArrayList<String>();

    /**
     * constructs the class and adds basic valid hotkeys and all a-z alpha keys
     * to their respective lists.
     */
    protected KeyHook() {
        validHotKeys.add("Ctrl");
        validHotKeys.add("Shift");
        validHotKeys.add("Alt");
        validHotKeys.add("Tab");
        for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
            validAlphaKeys.add("" + alphabet);
        }
    }

    /**
     * Adds key to the validhotkey list.
     * 
     * @param key
     */
    protected static void addValidHotkey(String key) {
        validHotKeys.add(key);
    }

    /**
     * adds key to the validalphakey list
     * 
     * @param key
     */
    protected static void addValidAlphaKey(String key) {
        validAlphaKeys.add(key);
    }

    /**
     * adds hotkey or alpha key to the repespective list if they are in the
     * valid list. prevents non valid keys from being added.
     */
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        String temp = NativeKeyEvent.getKeyText(e.getKeyCode());
        if (validHotKeys.contains(temp)) {
            if (!pressedHotKeys.contains(temp)) {
                pressedHotKeys.add(temp);
                System.out.println(pressedHotKeys.toString());
            }
        }
        if (validAlphaKeys.contains(temp)) {
            if (!pressedAlphaKeys.contains(temp)) {
                pressedAlphaKeys.add(temp);
                System.out.println(pressedAlphaKeys.toString());
            }
        }
    }

    /**
     * removes hotkey or alpha key to the repespective list if they are in the
     * valid list. prevents non valid keys from being removed.
     */
    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        String temp = NativeKeyEvent.getKeyText(e.getKeyCode());
        pressedHotKeys.remove(temp);
        if (pressedHotKeys.size() != 0) {
//            System.out.println(pressedHotKeys.toString());
        }

        pressedAlphaKeys.remove(temp);
        if (pressedAlphaKeys.size() != 0) {
//            System.out.println(pressedAlphaKeys.toString());
        }
    }

    public boolean isPressed(String key) {
        if (pressedAlphaKeys.contains(key)) {
            return true;
        }

        if (pressedHotKeys.contains(key)) {
            return true;
        }

        return false;
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
//        System.out.println("testing key" + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    protected static String[] getHotKeys() {
        return pressedHotKeys.toArray(new String[pressedHotKeys.size()]);
    }

    protected static String[] getAlphaKeys() {
        return pressedAlphaKeys.toArray(new String[pressedAlphaKeys.size()]);
    }

	@Override
	public void nativeMouseClicked(NativeMouseEvent arg0) {
		
	}

	@Override
	public void nativeMousePressed(NativeMouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nativeMouseWheelMoved(NativeMouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		
	}
   
}
