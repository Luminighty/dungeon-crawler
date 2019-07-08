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
public class PickupTreasure extends PickupItem {

	private static final int defaultAliveTime = 600;
	private final int count = 1;
	private static final String sprite = "treasure";

	public PickupTreasure(int x, int y) {
		super(x, y, sprite, defaultAliveTime);
	}

	public PickupTreasure(int x, int y, int aliveTime) {
		super(x, y, sprite, aliveTime);
	}

	@Override
	protected void OnPickUp() {
		Player p = Game.instance.player;
		UI ui = Game.instance.ui;
		p.treasureCount += count;
		ui.treasures.SetValue(p.treasureCount);
		Game.instance.PlayClip("collect");
		Destroy();
	}

}
