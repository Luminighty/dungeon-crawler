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
public class PickupArrow extends PickupItem {

	private static final int defaultAliveTime = 600;
	private final int count = 3;
	public static final String sprite = "arrow";

	public PickupArrow(int x, int y) {
		super(x, y, sprite, defaultAliveTime);
	}

	public PickupArrow(int x, int y, int aliveTime) {
		super(x, y, sprite, aliveTime);
	}

	@Override
	protected void OnPickUp() {
		Player p = Game.instance.player;
		UI ui = Game.instance.ui;
		p.arrowCount = Math.min(p.maxArrowCount, p.arrowCount + count);
		ui.arrows.SetValue(p.arrowCount);
		Game.instance.PlayClip("collect");
		Destroy();
	}

}
