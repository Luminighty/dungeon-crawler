/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.awt.Point;
import java.util.Vector;

/**
 *
 * @author Luminight
 */
public class UIHealth extends UIElement {

	final Point HeartSize = new Point(19, 19);
	Point HeartOffset = new Point(5, 5);
	final int rowCount = 5;
	int value = 6;

	public Vector<UIElement> hearts = new Vector();

	public UIHealth(int x, int y, int value) {
		super(x, y, 0, 0, null, null);
		this.name = "UINumber";
		this.value = value;

		UpdateHealthBar();
	}

	void UpdateHealthBar() {
		int fullHeart = value / 2;
		int halfHeart = value % 2;
		int i;
		while (fullHeart + halfHeart > hearts.size()) {
			AddHeart();
		}

		for (i = 0; i < fullHeart; i++) {
			hearts.get(i).SetImage("heart");
			hearts.get(i).SetHidden(false);
		}
		if (halfHeart > 0) {
			hearts.get(i).SetImage("halfheart");
			hearts.get(i).SetHidden(false);
		}

		for (i = Math.max(0, fullHeart + halfHeart); i < hearts.size(); i++) {
			hearts.get(i).SetHidden(true);
		}
	}

	void AddHeart() {
		int column = hearts.size() % rowCount;
		int row = hearts.size() / rowCount;
		UIElement e = new UIElement(column * (HeartSize.x + HeartOffset.x), row * (HeartSize.y + HeartOffset.y), HeartSize.x, HeartSize.y, "ui", "heart");
		e.SetParent(this);
		hearts.add(e);
	}

	public void SetValue(int value) {
		if (value == this.value) {
			return;
		}
		this.value = value;
		UpdateHealthBar();
	}

}
