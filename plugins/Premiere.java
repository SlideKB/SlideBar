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

package plugins;

import back.HotKeyManager;
import back.Process;
import back.Slider;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import front.ProcessListSelector;
import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;
import org.aeonbits.owner.Mutable;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

/**
 * Created by JackSec on 4/21/2017.
 */
public class Premiere implements Process {

    Slider s = new Slider();

    ThisConfig cfg;

    boolean playing = false;

    HotKeyManager HKM = new HotKeyManager();

    Robot robot = null;

    int virtualIndex = 15;

    int virtualparts = 30;

    boolean toggle = false;

    ProcessListSelector pls = new ProcessListSelector("Premiere.properties", getLabelName());

    autoHotKeyDll lib;

    public Premiere(){
        loadConfiguration();
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String[] getProcessNames() {
        return cfg.processList();
    }

    @Override
    public void reloadPropFile(){
        try {
            FileInputStream in = new FileInputStream(ClassLoader.getSystemClassLoader().getResource(".").getPath() + "\\configs\\Premiere.properties");
            cfg.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Sleeper(int delay){

            Instant start = Instant.now();
            do {
            } while (Duration.between(start, Instant.now()).toMillis() < delay);
            try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}

    }

    private boolean loadConfiguration(){
        cfg = ConfigFactory.create(ThisConfig.class);
        Pointer pointer;
        HKM.addKey("Space");
        System.out.println("running in " + System.getProperty("sun.arch.data.model"));
        lib = (autoHotKeyDll) Native.loadLibrary("AutoHotkey.dll", autoHotKeyDll.class);
        lib.ahktextdll(new WString(""),new WString(""),new WString(""));
        return true;
    }

    public interface autoHotKeyDll extends Library {
        public void ahkExec(WString s);
        public void ahkdll(WString s,WString o,WString p);
        public void addFile(WString s, int a);
        public void ahktextdll(WString s,WString o,WString p);
        public Pointer ahkFunction(WString f, WString p1, WString p2, WString p3, WString p4, WString p5, WString p6, WString p7, WString p8, WString p9, WString p10);
    }





    @Config.Sources({"classpath:configs/Premiere.properties" })
    private interface ThisConfig extends Accessible, Mutable {
        @DefaultValue(", Adobe Premiere Pro.exe")
        String[] processList();
    }

    @Override
    public void run(String process) {


        String[] keys = HKM.getHotkeys();
        String key = "";
        if (keys.length > 0) {
            key = keys[keys.length - 1];
            for (String k : keys){
                if (k.equals("Shift")){
                    toggle = true;
                }
            }

        } else {
            toggle = false;
        }
        int slideIndex = s.getVirtualPartIndex(virtualparts);
        if (virtualIndex < slideIndex){
            playing = false;
            System.out.println(Arrays.toString(keys));
            if (toggle){

                lib.ahkExec(new WString("SendEvent {g}"));

            } else {
                lib.ahkExec(new WString("SendEvent {Left}"));
            }
            virtualIndex++;
            System.out.println(slideIndex);
        }
        if (virtualIndex > slideIndex){
            playing = false;
            System.out.println(Arrays.toString(keys));
            if (toggle){

                lib.ahkExec(new WString("SendEvent {;}"));

            } else {
                lib.ahkExec(new WString("SendEvent {Right}"));
            }
            virtualIndex--;
            System.out.println(slideIndex);
        }
//        if (slideIndex == virtualparts-1){
//            playing = false;
//            if (toggle){
//                lib.ahkExec(new WString("Send {Shift}{Left}"));
//            } else {
//                lib.ahkExec(new WString("Send {Left}"));
//            }
//            Sleeper(5);
//        }
//        if (slideIndex == 0){
//            if (toggle){
//                lib.ahkExec(new WString("Send {Shift}{Right}"));
//            } else {
//                lib.ahkExec(new WString("Send {Right}"));
//            }
//            Sleeper(5);
//        }
        if (slideIndex == virtualparts-1 && !playing){
            playing = true;
            lib.ahkExec(new WString("Send {j}"));
        }
        if (slideIndex == 0 && !playing){
            playing = true;
            lib.ahkExec(new WString("Send {l}"));
        }
        if (key.equals("Space") || key.equals("Ctrl")) {
            playing = false;
            if (slideIndex != (virtualparts/2) && slideIndex != (virtualparts/2)+1 && slideIndex != (virtualparts/2) -1) {
                s.writeUntilComplete( 500);
                virtualIndex = s.getVirtualPartIndex(virtualparts);
            }
        }
    }

    @Override
    public JFrame getConfigWindow() {
        return null;
    }

    @Override
    public void runFirst(String process) {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            System.out.println("Robot could not be created...");
        }
        s.writeUntilComplete(512);
        virtualIndex = s.getVirtualPartIndex(virtualparts);
    }

    @Override
    public String getLabelName() {
        return "Adobe Premiere Scrubber";
    }

    @Override
    public JFrame getProcessWindow() {
        return pls.createGUI();
    }
}
