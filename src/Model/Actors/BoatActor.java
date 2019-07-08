/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Actors;

import Model.Actor;
import Model.Direction;
import Model.Game;
import Model.Items.Boat;
import java.awt.Point;

/**
 *
 * @author e6j9gs
 */
public class BoatActor extends Actor {

	Tile tile;
	Player player;
	final float MAXDISTANCE = 1.1f;
	public static final float spawnDistance = 1;

	public BoatActor(Tile overwriteTile) {
		super(20);
		player = Game.instance.player;
		Point pos = overwriteTile.GetPosition();
		pos.x += Game.TileSize / 2;
		pos.y += Game.TileSize / 2;
		SetPosition(pos);
		tile = overwriteTile;
		tile.GetCollider().SetActive(false);
		SetSize(new Point(Game.TileSize, Game.TileSize));
		Boat.isPlaced = true;
		SetFrame("item", "boat");
	}

	@Override
	public void Update() {

		Point p = GetPosition();

		if (player.GetPosition().distance(p.x, p.y) > MAXDISTANCE * Game.TileSize) {
			Remove();
		}
	}

	void Remove() {

		tile.GetCollider().SetActive(true);
		Destroy();
		Boat.isPlaced = false;

	}

}
