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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Mutable;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.back.MainBack;

import javax.swing.JComboBox;

public class UI {

    private static JFrame frame;
    private static JLabel lblConnected;
    private static ThisConfig cfg;

    @Config.Sources({ "classpath:Program/Main.properties" })
    private static interface ThisConfig extends Accessible, Mutable {
        @DefaultValue("default")
        String SliderDefault1();

        @DefaultValue("default2")
        String SliderDefault2();

        @DefaultValue("default3")
        String SliderDefault3();

        @DefaultValue("default4")
        String SliderDefault4();

        @DefaultValue("false")
        boolean SliderDefault1Reversed();

        @DefaultValue("false")
        boolean SliderDefault2Reversed();

        @DefaultValue("false")
        boolean SliderDefault3Reversed();

        @DefaultValue("false")
        boolean SliderDefault4Reversed();

        @DefaultValue("false")
        boolean RunOnStartUp();

        @DefaultValue("false")
        Boolean CheckForUpdates();
    }

    private static String SliderDefault1;
    private static String SliderDefault2;
    private static String SliderDefault3;
    private static String SliderDefault4;
    private static boolean slider1Reversed;
    private static boolean slider2Reversed;
    private static boolean slider3Reversed;
    private static boolean slider4Reversed;

    public static void readFromConfig() throws IOException {
        FileInputStream in = new FileInputStream("classpath:Program/Main.properties");
        cfg.load(in);
        in.close();
        SliderDefault1 = cfg.SliderDefault1();
        SliderDefault2 = cfg.SliderDefault2();
        SliderDefault3 = cfg.SliderDefault3();
        SliderDefault4 = cfg.SliderDefault4();
        slider1Reversed = cfg.SliderDefault1Reversed();
        slider2Reversed = cfg.SliderDefault2Reversed();
        slider3Reversed = cfg.SliderDefault3Reversed();
        slider4Reversed = cfg.SliderDefault4Reversed();
        System.out.println("reload complete");
    }

    public static void writeToConfig() throws IOException {
        FileInputStream in = new FileInputStream(ClassLoader.getSystemClassLoader().getResource(".").getPath() + "Program/Main.properties");
        Properties props = new Properties();
        props.load(in);
        in.close();

        FileOutputStream out = new FileOutputStream(ClassLoader.getSystemClassLoader().getResource(".").getPath() + "Program/Main.properties");
        System.out.println(ClassLoader.getSystemClassLoader().getResource(".").getPath() + "Program/Main.properties");
        props.setProperty("SliderDefault1", "testing");
        props.setProperty("SliderDefault2", SliderDefault2);
        props.setProperty("SliderDefault3", SliderDefault3);
        props.setProperty("SliderDefault4", SliderDefault4);
        props.setProperty("slider1Reversed", slider1Reversed + "");
        props.setProperty("slider2Reversed", slider2Reversed + "");
        props.setProperty("slider3Reversed", slider3Reversed + "");
        props.setProperty("slider4Reversed", slider4Reversed + "");
        props.store(out, null);
        out.close();
        System.out.println("Saved");
    }

    public static void main(String[] args) throws InterruptedException {
        Thread back = new Thread(new MainBack());
        back.start();
        MainBack.pluginMan.waitUntilProcessesLoaded();
        cfg = ConfigFactory.create(ThisConfig.class);
        try {
            readFromConfig();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        createAndShowGUI();
        setupTray();
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

    /**
     * Initialize the contents of the frame.
     * 
     * @wbp.parser.entryPoint
     */
    private static void createAndShowGUI() {
        // if (frame == null) {
        frame = new JFrame();
        frame.getContentPane().setLayout(null);
        frame.setBounds(100, 100, 989, 1008);
        frame.setPreferredSize(new Dimension(989, 1008));
        frame.setMinimumSize(new Dimension(989, 1008));
        frame.setMaximumSize(new Dimension(989, 1008));
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(15, 61, 177, 203);
        frame.getContentPane().add(layeredPane);

        if (MainBack.getSliderManager().getDefaultSlider() != null && MainBack.getSliderManager().getDefaultSlider().getID() != null) {
            JLabel lblSlider = new JLabel("Default Slider 1");
            lblSlider.setBounds(15, 16, 147, 20);
            layeredPane.add(lblSlider);
            lblSlider.setFont(new Font("Tahoma", Font.PLAIN, 20));

            JCheckBox chckbxReversed = new JCheckBox("Reversed");
            chckbxReversed.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        MainBack.getSliderManager().getDefaultSlider().setReversed(true);
                        System.out.println(cfg.SliderDefault1Reversed());
                        System.out.println("[UI] setting default slider 1 to reverse direction");

                    } else {
                        MainBack.getSliderManager().getDefaultSlider().setReversed(false);

                        System.out.println("[UI] setting default slider 1 to standard direction");
                    }
                    ;
                }
            });
            chckbxReversed.setBounds(11, 156, 139, 29);
            chckbxReversed.setSelected(cfg.SliderDefault1Reversed());
            layeredPane.add(chckbxReversed);

            JLabel lblId = new JLabel("ID: " + MainBack.getSliderManager().getDefaultSlider().getID());
            lblId.setBounds(15, 52, 69, 20);
            layeredPane.add(lblId);

            JLabel lblSize = new JLabel("Size:");
            lblSize.setBounds(15, 88, 69, 20);
            layeredPane.add(lblSize);

            JLabel lblPort = new JLabel("Port: ");
            lblPort.setBounds(15, 124, 69, 20);
            layeredPane.add(lblPort);
        }

        if (MainBack.getSliderManager().getDefaultSlider2() != null && MainBack.getSliderManager().getDefaultSlider2().getID() != null) {
            JButton btnNewButton = new JButton(">");
            btnNewButton.setBounds(206, 127, 45, 29);
            frame.getContentPane().add(btnNewButton);

            JButton button = new JButton("<");
            button.setBounds(206, 172, 45, 29);
            frame.getContentPane().add(button);

            JLayeredPane layeredPane_1 = new JLayeredPane();
            layeredPane_1.setBounds(266, 61, 177, 203);
            frame.getContentPane().add(layeredPane_1);

            JCheckBox checkBox = new JCheckBox("Reversed");
            checkBox.setBounds(11, 156, 139, 29);
            layeredPane_1.add(checkBox);

            JLabel label_1 = new JLabel("ID: ");
            label_1.setBounds(15, 52, 69, 20);
            layeredPane_1.add(label_1);

            JLabel label_2 = new JLabel("Size:");
            label_2.setBounds(15, 88, 69, 20);
            layeredPane_1.add(label_2);

            JLabel label_3 = new JLabel("Port: ");
            label_3.setBounds(15, 124, 69, 20);
            layeredPane_1.add(label_3);

            JLabel lblDefaultSlider = new JLabel("Default Slider 2");
            lblDefaultSlider.setFont(new Font("Tahoma", Font.PLAIN, 20));
            lblDefaultSlider.setBounds(15, 16, 147, 20);
            layeredPane_1.add(lblDefaultSlider);

        }

        if (MainBack.getSliderManager().getDefaultSlider3() != null && MainBack.getSliderManager().getDefaultSlider3().getID() != null) {
            JButton button_1 = new JButton(">");
            button_1.setBounds(458, 127, 45, 29);
            frame.getContentPane().add(button_1);

            JButton button_2 = new JButton("<");
            button_2.setBounds(458, 172, 45, 29);
            frame.getContentPane().add(button_2);

            JLayeredPane layeredPane_2 = new JLayeredPane();
            layeredPane_2.setBounds(518, 61, 177, 203);
            frame.getContentPane().add(layeredPane_2);

            JCheckBox checkBox_1 = new JCheckBox("Reversed");
            checkBox_1.setBounds(11, 156, 139, 29);
            layeredPane_2.add(checkBox_1);

            JLabel label_5 = new JLabel("ID: ");
            label_5.setBounds(15, 52, 69, 20);
            layeredPane_2.add(label_5);

            JLabel label_6 = new JLabel("Size:");
            label_6.setBounds(15, 88, 69, 20);
            layeredPane_2.add(label_6);

            JLabel label_7 = new JLabel("Port: ");
            label_7.setBounds(15, 124, 69, 20);
            layeredPane_2.add(label_7);

            JLabel lblDefaultSlider_1 = new JLabel("Default Slider 3");
            lblDefaultSlider_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
            lblDefaultSlider_1.setBounds(15, 16, 147, 20);
            layeredPane_2.add(lblDefaultSlider_1);
        }
        if (MainBack.getSliderManager().getDefaultSlider4() != null && MainBack.getSliderManager().getDefaultSlider4().getID() != null) {
            JButton button_3 = new JButton(">");
            button_3.setBounds(710, 127, 45, 29);
            frame.getContentPane().add(button_3);

            JButton button_4 = new JButton("<");
            button_4.setBounds(710, 172, 45, 29);
            frame.getContentPane().add(button_4);

            JLayeredPane layeredPane_3 = new JLayeredPane();
            layeredPane_3.setBounds(770, 61, 177, 203);
            frame.getContentPane().add(layeredPane_3);

            JCheckBox checkBox_2 = new JCheckBox("Reversed");
            checkBox_2.setBounds(11, 156, 139, 29);
            layeredPane_3.add(checkBox_2);

            JLabel label_9 = new JLabel("ID: ");
            label_9.setBounds(15, 52, 69, 20);
            layeredPane_3.add(label_9);

            JLabel label_10 = new JLabel("Size:");
            label_10.setBounds(15, 88, 69, 20);
            layeredPane_3.add(label_10);

            JLabel label_11 = new JLabel("Port: ");
            label_11.setBounds(15, 124, 69, 20);
            layeredPane_3.add(label_11);

            JLabel lblDefaultSlider_2 = new JLabel("Default Slider 4");
            lblDefaultSlider_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
            lblDefaultSlider_2.setBounds(15, 16, 147, 20);
            layeredPane_3.add(lblDefaultSlider_2);

        }

        JLabel lblSlidersConnected = new JLabel("Sliders Connected");
        lblSlidersConnected.setFont(new Font("Tahoma", Font.PLAIN, 36));
        lblSlidersConnected.setBounds(15, 16, 434, 29);
        frame.getContentPane().add(lblSlidersConnected);

        JLabel lblProgramSettings = new JLabel("Program Settings");
        lblProgramSettings.setFont(new Font("Tahoma", Font.PLAIN, 36));
        lblProgramSettings.setBounds(15, 275, 339, 44);
        frame.getContentPane().add(lblProgramSettings);

        JCheckBox chckbxRunOnStartup = new JCheckBox("Run on startup");
        chckbxRunOnStartup.setBounds(15, 331, 139, 29);
        frame.getContentPane().add(chckbxRunOnStartup);

        JCheckBox chckbxCheckForUpdates = new JCheckBox("Check for updates");
        chckbxCheckForUpdates.setBounds(15, 368, 177, 29);
        frame.getContentPane().add(chckbxCheckForUpdates);

        JLabel lblPluginSettings = new JLabel("Plugin Settings");
        lblPluginSettings.setFont(new Font("Tahoma", Font.PLAIN, 36));
        lblPluginSettings.setBounds(15, 409, 339, 44);
        frame.getContentPane().add(lblPluginSettings);

        JButton btnTestSliders = new JButton("Test \r\nSliders");
        btnTestSliders.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnTestSliders.setBounds(601, 331, 139, 66);
        frame.getContentPane().add(btnTestSliders);

        JButton btnReconnect = new JButton("Reconnect");
        btnReconnect.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnReconnect.setBounds(447, 331, 139, 66);
        btnReconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblConnected.setText("Reconnecting");
                if (MainBack.reconnect()) {
                    lblConnected.setText("Connected");
                } else {
                    lblConnected.setText("Not Connected");
                }
            }
        });
        frame.getContentPane().add(btnReconnect);

        // JList list = new JList();
        // list.setBounds(15, 469, 468, 467);
        // frame.getContentPane().add(list);

        ArrayList<String> arrayProcess = new ArrayList<String>();
        for (SlideBarPlugin p : MainBack.pluginMan.getProci()) {
            arrayProcess.add(p.getLabelName());
            System.out.println("adding to list: " + p.getLabelName());
        }
        String proci[] = arrayProcess.toArray(new String[arrayProcess.size()]);
        JList<String> processList = new JList<String>(proci);
        // JList<String> processList = new JList<String>();
        JScrollPane pane = new JScrollPane(processList);
        pane.setBounds(15, 469, 468, 467);
        frame.getContentPane().add(pane);

        JLayeredPane layeredPane_4 = new JLayeredPane();
        layeredPane_4.setBounds(498, 715, 449, 221);
        frame.getContentPane().add(layeredPane_4);

        JLabel lblSlider_1 = new JLabel("Slider 1");
        lblSlider_1.setBounds(15, 65, 69, 20);
        layeredPane_4.add(lblSlider_1);

        JComboBox comboBox = new JComboBox();
        comboBox.setBounds(83, 65, 148, 26);
        layeredPane_4.add(comboBox);

        JLabel lblPluginSliderAssignment = new JLabel("Plugin Slider Assignment");
        lblPluginSliderAssignment.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblPluginSliderAssignment.setBounds(15, 16, 303, 33);
        layeredPane_4.add(lblPluginSliderAssignment);

        JLabel lblSlider_2 = new JLabel("Slider 2");
        lblSlider_2.setBounds(15, 101, 69, 20);
        layeredPane_4.add(lblSlider_2);

        JComboBox comboBox_1 = new JComboBox();
        comboBox_1.setBounds(83, 101, 148, 26);
        layeredPane_4.add(comboBox_1);

        JLabel lblSlider_3 = new JLabel("Slider 3");
        lblSlider_3.setBounds(15, 137, 69, 20);
        layeredPane_4.add(lblSlider_3);

        JComboBox comboBox_2 = new JComboBox();
        comboBox_2.setBounds(83, 137, 148, 26);
        layeredPane_4.add(comboBox_2);

        JLabel lblSlider_4 = new JLabel("Slider 4");
        lblSlider_4.setBounds(15, 173, 69, 20);
        layeredPane_4.add(lblSlider_4);

        JComboBox comboBox_3 = new JComboBox();
        comboBox_3.setBounds(83, 173, 148, 26);
        layeredPane_4.add(comboBox_3);

        JButton btnInfo = new JButton("Details");
        btnInfo.setBounds(350, 62, 84, 29);
        layeredPane_4.add(btnInfo);

        JButton button_5 = new JButton("Details");
        button_5.setBounds(350, 98, 84, 29);
        layeredPane_4.add(button_5);

        JButton button_6 = new JButton("Details");
        button_6.setBounds(350, 134, 84, 29);
        layeredPane_4.add(button_6);

        JButton button_7 = new JButton("Details");
        button_7.setBounds(350, 173, 84, 29);
        layeredPane_4.add(button_7);

        JCheckBox checkBox_3 = new JCheckBox("Reversed");
        checkBox_3.setBounds(242, 65, 97, 29);
        layeredPane_4.add(checkBox_3);

        JCheckBox checkBox_4 = new JCheckBox("Reversed");
        checkBox_4.setBounds(242, 101, 97, 29);
        layeredPane_4.add(checkBox_4);

        JCheckBox checkBox_5 = new JCheckBox("Reversed");
        checkBox_5.setBounds(242, 137, 97, 29);
        layeredPane_4.add(checkBox_5);

        JCheckBox checkBox_6 = new JCheckBox("Reversed");
        checkBox_6.setBounds(242, 173, 97, 29);
        layeredPane_4.add(checkBox_6);

        JLabel lblSliderSettings = new JLabel("LabelName");
        lblSliderSettings.setFont(new Font("Tahoma", Font.PLAIN, 36));
        lblSliderSettings.setBounds(498, 470, 197, 47);
        frame.getContentPane().add(lblSliderSettings);

        JLabel lblRunsWithBased = new JLabel("Runs based on active program");
        lblRunsWithBased.setBounds(498, 533, 255, 20);
        frame.getContentPane().add(lblRunsWithBased);

        JButton btnConfigureList = new JButton("Edit Program List");
        btnConfigureList.setBounds(780, 529, 172, 29);
        frame.getContentPane().add(btnConfigureList);

        JCheckBox chckbxEnabled = new JCheckBox("Enabled");
        chckbxEnabled.setBounds(492, 565, 139, 29);
        frame.getContentPane().add(chckbxEnabled);

        JButton btnOpenConfigFile = new JButton("Open Config File");
        btnOpenConfigFile.setBounds(498, 670, 157, 29);
        frame.getContentPane().add(btnOpenConfigFile);

        JButton btnSaveAndReload = new JButton("Save and Reload");
        btnSaveAndReload.setBounds(790, 670, 157, 29);
        frame.getContentPane().add(btnSaveAndReload);

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(670, 670, 105, 29);
        frame.getContentPane().add(btnSave);

        JButton btnReloadAllPlugins = new JButton("Reload Plugins");
        btnReloadAllPlugins.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnReloadAllPlugins.setBounds(755, 331, 192, 66);
        frame.getContentPane().add(btnReloadAllPlugins);

        JLabel lblStatus = new JLabel("Running: ");
        lblStatus.setBounds(223, 335, 69, 20);
        frame.getContentPane().add(lblStatus);

        lblConnected = new JLabel(MainBack.isStarted() + "");
        lblConnected.setBounds(286, 335, 90, 20);
        frame.getContentPane().add(lblConnected);
        // }
        frame.pack();
        bringToFront();
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

    private static void bringToFront() {
        try {
            readFromConfig();
        } catch (IOException e) {
            System.out.println("[UI] failed to read from config");
        }
        getInstance().setVisible(true);
        getInstance().setExtendedState(JFrame.NORMAL);
        getInstance().toFront();
        getInstance().repaint();
    }

    private static JFrame getInstance() {
        return frame;
    }
}
