/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Items;

import Model.Actors.*;
import Model.Direction;
import Model.Game;
import View.UI;
import java.awt.Point;

/**
 *
 * @author e6j9gs
 */
public class Bow extends Item {

	private Player player;
	private final int preventMoveTime = 30;

	public Bow() {
		this.sprite = "bow";
		this.player = Game.instance.player;
	}

	@Override
	public boolean CanUse() {
		return player.arrowCount > 0;
	}

	@Override
	public void Use() {
		Point p = Game.instance.player.GetPosition();
		Direction d = Game.instance.player.look;
		Game.instance.PlayClip("slash");
		new ArrowActor(p, d);
		player.arrowCount--;
		UI.instance.arrows.SetValue(player.arrowCount);
		player.preventMoveTime = preventMoveTime;
	}

}
