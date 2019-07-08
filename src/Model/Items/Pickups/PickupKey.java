/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Items.Pickups;

import Model.Actors.Player;
import Model.Game;
import View.UI;

/**
 *
 * @author Luminight
 */
public class PickupKey extends PickupItem {

	private static final int defaultAliveTime = -1;
	private static final String sprite = "key";

	public PickupKey(int x, int y) {
		super(x, y, sprite, defaultAliveTime);
	}

	public PickupKey(int x, int y, int aliveTime) {
		super(x, y, sprite, aliveTime);
	}

	@Override
	protected void OnPickUp() {
		Player p = Game.instance.player;
		UI ui = Game.instance.ui;
		p.keyCount++;
		ui.keys.SetValue(p.keyCount);
		Game.instance.PlayClip("collect");
		Destroy();
	}

}
