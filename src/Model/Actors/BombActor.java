/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Actors;

import Model.Actor;
import Model.Animator;
import Model.Collider;
import Model.Game;
import java.awt.Point;

/**
 *
 * @author Luminight
 */
public class BombActor extends Actor {

	private int damage = 4;
	private int AliveTime = 170;
	private int explodeTime = 70;
	private boolean isExploding = false;

	public BombActor() {
		super(15);
		AddComponents();
		SetSize(new Point(16, 16));
	}

	public BombActor(int alivetime) {
		super(0);
		this.AliveTime = alivetime;
		SetSize(new Point(16, 16));
		SetTag(Tag.PROJECTILE);
		AddComponents();
	}

	void AddComponents() {
		SetFrame("item", "bomb0");
		AddAnimator(new Animator(GetFrame()) {

			private int explodeFrameCount = 2;
			private int bombFrameCount = 2;

			@Override
			public void Animate(int tick) {

				if (GetBool("explode")) {
					SetFrame("explode" + ((tick / 30) % explodeFrameCount));
					return;
				}
				// Less time -> frequent ticks
				int time = GetInt("time");
				int mod = Math.max(2, time);
				SetFrame("bomb" + ((tick - GetInt("bombOffset")) / mod) % bombFrameCount);
			}
		});
		GetAnimator().SetInt("maxTime", AliveTime);
		GetAnimator().SetInt("bombOffset", Game.instance.GetTick());
	}

	@Override
	public void Update() {
		if (AliveTime <= 0) {
			OnExplodeStart();
		} else {
			AliveTime--;
			GetAnimator().SetInt("time", AliveTime);
		}
	}

	void OnExplodeStart() {
		if (isExploding) {
			return;
		}
		Destroy(explodeTime);
		isExploding = true;
		Game.instance.PlayClip("boom");
		GetAnimator().SetBool("explode", true);
		SetFrame("item", "explode0");
		Point size = GetSize();
		size.x *= 4;
		size.y *= 4;
		SetSize(size);
		AddCollider(new Collider(this) {
			@Override
			public void Init() {
				AddTag(Tag.INVICIBLE);
				AddTag(Tag.PROJECTILE);
			}

			@Override
			public void OnCollisionBegin(Collider other) {
				switch (other.parent.GetTag()) {
					case DOOR:
						Door d = (Door) other.parent;
						if (d.doorType == Door.DoorType.CRACKED) {
							d.SetOpen(true);

						}
						break;
					case ENEMY:
						Enemy e = ((Enemy) (other.parent));
						if (!e.immune.contains(Enemy.Immunity.BOMB)) {
							e.TakeDamage(damage, parent.GetPosition());
						}
						break;
					case PLAYER:
						((Player) (other.parent)).TakeDamage(damage, parent.GetPosition());
						break;
					default:
						break;
				}
			}

		});
		CheckCollision();
	}
}
