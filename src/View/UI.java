/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.*;
import Model.Actors.Player;
import java.awt.Point;

/**
 *
 * @author Luminight
 */
public class UI extends Actor {

	public static UI instance;

	private final int NUM_BOLD_WIDTH = 8;
	private final int NUM_BOLD_HEIGHT = 14;
	private final int NUM_OFFSET = 1;
	private final int NUM_WIDTH = 12;
	private final int NUM_HEIGHT = 20;

	public int time = 500;

	public UIElement selectedItem;
	public UINumber arrows;
	public UINumber bombs;
	public UINumber keys;
	public UINumber treasures;
	public UINumber timer;
	public UIHealth health;

	public UI() {
		super(2);
		SetStaticPosition(true);
		instance = this;
		SetFrame("ui", "holder");
		SetRenderOffset(new PointF(0, 0));

		Point size = (Point) Game.instance.roomSize.clone();
		size.y = size.x - size.y;
		size.x *= Game.TileSize;
		size.y *= Game.TileSize;
		SetSize(size);
		SetPosition(new Point(0, 0));
		SetUI();
	}

	void SetUI() {
		time = Game.instance.Time;

		new UIElement(64, 38, 108, 24, "ui", "life").SetParent(this);
		new UIElement(260, 70, 42, 68, "ui", "useframe").SetParent(this);

		new UIElement(GetSize().x - 54 - 16, 38, 108, 24, "ui", "time").SetParent(this);
		timer = new UINumber(GetSize().x - 84, 88, NUM_WIDTH, NUM_HEIGHT, NUM_OFFSET, 3, time, false, true);
		timer.SetParent(this);

		health = new UIHealth(16, 70, Game.instance.player.health);
		health.SetRenderOffset(new PointF(0, 0));
		health.SetParent(this);

		selectedItem = new UIElement(260, 75, 26, 42, "ui", "");
		selectedItem.SetParent(this);
		Player p = Game.instance.player;
		if (!p.items.isEmpty()) {
			selectedItem.SetImage(p.items.get(0).sprite);
		}

		UIElement statHolder = new UIElement(150, 60, 0, 0, null, null);
		AddStatHolder(statHolder);
		statHolder.SetParent(this);
	}

	void AddStatHolder(UIElement parent) {
		int offsetY = 0;
		int addOffset = 16;

		treasures = new UINumber(22, offsetY, NUM_BOLD_WIDTH, NUM_BOLD_HEIGHT, NUM_OFFSET, 2, Game.instance.player.treasureCount, true, true);
		UIElement treasureIcon = new UIElement(0, offsetY, 13, 21, "ui", "treasure");
		UIElement treasurex = new UIElement(15, offsetY, 5, 5, "ui", "x");
		treasureIcon.SetParent(parent);
		treasureIcon.SetRenderOffset(new PointF(0, 1.0f));
		treasurex.SetParent(parent);
		treasurex.SetRenderOffset(new PointF(0, 1.0f));
		treasures.SetRenderOffset(new PointF(0, 1.0f));
		treasures.SetParent(parent);

		offsetY += addOffset;
		arrows = new UINumber(22, offsetY, NUM_BOLD_WIDTH, NUM_BOLD_HEIGHT, NUM_OFFSET, 2, Game.instance.player.arrowCount, true, true);
		UIElement arrowIcon = new UIElement(0, offsetY, 13, 21, "ui", "arrow");
		UIElement arrowx = new UIElement(15, offsetY, 5, 5, "ui", "x");
		arrowIcon.SetParent(parent);
		arrowIcon.SetRenderOffset(new PointF(0, 1.0f));
		arrowx.SetParent(parent);
		arrowx.SetRenderOffset(new PointF(0, 1.0f));
		arrows.SetRenderOffset(new PointF(0, 1.0f));
		arrows.SetParent(parent);

		offsetY += addOffset;
		bombs = new UINumber(22, offsetY, NUM_BOLD_WIDTH, NUM_BOLD_HEIGHT, NUM_OFFSET, 2, Game.instance.player.bombCount, true, true);
		UIElement bombIcon = new UIElement(0, offsetY, 13, 21, "ui", "bomb");
		UIElement bombx = new UIElement(15, offsetY, 5, 5, "ui", "x");
		bombIcon.SetParent(parent);
		bombIcon.SetRenderOffset(new PointF(0, 1.0f));
		bombx.SetParent(parent);
		bombx.SetRenderOffset(new PointF(0, 1.0f));
		bombs.SetRenderOffset(new PointF(0, 1.0f));
		bombs.SetParent(parent);

		offsetY += addOffset;
		keys = new UINumber(22, offsetY, NUM_BOLD_WIDTH, NUM_BOLD_HEIGHT, NUM_OFFSET, 2, Game.instance.player.keyCount, true, true);
		UIElement keyIcon = new UIElement(0, offsetY, 13, 21, "ui", "key");
		UIElement keyx = new UIElement(15, offsetY, 5, 5, "ui", "x");
		keyIcon.SetParent(parent);
		keyIcon.SetRenderOffset(new PointF(0, 1.0f));
		keyx.SetParent(parent);
		keyx.SetRenderOffset(new PointF(0, 1.0f));
		keys.SetRenderOffset(new PointF(0, 1.0f));
		keys.SetParent(parent);
	}

	@Override
	public void Update() {
		if (Game.instance.GetTick() % 70 == 0) {
			TimeUpdate();
		}
	}

	void TimeUpdate() {
		time = Math.max(0, time - 1);
		timer.SetValue(time);
	}

}
