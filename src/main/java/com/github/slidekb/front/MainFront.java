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

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.slidekb.back.MainBack;
import com.github.slidekb.util.Misc;

public class MainFront {

    private static JFrame frame;
    private static JList list;

    /**
     * The "About" Jframe
     */
    private static void createAndShowGUI() {
        if (frame == null) {
            frame = new JFrame();
            frame.setTitle("SlideBar Configuration");
            frame.setBounds(100, 100, 818, 339);
            frame.setPreferredSize(new Dimension(600, 339));
            frame.setMinimumSize(new Dimension(600, 339));
            frame.setMaximumSize(new Dimension(600, 339));
            frame.getContentPane().setLayout(null);
            frame.getContentPane().setLayout(null);
            try {
                frame.setIconImage(ImageIO.read(new File("icon.png")));
            } catch (IOException e3) {
                // TODO Auto-generated catch block
                e3.printStackTrace();
            }

            list = new JList();
            list.setListData(MainBack.getSliderManager().getSliderIDList());
            list.setBounds(15, 52, 244, 167);
            frame.getContentPane().add(list);

            JLabel lblSlidersConnected = new JLabel("Sliders Connected");
            lblSlidersConnected.setBounds(15, 16, 146, 20);
            frame.getContentPane().add(lblSlidersConnected);

            JButton sliderReconnect = new JButton("Reconnect");
            sliderReconnect.setBounds(15, 235, 115, 29);
            frame.getContentPane().add(sliderReconnect);

            JButton sliderConfigure = new JButton("Configure");
            sliderConfigure.setBounds(145, 235, 115, 29);
            frame.getContentPane().add(sliderConfigure);

            JList list_1 = new JList();
            list_1.setBounds(298, 52, 244, 167);
            frame.getContentPane().add(list_1);

            JLabel lblPluginsLoaded = new JLabel("Plugins Loaded");
            lblPluginsLoaded.setBounds(298, 16, 115, 20);
            frame.getContentPane().add(lblPluginsLoaded);

            JButton pluginsReload = new JButton("Reload");
            pluginsReload.setBounds(299, 235, 115, 29);
            frame.getContentPane().add(pluginsReload);
            ActionListener actionListenerReload = new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    MainBack.stop();
                    MainBack.startIt();
                    list.setListData(MainBack.getSliderManager().getSliderIDList());
                }
            };
            pluginsReload.addActionListener(actionListenerReload);
            sliderReconnect.addActionListener(actionListenerReload);

            JButton pluginsConfigure = new JButton("Configure");
            pluginsConfigure.setBounds(427, 235, 115, 29);
            frame.getContentPane().add(pluginsConfigure);
            ActionListener actionListenerPluginsConfigure = new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    PluginConfiguration.createAndShowGUI();
                }
            };
            pluginsConfigure.addActionListener(actionListenerPluginsConfigure);

            // JCheckBox chckbxOpenMinimized = new JCheckBox("Open Minimized");
            // chckbxOpenMinimized.setBounds(600, 49, 171, 29);
            // frame.getContentPane().add(chckbxOpenMinimized);
            //
            // JButton btnNewButton_2 = new JButton("Help");
            // btnNewButton_2.setBounds(705, 235, 76, 29);
            // frame.getContentPane().add(btnNewButton_2);
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
        ActionListener launchWebsite = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Misc.launchUrl("http://www.slidekb.com");
            }
        };
        website.addActionListener(launchWebsite);
        MenuItem reload = new MenuItem("Reload");
        ActionListener actionListenerReload = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                MainBack.stop();
                MainBack.startIt();
                list.setListData(MainBack.getSliderManager().getSliderIDList());
            }
        };
        reload.addActionListener(actionListenerReload);
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
            final TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(new URL("http://icons.iconarchive.com/icons/graphicloads/100-flat/256/home-icon.png")), "SlideBar Configuration");
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

    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        Thread back = new Thread(new MainBack());
        back.start();
        MainBack.PM.waitUntilProcessesLoaded();
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
