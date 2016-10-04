package utility;


import java.awt.event.InputEvent;
import java.util.ArrayList;

public class InputUtility {

	private static int mouseX, mouseY;
	private static boolean mouseIsClicked;
	private static ArrayList<InputEvent> inputEvents = new ArrayList<InputEvent>();
	
	
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

	public static final boolean isMouseIsClicked() {
		return mouseIsClicked;
	}

	public static final void setMouseX(int mouseX) {
		InputUtility.mouseX = mouseX;
	}

	public static final void setMouseY(int mouseY) {
		InputUtility.mouseY = mouseY;
	}

	public static final void setMouseIsClicked(boolean mouseIsClicked) {
		InputUtility.mouseIsClicked = mouseIsClicked;
	}

}
