/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Actors.Enemies;

import Model.Actors.Enemy;
import Model.Actors.Player;
import Model.Actors.SlimeProjectileActor;
import Model.Collider;
import Model.Direction;
import Model.Game;
import java.awt.Point;

/**
 *
 * @author Luminight
 */
public class PinkSlime extends Enemy {

	private final int SHOOTDELAY = 120;
	private final int PROJECTILEDAMAGE = 3;
	private final int MAXWAITTIME = 50;
	private int waitTime = 0;
	private int currentShootDelay = 0;
	Direction way = Direction.Down;

	public PinkSlime(int x, int y) {
		super(x, y, 22, 24, true);
		SetMaxHP(3);

		name = "PinkSlime";
		dmg = 1;

		SetFrame("pink", "down_0");

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
		currentShootDelay = Math.max(0, currentShootDelay - 1);
	}

	@Override
	protected void OnCollisionBegin(Collider other) {
		way = Direction.TurnAround(way);
	}

	void isOnTile() {
		Point pos = GetPosition();

		Point player = Game.instance.player.GetPosition();

		if (waitTime > 0) {
			if (pos.x / Game.TileSize == player.x / Game.TileSize || pos.y / Game.TileSize == player.y / Game.TileSize) {
				way = Direction.fromPoint(new Point(player.x - pos.x, player.y - pos.y));
				Shoot(way);
			}
		} else {
			if (((way == way.Right || way == way.Left) && pos.x % Game.TileSize == Game.TileSize / 2) || (pos.y % Game.TileSize == Game.TileSize / 2 && (way == way.Down || way == way.Up))) {
				waitTime = MAXWAITTIME;
			}
		}

	}

	void Shoot(Direction way) {
		if (currentShootDelay > 0) {
			return;
		}
		currentShootDelay = SHOOTDELAY;

		Point pos = GetPosition();
		new SlimeProjectileActor(pos.x, pos.y, "pink", way, 3, PROJECTILEDAMAGE);

	}

}
