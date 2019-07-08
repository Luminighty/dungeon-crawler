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
import Model.Items.Pickups.*;
import java.awt.Point;
import java.util.Vector;

/**
 *
 * @author Luminight
 */
public abstract class Enemy extends Actor {

	public enum Immunity {
		SWORD, ARROW, BOMB, HAMMER
	}

	public Vector<Immunity> immune = new Vector();

	public int health = 3;
	public int maxHealth = 3;

	protected boolean canRespawn = false;
	protected boolean canBePushed = true;

	private int pushSpeed = 4;
	private int pushTime = 0;
	private final int maxPushTime = 15;
	private Direction pushWay;
	private boolean isDead = false;
	private int invicibleTime = 0;
	private Point SpawnPosition = null;

	protected final int maxInvicibleTime = 20;
	protected static int killCount = 0;
	protected Vector<PickupItem.Pickup> dropItems;
	protected float treasureChance = 0.3f;

	protected int killScore = 100;
	protected int dmg = 1;

	public final Point GetRoom() {
		Point pos = GetPosition();
		return new Point(pos.x / Game.TileSize / Game.instance.roomSize.x, pos.y / Game.TileSize / Game.instance.roomSize.y);
	}

	public Enemy(int x, int y, int width, int height, boolean hasDefaultCollider) {
		super(10);
		SetPosition(new Point(x * Game.TileSize + Game.TileSize / 2, y * Game.TileSize + Game.TileSize / 2));
		SpawnPosition = new Point(x * Game.TileSize + Game.TileSize / 2, y * Game.TileSize + Game.TileSize / 2);
		SetTag(Tag.ENEMY);
		SetSize(new Point(width, height));
		if (hasDefaultCollider) {
			AddCollider(new Collider(this) {
				@Override
				public void Init() {
					AddTag(Tag.INVICIBLE);
					AddTag(Tag.ENEMY);
				}

				@Override
				public void OnCollisionBegin(Collider other) {
					switch (other.parent.GetTag()) {
						case PLAYER:
							Player p = ((Player) other.parent);
							p.TakeDamage(dmg, parent.GetPosition());
							break;
						default:
							break;
					}
					((Enemy) parent).OnCollisionBegin(other);
				}
			});
		}

		dropItems = new Vector();
		dropItems.add(null);
		dropItems.add(PickupItem.Pickup.HEART);
		dropItems.add(PickupItem.Pickup.BOMB);
		dropItems.add(null);
		dropItems.add(PickupItem.Pickup.ARROW);
	}

	@Override
	public void Update() {
		if (pushTime > 0) {
			Point way = pushWay.ToPoint();
			way.x *= pushSpeed;
			way.y *= pushSpeed;
			Translate(way);
			pushTime--;
		} else {
			if (health <= 0) {
				if (!isDead) {
					Die();
				}
			} else {
				Move();
			}
		}
		Invicibility();
	}

	void Invicibility() {
		if (invicibleTime <= 0) {
			return;
		}
		invicibleTime--;
		if (invicibleTime <= 0) {
			SetTag(Tag.ENEMY);
		}
	}

	protected final void SetMaxHP(int hp) {
		health = hp;
		maxHealth = hp;
	}

	protected abstract void Move();

	public void TakeDamage(int dmg, Point origin) {
		health -= dmg;
		invicibleTime = maxInvicibleTime;
		if (canBePushed) {
			Point way = GetPosition();
			way.x -= origin.x;
			way.y -= origin.y;
			pushWay = Direction.fromPoint(way);
			pushTime = maxPushTime;
			SetTag(Tag.INVICIBLE);
		}
	}

	protected void Die() {
		SetActive(false);
		isDead = true;
		Game.instance.EnemyCountInRoom--;
		Game.instance.Score += killScore;
		killCount++;
		DropItem();
	}

	protected void DropItem() {
		int i = killCount % dropItems.size();
		if (dropItems.get(i) == null) {
			return;
		}
		Player p = Game.instance.player;
		Point pos = GetPosition();
		switch (dropItems.get(i)) {
			case ARROW:
				if (p.HasItem(PickupBow.sprite) && p.arrowCount < p.maxArrowCount) {
					new PickupArrow(pos.x, pos.y);
				}
				break;
			case BOMB:
				if (p.HasItem(PickupBomb.sprite) && p.bombCount < p.maxBombCount) {
					new PickupBomb(pos.x, pos.y);
				}
				break;
			case HEART:
				if (p.health < p.maxHealth) {
					new PickupHeart(pos.x, pos.y);
				}
				break;
			case TREASURE:
				if (Math.random() < treasureChance) {
					new PickupTreasure(pos.x, pos.y);
				}
				break;
		}
	}

	@Override
	public void OnRoomChange(int x, int y) {
		if (!canRespawn && health <= 0) {
			return;
		}
		isDead = false;
		health = maxHealth;
		SetPosition(SpawnPosition);
		Point room = Game.instance.GetRoom(GetPosition());
		if (room.x == x && room.y == y) {
			SetActive(true);
			Game.instance.EnemyCountInRoom++;
		} else {
			SetActive(false);
		}
	}

	protected void OnCollisionBegin(Collider other) {
	}

}
