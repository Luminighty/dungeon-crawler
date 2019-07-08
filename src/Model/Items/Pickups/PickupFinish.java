/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Items.Pickups;

import Model.Actors.Player;
import Model.Game;
import View.UI;
import java.awt.Point;

/**
 *
 * @author Luminight
 */
public class PickupFinish extends PickupItem {

	private static final int defaultAliveTime = -1;
	public static final String sprite = "finish";

	public PickupFinish(int x, int y) {
		super(x, y, sprite, defaultAliveTime);
	}

	public PickupFinish(int x, int y, int aliveTime) {
		super(x, y, sprite, aliveTime);
	}

	@Override
	protected void OnPickUp() {
		Player p = Game.instance.player;
		Game.instance.Win();
		SetParent(p);
		SetPosition(new Point(0, -Game.TileSize));
		p.OnGetItem(450);
		Destroy(450);
	}

}
