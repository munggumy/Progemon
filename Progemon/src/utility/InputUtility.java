package utility;

import java.util.ArrayList;

import javafx.event.Event;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class InputUtility {

	private static int mouseX, mouseY;
	private static MouseEvent lastMouseClickEvent, lastMouseMoveEvent;
	private static ArrayList<Event> events = new ArrayList<Event>();
	/** When it gets elements, it clears the history of event. */
	private static KeyEvent lastKeyEvent, lastTypeKey;
	private static ArrayList<KeyEvent> holdingKeys = new ArrayList<KeyEvent>();
	private static ArrayList<KeyEvent> typeKeys = new ArrayList<KeyEvent>();

	public static void clear() {
		events.clear();
	}

	/** When get element, it clears the history of event. */
	public static final ArrayList<Event> getEvents() {
		ArrayList<Event> temp = new ArrayList<Event>(events);
		events.clear();
		return temp;
	}

	public static final void addEvents(Event event) {
		events.add(event);
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
<<<<<<< HEAD

	public static void addHoldingKeys(KeyEvent kEvent) {
		boolean isIn = false;
		for (KeyEvent holdingKey : holdingKeys) {
			if (holdingKey.getKeyChar() == kEvent.getKeyChar()) {
=======
	
	public static void addKeys(KeyEvent kEvent){
		boolean isIn = false;
		for (KeyEvent holdingKey : holdingKeys) {
			if (holdingKey.getCode() == kEvent.getCode()){
>>>>>>> 568bada80f9cfbe97bd4e374df501a6a2b400cf2
				isIn = true;
				break;
			}
		}
		if (!isIn) {
			holdingKeys.add(kEvent);
			typeKeys.add(kEvent);
		}
	}

	public static void removeHoldingKeys(KeyEvent kEvent) {
		for (int i = holdingKeys.size() - 1; i >= 0; i--) {
<<<<<<< HEAD
			if (holdingKeys.get(i).getKeyChar() == kEvent.getKeyChar()) {
				holdingKeys.remove(holdingKeys.get(i));
=======
			if (holdingKeys.get(i).getCode() == kEvent.getCode()){
				holdingKeys.remove(i);
				break;
>>>>>>> 568bada80f9cfbe97bd4e374df501a6a2b400cf2
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
<<<<<<< HEAD

	public static void addTypeKeys(KeyEvent kEvent) {
		typeKeys.add(kEvent);
	}

	public static KeyEvent getLastTypeKey() {
		return lastTypeKey;
	}

	public static void setLastTypeKey(KeyEvent lastTypeKey) {
		InputUtility.lastTypeKey = lastTypeKey;
	}

=======
	
>>>>>>> 568bada80f9cfbe97bd4e374df501a6a2b400cf2
}
