/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Items;

import Model.Actors.BoatActor;
import Model.Actors.Player;
import Model.Actors.Tile;
import Model.Game;
import java.awt.Point;
import java.util.Vector;

/**
 *
 * @author e6j9gs
 */
public class Boat extends Item {

	public static boolean isPlaced = false;

	private Tile overWriteTile;
	private Player player;
	private Vector<Tile.TileType> placeableTags = new Vector();

	public Boat() {
		this.sprite = "boat";
		this.player = Game.instance.player;
		placeableTags.add(Tile.TileType.WATER);
                isPlaced = false;
	}

	@Override
	public boolean CanUse() {
		if (isPlaced) {
			return false;
		}

		Point pos = player.GetPosition();
		Point way = player.look.ToPoint();

		pos.x += way.x * BoatActor.spawnDistance * Game.TileSize;
		pos.y += way.y * BoatActor.spawnDistance * Game.TileSize;

		pos.x /= Game.TileSize;
		pos.y /= Game.TileSize;

		if (pos.x < 0 || pos.y < 0) {
			return false;
		}

		Tile tile = Tile.Tiles[pos.x][pos.y];

		if (!placeableTags.contains(tile.type)) {
			return false;
		}

		overWriteTile = tile;
		return true;
	}

	@Override
	public void Use() {
		new BoatActor(overWriteTile);
	}

}
