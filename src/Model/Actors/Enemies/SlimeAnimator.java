/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Actors.Enemies;

import Model.Animator;
import Model.Direction;
import java.awt.Point;
import javafx.util.Pair;

/**
 *
 * @author Luminight
 */
public class SlimeAnimator extends Animator {

	private final int speed = 60;

	public SlimeAnimator(Pair<String, String> firstFrame) {
		super(firstFrame);
	}

	@Override
	public void Animate(int tick) {

		int frame = tick / speed % 2;

		int x = GetInt("wayX");
		int y = GetInt("wayY");

		Direction way = Direction.fromPoint(new Point(x, y));

		switch (way) {
			case Left:
			case Right:
				SetFrame("side_" + frame);
				break;
			default:
				SetFrame(way.toString().toLowerCase() + "_" + frame);
				break;
		}
		SetBool("flipX", x < 0);
	}

}
