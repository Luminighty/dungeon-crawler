/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Actors.Enemies;

import Model.Actors.Enemy;
import Model.Actors.Player;
import Model.Animator;
import Model.Collider;
import Model.Game;
import java.awt.Point;

/**
 *
 * @author Luminight
 */
public class Fire extends Enemy {

	private final int dmg = 1;

	public Fire(int x, int y) {
		super(x, y, Game.TileSize, Game.TileSize, true);
		name = "Fire";
		SetMaxHP(1);
		killScore = 10;
		canRespawn = true;
		canBePushed = false;

		immune.add(Immunity.BOMB);
		immune.add(Immunity.ARROW);
		immune.add(Immunity.HAMMER);
		SetFrame("enemy", "fire_0");
		AddComponents();
	}

	void AddComponents() {
		AddAnimator(new Animator(GetFrame()) {

			int speed = 10;

			@Override
			public void Animate(int tick) {
				int frame = tick / speed % 3;
				SetFrame("fire_" + frame);
			}
		});
	}

	@Override
	protected void Move() {
	}

}
