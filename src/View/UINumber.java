/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.util.Vector;

/**
 *
 * @author Luminight
 */
public class UINumber extends UIElement {

	public Vector<UIElement> numbers;
	int value = 0;
	int digits = 0;
	String bold = "";
	boolean hideZeros;

	public int GetValue() {
		return value;
	}

	public void SetValue(int val) {
		if (val == this.value) {
			return;
		}
		this.value = val;
		UpdateNumber();
	}

	public UINumber(int x, int y, int width, int height, int offset, int digits, int startValue, boolean bold, boolean hideZeros) {
		super(x, y, 0, 0, null, null);
		this.name = "UINumber";
		numbers = new Vector();
		this.digits = digits;
		this.bold = (bold) ? "bold_" : "";
		this.hideZeros = hideZeros;
		for (int i = 0; i < digits; i++) {
			UIElement num = new UIElement(((width + offset) * i), 0, width, height, "ui", "0");
			num.SetParent(this);
			num.SetRenderOffset(new PointF(0, 1.0f));
			numbers.add(num);
		}
		value = startValue;
		UpdateNumber();
	}

	void UpdateNumber() {
		String val = value + "";
		if (hideZeros) {
			for (int i = 0; i < digits; i++) {
				if (i < val.length()) {
					numbers.get(i).SetImage(bold + val.charAt(i));
					numbers.get(i).SetHidden(false);
				} else {
					numbers.get(i).SetHidden(true);
				}
			}
		} else {
			for (int i = 0; i < digits; i++) {
				char ch = (i < digits - val.length()) ? '0' : val.charAt(i - (digits - val.length()));
				numbers.get(i).SetImage(bold + ch);
			}
		}
	}

}
