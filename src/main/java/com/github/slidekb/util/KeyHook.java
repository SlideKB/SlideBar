package com.github.slidekb.util;

import java.util.ArrayList;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyHook implements NativeKeyListener {

    /**
     * list of valid hotKeys.
     */
    private ArrayList<String> validHotKeys = new ArrayList<String>();

    /**
     * list of valid alphakeys.
     */
    private ArrayList<String> validAlphaKeys = new ArrayList<String>();

    /**
     * list of pressed hotKeys.
     */
    private ArrayList<String> pressedHotKeys = new ArrayList<String>();

    /**
     * list of pressed alphakeys.
     */
    private ArrayList<String> pressedAlphaKeys = new ArrayList<String>();

    /**
     * constructs the class and adds basic valid hotkeys and all a-z alpha keys
     * to their respective lists.
     */
    public KeyHook() {
        validHotKeys.add("Ctrl");
        validHotKeys.add("Shift");
        validHotKeys.add("Alt");
        validHotKeys.add("Tab");
        validHotKeys.add("Esc");
        for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
            validAlphaKeys.add("" + alphabet);
        }
    }

    /**
     * Adds key to the validhotkey list.
     * 
     * @param key
     */
    public void addValidHotkey(String key) {
        validHotKeys.add(key);
    }

    /**
     * adds key to the validalphakey list
     * 
     * @param key
     */
    public void addValidAlphaKey(String key) {
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
            System.out.println(pressedHotKeys.toString());
        }

        pressedAlphaKeys.remove(temp);
        if (pressedAlphaKeys.size() != 0) {
            System.out.println(pressedAlphaKeys.toString());
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
        System.out.println(NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    public String[] getHotKeys() {
        return pressedHotKeys.toArray(new String[pressedHotKeys.size()]);
    }

    public String[] getAlphaKeys() {
        return pressedAlphaKeys.toArray(new String[pressedAlphaKeys.size()]);
    }

}
