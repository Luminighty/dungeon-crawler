/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Actors;

import Model.Actor;
import Model.Collider;
import Model.Direction;
import Model.Game;
import java.awt.Point;

/**
 * @author Luminight
 */
public class ArrowActor extends Actor {

	int aliveTime = 200;
	int speed = 2;
	public int damage = 4;
	boolean isStuck = false;
	Point Offset = new Point(0, 0);
	Direction way;

	public ArrowActor(Point origin, Direction way) {
		super(15);
		SetSize(new Point(22, 22));
		this.way = way;
		SetFrame("item", "arrow_" + way.toString().toLowerCase());
		SetPosition(origin);
		SetTag(Tag.PROJECTILE);

		AddCollider(new Collider(this) {

			boolean stuck = false;

			@Override
			public void Init() {
				AddTag(Tag.PLAYER);
				AddTag(Tag.INVICIBLE);
				AddTag(Tag.PROJECTILE);
				AddTag(Tag.GROUND_WALL);
				SetIsTrigger(true);
			}

			@Override
			public void OnCollisionBegin(Collider other) {
				if (stuck) {
					return;
				}
				switch (other.parent.GetTag()) {
					case ENEMY:
						Enemy enemy = (Enemy) other.parent;
						if (!enemy.immune.contains(Enemy.Immunity.ARROW)) {
							enemy.TakeDamage(damage, parent.GetPosition());
						}
						break;
					default:

						break;
				}
				this.SetActive(false);
				((ArrowActor) this.parent).isStuck = true;
				stuck = true;
				Point offset = parent.GetPosition();
				offset.x -= other.parent.GetPosition().x;
				offset.y -= other.parent.GetPosition().y;
				((ArrowActor) this.parent).Offset = offset;
				parent.SetPosition(offset);
				SetParent(other.parent);
				Game.instance.PlayClip("hit");

			}

		});

		Destroy(aliveTime);
	}

	@Override
	public void Update() {
		if (isStuck) {
			if (GetParent() == null || !GetParent().GetActive() || GetParent().GetHidden()) {
				Destroy();
			}
			SetPosition(Offset);
			return;
		}
		Point newPos = way.ToPoint();
		newPos.x *= speed;
		newPos.y *= speed;
		Translate(newPos);
	}

}
