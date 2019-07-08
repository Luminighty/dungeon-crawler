/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Controls;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Luminight
 */
public class Dialog_SetControl extends JDialog {

	public Dialog_SetControl(Dialog_Controls parent, Controls.Key key, boolean isAlt) {
		super(parent, "DungeonCrawler - Set " + key.toString());
		setIconImage(MainMenu.icon);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				keyCode = (keyCode == KeyEvent.VK_ESCAPE) ? -1 : keyCode;
				if (isAlt) {
					Controls.SetAltKey(key, keyCode);
				} else {
					Controls.SetKey(key, keyCode);
				}
				parent.UpdateControls();
				parent.setEnabled(true);
				setVisible(false);
				dispose();
			}
		});
		setLayout(new GridLayout(3, 1));
		setSize(200, 150);
		JLabel text = new JLabel("Press a key to assign '" + key.toString() + "'!");
		text.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel text2 = new JLabel("(Escape to unassign)");
		text2.setHorizontalAlignment(SwingConstants.CENTER);
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.setEnabled(true);
				setVisible(false);
				dispose();
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				parent.setEnabled(true);
				setVisible(false);
				dispose();
			}

		});
		setLocationRelativeTo(parent);
		add(text);
		add(text2);
		add(cancel);
		setVisible(true);
		setFocusable(true);
		requestFocusInWindow();
	}

}
