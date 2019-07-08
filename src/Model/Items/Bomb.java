/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Items;

import Model.*;
import Model.Actors.BombActor;
import View.UI;
import java.awt.Point;

/**
 *
 * @author Luminight
 */
public class Bomb extends Item {

	private Animator anim;
	private final int preventMoveTime = 20;

	public Bomb() {
		this.sprite = "bomb";
		player = Game.instance.player;
		anim = player.GetAnimator();
	}

	@Override
	public void Use() {
		anim.SetBool("use", true);
		Point pos = player.GetPosition();
		Point look = player.look.ToPoint();
		pos.x += look.x * player.GetSize().x;
		pos.y += look.y * player.GetSize().y;
		BombActor bomb = new BombActor();
		bomb.SetPosition(pos);
		player.bombCount--;
		UI.instance.bombs.SetValue(player.bombCount);
		player.preventMoveTime = preventMoveTime;
	}

	@Override
	public boolean CanUse() {
		return player.bombCount > 0;
	}

}
