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
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.back.MainBack;
import com.github.slidekb.util.Misc;
import com.github.slidekb.util.Update;

import javax.swing.JProgressBar;
import javax.swing.JSlider;

public class MainFront {

    private static JFrame frame;
    private static JList list;
	private static JList pluginList;
	private static JProgressBar progressBar1 = new JProgressBar(0, 1022);
	private static JProgressBar progressBar2 = new JProgressBar(0, 1022);
	private static JProgressBar progressBar3 = new JProgressBar(0, 1022);
	private static JProgressBar progressBar4 = new JProgressBar(0, 1022);

    /**
     * The "About" Jframe
     * @wbp.parser.entryPoint
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
                frame.setIconImage(ImageIO.read(MainFront.class.getResourceAsStream("/icon.png")));
            } catch (IOException e3) {
                // TODO Auto-generated catch block
                e3.printStackTrace();
            }

            list = new JList();
            list.setListData(MainBack.getSliderManager().getSliderIDList());
            list.setBounds(15, 52, 244, 67);
            frame.getContentPane().add(list);

            JLabel lblSlidersConnected = new JLabel("Sliders Connected");
            lblSlidersConnected.setBounds(15, 16, 146, 20);
            frame.getContentPane().add(lblSlidersConnected);

            JButton slidersVibrate = new JButton("Vibrate Sliders");
            slidersVibrate.setBounds(15, 235, 115, 29);
            frame.getContentPane().add(slidersVibrate);

            JButton sliderConfigure = new JButton("Configure");
            sliderConfigure.setBounds(145, 235, 115, 29);
            frame.getContentPane().add(sliderConfigure);

            pluginList = new JList();
            pluginList.setBounds(298, 52, 244, 167);
            ArrayList<String> temp = new ArrayList<String>();
            for (SlideBarPlugin p : MainBack.pluginMan.getProci()){
            	temp.add(p.getLabelName());
            }
            pluginList.setListData(temp.toArray(new String[temp.size()]));
            frame.getContentPane().add(pluginList);

            JLabel lblPluginsLoaded = new JLabel("Plugins Loaded");
            lblPluginsLoaded.setBounds(298, 16, 115, 20);
            frame.getContentPane().add(lblPluginsLoaded);
            ActionListener actionListenerReload = new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    MainBack.stop();
                    MainBack.startIt();
                    list.setListData(MainBack.getSliderManager().getSliderIDList());
                    ArrayList<String> temp = new ArrayList<String>();
                    for (SlideBarPlugin p : MainBack.pluginMan.getProci()){
                    	temp.add(p.getLabelName());
                    }
                    pluginList.setListData(temp.toArray(new String[temp.size()]));
                }
            };
            ActionListener actionListenerVibrate = new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    MainBack.testVibrate(5);
                }
            };

            JButton pluginsReload = new JButton("Reload");
            pluginsReload.setBounds(299, 235, 115, 29);
            frame.getContentPane().add(pluginsReload);
            pluginsReload.addActionListener(actionListenerReload);
            slidersVibrate.addActionListener(actionListenerVibrate);

            JButton pluginsConfigure = new JButton("Configure");
            pluginsConfigure.setBounds(427, 235, 115, 29);
            frame.getContentPane().add(pluginsConfigure);
            
            JLabel lblSlider1 = new JLabel("Slider 1");
            lblSlider1.setBounds(15, 130, 46, 14);
            JLabel lblSlider2 = new JLabel("Slider 2");
            lblSlider2.setBounds(15, 150, 46, 14);
            JLabel lblSlider3 = new JLabel("Slider 3");
            lblSlider3.setBounds(15, 170, 46, 14);
            JLabel lblSlider4 = new JLabel("Slider 4");
            lblSlider4.setBounds(15, 190, 46, 14);
            
            
            progressBar1.setBounds(56, 130, 203, 14);
            if (MainBack.getSliderManager().getSliderIDList().length >= 1){
            	frame.getContentPane().add(progressBar1);
            	frame.getContentPane().add(lblSlider1);
            }
            progressBar2.setBounds(56, 150, 203, 14);
            if (MainBack.getSliderManager().getSliderIDList().length >= 2){
            	frame.getContentPane().add(progressBar2);
            	frame.getContentPane().add(lblSlider2);
            }
            progressBar3.setBounds(56, 170, 203, 14);
            if (MainBack.getSliderManager().getSliderIDList().length >= 3){
            	frame.getContentPane().add(progressBar3);
            	frame.getContentPane().add(lblSlider3);
            }
            progressBar4.setBounds(56, 190, 203, 14);
            if (MainBack.getSliderManager().getSliderIDList().length == 4){
            	frame.getContentPane().add(progressBar4);
            	frame.getContentPane().add(lblSlider4);
            }
            
            
//            JSlider slider = new JSlider();
//            slider.setBounds(56, 141, 203, 26);
//            frame.getContentPane().add(slider);
            
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
//         Display the window.
        frame.pack();

        bringToFront();
    }
    
    public static void updateSliderInfo(String ID, int value){
    	if (MainBack.getSliderManager().getSliderIDList()[0].equals(ID)){
    		progressBar1.setValue(1022 - value);
    	}
    	if (MainBack.getSliderManager().getSliderIDList()[1].equals(ID)){
    		progressBar2.setValue(1022 - value);
    	}
    	if (MainBack.getSliderManager().getSliderIDList()[2].equals(ID)){
    		progressBar3.setValue(1022 - value);
    	}
    	if (MainBack.getSliderManager().getSliderIDList()[3].equals(ID)){
    		progressBar4.setValue(1022 - value);
    	}
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
            final TrayIcon trayIcon = new TrayIcon(ImageIO.read(MainFront.class.getResourceAsStream("/icon.png")), "SlideBar Configuration");
            trayIcon.setImageAutoSize(true);
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
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     * @wbp.parser.entryPoint
     */
    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        Update update = new Update();
       
        String version = update.isAndGetUpdate("1.2.3");
        if (version != ""){
        	Object[] options = {"Download",
            "Ignore!"};
			int response = JOptionPane.showOptionDialog(frame,
			"New version (" + version + ") is available at http://www.slidekb.com/pages/software",
			"Update Available",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,
			options,  
			options[0]); 
			if (response == JOptionPane.NO_OPTION) {
		      System.out.println("MainFront->main()-> ignore button clicked");
		    } else if (response == JOptionPane.YES_OPTION) {
		    	Misc.launchUrl("http://www.slidekb.com/pages/software");
		    } else if (response == JOptionPane.CLOSED_OPTION) {
		      System.out.println("MainFront->main()-> JOptionPane closed");
		    }
        }
        Thread back = new Thread(new MainBack());
        back.start();
        MainBack.pluginMan.waitUntilProcessesLoaded();
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
