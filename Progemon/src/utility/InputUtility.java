package utility;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class InputUtility {

	private static int mouseX, mouseY;
	private static MouseEvent lastMouseClickEvent, lastMouseMoveEvent;
	private static ArrayList<InputEvent> inputEvents = new ArrayList<InputEvent>();
	/** When it gets elements, it clears the history of event. */
	private static KeyEvent lastKeyEvent, lastTypeKey;
	private static ArrayList<KeyEvent> holdingKeys = new ArrayList<KeyEvent>();
	private static ArrayList<KeyEvent> typeKeys = new ArrayList<KeyEvent>();

	public static void clear() {
		inputEvents.clear();
	}

	/** When get element, it clears the history of event. */
	public static final ArrayList<InputEvent> getInputEvents() {
		ArrayList<InputEvent> temp = new ArrayList<InputEvent>(inputEvents);
		inputEvents.clear();
		return temp;
	}

	public static final void addInputEvents(InputEvent inputEvent) {
		InputUtility.inputEvents.add(inputEvent);
	}

	public static final int getMouseX() {
		return mouseX;
	}

	public static final int getMouseY() {
		return mouseY;
	}

	public static final void setMouseX(int mouseX) {
		InputUtility.mouseX = mouseX;
	}

	public static final void setMouseY(int mouseY) {
		InputUtility.mouseY = mouseY;
	}

	public static final MouseEvent getLastMouseClickEvent() {
		MouseEvent temp = lastMouseClickEvent;
		lastMouseClickEvent = null;
		return temp;
	}

	public static final void setLastMouseClickEvent(MouseEvent lastMouseClickEvent) {
		InputUtility.lastMouseClickEvent = lastMouseClickEvent;
	}

	public static final MouseEvent getLastMouseMoveEvent() {
		return lastMouseMoveEvent;
	}

	public static final void setLastMouseMoveEvent(MouseEvent lastMouseMoveEvent) {
		InputUtility.lastMouseMoveEvent = lastMouseMoveEvent;
	}

	public static KeyEvent getLastKeyEvent() {
		KeyEvent temp = lastKeyEvent;
		lastKeyEvent = null;
		return temp;
	}

	public static void setLastKeyEvent(KeyEvent lastKeyEvent) {
		InputUtility.lastKeyEvent = lastKeyEvent;
	}

	public static void addHoldingKeys(KeyEvent kEvent) {
		boolean isIn = false;
		for (KeyEvent holdingKey : holdingKeys) {
			if (holdingKey.getKeyChar() == kEvent.getKeyChar()) {
				isIn = true;
			}
		}
		if (!isIn) {
			holdingKeys.add(kEvent);
		}
	}

	public static void removeHoldingKeys(KeyEvent kEvent) {
		for (int i = holdingKeys.size() - 1; i >= 0; i--) {
			if (holdingKeys.get(i).getKeyChar() == kEvent.getKeyChar()) {
				holdingKeys.remove(holdingKeys.get(i));
			}
		}
	}

	public static ArrayList<KeyEvent> getHoldingKeys() {
		return holdingKeys;
	}

	public static ArrayList<KeyEvent> getTypeKeys() {
		ArrayList<KeyEvent> token = new ArrayList<KeyEvent>(typeKeys);
		typeKeys.clear();
		return token;
	}

	public static void addTypeKeys(KeyEvent kEvent) {
		typeKeys.add(kEvent);
	}

	public static KeyEvent getLastTypeKey() {
		return lastTypeKey;
	}

	public static void setLastTypeKey(KeyEvent lastTypeKey) {
		InputUtility.lastTypeKey = lastTypeKey;
	}

}
