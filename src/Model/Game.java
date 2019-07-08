/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import View.UI;
import Model.Actors.*;
import Model.Items.*;
import java.awt.Point;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.Vector;
import javafx.util.Pair;

public class Game {

	public static final int RoomSizeX = 13;
	public static final int RoomSizeY = 9;
	public static Game instance;
	public static final int TileSize = 32;
	public static final int LoseDelay = 130;

	//Render Priority
	public OrderedList<Actor> actors = new OrderedList<Actor>();
	public Vector<Pair<Collider, Collider>> collisions = new Vector();
	public Vector<String> clipsToPlay = new Vector();

	public Point BoardSize() {
		return new Point(roomSize.x * TileSize, roomSize.x * TileSize);
	}
	;
    public Point boardOffset = new Point(0, 0);
	private Point moveToOffset = new Point(0, 0);
	public Point roomSize = new Point(RoomSizeX, RoomSizeY);
	public Point roomCount = new Point(1, 2);

	public int Score = 0;
	public int Time = 500;
	public Player player;
	public UI ui;

	int tick = 0;
	private int idCount = 0;
	public boolean isPaused = false;

	public int EnemyCountInRoom = 0;

	public int GetID() {
		return idCount++;
	}

	public Game() {
		if (instance != null) {
			return;
		}
		instance = this;
	}

	public void StartGame(Level l) {
		l.BuildLevel();
		ui = new UI();
		win = false;
		lose = false;
		Point startRoom = GetRoom(player.GetPosition());
		boardOffset = new Point(-startRoom.x * RoomSizeX * TileSize, -(startRoom.y * RoomSizeY * TileSize) + ((roomSize.x - roomSize.y) * TileSize));
		moveToOffset.setLocation(boardOffset);

		Point room = GetRoom(player.GetPosition());
		for (Actor a : actors) {
			a.OnRoomChange(room.x, room.y);
		}

	}

	public Point GetRoom(Point p) {
		Point r = new Point(0, 0);
		r.x = p.x / TileSize / roomSize.x;
		r.y = p.y / TileSize / roomSize.y;
		return r;
	}

	public void MoveToRoom(Direction dir) {
		Point size = new Point(roomSize);
		size.x *= TileSize;
		size.y *= TileSize;

		Point way = dir.ToPoint();

		size.x *= -way.x;
		size.y *= -way.y;

		moveToOffset.x += size.x;
		moveToOffset.y += size.y;

		player.Translate(new Point(way.x * TileSize * 2, way.y * TileSize * 2));
		EnemyCountInRoom = 0;
		Point room = GetRoom(player.GetPosition());
		for (Actor a : actors) {
			a.OnRoomChange(room.x, room.y);
		}
	}

	public void Tick() {
		if (Controls.isButtonPressed(Controls.Key.PAUSE)) {
			isPaused = !isPaused;
			PlayClip(((!isPaused) ? "un" : "") + "pause");
		}

		if (isPaused) {
			return;
		}

		if (!moveToOffset.equals(boardOffset)) {
			boardOffset = MoveTowards(boardOffset, moveToOffset, 5);
			return;
		}
		tick++;

		UpdateCollisions();
		for (Actor actor : actors) {
			actor.Tick(tick);
		}
		Controls.UpdatePresses();
	}

	void UpdateCollisions() {
		for (int i = 0; i < collisions.size(); i++) {
			Pair<Collider, Collider> p = collisions.get(i);
			if (p.getKey().isInDistance(p.getValue())) {
				p.getKey().OnCollisionStay(p.getValue());
				p.getValue().OnCollisionStay(p.getKey());
			} else {
				p.getKey().OnCollisionEnd(p.getValue());
				p.getValue().OnCollisionEnd(p.getKey());
				collisions.remove(p);
			}
		}
	}

	Point MoveTowards(Point currentPosition, Point destination, float maxDistance) {
		Point pos = new Point(0, 0);
		int x = destination.x - currentPosition.x;
		if (Math.abs(x) <= maxDistance) {
			pos.x = destination.x;
		} else {
			x = (int) Math.signum(x);
			x *= maxDistance;
			pos.x = currentPosition.x + x;
		}
		int y = destination.y - currentPosition.y;
		if (Math.abs(y) <= maxDistance) {
			pos.y = destination.y;
		} else {
			y = (int) Math.signum(y);
			y *= maxDistance;
			pos.y = currentPosition.y + y;
		}
		return pos;
	}

	public boolean HasCollision(Collider a, Collider b) {
		for (Pair c : collisions) {
			if ((c.getKey() == a && c.getValue() == b) || (c.getValue() == a && c.getKey() == b)) {
				return true;
			}
		}
		return false;
	}

	public Actor FindActorByName(String name) {
		for (Actor a : actors) {
			if (a.GetName().equals(name)) {
				return a;
			}
		}
		return null;
	}

	public int GetTick() {
		return tick;
	}

	public void PlayClip(String Clip) {
		clipsToPlay.add(Clip);
	}

	public boolean win = false;
	public boolean lose = false;

	public boolean isGameEnd() {
		return win || lose;
	}

	public void Lose() {
		for (Actor a : actors) {
			if (a.GetName() != player.GetName()) {
				a.SetHidden(true);
				a.SetActive(false);
			}
		}
		lose = true;
	}

	public void Win() {
		win = true;
		PlayClip("win");
		Score += 50 * ui.time;
		Score += player.treasureCount * 500;
	}

}
