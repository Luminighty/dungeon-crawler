/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Items;

import Model.Actors.HammerActor;
import Model.Game;

/**
 *
 * @author e6j9gs
 */
public class Hammer extends Item {

	private final int preventMoveTime = 50;

	public Hammer() {
		this.sprite = "hammer";
		this.player = Game.instance.player;
	}

	@Override
	public boolean CanUse() {
		return true;
	}

	@Override
	public void Use() {
		player.preventMoveTime = preventMoveTime;
		new HammerActor(player.look, player, 50);
		player.GetAnimator().SetBool("isHitting", true);
	}
}
