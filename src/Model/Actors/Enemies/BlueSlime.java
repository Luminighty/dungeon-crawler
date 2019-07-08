/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Actors.Enemies;

import Model.Actors.Enemy;
import Model.Actors.Player;
import Model.Collider;
import Model.Direction;
import Model.Game;
import java.awt.Point;

/**
 *
 * @author Luminight
 */
public class BlueSlime extends Enemy {

	private final int MAXWAITTIME = 50;
	private int waitTime = 0;
	Direction way = Direction.Down;

	public BlueSlime(int x, int y) {
		super(x, y, 22, 24, true);
		SetMaxHP(3);

		name = "BlueSlime";

		SetFrame("blue", "down_0");

		canRespawn = true;
		canBePushed = true;
		AddAnimator(new SlimeAnimator(GetFrame()));
	}

	@Override
	protected void Move() {
		Point p = way.ToPoint();
		GetAnimator().SetInt("wayX", p.x);
		GetAnimator().SetInt("wayY", p.y);

		if (waitTime <= 0) {
			Translate(p);
		}
		isOnTile();

		SetFlipX(GetAnimator().GetBool("flipX"));
		waitTime--;
	}

	void isOnTile() {
		Point pos = GetPosition();

		Point player = Game.instance.player.GetPosition();

		if (waitTime > 0) {
			if (pos.x / Game.TileSize == player.x / Game.TileSize || pos.y / Game.TileSize == player.y / Game.TileSize) {
				way = Direction.fromPoint(new Point(player.x - pos.x, player.y - pos.y));
			}
		} else {
			if (((way == way.Right || way == way.Left) && pos.x % Game.TileSize == Game.TileSize / 2) || (pos.y % Game.TileSize == Game.TileSize / 2 && (way == way.Down || way == way.Up))) {
				waitTime = MAXWAITTIME;
			}
		}

	}

	@Override
	protected void OnCollisionBegin(Collider other) {
		way = Direction.TurnAround(way);
	}

}
