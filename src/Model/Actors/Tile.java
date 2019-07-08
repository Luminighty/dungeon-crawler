/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Actors;

import Model.Actor;
import Model.Collider;
import Model.Game;
import java.awt.Point;

/**
 *
 * @author Luminight
 */
public class Tile extends Actor {

	public enum TileType {
		BLOCK("block"), TOP("wall_t"), BOTTOM("wall_b"), LEFT("wall_l"), RIGHT("wall_r"),
		CRN_TL("crn_tl"), CRN_TR("crn_tr"), CRN_BL("crn_bl"), CRN_BR("crn_br"),
		DOOR_T("door_t"), DOOR_B("door_b"), DOOR_L("door_l"), DOOR_R("door_r"),
		NONE("ground"), WATER("water"), PEG("peg");

		TileType(String frame) {
			this.frame = frame;
		}
		String frame;

		public String GetFrame() {
			return frame;
		}

		public static TileType FromPoint(int x, int y, int sizeX, int sizeY) {
			TileType wall = TOP;
			if (x == 0) {
				if (y == 0) {
					wall = TileType.CRN_TL;
				} else if (y == sizeY) {
					wall = TileType.CRN_BL;
				} else {
					wall = TileType.LEFT;
				}
			} else if (x == sizeX) {
				if (y == 0) {
					wall = TileType.CRN_TR;
				} else if (y == sizeY) {
					wall = TileType.CRN_BR;
				} else {
					wall = TileType.RIGHT;
				}
			} else {
				if (y == 0) {
					wall = TileType.TOP;
				} else if (y == sizeY) {
					wall = TileType.BOTTOM;
				} else {
					wall = TileType.BLOCK;
				}
			}
			return wall;
		}

		public static TileType WallToDoor(TileType type) {
			switch (type) {
				case TOP:
					return DOOR_T;
				case BOTTOM:
					return DOOR_B;
				case LEFT:
					return DOOR_L;
				case RIGHT:
					return DOOR_R;
				default:
					return NONE;
			}
		}

	}

	public TileType type;
	public static Tile[][] Tiles;

	public Tile(int x, int y, TileType type, boolean hasCollider) {
		super(500);
		name = "wall";
		Point size = new Point(Game.TileSize, Game.TileSize);
		Tiles[x][y] = this;
		this.type = type;

		SetSize(size);
		SetPosition(new Point(x * size.x, y * size.y));
		SetFrame("tiles", type.GetFrame());
		SetRenderOffset(new PointF(0, 0));
		switch (type) {
			case NONE:
				SetTag(Tag.NONE);
				break;
			case WATER:
				SetTag(Actor.Tag.GROUND_WALL);
				break;
			default:
				SetTag(Actor.Tag.WALL);
				break;
		}
		if (hasCollider || this.type == TileType.WATER) {
			AddCollider(new Collider(this) {
			});
		}
	}
}
