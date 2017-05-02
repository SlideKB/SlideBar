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

package com.github.slidekb.plugins;

import java.awt.AWTException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;
import org.aeonbits.owner.Mutable;

import com.github.slidekb.api.AlphaKeyManager;
import com.github.slidekb.api.HotKeyManager;
import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.api.Slider;
import com.google.auto.service.AutoService;

/**
 * Created by JackSB on 3/12/2017.
 */
@AutoService(SlideBarPlugin.class)
public class AltProcess implements SlideBarPlugin {

    HotKeyManager hotKeyManager;

    Slider slider;

    String previous = "";

    Robot rob = null;

    private int virtualIndex = 1;

    ThisConfig cfg;

    public AltProcess() {
        loadConfiguration();
    }

    @Override
    public int getPriority() {
        return -1;
    }

    @Override
    public String[] getProcessNames() {
        String[] hotKey1 = { "Alt" };
        String[] hotKey2 = { "Ctrl", "Alt" };
        String[] hotKey3 = { "Alt", "Ctrl" };
        String[] list = { Arrays.toString(hotKey1), Arrays.toString(hotKey2), Arrays.toString(hotKey3) };
        return list;
    }

    private boolean loadConfiguration() {
        cfg = ConfigFactory.create(ThisConfig.class);

        return true;
    }

    public JFrame getConfigWindow() {
        JFrame frame = new JFrame();
        frame.setSize(320, 140);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setTitle("Alt+Slide Configuration");
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 34, 93, 91, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 21, 0, 20, 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        frame.setLayout(gridBagLayout);

        JLabel lblNumberOfParts = new JLabel("Number of Parts");
        GridBagConstraints gbc_lblNumberOfParts = new GridBagConstraints();
        gbc_lblNumberOfParts.insets = new Insets(0, 0, 5, 5);
        gbc_lblNumberOfParts.gridx = 1;
        gbc_lblNumberOfParts.gridy = 2;
        frame.add(lblNumberOfParts, gbc_lblNumberOfParts);

        NumberFormat number;
        number = NumberFormat.getNumberInstance();
        number.setMaximumIntegerDigits(2);
        JFormattedTextField formattedTextField = new JFormattedTextField(number);
        formattedTextField.setValue(cfg.numberOfParts());
        GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
        gbc_formattedTextField.insets = new Insets(0, 0, 5, 5);
        gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_formattedTextField.gridx = 2;
        gbc_formattedTextField.gridy = 2;
        frame.add(formattedTextField, gbc_formattedTextField);

        JLabel lblStartingIndex = new JLabel("Starting index");
        GridBagConstraints gbc_lblStartingIndex = new GridBagConstraints();
        gbc_lblStartingIndex.insets = new Insets(0, 0, 5, 5);
        gbc_lblStartingIndex.gridx = 1;
        gbc_lblStartingIndex.gridy = 3;
        frame.add(lblStartingIndex, gbc_lblStartingIndex);

        NumberFormat number2;
        number2 = NumberFormat.getNumberInstance();
        number2.setMaximumIntegerDigits(2);
        JFormattedTextField formattedTextField_1 = new JFormattedTextField(number2);
        formattedTextField_1.setValue(cfg.StartingPart());
        GridBagConstraints gbc_formattedTextField_1 = new GridBagConstraints();
        gbc_formattedTextField_1.insets = new Insets(0, 0, 5, 5);
        gbc_formattedTextField_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_formattedTextField_1.gridx = 2;
        gbc_formattedTextField_1.gridy = 3;
        frame.add(formattedTextField_1, gbc_formattedTextField_1);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int temp1 = Integer.parseInt("" + formattedTextField.getValue());
                int temp2 = Integer.parseInt("" + formattedTextField_1.getValue());
                if (temp1 > 0 && temp1 < 25) {
                    if (temp2 >= 0 && temp2 < temp1 - 1)
                        try {

                            FileInputStream in = new FileInputStream(ClassLoader.getSystemClassLoader().getResource(".").getPath() + "\\configs\\AltProcess.properties");
                            Properties props = new Properties();
                            props.load(in);
                            in.close();

                            FileOutputStream out = new FileOutputStream(ClassLoader.getSystemClassLoader().getResource(".").getPath() + "\\configs\\AltProcess.properties");
                            props.setProperty("numberOfParts", "" + temp1);
                            props.setProperty("StartingPart", "" + temp2);
                            props.store(out, null);
                            out.close();
                            System.out.println("Saved");
                            FileInputStream in2 = new FileInputStream(ClassLoader.getSystemClassLoader().getResource(".").getPath() + "\\configs\\AltProcess.properties");
                            cfg.load(in2);
                            in2.close();
                            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                }
            }
        });
        GridBagConstraints gbc_btnSave = new GridBagConstraints();
        gbc_btnSave.insets = new Insets(0, 0, 5, 5);
        gbc_btnSave.gridx = 2;
        gbc_btnSave.gridy = 5;
        frame.add(btnSave, gbc_btnSave);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        GridBagConstraints gbc_btnCancel = new GridBagConstraints();
        gbc_btnCancel.insets = new Insets(0, 0, 5, 0);
        gbc_btnCancel.gridx = 3;
        gbc_btnCancel.gridy = 5;
        frame.add(btnCancel, gbc_btnCancel);
        return frame;
    }

    @Config.Sources({ "classpath:configs/AltProcess.properties" })
    private interface ThisConfig extends Accessible, Mutable {
        @DefaultValue("7")
        int numberOfParts();

        @DefaultValue("0")
        int StartingPart();

        @DefaultValue("default")
        String SliderID();

    }

    @Override
    public void run(String process) {
        int slideIndex = slider.getPartIndex();
        // System.out.println(slideIndex);

        if (virtualIndex < slideIndex) {
            rob.keyPress(KeyEvent.VK_TAB);
            rob.keyRelease(KeyEvent.VK_TAB);
            virtualIndex++;
        }
        if (virtualIndex > slideIndex) {
            // System.out.println(virtualIndex + " - 1");
            rob.keyPress(KeyEvent.VK_SHIFT);
            rob.keyPress(KeyEvent.VK_TAB);
            rob.keyRelease(KeyEvent.VK_TAB);
            rob.keyRelease(KeyEvent.VK_SHIFT);
            virtualIndex--;
        }
    }

    @Override
    public void runFirst(String process) {
        try {
            rob = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        virtualIndex = cfg.StartingPart();
        slider.createParts(cfg.numberOfParts());
        slider.goToPartComplete(cfg.StartingPart());
        slider.removeParts();
        slider.createParts(cfg.numberOfParts());
        long start = new Date().getTime();
    }

    @Override
    public String getLabelName() {
        return "Alt+Slide";
    }

    @Override
    public JFrame getProcessWindow() {
        return null;
    }

    @Override
    public void reloadPropFile() {

    }

    @Override
    public void setAlphaKeyManager(AlphaKeyManager alphaKeyManager) {
        // NOP
    }

    @Override
    public void setHotKeyManager(HotKeyManager hotKeyManager) {
        this.hotKeyManager = hotKeyManager;
    }

    @Override
    public void setSlider(Slider slider) {
        this.slider = slider;
    }

    @Override
    public void attachToProcess(String processName) {
        // NOP
    }

    @Override
    public void detachFromProcess(String processName) {
        // NOP
    }

    @Override
    public String currentlyUsedSlider() {
        return cfg.SliderID();
    }

    @Override
    public boolean usesProcessNames() {
        return false;
    }
}
