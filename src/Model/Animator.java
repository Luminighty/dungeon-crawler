package Model;

import java.util.TreeMap;
import javafx.util.Pair;

/**
 *
 * @author Luminight
 */
public abstract class Animator {

	public Animator(Pair<String, String> firstFrame) {
		frame = firstFrame;
		integers = new TreeMap();
		strings = new TreeMap();
		booleans = new TreeMap();
		Init();
	}

	private Pair<String, String> frame;
	private boolean active = true;

	public final Pair<String, String> getFrame() {
		return frame;
	}

	public final String getFrameString() {
		return frame.getValue();
	}

	public final String getSprite() {
		return frame.getKey();
	}

	public final boolean GetActive() {
		return active;
	}

	public final void SetActive(boolean active) {
		this.active = active;
	}

	public final void SetFrame(Pair<String, String> frame) {
		this.frame = frame;
	}

	public final void SetFrame(String sprite, String frame) {
		this.frame = new Pair(sprite, frame);
	}

	public final void SetSprite(String sprite) {
		this.frame = new Pair(sprite, frame.getValue());
	}

	public final void SetFrame(String frame) {
		this.frame = new Pair(this.frame.getKey(), frame);
	}

	private TreeMap<String, Integer> integers;
	private TreeMap<String, String> strings;
	private TreeMap<String, Boolean> booleans;

	public final void SetInt(String name, int value) {
		integers.put(name, value);
	}

	public final void SetString(String name, String value) {
		strings.put(name, value);
	}

	public final void SetBool(String name, boolean value) {
		booleans.put(name, value);
	}

	public final int GetInt(String name) {
		if (!integers.containsKey(name)) {
			SetInt(name, 0);
		}
		return integers.get(name);
	}

	public final String GetString(String name) {
		if (!strings.containsKey(name)) {
			SetString(name, "");
		}
		return strings.get(name);
	}

	public final boolean GetBool(String name) {
		if (!booleans.containsKey(name)) {
			SetBool(name, false);
		}
		return booleans.get(name);
	}

	public abstract void Animate(int tick);

	public void Init() {
	}

}
