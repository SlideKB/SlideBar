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

package com.github.slidekb.front;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.github.slidekb.back.Arduino;
import com.github.slidekb.back.MainBack;
import com.github.slidekb.back.SlideBarPlugin;

public class MainFront {

    private static JFrame frame;

    private static JPanel contentPane;

    /**
     * The "About" Jframe
     */
    private static void createAndShowGUI() {
        if (frame == null) {
            frame = new JFrame();
            frame.setTitle("SlideBar Configuration");
            frame.setBounds(100, 100, 816, 534);
            contentPane = new JPanel();
            contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
            frame.setContentPane(contentPane);
            GridBagLayout gbl_contentPane = new GridBagLayout();
            gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 130, 0, 208, 37, 0, 0 };
            gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 160, 0, 0, 0, 0, 0, 0, 0, 0 };
            gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0,
                    Double.MIN_VALUE };
            gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                    0.0, 0.0, Double.MIN_VALUE };
            contentPane.setLayout(gbl_contentPane);

            JLabel lblStatus = new JLabel("Status");
            GridBagConstraints gbc_lblStatus = new GridBagConstraints();
            gbc_lblStatus.insets = new Insets(0, 0, 5, 5);
            gbc_lblStatus.gridx = 2;
            gbc_lblStatus.gridy = 1;
            contentPane.add(lblStatus, gbc_lblStatus);

            JLabel lblComPort = new JLabel("COM Port");
            GridBagConstraints gbc_lblComPort = new GridBagConstraints();
            gbc_lblComPort.insets = new Insets(0, 0, 5, 5);
            gbc_lblComPort.gridx = 4;
            gbc_lblComPort.gridy = 1;
            contentPane.add(lblComPort, gbc_lblComPort);

            JLabel lblPluginsLoaded = new JLabel("Plugins Loaded");
            GridBagConstraints gbc_lblPluginsLoaded = new GridBagConstraints();
            gbc_lblPluginsLoaded.gridwidth = 2;
            gbc_lblPluginsLoaded.insets = new Insets(0, 0, 5, 5);
            gbc_lblPluginsLoaded.gridx = 6;
            gbc_lblPluginsLoaded.gridy = 1;
            contentPane.add(lblPluginsLoaded, gbc_lblPluginsLoaded);

            // String temp = "Connected";
            //
            JLabel status = new JLabel(MainBack.isStarted() + "");
            GridBagConstraints gbc_label = new GridBagConstraints();
            gbc_label.insets = new Insets(0, 0, 5, 5);
            gbc_label.gridx = 2;
            gbc_label.gridy = 2;
            contentPane.add(status, gbc_label);

            JComboBox<String> comboBox = new JComboBox<>();
            comboBox.addItem("Auto");
            for (String s : Arduino.getPortList(0)) {
                comboBox.addItem(s);
            }

            GridBagConstraints gbc_comboBox = new GridBagConstraints();
            gbc_comboBox.insets = new Insets(0, 0, 5, 5);
            gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
            gbc_comboBox.gridx = 4;
            gbc_comboBox.gridy = 2;
            contentPane.add(comboBox, gbc_comboBox);

            ArrayList<String> arrayProcess = new ArrayList<String>();
            do {
                System.out.println("waiting for proci");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (MainBack.PM.getProci() == null);
            for (SlideBarPlugin p : MainBack.PM.getProci()) {
                arrayProcess.add(p.getLabelName());
            }
            System.out.println(arrayProcess.toString());
            String proci[] = arrayProcess.toArray(new String[arrayProcess.size()]);

            JList<String> processList = new JList<>(proci);
            JScrollPane pane = new JScrollPane(processList);
            GridBagConstraints gbc_list = new GridBagConstraints();
            gbc_list.gridwidth = 2;
            gbc_list.gridheight = 10;
            gbc_list.insets = new Insets(0, 0, 5, 5);
            gbc_list.fill = GridBagConstraints.BOTH;
            gbc_list.gridx = 6;
            gbc_list.gridy = 2;
            contentPane.add(pane, gbc_list);

            JCheckBox chckbxRunOnStart = new JCheckBox("Run on Start");
            GridBagConstraints gbc_chckbxRunOnStart = new GridBagConstraints();
            gbc_chckbxRunOnStart.anchor = GridBagConstraints.WEST;
            gbc_chckbxRunOnStart.gridwidth = 2;
            gbc_chckbxRunOnStart.insets = new Insets(0, 0, 5, 5);
            gbc_chckbxRunOnStart.gridx = 1;
            gbc_chckbxRunOnStart.gridy = 4;
            contentPane.add(chckbxRunOnStart, gbc_chckbxRunOnStart);

            JCheckBox chckbxSwapEnds = new JCheckBox("Swap ends");
            GridBagConstraints gbc_chckbxSwapEnds = new GridBagConstraints();
            gbc_chckbxSwapEnds.anchor = GridBagConstraints.WEST;
            gbc_chckbxSwapEnds.gridwidth = 2;
            gbc_chckbxSwapEnds.insets = new Insets(0, 0, 5, 5);
            gbc_chckbxSwapEnds.gridx = 1;
            gbc_chckbxSwapEnds.gridy = 5;
            contentPane.add(chckbxSwapEnds, gbc_chckbxSwapEnds);

            JButton btnReload = new JButton("Reload");
            btnReload.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    status.setText("Stopping");
                    MainBack.stop();
                    if (MainBack.startIt((String) comboBox.getSelectedItem())) {
                        status.setText("Connected");
                    } else {
                        status.setText("Not Connected");
                    }
                }
            });
            GridBagConstraints gbc_btnReload = new GridBagConstraints();
            gbc_btnReload.fill = GridBagConstraints.HORIZONTAL;
            gbc_btnReload.insets = new Insets(0, 0, 5, 5);
            gbc_btnReload.gridx = 2;
            gbc_btnReload.gridy = 13;
            contentPane.add(btnReload, gbc_btnReload);

            JButton btnTestSlider = new JButton("Test");
            btnTestSlider.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("testing");
                    MainBack.testVibrate(5);
                }
            });
            GridBagConstraints gbc_btnTestSlider = new GridBagConstraints();
            gbc_btnTestSlider.anchor = GridBagConstraints.WEST;
            gbc_btnTestSlider.insets = new Insets(0, 0, 5, 5);
            gbc_btnTestSlider.gridx = 4;
            gbc_btnTestSlider.gridy = 13;
            contentPane.add(btnTestSlider, gbc_btnTestSlider);

            JButton btnProcessList = new JButton("Program List");
            btnProcessList.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selected[] = processList.getSelectedIndices();
                    System.out.println("Selected Elements:  ");
                    for (int i = 0; i < selected.length; i++) {
                        String element = (String) processList.getModel().getElementAt(selected[i]);
                        ArrayList<SlideBarPlugin> temp = MainBack.PM.getProci();
                        for (SlideBarPlugin p : temp) {
                            if (p.getLabelName().contentEquals(element)) {
                                System.out.println(p.getLabelName());
                                p.getProcessWindow().pack();
                                p.getProcessWindow().setVisible(true);
                            }
                        }
                    }
                }
            });
            GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
            gbc_btnNewButton.anchor = GridBagConstraints.EAST;
            gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
            gbc_btnNewButton.gridx = 6;
            gbc_btnNewButton.gridy = 13;
            contentPane.add(btnProcessList, gbc_btnNewButton);

            JButton btnConfigure = new JButton("Configure");

            class ConfigureListener implements ActionListener {
                public void actionPerformed(ActionEvent e) {
                    int selected[] = processList.getSelectedIndices();
                    System.out.println("Selected Elements:  ");
                    for (int i = 0; i < selected.length; i++) {
                        String element = (String) processList.getModel().getElementAt(selected[i]);
                        ArrayList<SlideBarPlugin> temp = MainBack.PM.getProci();
                        for (SlideBarPlugin p : temp) {
                            if (p.getLabelName().contentEquals(element)) {
                                System.out.println(p.getLabelName());
                                JFrame window = p.getConfigWindow();
                                window.pack();
                                window.setVisible(true);
                            }
                        }
                    }
                }
            }

            processList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {

                }
            });
            btnConfigure.addActionListener(new ConfigureListener());
            GridBagConstraints gbc_btnConfigure = new GridBagConstraints();
            gbc_btnConfigure.anchor = GridBagConstraints.EAST;
            gbc_btnConfigure.insets = new Insets(0, 0, 5, 5);
            gbc_btnConfigure.gridx = 7;
            gbc_btnConfigure.gridy = 13;
            contentPane.add(btnConfigure, gbc_btnConfigure);

            // // Run/Stop button
            // JButton stopButton = new JButton("Stop");
            // stopButton.addActionListener(new ActionListener() {
            // @Override
            // public void actionPerformed(ActionEvent e) {
            // MainBack.stop();
            // }
            // });
            //
            // // Run/Stop button
            // JButton startButton = new JButton("Start");
            // startButton.addActionListener(new ActionListener() {
            // @Override
            // public void actionPerformed(ActionEvent e) {
            // MainBack.startIt();
            // }
            // });

            // JButton configureButton = new JButton("Configure");
            // configureButton.addActionListener(new ActionListener() {
            // @Override
            // public void actionPerformed(ActionEvent e) {
            // ArrayList<Process> temp = MainBack.PM.getProci();
            // for (Process p : temp){
            // System.out.println(p.getLabelName());
            // if (p.getLabelName().contentEquals("Alt+Slide")){
            // System.out.println("found Alt+Slide!");
            // p.getConfigWindow().pack();
            // p.getConfigWindow().setVisible(true);
            // }
            // }
            // }
            // });

            // frame.getContentPane().add(label);
            // frame.getContentPane().add(status);
            // frame.getContentPane().add(startup);
            // frame.getContentPane().add(startButton);
            // frame.getContentPane().add(stopButton);
            // frame.getContentPane().add(configureButton);
            if (MainBack.isStarted()) {
                status.setText("Connected");
            } else {
                status.setText("Not Connected");
            }
        }
        // Display the window.
        frame.pack();

        bringToFront();
    }

    private static void bringToFront() {
        getInstance().setVisible(true);
        getInstance().setExtendedState(JFrame.NORMAL);
        getInstance().toFront();
        getInstance().repaint();
    }

    private static JFrame getInstance() {
        return frame;
    }

    /**
     * Popup Menu
     * 
     * @return
     */
    protected static PopupMenu createPopupMenu() {
        final PopupMenu popup = new PopupMenu();
        MenuItem aboutItem = new MenuItem("Open Configuration");
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAndShowGUI();
            }
        });
        MenuItem about = new MenuItem("About");
        MenuItem website = new MenuItem("Website");
        MenuItem reload = new MenuItem("Reload");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        // Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(about);
        popup.add(website);
        popup.add(reload);
        popup.addSeparator();
        popup.add(exitItem);
        return popup;
    }

    private static void setupTray() {
        try {
            // Check the SystemTray is supported
            if (!SystemTray.isSupported()) {
                System.out.println("SystemTray is not supported");
                return;
            }
            final TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(new URL("http://home.comcast.net/~supportcd/Icons/Java_Required.jpg")), "Library Drop");
            final SystemTray tray = SystemTray.getSystemTray();

            // Create a pop-up menu components
            final PopupMenu popup = createPopupMenu();
            trayIcon.setPopupMenu(popup);
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        createAndShowGUI();
                    }
                }
            });

            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        } catch (MalformedURLException e) {
            System.out.println("Malformed Exception");
        }
    }

    public static void main(String[] args) {
        Thread back = new Thread(new MainBack());
        back.start();
        createAndShowGUI();
        setupTray();
    }

    static class SlideListener implements ChangeListener {
        SlideListener() {
        }

        public synchronized void stateChanged(ChangeEvent e) {

        }
    }

}
