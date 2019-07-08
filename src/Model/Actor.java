package Model;

import java.awt.Point;
import java.util.Vector;
import javafx.util.Pair;

/**
 *
 * @author E6J9GS
 */
public class Actor {

	public enum Tag {
		NONE, PLAYER, ENEMY, WALL, GROUND_WALL, INVICIBLE, ITEM, PROJECTILE, DOOR
	}

	public class PointF {

		public float x;
		public float y;

		public PointF(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}

	protected String name = "";
	private int id = 0;
	private Actor parent = null;
	private Point position = new Point(0, 0);
	private boolean staticPosition = false;
	private Point Size = new Point(Game.TileSize, Game.TileSize);
	private PointF RenderOffset = new PointF(0.5f, 0.5f);
	private boolean isHidden = false;
	private boolean isActive = true;
	private Animator animator = null;
	private Collider collider = null;
	private Pair<String, String> sprite;
	private Tag tag = Tag.NONE;
	private boolean isFlipX = false;
	private boolean isFlipY = false;
	private Vector<MyComponent> components = new Vector();

	// GETTER/SETTER
	public final void SetParent(Actor p) {
		parent = p;
	}

	public final Actor GetParent() {
		return parent;
	}

	public final void SetPosition(Point p) {
		position = p;
	}

	public final Point GetPosition() {
		if (parent != null) {
			Point p = parent.GetPosition();
			return new Point(position.x + p.x, position.y + p.y);
		}
		return new Point(position.x, position.y);
	}

	public final void SetSize(Point p) {
		Size = p;
	}

	public final Point GetSize() {
		return new Point(Size.x, Size.y);
	}

	public final void SetRenderOffset(PointF p) {
		RenderOffset = p;
	}

	public final PointF GetRenderOffset() {
		return new PointF(RenderOffset.x, RenderOffset.y);
	}

	public final void SetHidden(boolean b) {
		if (isHidden && !b) {
			OnHide();
		}
		if (!isHidden && b) {
			OnShow();
		}
		isHidden = b;
	}

	public final boolean GetHidden() {
		return isHidden;
	}

	public final void SetActive(boolean b) {
		if (isActive && !b) {
			OnEnabled();
		}
		if (!isActive && b) {
			OnDisabled();
		}
		isActive = b;
	}

	public final boolean GetActive() {
		return isActive;
	}

	public final void SetTag(Tag tag) {
		this.tag = tag;
	}

	public final Tag GetTag() {
		return tag;
	}

	public final void SetFrame(String sprite, String frame) {
		this.sprite = new Pair(sprite, frame);
	}

	public final Pair<String, String> GetFrame() {
		return (HasAnimator() ? animator.getFrame() : sprite);
	}

	public final void SetFlipX(boolean flip) {
		isFlipX = flip;
	}

	public final boolean GetFlipX() {
		return isFlipX;
	}

	public final void SetFlipY(boolean flip) {
		isFlipY = flip;
	}

	public final boolean GetFlipY() {
		return isFlipY;
	}

	public final int GetId() {
		return id;
	}

	public final String GetName() {
		return name;
	}

	public final boolean IsStaticPosition() {
		return this.staticPosition;
	}

	public final void SetStaticPosition(boolean value) {
		this.staticPosition = value;
	}

	public final boolean GetColliderActive() {
		return HasCollider() && collider.GetActive();
	}

	public final boolean GetAnimatorActive() {
		return HasAnimator() && animator.GetActive();
	}

	public final Collider GetCollider() {
		return collider;
	}

	public final Animator GetAnimator() {
		return animator;
	}

	public final boolean HasAnimator() {
		return animator != null;
	}

	public final boolean HasCollider() {
		return collider != null;
	}

	protected final void AddAnimator(Animator animator) {
		this.animator = animator;
	}

	protected final void AddCollider(Collider collider) {
		this.collider = collider;
	}

	public final void Translate(Point way) {
		Point oldPosition = GetPosition();
		SetPosition(new Point(oldPosition.x + way.x, oldPosition.y + way.y));
		if (CheckCollision()) {
			SetPosition(oldPosition);
		}
		// Check collision

	}

	public final boolean CheckCollision() {
		boolean found = false;
		if (GetColliderActive()) {
			for (Actor a : Game.instance.actors) {
				if (a.equals(this) || !a.HasCollider()) {
					continue;
				}
				Collider other = a.GetCollider();
				if (other.isOverlapping(collider)) {
					if (!Game.instance.HasCollision(collider, other)) {
						other.OnCollisionBegin(collider);
						collider.OnCollisionBegin(other);
						Game.instance.collisions.add(new Pair(other, GetCollider()));
					}
					if (!other.GetIsTrigger()) {
						found = true;
					}
				}
			}
		}
		return found;
	}

	/**
	 *
	 * @param renderPriority Render Priority (Bigger means later/upper)
	 */
	public Actor(int renderPriority) {
		Game.instance.actors.add(this, renderPriority);
		this.id = Game.instance.GetID();
	}

	public Actor(int renderPriority, String name) {
		Game.instance.actors.add(this, renderPriority);
		this.name = name;
		this.id = Game.instance.GetID();
	}

	private int destroyedIn = -1;

	public final void Tick(int tick) {
		if (destroyedIn > -1) {
			if (destroyedIn == 0) {
				Destroy();
			}
			destroyedIn--;
		}
		if (!isActive || id == -1) {
			return;
		}
		if (GetAnimatorActive()) {
			animator.Animate(tick);
		}
		Update();
		for (MyComponent c : components) {
			c.Update();
		}
	}

	public final void Destroy() {
		Game.instance.actors.remove(this);
		this.id = -1;
	}

	public final void Destroy(int delay) {
		destroyedIn = delay;
	}

	@Override
	public boolean equals(Object obj) {
		return this.id != -1 && this.GetId() == ((Actor) obj).GetId();
	}

	public void OnShow() {
	}

	public void OnHide() {
	}

	public void Update() {
	}

	public void OnEnabled() {
	}

	public void OnDisabled() {
	}

	public void OnRoomChange(int x, int y) {
	}
}
