package Model;

public class Controls {

	public enum Key {
		UP(0), DOWN(1), LEFT(2), RIGHT(3), ATTACK(4), USE(5), SELECT(6), PAUSE(7);

		Key(int id) {
			this.id = id;
		}
		public int id;
	}

	private static int[] keys = new int[Key.values().length];
	private static int[] altKeys = new int[Key.values().length];

	private static boolean[] keyPressed = new boolean[Key.values().length];
	private static boolean[] keyReleased = new boolean[Key.values().length];
	private static boolean[] keyHolded = new boolean[Key.values().length];

	public static boolean isButtonHeld(Key key) {
		return keyHolded[key.id];
	}

	public static boolean isButtonReleased(Key key) {
		return keyReleased[key.id];
	}

	public static boolean isButtonPressed(Key key) {
		return keyPressed[key.id];
	}

	public static int GetKey(Key key) {
		return keys[key.id];
	}

	public static int GetAltKey(Key key) {
		return altKeys[key.id];
	}

	public static void SetKey(Key key, int keyCode) {
		keys[key.id] = keyCode;
	}

	public static void SetAltKey(Key key, int keyCode) {
		keys[key.id] = keyCode;
	}

	public static void HoldButton(int keyCode) {
		for (int i = 0; i < Key.values().length; i++) {
			if (keys[i] == keyCode || altKeys[i] == keyCode) {
				if (!keyHolded[i]) {
					keyPressed[i] = true;
				}
				keyHolded[i] = true;
			}
		}
	}

	public static void ReleaseButton(int keyCode) {
		for (int i = 0; i < Key.values().length; i++) {
			if (keys[i] == keyCode || altKeys[i] == keyCode) {
				keyReleased[i] = true;
				keyHolded[i] = false;
			}
		}
	}

	public static void UpdatePresses() {
		for (int i = 0; i < Key.values().length; i++) {
			keyPressed[i] = false;
			keyReleased[i] = false;
		}
	}

	public static void SetDefaultControls() {
		keys[Key.UP.id] = 38;	    // Arrow Up
		keys[Key.DOWN.id] = 40;	    // Arrow Down
		keys[Key.LEFT.id] = 37;	    // Arrow Left
		keys[Key.RIGHT.id] = 39;    // Arrow Right

		keys[Key.ATTACK.id] = 67;   // C
		keys[Key.USE.id] = 88;	    // X
		keys[Key.SELECT.id] = 32;   // Space
		keys[Key.PAUSE.id] = 10;    // Enter

		altKeys[Key.UP.id] = 87;    // W
		altKeys[Key.DOWN.id] = 83;  // S
		altKeys[Key.LEFT.id] = 65;  // A
		altKeys[Key.RIGHT.id] = 68; // D

		altKeys[Key.ATTACK.id] = 71; // C
		altKeys[Key.USE.id] = 72;    // X
		altKeys[Key.SELECT.id] = -1; // Space
		altKeys[Key.PAUSE.id] = -1;  // Enter

	}

}
