/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Vector;

/**
 *
 * @author Luminight
 */
public abstract class Collider {

	public Collider(Actor parent) {
		this.parent = parent;
		bounds = new Rectangle(0, 0, parent.GetSize().x, parent.GetSize().y);
		Init();
	}

	protected Rectangle bounds;
	private static final int distance = 1;
	public Actor parent;
	private boolean active = true;
	private boolean isTrigger = false;

	protected boolean debugCollision = false;
	protected Color debugColor = Color.GREEN;

	private Vector<Actor.Tag> ignoreTags = new Vector();

	public void OnCollisionBegin(Collider other) {
	}

    public void OnCollisionStay(Collider other) {
	}

    public void OnCollisionEnd(Collider other) {
	}

    
    /**
     * Add Ignored tags (Ignores specific tagged actors)
     * @param tag 
     */
    public final void AddTag(Actor.Tag tag) {
		if (!ignoreTags.contains(tag)) {
			ignoreTags.add(tag);
		}
	}

	/**
	 * Remove Ignored tags (Ignores specific tagged actors)
	 *
	 * @param tag
	 */
	public final void RemoveTag(Actor.Tag tag) {
		ignoreTags.remove(tag);
	}

	public final boolean GetActive() {
		return active;
	}

	public final void SetActive(boolean active) {
		this.active = active;
	}

	public final boolean getDebugCollision() {
		return debugCollision;
	}

	public final Color getDebugColor() {
		return debugColor;
	}

	public final boolean GetIsTrigger() {
		return isTrigger;
	}

	public final void SetIsTrigger(boolean active) {
		this.isTrigger = active;
	}

	public final Rectangle GetBounds() {
		Rectangle b = new Rectangle();
		b.x = (int) (parent.GetPosition().x - (parent.GetSize().x * parent.GetRenderOffset().x) + bounds.x);
		b.y = (int) (parent.GetPosition().y - (parent.GetSize().y * parent.GetRenderOffset().y) + bounds.y);
		b.width = bounds.width;
		b.height = bounds.height;
		return b;
	}

	public final boolean isOverlapping(Collider other) {
		if (!other.active || !active || !parent.GetActive() || !other.parent.GetActive() || ignoreTags.contains(other.parent.GetTag()) || other.ignoreTags.contains(parent.GetTag())) {
			return false;
		}
		return GetBounds().intersects(other.GetBounds());
	}

	public final boolean isInDistance(Collider other) {
		if (!other.active || !active || !parent.GetActive() || !other.parent.GetActive() || ignoreTags.contains(other.parent.GetTag()) || other.ignoreTags.contains(parent.GetTag())) {
			return false;
		}
		Rectangle b = GetBounds();
		Rectangle tempBounds = new Rectangle(b.x - distance, b.y - distance, b.width + (distance * 2), b.height + (distance * 2));
		return tempBounds.intersects(other.GetBounds());
	}

	public void Init() {
	}

}
