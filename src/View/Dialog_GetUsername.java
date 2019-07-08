/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Luminight
 */
public class Dialog_GetUsername extends JDialog {

	JTextField input;

	public Dialog_GetUsername(JFrame parent, int score, String map) {
		super(parent, "New High Score!", true);
		setIconImage(MainMenu.icon);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		setLayout(new GridLayout(3, 1, 5, 10));

		input = new JTextField(20);
		JLabel lbl_Text = new JLabel("Congratulations! You've reached a new HighScore (" + score + ")");
		lbl_Text.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_Text.setAlignmentX(CENTER_ALIGNMENT);

		JLabel lbl_player = new JLabel("Player Name:");

		JPanel panel = new JPanel();

		panel.add(lbl_player);
		panel.add(input);

		JButton btn = new JButton("Send");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (input.getText().length() == 0) {
					MainMenu.showDialog(getContentPane(), "Set Name", "No player name was set!", new Object[]{"Ok"});
				} else {
					try {
						Database.AddScore(input.getText(), map, score);
					} catch (Exception ex) {
						Logger.getLogger(Dialog_GetUsername.class.getName()).log(Level.SEVERE, null, ex);
					}
					MainMenu.main.setVisible(true);
					setVisible(false);
					dispose();
					parent.dispose();
				}
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Object[] options = new Object[]{"Quit", "Cancel"};
				if (MainMenu.showDialog(parent, "Quit", "Are you sure you don't want to send your new score?", options)) {
					MainMenu.main.setVisible(true);
					setVisible(false);
					dispose();
					parent.dispose();
				}
			}

		});

		add(lbl_Text);
		add(panel);
		add(btn);

		setSize(350, 150);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public String GetName() {
		return input.getText();
	}

}
