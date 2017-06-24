package com.github.slidekb.front;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.back.MainBack;
import com.github.slidekb.util.SettingsHelper;

public class PluginConfiguration {

    private static JFrame frame;
    private static JPanel contentPane;
    private static ArrayList<String> arrayProcess = new ArrayList<String>();
    private static ArrayList<String> arrayHotkey = new ArrayList<String>();
    private static JList<String> processList = new JList<>();
    private static JList<String> hotkeyList = new JList<>();
    private static JCheckBox chckbxAlwaysRun;
    protected static String element;
    protected static ArrayList<SlideBarPlugin> temp;

    /**
     * Create the frame.
     */
    public static void createAndShowGUI() {
        if (frame == null) {
            frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setBounds(100, 100, 839, 546);
            frame.setPreferredSize(new Dimension(839, 546));
            frame.setMinimumSize(new Dimension(839, 546));
            frame.setMaximumSize(new Dimension(839, 546));
            contentPane = new JPanel();
            contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
            frame.setContentPane(contentPane);
            contentPane.setLayout(null);

            ArrayList<String> arrayPlugins = new ArrayList<String>();
            for (SlideBarPlugin p : MainBack.PM.getProci()) {
                arrayPlugins.add(p.getLabelName());
            }
            String plugins[] = arrayPlugins.toArray(new String[arrayPlugins.size()]);
            JList<String> pluginList = new JList<>(plugins);
            MouseListener mouseListener = new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        int selected[] = pluginList.getSelectedIndices();
                        for (int i = 0; i < selected.length; i++) {
                            element = (String) pluginList.getModel().getElementAt(selected[i]);
                            temp = MainBack.PM.getProci();

                            arrayProcess.clear();
                            arrayHotkey.clear();

                            for (SlideBarPlugin p : temp) {
                                if (p.getLabelName().equals(element)) {
                                    System.out.println("Selected Elements:  " + p.getLabelName());
                                    System.out.println("Selected Elements:  " + p.getClass().getCanonicalName());
                                    try {
                                        for (String processName : SettingsHelper.listProcesses(p.getClass().getCanonicalName())) {
                                            arrayProcess.add(processName);
                                        }

                                        String proci[] = arrayProcess.toArray(new String[arrayProcess.size()]);
                                        processList.setListData(proci);
                                    } catch (Exception e1) {
                                        System.out.println("somthing went wrong");
                                    }

                                    try {
                                        for (String processName : SettingsHelper.listHotkeys(p.getClass().getCanonicalName())) {
                                            arrayHotkey.add(processName);
                                        }

                                        String hotkey[] = arrayHotkey.toArray(new String[arrayHotkey.size()]);
                                        hotkeyList.setListData(hotkey);
                                    } catch (Exception e1) {
                                        System.out.println("somthing went wrong");
                                    }
                                    try {
                                        chckbxAlwaysRun.setSelected(SettingsHelper.isAlwaysRun(p.getClass().getCanonicalName()));
                                    } catch (Exception e2) {
                                        System.out.println("something went wrong");
                                    }

                                    frame.revalidate();
                                    frame.repaint();
                                }
                            }
                        }
                    }
                }
            };
            pluginList.addMouseListener(mouseListener);
            pluginList.setBounds(10, 44, 313, 452);
            contentPane.add(pluginList);

            JLabel lblPluginsLoaded = new JLabel("Plugins Loaded");
            lblPluginsLoaded.setFont(new Font("Tahoma", Font.PLAIN, 18));
            lblPluginsLoaded.setBounds(10, 11, 130, 22);
            contentPane.add(lblPluginsLoaded);

            JLabel lblProcesses = new JLabel("Processes");
            lblProcesses.setFont(new Font("Tahoma", Font.PLAIN, 18));
            lblProcesses.setBounds(333, 11, 130, 22);
            contentPane.add(lblProcesses);

            JLabel lblHotkeys = new JLabel("Hotkeys");
            lblHotkeys.setFont(new Font("Tahoma", Font.PLAIN, 18));
            lblHotkeys.setBounds(333, 223, 130, 22);
            contentPane.add(lblHotkeys);

            chckbxAlwaysRun = new JCheckBox("Always Run");
            chckbxAlwaysRun.setBounds(329, 425, 97, 23);
            contentPane.add(chckbxAlwaysRun);
            ActionListener actionListener = new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    boolean selected = chckbxAlwaysRun.getModel().isSelected();
                    for (SlideBarPlugin p : temp) {
                        if (p.getLabelName().equals(element)) {
                            SettingsHelper.setAlwaysRun(p.getClass().getCanonicalName(), selected);
                        }
                    }
                }
            };
            chckbxAlwaysRun.addActionListener(actionListener);
            processList.setBounds(333, 44, 425, 162);
            contentPane.add(processList);

            hotkeyList.setBounds(333, 256, 425, 162);
            contentPane.add(hotkeyList);

            JButton processAddButton = new JButton("+");
            processAddButton.setBounds(768, 44, 45, 22);
            contentPane.add(processAddButton);
            ActionListener actionListenerProcessAdd = new ActionListener() {
            	public void actionPerformed(ActionEvent actionEvent) {
            		JFrame frame = new JFrame("InputDialog Example #1");
                    String process = JOptionPane.showInputDialog(frame, "Enter a Process");
                    System.out.println("added " + process);
                    for (SlideBarPlugin p : temp) {
                        if (p.getLabelName().equals(element)) {
                            SettingsHelper.addProcess(p.getClass().getCanonicalName(), process);
                            frame.revalidate();
                            frame.repaint();
                        }
                    }
                }
            };
            processAddButton.addActionListener(actionListenerProcessAdd);
            
            JButton processMinusButton = new JButton("-");
            processMinusButton.setBounds(768, 77, 45, 22);
            contentPane.add(processMinusButton);

            JButton hotkeyMinusButton = new JButton("-");
            hotkeyMinusButton.setBounds(768, 289, 45, 22);
            contentPane.add(hotkeyMinusButton);

            JButton hotkeyAddButton = new JButton("+");
            hotkeyAddButton.setBounds(768, 256, 45, 22);
            contentPane.add(hotkeyAddButton);

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

    public static void main(String[] args) throws InterruptedException {
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
