package utility;

import java.util.ArrayList;

import javafx.scene.input.KeyCode;

public class InputUtility {

	private static int mouseX, mouseY;
	// private static MouseEvent lastMouseClickEvent, lastMouseMoveEvent;
	// private static ArrayList<Event> events = new ArrayList<Event>();
	private static boolean mouseLeftDown, mouseRightDown, mouseOnScreen;
	private static boolean mouseLeftLastDown, mouseRightLastDown;
	// private static KeyEvent lastKeyEvent, lastTypeKey;
	private static ArrayList<KeyCode> keyPressed = new ArrayList<KeyCode>();
	private static ArrayList<KeyCode> keyTriggered = new ArrayList<KeyCode>();

	public static int getMouseX() {
		return mouseX;
	}

	public static void setMouseX(int mouseX) {
		InputUtility.mouseX = mouseX;
	}

	public static int getMouseY() {
		return mouseY;
	}

	public static void setMouseY(int mouseY) {
		InputUtility.mouseY = mouseY;
	}

	public static boolean isMouseLeftDown() {
		return mouseLeftDown;
	}

	public static void setMouseLeftDown(boolean mouseLeftDown) {
		InputUtility.mouseLeftDown = mouseLeftDown;
	}

	public static boolean isMouseRightDown() {
		return mouseRightDown;
	}

	public static void setMouseRightDown(boolean mouseRightDown) {
		InputUtility.mouseRightDown = mouseRightDown;
	}

	public static boolean isMouseOnScreen() {
		return mouseOnScreen;
	}

	public static void setMouseOnScreen(boolean mouseOnScreen) {
		InputUtility.mouseOnScreen = mouseOnScreen;
	}

	public static boolean isMouseLeftClicked() {
		return mouseLeftLastDown;
	}

	public static void setMouseLeftLastDown(boolean mouseLeftLastDown) {
		InputUtility.mouseLeftLastDown = mouseLeftLastDown;
	}

	public static boolean isMouseRightClicked() {
		return mouseRightLastDown;
	}

	public static void setMouseRightLastDown(boolean mouseRightLastDown) {
		InputUtility.mouseRightLastDown = mouseRightLastDown;
	}

	public static boolean getKeyPressed(KeyCode keycode) {
		return keyPressed.contains(keycode);
	}

	public static void setKeyPressed(KeyCode keycode, boolean pressed) {
		if (!keyPressed.contains(keycode) && pressed) {
			keyPressed.add(keycode);
		}
		if (keyPressed.contains(keycode) && !pressed) {
			keyPressed.remove(keycode);
		}
	}

	public static boolean getKeyTriggered(KeyCode keycode) {
		return keyTriggered.contains(keycode);
	}

	public static void setKeyTriggered(KeyCode keycode, boolean pressed) {
		if (!keyTriggered.contains(keycode) && pressed) {
			keyTriggered.add(keycode);
		}
		if (keyTriggered.contains(keycode) && !pressed) {
			keyTriggered.remove(keycode);
		}
	}

	/** Update Trigger Mechanism at end of Clock.tick()*/
	public static void postUpdate() {
		setMouseLeftLastDown(false);
		setMouseRightLastDown(false);
		keyTriggered.clear();
	}

	// public static void clear() {
	// events.clear();
	// }
	//
	// /** When get element, it clears the history of event. */
	// public static final ArrayList<Event> getEvents() {
	// ArrayList<Event> temp = new ArrayList<Event>(events);
	// events.clear();
	// return temp;
	// }
	//
	// public static final void addEvents(Event event) {
	// events.add(event);
	// }

	// public static final MouseEvent getLastMouseClickEvent() {
	// MouseEvent temp = lastMouseClickEvent;
	// lastMouseClickEvent = null;
	// return temp;
	// }
	//
	// public static final void setLastMouseClickEvent(MouseEvent
	// lastMouseClickEvent) {
	// InputUtility.lastMouseClickEvent = lastMouseClickEvent;
	// }
	//
	// public static final MouseEvent getLastMouseMoveEvent() {
	// return lastMouseMoveEvent;
	// }
	//
	// public static final void setLastMouseMoveEvent(MouseEvent
	// lastMouseMoveEvent) {
	// InputUtility.lastMouseMoveEvent = lastMouseMoveEvent;
	// }

	// public static KeyEvent getLastKeyEvent() {
	// KeyEvent temp = lastKeyEvent;
	// lastKeyEvent = null;
	// return temp;
	// }
	//
	// public static void setLastKeyEvent(KeyEvent lastKeyEvent) {
	// InputUtility.lastKeyEvent = lastKeyEvent;
	// }

	// public static void addKeys(KeyEvent kEvent) {
	// boolean isIn = false;
	// for (KeyEvent holdingKey : keyPressed) {
	// if (holdingKey.getCode() == kEvent.getCode()) {
	// isIn = true;
	// break;
	// }
	// }
	// if (!isIn) {
	// keyPressed.add(kEvent);
	// keyTrigger.add(kEvent);
	// }
	// }
	//
	// public static void removeHoldingKeys(KeyEvent kEvent) {
	// for (int i = keyPressed.size() - 1; i >= 0; i--) {
	// if (keyPressed.get(i).getCode() == kEvent.getCode()) {
	// keyPressed.remove(i);
	// break;
	// }
	// }
	// }
	//
	// public static ArrayList<KeyEvent> getHoldingKeys() {
	// return keyPressed;
	// }
	//
	// public static ArrayList<KeyEvent> getTypeKeys() {
	// ArrayList<KeyEvent> token = new ArrayList<KeyEvent>(keyTrigger);
	// keyTrigger.clear();
	// return token;
	// }

}
