/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Items.Pickups;

import Model.Actor;
import Model.Actors.Player;
import Model.Collider;
import Model.Game;
import View.UI;
import java.awt.Point;

/**
 *
 * @author Luminight
 */
public abstract class PickupItem extends Actor {

	public enum Pickup {
		ARROW, BOMB, HEART, TREASURE
	}

	protected int GetItemStopTime = 30;

	public PickupItem(int x, int y, String sprite, int aliveTime) {
		super(20);
		SetPosition(new Point(x, y));
		SetSize(new Point(24, 24));
		SetFrame("item", sprite);
		AddCollider(new Collider(this) {
			@Override
			public void Init() {
				SetIsTrigger(true);
				SetTag(Tag.PROJECTILE);
			}

			@Override
			public void OnCollisionBegin(Collider other) {
				if (other.parent.GetTag() == Tag.PLAYER) {
					OnPickUp();
				}
			}

		});
		if (aliveTime > 0) {
			Destroy(aliveTime);
		}
	}

	protected abstract void OnPickUp();

}
