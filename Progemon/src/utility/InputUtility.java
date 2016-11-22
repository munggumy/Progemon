package utility;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.input.KeyCode;

public class InputUtility {

	private static int mouseX, mouseY;
	private static boolean mouseLeftDown, mouseRightDown, mouseOnScreen;
	private static boolean mouseLeftLastDown, mouseRightLastDown;
	private static Set<KeyCode> keyPressed = new HashSet<KeyCode>();
	private static Set<KeyCode> keyTriggered = new HashSet<KeyCode>();

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
		if (pressed) {
			keyPressed.add(keycode);
		} else {
			keyPressed.remove(keycode);
		}
	}

	public static boolean getKeyTriggered(KeyCode keycode) {
		return keyTriggered.contains(keycode);
	}

	public static void setKeyTriggered(KeyCode keycode, boolean pressed) {
		if (pressed) {
			keyPressed.add(keycode);
		} else {
			keyPressed.remove(keycode);
		}
	}

	/** Update Trigger Mechanism at end of Clock.tick() */
	public static void postUpdate() {
		setMouseLeftLastDown(false);
		setMouseRightLastDown(false);
		keyTriggered.clear();
	}
}
