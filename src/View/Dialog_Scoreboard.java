/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.awt.*;
import java.sql.ResultSet;
import javax.swing.*;

/**
 *
 * @author Luminight
 */
public class Dialog_Scoreboard extends JDialog {

	public static final int LIMIT = 10;

	public Dialog_Scoreboard(JFrame parent, String level) {
		super(parent, "Dungeon Crawler - " + level, true);
		setIconImage(MainMenu.icon);
		setSize(300, 400);
		setResizable(false);

		setFocusable(true);
		setLocationRelativeTo(parent);
		AddComponent(level);
		setVisible(true);

	}

	void AddComponent(String level) {

		Font bold = new Font(Font.SANS_SERIF, Font.BOLD, 18);
		Font normal = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(LIMIT + 1, 2));

		AddRow(new String[]{"Name", "Score"}, bold, panel);

		try {
			ResultSet rs = Database.GetHighScores(level, LIMIT);
			int i = 0;
			while (rs.next()) {
				String player = rs.getString("Player");
				String Score = rs.getInt("Score") + "";
				AddRow(new String[]{player, Score}, normal, panel);
				i++;
			}
			for (i = i; i < LIMIT; i++) {
				AddRow(new String[]{"", ""}, normal, panel);
			}
		} catch (Exception exc) {
			System.err.println(exc.toString());
		}

		add(panel);
	}

	void AddRow(String[] columns, Font font, JComponent parent) {
		for (int i = 0; i < columns.length; i++) {

			JLabel label = new JLabel(columns[i]);
			label.setFont(font);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			parent.add(label);

		}
	}

}
