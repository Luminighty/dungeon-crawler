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
public class GreenSlime extends Enemy {

	private final int dmg = 1;
	private final int MAXWAITTIME = 50;
	private int waitTime = 0;
	Direction way = Direction.Down;

	public GreenSlime(int x, int y) {
		super(x, y, 22, 24, true);
		SetMaxHP(2);
		name = "GreenSlime";
		canRespawn = true;
		canBePushed = true;
		SetFrame("green", "down_0");

		AddAnimator(new SlimeAnimator(GetFrame()));
	}

	@Override
	protected void OnCollisionBegin(Collider other) {
		way = Direction.TurnAround(way);
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
		if (waitTime <= 0) {
			if (((way == way.Right || way == way.Left) && pos.x % Game.TileSize == Game.TileSize / 2) || (pos.y % Game.TileSize == Game.TileSize / 2 && (way == way.Down || way == way.Up))) {
				waitTime = MAXWAITTIME;
			}
		}
	}

}
