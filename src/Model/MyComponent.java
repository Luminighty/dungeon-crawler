/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Luminight
 */
public class MyComponent {

	private boolean isActive = true;
	private int id = 0;

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

	public final int GetId() {
		return id;
	}

	public void Update() {
	}

	public void OnEnabled() {
	}

	public void OnDisabled() {
	}

	public MyComponent() {
		id = Game.instance.GetID();
	}

	@Override
	public boolean equals(Object obj) {
		return this.id != -1 && this.GetId() == ((Actor) obj).GetId();
	}
}
