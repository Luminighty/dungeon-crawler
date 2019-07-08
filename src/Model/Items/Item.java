/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Items;

import Model.Actors.Player;

/**
 *
 * @author Luminight
 */
public abstract class Item {

	public String sprite;

	protected Player player;

	public abstract void Use();

	public abstract boolean CanUse();

}
