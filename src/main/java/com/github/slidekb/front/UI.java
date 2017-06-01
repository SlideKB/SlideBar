package com.github.slidekb.front;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JComboBox;

public class UI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI window = new UI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 989, 1008);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBounds(15, 61, 177, 203);
		frame.getContentPane().add(layeredPane);
		
		JLabel lblSlider = new JLabel("Default Slider 1");
		lblSlider.setBounds(15, 16, 147, 20);
		layeredPane.add(lblSlider);
		lblSlider.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		JCheckBox chckbxReversed = new JCheckBox("Reversed");
		chckbxReversed.setBounds(11, 156, 139, 29);
		layeredPane.add(chckbxReversed);
		
		JLabel lblId = new JLabel("ID:");
		lblId.setBounds(15, 52, 69, 20);
		layeredPane.add(lblId);
		
		JLabel lblSize = new JLabel("Size:");
		lblSize.setBounds(15, 88, 69, 20);
		layeredPane.add(lblSize);
		
		JLabel lblPort = new JLabel("Port: ");
		lblPort.setBounds(15, 124, 69, 20);
		layeredPane.add(lblPort);
		
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
		
		JLabel label_1 = new JLabel("ID:");
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
		
		JLabel label_5 = new JLabel("ID:");
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
		
		JLabel label_9 = new JLabel("ID:");
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
		frame.getContentPane().add(btnReconnect);
		
		JList list = new JList();
		list.setBounds(15, 469, 468, 467);
		frame.getContentPane().add(list);
		
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
	}
}
