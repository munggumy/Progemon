package graphic;

import java.io.File;

import audio.SFXUtility;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import utility.Clock;
import utility.InputUtility;

public class DialogBox implements IRenderable {

	protected static final String DIALOG_BOX_PATH = "load\\img\\dialogbox\\Theme2.png";
	private static Image dialogBoxImage = null;

	private static final int x = 2, y = 294;
	// private static final Font DEFAULT_FONT = new Font(Font.MONOSPACED,
	// Font.PLAIN, 15);
	private static final Font DEFAULT_FONT = new Font("Monospaced", 20);
	private static String message = "", nextWord = "";
	private static String[] messageOnScreen = { "", "" };
	private static Font font = DEFAULT_FONT;
	private static int textDelay = 5, textDelayCounter = 5, newLineDelay = 0, newLineDelayCounter = 0, currentLine = 0;
	private static int yShift = 0;
	private static boolean hasSentMessage = true;
	private static int endLineWidth;
	private static boolean visible = true;

	public static final KeyCode advancingKey = KeyCode.A;

	@Override
	public void draw() {
		DrawingUtility.drawDialogBox();
	}

	@Override
	public int getDepth() {
		return 0;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		DialogBox.visible = visible;
	}

	@Override
	public void hide() {
		visible = false;
		IRenderableHolder.removeWorldObject(this);
	}

	@Override
	public void show() {
		IRenderableHolder.addWorldObject(this);
		visible = true;
	}

	public static void update() {
		// KeyEvent kEvent = InputUtility.getLastKeyEvent();
		// if (kEvent != null && kEvent.getCode().equals(advancingKey)) {
		// textDelay = 0;
		// }
		if (InputUtility.getKeyTriggered(advancingKey)) {
			textDelay = 0;
		}
		if (nextWord.length() > 0) {
			if (textDelayCounter >= textDelay) {
				messageOnScreen[currentLine] += nextWord.substring(0, 1);
				nextWord = nextWord.substring(1, nextWord.length());
				textDelayCounter = 0;
			} else {
				textDelayCounter++;
			}
		} else if (message.length() > 0) {
			if (DrawingUtility.computeStringWidth(messageOnScreen[currentLine] + message.split(" ")[0], font) > 280) {
				if (currentLine < 1) {
					currentLine += 1;
				} else {
					toNewLine();
				}
			} else {
				nextWord = message.split(" ")[0];
				message = message.substring(nextWord.length(), message.length());
				if (message.split(" ").length > 0 && message.length() > 0) {
					message = message.substring(1, message.length());
					nextWord += " ";
				}
			}
			// } else if (kEvent != null &&
			// kEvent.getCode().equals(advancingKey)) {
		} else if (InputUtility.getKeyTriggered(advancingKey)) {
			SFXUtility.playSound("dialog_box_next");
			clear();
			hasSentMessage = true;
		} else if (endLineWidth == 0) {
			endLineWidth = (int) DrawingUtility.computeStringWidth(messageOnScreen[currentLine], font);
		}
	}

	private static void toNewLine() {
		if (newLineDelayCounter == newLineDelay) {
			if (yShift >= 25) {
				messageOnScreen[0] = messageOnScreen[1];
				messageOnScreen[1] = "";
				yShift = 0;
				textDelay = 5;
			} else {
				yShift += 5;
			}
			newLineDelayCounter = 0;
		} else {
			newLineDelayCounter++;
		}
	}

	public static Image getDialogBoxImage() {
		if (dialogBoxImage == null) {
			loadDialogBoxImage();
		}
		return dialogBoxImage;
	}

	public static void setDialogBoxImage(Image dialogBoxImage) {
		DialogBox.dialogBoxImage = dialogBoxImage;
	}

	public static void loadDialogBoxImage() {
		File file = new File(DialogBox.DIALOG_BOX_PATH);
		dialogBoxImage = DrawingUtility.resize(new Image(file.toURI().toString()), 2);
	}

	public static int getX() {
		return x;
	}

	public static int getY() {
		return y;
	}

	public static String getMessage() {
		return message;
	}

	public static void sentMessage(String message) {
		DialogBox.message = message;
		hasSentMessage = false;
		while (!hasSentMessage) {
			update();
			// MyCanvas.repaint();
			Clock.tick();
		}
	}

	public static void clear() {
		messageOnScreen[0] = "";
		messageOnScreen[1] = "";
		currentLine = 0;
		endLineWidth = 0;
		textDelay = 5;
	}

	public static Font getFont() {
		return font;
	}

	public static void setFont(Font font) {
		DialogBox.font = font;
	}

	public static int getTextDelay() {
		return textDelay;
	}

	public static void setTextDelay(int textDelay) {
		DialogBox.textDelay = textDelay;
	}

	public static String[] getMessageOnScreen() {
		return messageOnScreen;
	}

	public static int getyShift() {
		return yShift;
	}

	public static boolean hasSentMessage() {
		return hasSentMessage;
	}

	public static int getEndLineWidth() {
		return endLineWidth;
	}

	public static int getCurrentLine() {
		return currentLine;
	}

}
