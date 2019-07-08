/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Actors;

import Model.Actor;
import static Model.Actors.Tile.Tiles;
import Model.Collider;
import Model.Game;
import java.awt.Point;

/**
 *
 * @author Luminight
 */
public class Door extends Actor {

	public enum DoorType {
		LOCKED("l"), CRACKED("c"), STUCK("s");

		DoorType(String type) {
			this.Prefix = type;
		}
		public String Prefix;
	}

	Tile.TileType tileType;
	DoorType doorType;

	boolean canRespawn = false;
	boolean isOpen = false;
	boolean checkEnemyCount = false;

	public Door(int x, int y, Tile.TileType tileType, DoorType doorType) {
		super(400);
		name = "wall";
		Point size = new Point(Game.TileSize, Game.TileSize);
		this.tileType = tileType;
		this.doorType = doorType;

		SetSize(size);
		SetPosition(new Point(x * size.x, y * size.y));
		SetFrame("tiles", doorType.Prefix + tileType.GetFrame());
		SetRenderOffset(new PointF(0, 0));
		SetTag(Tag.DOOR);
		if (this.doorType == DoorType.STUCK) {
			canRespawn = true;
		}

		AddCollider(new Collider(this) {
		});

	}

	@Override
	public void Update() {
		if (!isOpen && checkEnemyCount && Game.instance.EnemyCountInRoom <= 0) {
			SetOpen(true);
		}
	}

	@Override
	public void OnRoomChange(int x, int y) {
		Point room = Game.instance.GetRoom(GetPosition());
		if (room.x != x || room.y != y) {
			SetActive(false);
			checkEnemyCount = false;
			return;
		}
		SetActive(true);

		if (doorType == DoorType.STUCK) {
			checkEnemyCount = true;
		}
		if (canRespawn) {
			SetOpen(false);
		}
	}

	public void SetOpen(boolean open) {
		this.isOpen = open;
		GetCollider().SetActive(!isOpen);
		SetHidden(isOpen);
	}

}
