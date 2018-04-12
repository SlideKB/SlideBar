package com.github.slidekb.front;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.github.slidekb.api.SlideBarPlugin;
import com.github.slidekb.back.MainBack;
import com.github.slidekb.util.SettingsHelper;
import javax.swing.JLayeredPane;

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
    private static JTextField textField;
    private static JTextField textField_1;
    private static JTextField textField_2;
    private static JTextField textField_3;

    /**
     * Create the frame.
     * 
     * @wbp.parser.entryPoint
     */
    public static void createAndShowGUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(100, 100, 1135, 546);
        frame.setPreferredSize(new Dimension(1140, 546));
        frame.setMinimumSize(new Dimension(1140, 546));
        frame.setMaximumSize(new Dimension(1140, 546));
        try {
            frame.setIconImage(ImageIO.read(new File("icon.png")));
        } catch (IOException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setContentPane(contentPane);
        contentPane.setLayout(null);

        ArrayList<String> arrayPlugins = new ArrayList<String>();
        for (SlideBarPlugin p : MainBack.pluginMan.getProci()) {
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
                        temp = MainBack.pluginMan.getProci();

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

                                try {
                                    textField.setText(SettingsHelper.getUsedSliderAtIndex(p.getClass().getCanonicalName(), 0));
                                } catch (Exception e3) {
                                    textField.setText("");
                                }

                                try {
                                    textField_1.setText(SettingsHelper.getUsedSliderAtIndex(p.getClass().getCanonicalName(), 1));
                                } catch (Exception e3) {
                                    textField_1.setText("");
                                }

                                try {
                                    textField_2.setText(SettingsHelper.getUsedSliderAtIndex(p.getClass().getCanonicalName(), 2));
                                } catch (Exception e3) {
                                    textField_2.setText("");
                                }

                                try {
                                    textField_3.setText(SettingsHelper.getUsedSliderAtIndex(p.getClass().getCanonicalName(), 3));
                                } catch (Exception e3) {
                                    textField_3.setText("");
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
        chckbxAlwaysRun.setBounds(329, 425, 146, 23);
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
                JFrame frameDialog = new JFrame("Add a process");
                String process = JOptionPane.showInputDialog(frameDialog, "Enter a Process");
                System.out.println("added " + process);
                for (SlideBarPlugin p : temp) {
                    if (p.getLabelName().equals(element)) {
                        SettingsHelper.addProcess(p.getClass().getCanonicalName(), process);
                        arrayProcess.add(process);
                        String proci[] = arrayProcess.toArray(new String[arrayProcess.size()]);
                        processList.setListData(proci);
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
        ActionListener actionListenerProcessMinus = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                for (SlideBarPlugin p : temp) {
                    if (p.getLabelName().equals(element)) {
                        arrayProcess.remove(processList.getSelectedValue());
                        SettingsHelper.removeProcess(p.getClass().getCanonicalName(), processList.getSelectedValue());
                        String proci[] = arrayProcess.toArray(new String[arrayProcess.size()]);
                        processList.setListData(proci);
                        frame.revalidate();
                        frame.repaint();
                    }
                }
            }
        };
        processMinusButton.addActionListener(actionListenerProcessMinus);

        JButton hotkeyMinusButton = new JButton("-");
        hotkeyMinusButton.setBounds(768, 289, 45, 22);
        contentPane.add(hotkeyMinusButton);
        ActionListener actionListenerHotkeyMinus = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                for (SlideBarPlugin p : temp) {
                    if (p.getLabelName().equals(element)) {
                        arrayHotkey.remove(hotkeyList.getSelectedValue());
                        SettingsHelper.removeHotkey(p.getClass().getCanonicalName(), hotkeyList.getSelectedValue());
                        String hotkeys[] = arrayHotkey.toArray(new String[arrayHotkey.size()]);
                        hotkeyList.setListData(hotkeys);
                        frame.revalidate();
                        frame.repaint();
                    }
                }
            }
        };
        hotkeyMinusButton.addActionListener(actionListenerHotkeyMinus);

        JButton hotkeyAddButton = new JButton("+");
        hotkeyAddButton.setBounds(768, 256, 45, 22);
        contentPane.add(hotkeyAddButton);

        JLabel lblSliders = new JLabel("Sliders");
        lblSliders.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblSliders.setBounds(828, 12, 69, 20);
        contentPane.add(lblSliders);

        JPanel panel = new JPanel();
        panel.setBounds(828, 44, 269, 95);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(15, 56, 33, 20);
        panel.add(lblId);

        textField = new JTextField();
        // Listen for changes in the text
        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                System.out.println("saving: " + textField.getText());
                for (SlideBarPlugin p : temp) {
                    if (p.getLabelName().equals(element)) {
                        SettingsHelper.setUsedSliderAtIndex(p.getClass().getCanonicalName(), 0, textField.getText());
                    }
                }
            }
        });

        textField.setBounds(63, 53, 83, 26);
        panel.add(textField);
        textField.setColumns(10);

        JLabel lblFirstSlider = new JLabel("First Slider");
        lblFirstSlider.setBounds(15, 16, 103, 20);
        panel.add(lblFirstSlider);

        JCheckBox chckbxReverse = new JCheckBox("Reversed");
        chckbxReverse.setBounds(157, 52, 127, 29);
        panel.add(chckbxReverse);

        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel_1.setBounds(828, 155, 269, 95);
        contentPane.add(panel_1);

        JLabel label = new JLabel("ID:");
        label.setBounds(15, 56, 33, 20);
        panel_1.add(label);

        textField_1 = new JTextField();
        textField_1.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                System.out.println("saving: " + textField_1.getText());
                for (SlideBarPlugin p : temp) {
                    if (p.getLabelName().equals(element)) {
                        SettingsHelper.setUsedSliderAtIndex(p.getClass().getCanonicalName(), 1, textField_1.getText());
                    }
                }
            }
        });
        textField_1.setColumns(10);
        textField_1.setBounds(63, 53, 83, 26);
        panel_1.add(textField_1);

        JLabel lblSecondSlider = new JLabel("Second Slider");
        lblSecondSlider.setBounds(15, 16, 103, 20);
        panel_1.add(lblSecondSlider);

        JCheckBox checkBox = new JCheckBox("Reversed");
        checkBox.setBounds(157, 52, 127, 29);
        panel_1.add(checkBox);

        JPanel panel_2 = new JPanel();
        panel_2.setLayout(null);
        panel_2.setBounds(828, 266, 269, 95);
        contentPane.add(panel_2);

        JLabel label_2 = new JLabel("ID:");
        label_2.setBounds(15, 56, 33, 20);
        panel_2.add(label_2);

        textField_2 = new JTextField();
        textField_2.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                System.out.println("saving: " + textField_2.getText());
                for (SlideBarPlugin p : temp) {
                    if (p.getLabelName().equals(element)) {
                        SettingsHelper.setUsedSliderAtIndex(p.getClass().getCanonicalName(), 2, textField_2.getText());
                    }
                }
            }
        });
        textField_2.setColumns(10);
        textField_2.setBounds(63, 53, 83, 26);
        panel_2.add(textField_2);

        JLabel lblThirdSlider = new JLabel("Third Slider");
        lblThirdSlider.setBounds(15, 16, 103, 20);
        panel_2.add(lblThirdSlider);

        JCheckBox checkBox_1 = new JCheckBox("Reversed");
        checkBox_1.setBounds(157, 52, 127, 29);
        panel_2.add(checkBox_1);

        JPanel panel_3 = new JPanel();
        panel_3.setLayout(null);
        panel_3.setBounds(828, 377, 269, 95);
        contentPane.add(panel_3);

        JLabel label_4 = new JLabel("ID:");
        label_4.setBounds(15, 56, 33, 20);
        panel_3.add(label_4);

        textField_3 = new JTextField();
        textField_3.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                System.out.println("saving: " + textField_3.getText());
                for (SlideBarPlugin p : temp) {
                    if (p.getLabelName().equals(element)) {
                        SettingsHelper.setUsedSliderAtIndex(p.getClass().getCanonicalName(), 3, textField_3.getText());
                    }
                }
            }
        });
        textField_3.setColumns(10);
        textField_3.setBounds(63, 53, 83, 26);
        panel_3.add(textField_3);

        JLabel lblForthSlider = new JLabel("Forth Slider");
        lblForthSlider.setBounds(15, 16, 103, 20);
        panel_3.add(lblForthSlider);

        JCheckBox checkBox_2 = new JCheckBox("Reversed");
        checkBox_2.setBounds(157, 52, 127, 29);
        panel_3.add(checkBox_2);
        ActionListener actionListenerHotkeyAdd = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame frameDialog = new JFrame("Add a hotkey");
                String hotkey = JOptionPane.showInputDialog(frameDialog, "Enter a Hotkey");
                System.out.println("added " + hotkey);
                for (SlideBarPlugin p : temp) {
                    if (p.getLabelName().equals(element)) {
                        SettingsHelper.addHotkey(p.getClass().getCanonicalName(), hotkey);
                        arrayHotkey.add(hotkey);
                        String hotkeys[] = arrayHotkey.toArray(new String[arrayHotkey.size()]);
                        hotkeyList.setListData(hotkeys);
                        frame.revalidate();
                        frame.repaint();
                    }
                }
            }
        };
        hotkeyAddButton.addActionListener(actionListenerHotkeyAdd);

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
}
