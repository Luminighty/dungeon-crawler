/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.*;
import java.awt.Point;

/**
 *
 * @author Luminight
 */
public class UIElement extends Actor {

	public UIElement(int x, int y, int width, int height, String sprite, String frame) {
		super(0, "UIelement");
		if (sprite != null && frame != null) {
			SetFrame(sprite, frame);
		}
		SetPosition(new Point(x, y));
		SetSize(new Point(width, height));
		SetStaticPosition(true);
	}

	public void SetImage(String img) {
		SetFrame(GetFrame().getKey(), img);
	}

}
