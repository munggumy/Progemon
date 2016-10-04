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
	private static KeyEvent lastKeyEvent;

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
		return lastMouseClickEvent;
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

}
