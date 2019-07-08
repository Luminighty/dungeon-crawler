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
public class Dialog_About extends JDialog {

	public Dialog_About(JFrame parent) {
		super(parent, "Dungeon Crawler - About", true);
		setIconImage(MainMenu.icon);

		setSize(400, 350);
		getContentPane().setLayout(new BorderLayout());
		Box box = Box.createVerticalBox();
		box.add(Box.createGlue());

		JLabel title = new JLabel("About");
		title.setAlignmentX(CENTER_ALIGNMENT);
		title.setFont(new Font(Font.SERIF, Font.BOLD, 20));

		box.add(title);
		box.add(Box.createVerticalStrut(10));

		JTextArea text = new JTextArea(10, 10);
		text.setText("Dungeon Crawler is a Legend of Zelda-like action-rpg game.\n"
				+ "\n"
				+ "Your objective is to reach the treasure at the end of the dungeon.\n"
				+ "\n"
				+ "On your journey you'll find plenty of helpful items such as Bombs, Arrows, a Boat and a hammer even.\n"
				+ "These items will help you on your way once you press the Use button ('X' by default) and you can change your selected item with the Select button (Space by default)\n"
				+ "\n"
				+ "You might find enemies while exploring a dungeon. You can hit them using your sword ('C' by default) or you can try to use your items. Be wary, some enemies can shoot you and deal plenty of damage.");
		text.setEditable(false);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setBackground(null);
		text.setOpaque(true);
		text.setHighlighter(null);
		text.setPreferredSize(new Dimension(10, 500));
		box.add(text);
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

		Component paddingLeft = Box.createHorizontalStrut(10);
		Component paddingRight = Box.createHorizontalStrut(10);

		getContentPane().add(box, BorderLayout.CENTER);
		getContentPane().add(paddingLeft, BorderLayout.WEST);
		getContentPane().add(paddingRight, BorderLayout.EAST);
		getContentPane().add(closePanel, BorderLayout.SOUTH);
		setResizable(false);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setVisible(true);
	}

}
