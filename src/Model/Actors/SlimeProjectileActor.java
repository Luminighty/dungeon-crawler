/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Actors;

import Model.Actor;
import Model.Actors.Enemies.SlimeAnimator;
import Model.Animator;
import Model.Collider;
import Model.Direction;
import Model.Game;
import java.awt.Point;

/**
 *
 * @author Luminight
 */
public class SlimeProjectileActor extends Actor {

	private final int ANIMATIONSPEED = 10;
	private int dmg;
	private int speed;
	private Direction way;

	public SlimeProjectileActor(int x, int y, String sprite, Direction way, int speed, int dmg) {
		super(11);
		SetFrame(sprite, "shoot_0");
		this.way = way;
		this.speed = speed;
		this.dmg = dmg;
		SetSize(new Point(Game.TileSize / 2, Game.TileSize / 2));
		SetPosition(new Point(x, y));
		AddAnimator(new Animator(GetFrame()) {
			@Override
			public void Animate(int tick) {
				SetFrame("shoot_" + tick / ANIMATIONSPEED % 2);
			}

		});

		AddCollider(new Collider(this) {
			@Override
			public void Init() {
				SetIsTrigger(true);
				AddTag(Tag.ENEMY);
				AddTag(Tag.INVICIBLE);
				AddTag(Tag.ITEM);
				AddTag(Tag.GROUND_WALL);
			}

			@Override
			public void OnCollisionBegin(Collider other) {
				switch (other.parent.GetTag()) {
					case PLAYER:
						Player p = (Player) other.parent;
						p.TakeDamage(dmg, parent.GetPosition());
						break;
					default:
						break;
				}

				Destroy();

			}

		});

	}

	@Override
	public void Update() {
		Point p = way.ToPoint();
		p.x *= speed;
		p.y *= speed;

		Translate(p);

	}

}
