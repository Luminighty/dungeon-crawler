/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Items.Pickups;

import Model.Actors.Player;
import Model.Game;
import Model.Items.Hammer;
import View.UI;
import java.awt.Point;

/**
 *
 * @author Luminight
 */
public class PickupHammer extends PickupItem {

	private static final int defaultAliveTime = -1;
	public static final String sprite = "hammer";

	public PickupHammer(int x, int y) {
		super(x, y, sprite, defaultAliveTime);
	}

	public PickupHammer(int x, int y, int aliveTime) {
		super(x, y, sprite, aliveTime);
	}

	@Override
	protected void OnPickUp() {
		Player p = Game.instance.player;
		UI ui = Game.instance.ui;
		if (!p.HasItem(sprite)) {
			p.items.add(new Hammer());
			p.selectedItem = p.items.size() - 1;
			ui.selectedItem.SetImage(p.items.get(p.selectedItem).sprite);
			p.OnGetItem(GetItemStopTime);
			SetParent(p);
			SetPosition(new Point(0, -Game.TileSize));
			Destroy(GetItemStopTime);
			Game.instance.PlayClip("newitem");
		} else {
			Game.instance.PlayClip("collect");
			Destroy();
		}
	}

}
