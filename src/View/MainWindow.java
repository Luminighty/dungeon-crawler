/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Controls;
import Model.Game;
import Model.Level;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Luminight
 */
public class MainWindow extends JFrame {

	Board board;

	public MainWindow(Level level) {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Dungeon Crawler - " + level.name);
		setIconImage(MainMenu.icon);
		addWindowListener(exitEvent);
		board = new Board(level, this);
		add(board);
		Point size = board.getBoardSize();
		setResizable(false);
		setVisible(true);
		Insets in = getInsets();
		setSize(size.x + in.left + in.right, size.y + in.top + in.bottom);
		setLocationRelativeTo(MainMenu.main);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				Controls.HoldButton(e.getKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				Controls.ReleaseButton(e.getKeyCode());
			}
		});

	}

	void CenterWindow() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = getSize().width;
		int height = getSize().height;
		int x = (dim.width - width) / 2;
		int y = (dim.height - height) / 2;
		setLocation(x, y);
	}

	void Exit() {

		MainMenu.main.setVisible(true);
		board.ExitGame();
		setVisible(false);
	}

	void tryExit() {
		Game.instance.isPaused = true;
		Object[] options = {"Exit", "Cancel"};
		if (board.endTimer != null) {
			board.endTimer.stop();
		}
		if (MainMenu.showDialog(this, "Exit Game", "Are you sure you want to exit? All data will be lost!", options)) {
			Exit();
		} else {
			if (board.endTimer != null) {
				board.endTimer.start();
			}
		}
	}

	WindowAdapter exitEvent = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			tryExit();
		}

		@Override
		public void windowLostFocus(WindowEvent e) {
			Game.instance.isPaused = true;
		}

	};

}
