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

package front;

import back.MainBack;
import back.Process;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by JackSec on 4/1/2017.
 */
public class ProcessListSelector {

    JFrame frame;

    ArrayList<String> left = new ArrayList<String>();

    ArrayList<String> right = new ArrayList<String>();

    ArrayList<String> tempRight = new ArrayList<String>();

    private String configName;

    private String labelName;

    public ProcessListSelector(String configName, String labelName) {
        this.configName = configName;
        this.labelName = labelName;
    }

    public void readFile() {
        FileInputStream in = null;
        right.clear();
        tempRight.clear();
        try {
            in = new FileInputStream(ClassLoader.getSystemClassLoader().getResource(".").getPath() + "\\configs\\" + configName);
            Properties props = new Properties();
            props.load(in);
            in.close();
            String list = props.getProperty("processList");
            String[] temp = list.split(", ");
            for (String s : temp) {
                addToRight(s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToRight(String given) {
        addToTempRight(given);
        if (right.contains(given)) {
            right.remove(given);
        }
        right.add(given);
    }

    private void addToTempRight(String given) {
        if (tempRight.contains(given)) {
            tempRight.remove(given);
        }
        tempRight.add(given);
    }

    public void removeFromRight(String given) {
        removeFromTempRight(given);
        if (right.contains(given)) {
            right.remove(given);
        }
    }

    private void removeFromTempRight(String given) {
        if (tempRight.contains(given)) {
            tempRight.remove(given);
        }
    }

    public JFrame createGUI() {
        readFile();
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setBounds(100, 100, 450, 300);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[] { 0, 102, 77, 0, 102, 77, 0, 0 };
        gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
        gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        gbl_contentPane.rowWeights = new double[] { 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
        contentPane.setLayout(gbl_contentPane);
        JList<String> list = new JList<>(MainBack.getPrev20());
        JScrollPane pane = new JScrollPane(list);
        GridBagConstraints gbc_list = new GridBagConstraints();
        gbc_list.gridwidth = 2;
        gbc_list.gridheight = 2;
        gbc_list.insets = new Insets(0, 0, 5, 5);
        gbc_list.fill = GridBagConstraints.BOTH;
        gbc_list.gridx = 1;
        gbc_list.gridy = 1;
        contentPane.add(pane, gbc_list);

        JList<String> list_1 = new JList<>(right.toArray(new String[right.size()]));
        JScrollPane pane_1 = new JScrollPane(list_1);
        GridBagConstraints gbc_list_1 = new GridBagConstraints();
        gbc_list_1.gridwidth = 2;
        gbc_list_1.gridheight = 2;
        gbc_list_1.insets = new Insets(0, 0, 5, 5);
        gbc_list_1.fill = GridBagConstraints.BOTH;
        gbc_list_1.gridx = 4;
        gbc_list_1.gridy = 1;
        contentPane.add(pane_1, gbc_list_1);

        JButton button = new JButton("Add");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected[] = list.getSelectedIndices();
                for (int i = 0; i < selected.length; i++) {
                    String element = (String) list.getModel().getElementAt(selected[i]);
                    addToTempRight(element);
                    list_1.setListData(tempRight.toArray(new String[tempRight.size()]));
                    pane_1.revalidate();
                    pane_1.repaint();
                    for (String s : tempRight) {
                        System.out.println("temp list - " + s);
                    }
                    for (String s : right) {
                        System.out.println("real list - " + s);
                    }
                    System.out.println("---");
                }
            }
        });
        GridBagConstraints gbc_button = new GridBagConstraints();
        gbc_button.insets = new Insets(0, 0, 5, 5);
        gbc_button.gridx = 3;
        gbc_button.gridy = 1;
        contentPane.add(button, gbc_button);

        JButton button_1 = new JButton("Remove");
        button_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected[] = list_1.getSelectedIndices();
                for (int i = 0; i < selected.length; i++) {
                    String element = (String) list_1.getModel().getElementAt(selected[i]);
                    removeFromTempRight(element);
                    list_1.setListData(tempRight.toArray(new String[tempRight.size()]));
                    pane_1.revalidate();
                    pane_1.repaint();
                    for (String s : tempRight) {
                        System.out.println("temp list - " + s);
                    }
                    for (String s : right) {
                        System.out.println("real list - " + s);
                    }
                    System.out.println("---");
                }
            }
        });
        GridBagConstraints gbc_button_1 = new GridBagConstraints();
        gbc_button_1.insets = new Insets(0, 0, 5, 5);
        gbc_button_1.gridx = 3;
        gbc_button_1.gridy = 2;
        contentPane.add(button_1, gbc_button_1);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    right = tempRight;
                    FileInputStream in = new FileInputStream(ClassLoader.getSystemClassLoader().getResource(".").getPath() + "\\configs\\" + configName);
                    Properties props = new Properties();
                    props.load(in);
                    in.close();

                    FileOutputStream out = new FileOutputStream(ClassLoader.getSystemClassLoader().getResource(".").getPath() + "\\configs\\" + configName);
                    String temp = "";
                    for (int i = 0; i < right.size(); i++) {
                        temp = temp + right.get(i);
                        if (i != right.size() - 1) {
                            temp = temp + ", ";
                        }
                    }
                    props.setProperty("processList", temp);
                    props.store(out, null);
                    out.close();
                    updatePlugin();
                    System.out.println("Saved");
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        });
        GridBagConstraints gbc_btnSave = new GridBagConstraints();
        gbc_btnSave.anchor = GridBagConstraints.EAST;
        gbc_btnSave.insets = new Insets(0, 0, 0, 5);
        gbc_btnSave.gridx = 4;
        gbc_btnSave.gridy = 4;
        contentPane.add(btnSave, gbc_btnSave);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tempRight = right;
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        GridBagConstraints gbc_btnCancel = new GridBagConstraints();
        gbc_btnCancel.anchor = GridBagConstraints.WEST;
        gbc_btnCancel.insets = new Insets(0, 0, 0, 5);
        gbc_btnCancel.gridx = 5;
        gbc_btnCancel.gridy = 4;
        contentPane.add(btnCancel, gbc_btnCancel);
        return frame;
    }

    public void updatePlugin() {
        for (Process p : MainBack.PM.getProci()) {
            if (p.getLabelName().equals(this.labelName)) {
                p.reloadPropFile();
            }
        }
    }
}
