/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Actors;

import View.UI;
import Model.*;
import Model.Items.*;
import java.awt.Point;
import java.util.Vector;

/**
 *
 * @author Luminight
 */
public final class Player extends Actor {

	public Direction look = Direction.Up;

	private int swordDmg = 1;

	public int treasureCount = 0;
	public int health = 6;
	public int maxHealth = 6;
	public int bombCount = 9;
	public int maxBombCount = 3;
	public int arrowCount = 6;
	public int maxArrowCount = 20;
	public int invicibleTime = 0;
	public int keyCount = 1;

	private final int MAX_INVICIBLE_TIME = 100;
	private int pushSpeed = 4;
	private int pushTime = 0;
	private final int maxPushTime = 10;
	private Direction pushWay;

	public int preventMoveTime = 0;
	private final int speed = 2;

	public Vector<Item> items = new Vector();
	public int selectedItem = 0;

	public Point GetRoom() {
		Point pos = GetPosition();
		return new Point(pos.x / Game.TileSize / Game.instance.roomSize.x, pos.y / Game.TileSize / Game.instance.roomSize.y);
	}

	public Player() {
		super(10);
		name = "Player";
		SetFrame("player", "down_stand");
		SetTag(Tag.PLAYER);
		SetSize(new Point(18, 24));
		SetPosition(new Point(50, 50));
		AddComponents();
	}

	protected void AddComponents() {
		AddCollider(new Collider(this) {
			@Override
			public void Init() {
				bounds.x = 2;
				bounds.y = 10;
				bounds.width = 14;
				bounds.height = 14;
				//debugCollision= true;
			}

			@Override
			public void OnCollisionBegin(Collider other) {

				switch (other.parent.GetTag()) {
					case DOOR:
						Door d = (Door) other.parent;
						if (d.doorType == Door.DoorType.LOCKED && keyCount > 0) {
							d.SetOpen(true);
							keyCount--;
							Game.instance.ui.keys.SetValue(keyCount);
						}
						break;
					case ENEMY:

						break;
					default:
						break;
				}
				if (other.parent.GetTag() == Tag.DOOR) {

				}
				//Game.instance.PlayClip("hit");
			}
		});

		AddAnimator(new Animator(GetFrame()) {

			int moveTickOffset = 0;
			int invicibleOffset = 0;
			int invicibleFrames = 10;
			boolean isInvicible = false;
			int lastMove = 0;
			int moveSpeed = 15;
			Direction look;
			int currentHitTime = 0;
			int HitTime;

			@Override
			public void Init() {
				SetInt("HitTime", 50);
				HitTime = GetInt("HitTime");
			}

			@Override
			public void Animate(int tick) {
				if (isDead(tick)) {
					return;
				}
				if (GetInt("MoveX") + GetInt("MoveY") != 0) {
					Move(tick);
				} else {
					SetFrame(GetWay(look) + "_stand");
				}
				Hit();

				lastMove = GetInt("MoveX") + GetInt("MoveY");
				SetBool("use", false);
				Invicible(tick);

			}

			int deadTime = Game.LoseDelay - (Game.LoseDelay / 10);
			int deadRotatingSpeed = 6;

			boolean isDead(int tick) {
				if (!GetBool("isDead")) {
					return false;
				}
				if (deadTime > 70) {

				} else if (deadTime > 0) {
					int frame = tick / deadRotatingSpeed % 4;

					if (frame % 2 == 0) {
						SetFrame("side_stand");

					} else if (frame == 3) {
						SetFrame("up_stand");
						SetBool("flipX", true);
					} else {
						SetFrame("down_stand");
						SetBool("flipX", false);
					}
				} else if (deadTime > -20) {
					SetFrame("dead");
				} else {
					SetHidden(true);
				}

				deadTime--;

				return true;
			}

			void Hit() {
				if (!GetBool("isHitting")) {
					return;
				}
				// Start Hitting
				if (currentHitTime <= 0) {
					currentHitTime = HitTime;
					return;
				}
				currentHitTime--;
				if (currentHitTime > HitTime * 0.9f || currentHitTime < HitTime * 0.2f) {
					SetFrame(GetWay(look) + "_hit0");
				} else {
					SetFrame(GetWay(look) + "_hit1");
				}
				// is hitting stopped
				if (currentHitTime <= 0) {
					SetBool("isHitting", false);
				}
				SetInt("currentHitTime", currentHitTime);
			}

			void Move(int tick) {
				// Set Animation Offset
				if (lastMove == 0) {
					moveTickOffset = tick % (moveSpeed * 2); // (moveSpeed * 2) -> full animation length
				}
				look = Direction.fromPoint(new Point(GetInt("MoveX"), GetInt("MoveY")));
				//Move
				if (((tick - moveTickOffset) / moveSpeed) % 2 == 0) {
					SetFrame(GetWay(look) + "_walk");
				} else {
					SetFrame(GetWay(look) + "_stand");
				}
			}

			String GetWay(Direction way) {
				if (way == Direction.Left || way == Direction.Right) {
					return "side";
				}
				if (way == Direction.Down) {
					return "down";
				}
				return "up";
			}

			void Invicible(int tick) {
				if (GetBool("invicible")) {
					if (!isInvicible) {
						invicibleOffset = tick % (invicibleFrames * 2);
					}

					if (((tick - invicibleOffset) / invicibleFrames % 2) == 0) {
						SetFrame("");
					}
				}
				isInvicible = GetBool("invicible");
			}

		});
	}

	private boolean CanDoAction() {
		return health > 0 && !isGettingPushed() && (preventMoveTime <= 0) && !GetAnimator().GetBool("isHitting") && !GetAnimator().GetBool("use");
	}

	@Override
	public void Update() {
		if (preventMoveTime > 0) {
			preventMoveTime--;
		}
		if (CanDoAction()) {
			Move();
			Hit();
			Use();
		}
		SelectItem();
		Invicibility();
		RoomChange();
		SetFlipX(GetAnimator().GetBool("flipX"));
	}

	void RoomChange() {
		Point room = Game.instance.GetRoom(GetPosition());
		//Top Left pos of room
		room.x *= Game.instance.roomSize.x * Game.TileSize;
		room.y *= Game.instance.roomSize.y * Game.TileSize;

		Point pos = GetPosition();

		pos.x -= room.x;
		pos.y -= room.y;
		// Go Up
		if (pos.y < Game.TileSize / 2) {
			Game.instance.MoveToRoom(Direction.Up);
		}

		if (pos.y > (Game.instance.roomSize.y * Game.TileSize) - (Game.TileSize / 2)) {
			Game.instance.MoveToRoom(Direction.Down);
		}

		if (pos.x > (Game.instance.roomSize.x * Game.TileSize) - (Game.TileSize / 2)) {
			Game.instance.MoveToRoom(Direction.Right);
		}

		if (pos.x < Game.TileSize / 2) {
			Game.instance.MoveToRoom(Direction.Left);
		}
	}

	void Hit() {
		if (!Controls.isButtonPressed(Controls.Key.ATTACK)) {
			return;
		}
		GetAnimator().SetBool("isHitting", true);
		int time = GetAnimator().GetInt("HitTime");
		Game.instance.PlayClip("slash");
		new Sword(look, this, time);
	}

	void Move() {
		Point way = new Point(0, 0);
		if (Controls.isButtonHeld(Controls.Key.UP)) {
			way.y--;
		}
		if (Controls.isButtonHeld(Controls.Key.DOWN)) {
			way.y++;
		}
		if (way.y == 0 && Controls.isButtonHeld(Controls.Key.LEFT)) {
			way.x--;
		}
		if (way.y == 0 && Controls.isButtonHeld(Controls.Key.RIGHT)) {
			way.x++;
		}
		GetAnimator().SetInt("MoveX", way.x);
		GetAnimator().SetInt("MoveY", way.y);
		if (GetFlipX() && (way.x > 0 || way.y != 0)) {
			GetAnimator().SetBool("flipX", false);
		}
		if (!GetFlipX() && way.x < 0) {
			GetAnimator().SetBool("flipX", true);
		}

		if (way.x != 0 || way.y != 0) {
			look = Direction.fromPoint(way);
			Translate(new Point(way.x * speed, way.y * speed));
		}
	}

	boolean canUseItem(int index) {
		if (items.size() <= index) {
			return false;
		}
		return items.get(index).CanUse();
	}

	void Use() {
		if (!Controls.isButtonPressed(Controls.Key.USE)) {
			return;
		}

		if (!canUseItem(selectedItem)) {
			Game.instance.PlayClip("stop");
			return;
		}
		items.get(selectedItem).Use();
	}

	void SelectItem() {
		if (!Controls.isButtonPressed(Controls.Key.SELECT) || items.size() < 1) {
			return;
		}

		selectedItem = (selectedItem + 1) % items.size();
		if (selectedItem >= items.size()) {
			UI.instance.selectedItem.SetImage("");
		} else {
			UI.instance.selectedItem.SetImage(items.get(selectedItem).sprite);
		}
	}

	public boolean HasItem(String sprite) {
		for (Item i : items) {
			if (i.sprite == sprite) {
				return true;
			}
		}
		return false;
	}

	void Invicibility() {
		if (invicibleTime > 0) {
			invicibleTime--;
		} else if (GetAnimator().GetBool("invicible")) {
			GetAnimator().SetBool("invicible", false);
			SetTag(Tag.PLAYER);
			CheckCollision();
		}
	}

	public void TakeDamage(int dmg, Point origin) {
		health -= dmg;
		health = Math.min(health, maxHealth);
		UI.instance.health.SetValue(health);
		GetAnimator().SetBool("invicible", true);
		invicibleTime = MAX_INVICIBLE_TIME;
		SetTag(Tag.INVICIBLE);

		Point way = GetPosition();
		way.x -= origin.x;
		way.y -= origin.y;
		pushWay = Direction.fromPoint(way);
		pushTime = maxPushTime;
		SetTag(Tag.INVICIBLE);

		if (health <= 0) {
			Die();
		} else {
			Game.instance.PlayClip("hurt");
		}
	}

	boolean isGettingPushed() {
		if (pushTime <= 0) {
			return false;
		}
		Point way = pushWay.ToPoint();
		way.x *= pushSpeed;
		way.y *= pushSpeed;
		Translate(way);
		pushTime--;
		return true;
	}

	void Die() {
		GetAnimator().SetBool("isDead", true);
		Game.instance.PlayClip("death");
		Game.instance.Lose();
	}

	public void OnGetItem(int waitTime) {
		look = Direction.Down;
		GetAnimator().SetInt("MoveX", 0);
		GetAnimator().SetInt("MoveY", 1);
		SetFrame("player", "down_stand");
		preventMoveTime = waitTime;
	}

}
