/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.awt.Point;

/**
 *
 * @author Luminight
 */
public enum Direction {
	Up(0), Down(1), Left(2), Right(3);

	Direction(int id) {
		this.id = id;
	}

	/**
	 * Directions: (0,-1) -> Up(0); (0,1) -> Down(1); (1,0) -> Right(3); (-1,0)
	 * -> Left(2);
	 *
	 * @param p Point
	 */
	public static Direction fromPoint(Point p) {
		int id = 0;
		if (Math.abs(p.x) > Math.abs(p.y)) {
			if (p.x > 0) {
				return Direction.Right;
			} else {
				return Direction.Left;
			}
		} else {
			if (p.y > 0) {
				return Direction.Down;
			} else {
				return Direction.Up;
			}
		}
	}

	public Point ToPoint() {
		Point p = new Point(0, 0);
		switch (id) {
			case 0:
				p.y = -1;
				break;
			case 1:
				p.y = 1;
				break;
			case 2:
				p.x = -1;
				break;
			case 3:
				p.x = 1;
				break;
		}
		return p;
	}

	public static Direction TurnAround(Direction way) {
		switch (way) {
			case Down:
				return Up;
			case Up:
				return Down;
			case Left:
				return Right;
			case Right:
				return Left;
		};
		return null;
	}

	int id;
}
