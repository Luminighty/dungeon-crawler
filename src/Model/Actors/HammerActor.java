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
 * @author e6j9gs
 */
public class HammerActor extends Actor {

	Point offset;
	final int damage = 1;

	public HammerActor(Direction way, Actor parent, int aliveTime) {
		super(12);
		SetFrame("player", "hammer_side0");
		SetParent(parent);
		AddComponents();
		GetOffset(way);
		SetPosition(offset);
		SetSize(new Point(20, 20));
		GetAnimator().SetInt("time", aliveTime);
		GetAnimator().SetInt("maxTime", aliveTime);
		Destroy(aliveTime);
		Game.instance.PlayClip("hammer");
	}

	@Override
	public void Update() {
		CheckCollision();
	}

	protected void AddComponents() {

		AddCollider(new Collider(this) {
			@Override
			public void Init() {
				bounds.x = -3;
				bounds.y = -5;
				bounds.width = 26;
				bounds.height = 26;
				debugColor = Color.RED;
				AddTag(Actor.Tag.PLAYER);
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
					case WALL:
						Tile w = (Tile) other.parent;
						if (w.type == Tile.TileType.PEG) {
							w.GetCollider().SetActive(false);
							w.SetFrame(w.GetFrame().getKey(), "peg_down");
						}
					default:
						break;
				}
				Game.instance.PlayClip("hit");
			}

		});
		AddAnimator(new Animator(GetFrame()) {
			@Override
			public void Init() {
				SetString("frame", "hammer_side");
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
				SetFrame("player", "hammer_side0");
				GetAnimator().SetString("frame", "hammer_side");
				break;
			case Left:
				offset = new Point(-15, 2);
				SetFrame("player", "hammer_side0");
				GetAnimator().SetString("frame", "hammer_side");
				SetFlipX(true);
				break;
			case Up:
				offset = new Point(1, -20);
				SetFrame("player", "hammer_up0");
				GetAnimator().SetString("frame", "hammer_up");
				break;
			case Down:
				offset = new Point(-1, 20);
				SetFrame("player", "hammer_down0");
				GetAnimator().SetString("frame", "hammer_down");
				break;
		}
	}

}
