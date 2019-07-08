/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Luminight
 */
public class Dialog_Credits extends JDialog {

	public Dialog_Credits(JFrame parent) {
		super(parent, "Dungeon Crawler - Credits", true);
		setIconImage(MainMenu.icon);

		getContentPane().setLayout(new BorderLayout());

		Box box = Box.createVerticalBox();
		box.add(Box.createGlue());

		Font normal = new Font(Font.SERIF, Font.PLAIN, 14);
		Font titleFont = new Font(Font.SERIF, Font.BOLD, 18);
		JLabel title = new JLabel("Programming Technology 1.");
		JLabel lbl1 = new JLabel("3. Assignment: Dungeon Crawler");
		JLabel lbl2 = new JLabel("Made by: Balázs Csabai");
		title.setFont(titleFont);
		title.setAlignmentX(CENTER_ALIGNMENT);
		lbl1.setFont(normal);
		lbl1.setAlignmentX(CENTER_ALIGNMENT);
		lbl2.setFont(normal);
		lbl2.setAlignmentX(CENTER_ALIGNMENT);
		box.add(title);
		box.add(Box.createVerticalStrut(10));
		box.add(lbl1);
		box.add(lbl2);
		box.add(Box.createGlue());

		JPanel closePanel = new JPanel();
		JButton okButton = new JButton("Close");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
				dispose();
			}
		});
		closePanel.add(okButton);

		getContentPane().add(box, BorderLayout.CENTER);
		getContentPane().add(closePanel, BorderLayout.SOUTH);
		setResizable(false);
		setSize(250, 150);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setVisible(true);

	}

}
