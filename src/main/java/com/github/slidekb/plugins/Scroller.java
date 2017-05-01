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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;
import org.aeonbits.owner.Mutable;
import org.sikuli.script.App;
import org.sikuli.script.Location;
import org.sikuli.script.Mouse;
import org.sikuli.script.Region;

import com.github.slidekb.api.AlphaKeyManager;
import com.github.slidekb.api.HotKeyManager;
import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.api.Slider;
import com.github.slidekb.front.ProcessListSelector;
import com.google.auto.service.AutoService;

/**
 * Created by JackSec on 3/24/2017.
 */
@AutoService(SlideBarPlugin.class)
public class Scroller implements SlideBarPlugin {

    Slider slider;

    ThisConfig cfg;

    Region r;

    Robot robot = null;

    int virtualIndex = 12;

    int virtualparts = 25;

    Location prev = null;

    List<String> attachedProcesses = new ArrayList<>();

    ProcessListSelector pls = new ProcessListSelector("Scroller.properties", getLabelName());

    public Scroller() {
        loadConfiguration();
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String[] getProcessNames() {
        return attachedProcesses.toArray(new String[attachedProcesses.size()]);
    }

    private void Sleeper(int delay) {
        for (int i = cfg.speed(); i < delay; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reloadPropFile() {
        try {
            FileInputStream in = new FileInputStream(ClassLoader.getSystemClassLoader().getResource(".").getPath() + "\\configs\\Scroller.properties");
            cfg.load(in);
            in.close();

            attachedProcesses = new ArrayList<>(Arrays.asList(cfg.processList()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String process) {
        int slideIndex = slider.getVirtualPartIndex(virtualparts);
        Location current = Mouse.at();

        if (virtualIndex < slideIndex) {
            slider.scrollDown(2);
            virtualIndex++;
            System.out.println(slideIndex);
        }
        if (virtualIndex > slideIndex) {
            slider.scrollUp(2);
            virtualIndex--;
            System.out.println(slideIndex);
        }
        if (slideIndex == virtualparts - 2) {
            slider.scrollDown(1);
            Sleeper(11);
        }
        if (slideIndex == virtualparts - 1) {
            slider.scrollDown(1);
            Sleeper(7);
        }
        if (slideIndex == 1) {
            slider.scrollUp(1);
            Sleeper(11);
        }
        if (slideIndex == 0) {
            slider.scrollUp(1);
            Sleeper(7);
        }
        if (Math.abs(prev.getX() - current.getX()) > 2 || Math.abs(prev.getY() - current.getY()) > 2) {
            if (slideIndex != (virtualparts / 2) && slideIndex != (virtualparts / 2) + 1 && slideIndex != (virtualparts / 2) - 1) {
                slider.writeUntilComplete(500);
                virtualIndex = slider.getVirtualPartIndex(virtualparts);
            }
            prev = current;
        }
    }

    private boolean loadConfiguration() {
        cfg = ConfigFactory.create(ThisConfig.class);
        attachedProcesses = new ArrayList<>(Arrays.asList(cfg.processList()));
        return true;
    }

    @Override
    public JFrame getConfigWindow() {
        JFrame frame = new JFrame();
        frame.setTitle("Scroller Configuration");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setBounds(100, 100, 314, 142);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0 };
        gbl_contentPane.rowHeights = new int[] { 29, 0, 0, 0 };
        gbl_contentPane.columnWeights = new double[] { 1.0, 0.0, 0.0, Double.MIN_VALUE };
        gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
        contentPane.setLayout(gbl_contentPane);

        JLabel lblScrollSpeed = new JLabel("Scroll Speed");
        GridBagConstraints gbc_lblScrollSpeed = new GridBagConstraints();
        gbc_lblScrollSpeed.anchor = GridBagConstraints.SOUTH;
        gbc_lblScrollSpeed.gridwidth = 2;
        gbc_lblScrollSpeed.insets = new Insets(0, 0, 5, 5);
        gbc_lblScrollSpeed.gridx = 0;
        gbc_lblScrollSpeed.gridy = 0;
        contentPane.add(lblScrollSpeed, gbc_lblScrollSpeed);

        JSlider slider = new JSlider();
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(5);
        slider.setMinorTickSpacing(1);
        slider.setValue(cfg.speed());
        slider.setMaximum(10);
        GridBagConstraints gbc_slider = new GridBagConstraints();
        gbc_slider.fill = GridBagConstraints.HORIZONTAL;
        gbc_slider.gridwidth = 2;
        gbc_slider.insets = new Insets(0, 0, 5, 5);
        gbc_slider.gridx = 0;
        gbc_slider.gridy = 1;
        contentPane.add(slider, gbc_slider);

        JCheckBox chckbxReversed = new JCheckBox("Reversed");
        chckbxReversed.setSelected(cfg.reversed());
        GridBagConstraints gbc_chckbxReversed = new GridBagConstraints();
        gbc_chckbxReversed.insets = new Insets(0, 0, 5, 0);
        gbc_chckbxReversed.gridx = 2;
        gbc_chckbxReversed.gridy = 1;
        contentPane.add(chckbxReversed, gbc_chckbxReversed);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int temp1 = Integer.parseInt("" + slider.getValue());
                boolean reversed = chckbxReversed.isSelected();
                try {
                    FileInputStream in = new FileInputStream(ClassLoader.getSystemClassLoader().getResource(".").getPath() + "\\configs\\Scroller.properties");
                    Properties props = new Properties();
                    props.load(in);
                    in.close();
                    FileOutputStream out = new FileOutputStream(ClassLoader.getSystemClassLoader().getResource(".").getPath() + "\\configs\\Scroller.properties");
                    props.setProperty("speed", "" + temp1);
                    props.setProperty("reversed", "" + reversed);
                    props.store(out, null);
                    out.close();
                    System.out.println("Saved");
                    reloadPropFile();
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        });
        GridBagConstraints gbc_btnSave = new GridBagConstraints();
        gbc_btnSave.anchor = GridBagConstraints.SOUTH;
        gbc_btnSave.insets = new Insets(0, 0, 0, 5);
        gbc_btnSave.gridx = 1;
        gbc_btnSave.gridy = 2;
        contentPane.add(btnSave, gbc_btnSave);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        GridBagConstraints gbc_btnCancel = new GridBagConstraints();
        gbc_btnCancel.anchor = GridBagConstraints.SOUTH;
        gbc_btnCancel.gridx = 2;
        gbc_btnCancel.gridy = 2;
        contentPane.add(btnCancel, gbc_btnCancel);
        return frame;
    }

    @Config.Sources({ "classpath:configs/Scroller.properties" })
    private interface ThisConfig extends Accessible, Mutable {
        @DefaultValue("5")
        int speed();

        @DefaultValue("0")
        boolean reversed();

        @DefaultValue("chrome.exe, idea64.exe")
        String[] processList();
        
        @DefaultValue("default")
        String SliderID();
    }

    @Override
    public void runFirst(String process) {
    	slider.defineSlider(cfg.SliderID());
    	slider.removeParts();
        System.out.println("Scroller is running!");
        // s.createParts(50);
        slider.writeUntilComplete(512);
        virtualIndex = slider.getVirtualPartIndex(virtualparts);
        r = App.focusedWindow();
        prev = Mouse.at();
    }

    @Override
    public String getLabelName() {
        return "Scroller";
    }

    @Override
    public JFrame getProcessWindow() {
        return pls.createGUI();
    }

    @Override
    public void setAlphaKeyManager(AlphaKeyManager alphaKeyManager) {
        // NOP
    }

    @Override
    public void setHotKeyManager(HotKeyManager hotKeyManager) {
        // NOP
    }

    @Override
    public void setSlider(Slider slider) {
        this.slider = slider;
    }

    @Override
    public void attachToProcess(String processName) {
        attachedProcesses.add(processName);
    }

    @Override
    public void detachFromProcess(String processName) {
        attachedProcesses.remove(processName);
    }
}
