/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Items.Pickups;

import Model.Actors.Player;
import Model.Game;
import Model.Items.Bomb;
import View.UI;
import java.awt.Point;

/**
 *
 * @author Luminight
 */
public class PickupBomb extends PickupItem {

	private static final int defaultAliveTime = 600;
	private final int count = 1;
	private final int firstCount = 3;
	public static final String sprite = "bomb";

	public PickupBomb(int x, int y) {
		super(x, y, sprite, defaultAliveTime);
	}

	public PickupBomb(int x, int y, int aliveTime) {
		super(x, y, sprite, aliveTime);
	}

	@Override
	protected void OnPickUp() {
		Player p = Game.instance.player;
		UI ui = Game.instance.ui;

		if (!p.HasItem(sprite)) {
			p.items.add(new Bomb());
			p.selectedItem = p.items.size() - 1;
			ui.selectedItem.SetImage(p.items.get(p.selectedItem).sprite);
			p.OnGetItem(GetItemStopTime);
			SetParent(p);
			SetPosition(new Point(0, -Game.TileSize));
			p.bombCount = Math.min(p.maxBombCount, p.bombCount + firstCount);
			ui.bombs.SetValue(p.bombCount);
			Game.instance.PlayClip("newitem");
			Destroy(GetItemStopTime);
			return;
		} else {
			p.bombCount = Math.min(p.maxBombCount, p.bombCount + count);
			ui.bombs.SetValue(p.bombCount);
			Game.instance.PlayClip("collect");
			Destroy();
		}
	}

}
