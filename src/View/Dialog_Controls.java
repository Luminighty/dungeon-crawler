/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Controls;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javafx.scene.input.KeyCode;
import javax.swing.*;

/**
 *
 * @author Luminight
 */
public class Dialog_Controls extends JDialog {

	JButton[] buttons;
	JButton[] altButtons;

	public Dialog_Controls(JFrame parent) {
		super(parent, "Dungeon Crawler - Controls", true);
		setSize(350, 350);
		setIconImage(MainMenu.icon);

		AddComponent();
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setResizable(false);
		setVisible(true);
	}

	void AddComponent() {
		setLayout(new BorderLayout());

		JPanel holder = new JPanel();
		Controls.Key[] keys = Controls.Key.values();
		holder.setLayout(new GridLayout(keys.length + 1, 3, 10, 5));
		Font titleFont = new Font(Font.SERIF, Font.BOLD, 18);
		JLabel title = new JLabel("Control");
		JLabel key = new JLabel("Key");
		JLabel altKey = new JLabel("Alt Key");
		title.setFont(titleFont);
		key.setFont(titleFont);
		altKey.setFont(titleFont);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		key.setHorizontalAlignment(SwingConstants.CENTER);
		altKey.setHorizontalAlignment(SwingConstants.CENTER);
		holder.add(title);
		holder.add(key);
		holder.add(altKey);
		buttons = new JButton[keys.length];
		altButtons = new JButton[keys.length];
		for (int i = 0; i < keys.length; i++) {
			AddButton(holder, keys[i].toString(), keys[i]);
		}
		add(holder, BorderLayout.CENTER);
		add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
		add(Box.createHorizontalStrut(10), BorderLayout.EAST);
		add(Box.createHorizontalStrut(10), BorderLayout.WEST);
	}

	void AddButton(JPanel parent, String text, Controls.Key key) {
		JLabel label = new JLabel(text);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		String keyText = (Controls.GetKey(key) != -1) ? KeyEvent.getKeyText(Controls.GetKey(key)) : "Unassigned";
		String altKeyText = (Controls.GetAltKey(key) != -1) ? KeyEvent.getKeyText(Controls.GetAltKey(key)) : "Unassigned";
		JButton button = new JButton(keyText);
		JButton buttonAlt = new JButton(altKeyText);
		buttons[key.id] = button;
		altButtons[key.id] = buttonAlt;
		Dialog_Controls dialog = this;
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Dialog_SetControl(dialog, key, false);
				setEnabled(false);
			}
		});

		parent.add(label);
		parent.add(button);
		parent.add(buttonAlt);
	}

	public void UpdateControls() {
		Controls.Key[] keys = Controls.Key.values();
		for (int i = 0; i < keys.length; i++) {
			String keyText = (Controls.GetKey(keys[i]) != -1) ? KeyEvent.getKeyText(Controls.GetKey(keys[i])) : "Unassigned";
			String altKeyText = (Controls.GetAltKey(keys[i]) != -1) ? KeyEvent.getKeyText(Controls.GetAltKey(keys[i])) : "Unassigned";
			buttons[i].setText(keyText);
			altButtons[i].setText(altKeyText);
		}
	}

}
