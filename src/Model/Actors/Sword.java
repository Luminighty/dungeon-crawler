/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Actors;

import Model.*;
import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author Luminight
 */
public class Sword extends Actor {

	Point offset;
	int damage = 1;

	public Sword(Direction way, Actor parent, int aliveTime) {
		super(12);
		SetFrame("player", "sword_side0");
		SetParent(parent);
		AddComponents();
		GetOffset(way);
		SetPosition(offset);
		SetSize(new Point(20, 20));
		GetAnimator().SetInt("time", aliveTime);
		GetAnimator().SetInt("maxTime", aliveTime);
		Destroy(aliveTime);
	}

	@Override
	public void Update() {
		CheckCollision();
	}

	protected void AddComponents() {

		AddCollider(new Collider(this) {
			@Override
			public void Init() {
				bounds.x = -2;
				bounds.y = 0;
				bounds.width = 20;
				bounds.height = 20;
				debugColor = Color.RED;
				AddTag(Tag.PLAYER);
				AddTag(Tag.INVICIBLE);
			}

			@Override
			public void OnCollisionBegin(Collider other) {
				switch (other.parent.GetTag()) {
					case ENEMY:
						Enemy e = (Enemy) (other.parent);
						if (!e.immune.contains(Enemy.Immunity.SWORD)) {
							e.TakeDamage(damage, Game.instance.player.GetPosition());
						}
						break;
					default:
						break;
				}
				Game.instance.PlayClip("hit");
			}

		});
		AddAnimator(new Animator(GetFrame()) {
			@Override
			public void Init() {
				SetString("frame", "sword_side");
			}

			@Override
			public void Animate(int tick) {
				int time = GetInt("time");
				int hitTime = GetInt("maxTime");
				time--;
				String frame = GetString("frame");
				if (time > hitTime * 0.9f || time < hitTime * 0.2f) {
					SetFrame(frame + "0");
				} else {
					SetFrame(frame + "1");
				}
				SetInt("time", time);
			}

		});
	}

	void GetOffset(Direction way) {
		offset = new Point(0, 0);
		switch (way) {
			case Right:
				offset = new Point(15, 2);
				SetFrame("player", "sword_side0");
				GetAnimator().SetString("frame", "sword_side");
				break;
			case Left:
				offset = new Point(-15, 2);
				SetFrame("player", "sword_side0");
				GetAnimator().SetString("frame", "sword_side");
				SetFlipX(true);
				break;
			case Up:
				offset = new Point(1, -20);
				SetFrame("player", "sword_up0");
				GetAnimator().SetString("frame", "sword_up");
				break;
			case Down:
				offset = new Point(-1, 20);
				SetFrame("player", "sword_down0");
				GetAnimator().SetString("frame", "sword_down");
				break;
		}
	}

}
