/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Items.Pickups;

import Model.Actors.Player;
import Model.Game;
import Model.Items.Bow;
import View.UI;
import java.awt.Point;

/**
 *
 * @author Luminight
 */
public class PickupBow extends PickupItem {

	private static final int defaultAliveTime = -1;
	private final int count = 5;
	private final int firstCount = 5;
	public static final String sprite = "bow";

	public PickupBow(int x, int y) {
		super(x, y, sprite, defaultAliveTime);
	}

	public PickupBow(int x, int y, int aliveTime) {
		super(x, y, sprite, aliveTime);
	}

	@Override
	protected void OnPickUp() {
		Player p = Game.instance.player;
		UI ui = Game.instance.ui;

		if (!p.HasItem(sprite)) {
			p.items.add(new Bow());
			p.selectedItem = p.items.size() - 1;
			ui.selectedItem.SetImage(p.items.get(p.selectedItem).sprite);
			p.OnGetItem(GetItemStopTime);
			SetParent(p);
			SetPosition(new Point(0, -Game.TileSize));
			p.arrowCount = Math.min(p.maxArrowCount, p.arrowCount + firstCount);
			ui.arrows.SetValue(p.arrowCount);
			Game.instance.PlayClip("newitem");
			Destroy(GetItemStopTime);
			return;
		} else {
			p.arrowCount = Math.min(p.maxArrowCount, p.arrowCount + count);
			ui.arrows.SetValue(p.arrowCount);
			Game.instance.PlayClip("collect");
			Destroy();
		}
	}

}
