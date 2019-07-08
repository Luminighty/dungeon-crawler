/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Resources.ResourcePack;
import Model.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.sql.ResultSet;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Luminight
 */
public class Board extends JPanel {

	public Timer endTimer = null;

	private JFrame parent;
	private Level level;
	private Game game;
	private ResourcePack currentResourcePack;
	private Clip music;
	private float scale = 1f;
	private final int tickDelay = 30;
	private Timer timer;
	private Color BackgroundColor;
	private boolean endTriggered = false;

	public Board(Level level, JFrame parent) {
		this.level = level;
		this.parent = parent;
		game = (Game.instance == null) ? new Game() : Game.instance;
		game.isPaused = false;
		timer = new Timer(tickDelay, (e) -> {
			if (!game.win) {
				game.Tick();
				repaint();
			}
			Controls.UpdatePresses();
		});
		level.LoadLevel();
		timer.setRepeats(true);
		timer.start();
		setOpaque(false);
		BackgroundColor = Color.BLACK;
		currentResourcePack = ResourcePack.FromFolder(level.ResourcePack);
		try {
			currentResourcePack.LoadResources();
			music = currentResourcePack.GetClip("music");
			music.setFramePosition(0);
			music.loop(Clip.LOOP_CONTINUOUSLY);
			music.setLoopPoints(530325, -1);
		} catch (Exception exc) {
			System.err.println("Failed loading resources: " + exc.toString());
		}
		setScale(scale);
		game.StartGame(level);
	}

	public Point getBoardSize() {
		Point size = game.BoardSize();
		return new Point((int) (scale * size.x), (int) (scale * size.y));
	}

	public boolean setScale(float scale) {
		this.scale = scale;
		if (game == null) {
			return false;
		}
		Point size = getBoardSize();

		Dimension dim = new Dimension(size.x, size.y);
		setPreferredSize(dim);
		setMaximumSize(dim);
		setSize(dim);
		repaint();
		return true;
	}

	@Override
	protected void paintComponent(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		g.setColor(BackgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		game.Tick();
		for (Actor actor : game.actors) {

			if (actor.GetHidden() || !actor.GetActive()) {
				continue;
			}

			Pair<String, String> p = actor.GetFrame();
			if (p == null || p.getKey() == "" || p.getValue() == "") {
				continue;
			}
			Image img = currentResourcePack.GetImage(p.getKey(), p.getValue());
			Point pos = actor.GetPosition();
			Point size = actor.GetSize();
			if (!actor.IsStaticPosition()) {
				pos.x += game.boardOffset.x;
				pos.y += game.boardOffset.y;
			}
			pos.x -= size.x * actor.GetRenderOffset().x;
			pos.y -= size.y * actor.GetRenderOffset().y;

			if (actor.GetFlipX()) {
				pos.x += size.x;
				size.x *= -1;
			}
			if (actor.GetFlipY()) {
				pos.y += size.y;
				size.y *= -1;
			}

			if (actor.HasCollider() && actor.GetCollider().getDebugCollision()) {
				Rectangle r = actor.GetCollider().GetBounds();
				g.setColor(actor.GetCollider().getDebugColor());
				r.x += game.boardOffset.x;
				r.y += game.boardOffset.y;

				g.drawRect((int) (r.x * scale), (int) (r.y * scale), (int) (r.width * scale), (int) (r.height * scale));
			}
			g.drawImage(img, (int) (pos.x * scale), (int) (pos.y * scale), (int) (size.x * scale), (int) (size.y * scale), null, this);
		}

		// Play Clips
		for (String c : game.clipsToPlay) {
			Clip clip = currentResourcePack.GetClip(c);
			clip.setFramePosition(0);
			clip.start();
		}

		if (game.isPaused) {
			int x = getWidth() / 2;
			int y = getWidth() / 2;
			int width = 93 * 2;
			int height = 21 * 2;
			x -= width * 0.5f;
			y -= height * 0.5f;
			Image img = currentResourcePack.GetImage("ui", "pause");
			g.drawImage(img, (int) (x * scale), (int) (y * scale), (int) (width * scale), (int) (height * scale), null, this);
			music.stop();
		} else if (!music.isRunning() && !game.isGameEnd()) {
			music.start();
			music.loop(Clip.LOOP_CONTINUOUSLY);
		}

		game.clipsToPlay.clear();
		if (game.isGameEnd() && !endTriggered) {
			EndGame();
		}
	}

	void EndGame() {
		music.stop();
		int delay = (Game.instance.win) ? 7000 : Game.LoseDelay * tickDelay;
		endTriggered = true;
		Timer t = new Timer(delay, (e) -> {
			if (Game.instance.win) {
				HighScore(Game.instance.Score);
			} else {
				LoseScreen();
			}
		});
		t.start();
		t.setRepeats(false);
		endTimer = t;
	}

	void LoseScreen() {
		Object[] options = {"Restart", "Quit"};
		timer.stop();
		if (MainMenu.showDialog(parent, "Too bad", "You've lost! Restart?", options)) {
			RestartGame();
		} else {
			MainMenu.main.setVisible(true);
			setVisible(false);
			parent.dispose();
		}
	}

	void HighScore(int score) {
		try {
			ResultSet rs = Database.GetHighScores(level.name, Dialog_Scoreboard.LIMIT);
			int highScore = score + 1;
			int c = 0;

			while (rs.next() && highScore > score) {
				highScore = rs.getInt("Score");
				c++;
			}
			timer.stop();
			if (c >= Dialog_Scoreboard.LIMIT) {
				if (MainMenu.showDialog(this, "Dungeon Crawler - Highscore", "You didn't reach the TOP 10 in this map.", new Object[]{"Restart", "Back to Menu"})) {
					RestartGame();
				} else {
					MainMenu.main.setVisible(true);
					setVisible(false);
					parent.dispose();
				}
			} else {
				new Dialog_GetUsername(parent, score, level.name);
			}

		} catch (Exception ex) {
			Logger.getLogger(Board.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			if (MainMenu.showDialog(this, "Dungeon Crawler - Highscore", "Couldn't connect to the database. Want to restart the dungeon?", new Object[]{"Restart", "Back to Menu"})) {
				RestartGame();
			} else {
				MainMenu.main.setVisible(true);
				setVisible(false);
				parent.dispose();
			}
		}
	}

	public void RestartGame() {
		game.StartGame(level);
		timer.start();
		music.setFramePosition(0);
		music.start();
		music.loop(Clip.LOOP_CONTINUOUSLY);
		endTriggered = false;
	}

	public void ExitGame() {
		music.stop();
		timer.stop();
	}

}
