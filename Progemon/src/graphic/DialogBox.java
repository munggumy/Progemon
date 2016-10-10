package graphic;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import utility.Clock;
import utility.InputUtility;

public class DialogBox implements IRenderable {

	protected static final String DIALOG_BOX_PATH = "load\\img\\dialogbox\\Theme1.png";

	private static BufferedImage dialogBoxImage = null;

	private static final int x = 0, y = 240;
	private static final Font DEFAULT_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 15);
	private static String message = "", nextWord = "";
	private static String[] messageOnScreen = { "", "" };
	private static Font font = DEFAULT_FONT;
	private static int textDelay = 5, textDelayCounter = 5, newLineDelay = 0, newLineDelayCounter = 0, currentLine = 0;
	private static int yShift = 0;
	private static boolean hasSentMessage = true;
	private static int endLineWidth;

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		DrawingUtility.drawDialogBox();
	}

	@Override
	public void getDepth() {
		// TODO Auto-generated method stub

	}

	public static void update() {
		KeyEvent kEvent = InputUtility.getLastKeyEvent();
		if (kEvent != null && kEvent.getKeyChar() == 'a') {
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
			if (DrawingUtility.computeStringWidth(font, (messageOnScreen[currentLine] + message.split(" ")[0])) > 280) {
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
		} else if (kEvent != null && kEvent.getKeyChar() == 'a') {
			clear();
			hasSentMessage = true;
		} else {
			endLineWidth = DrawingUtility.computeStringWidth(font, messageOnScreen[currentLine]);
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

	public static BufferedImage getDialogBoxImage() {
		if (dialogBoxImage == null) {
			loadDialogBoxImage();
		}
		return dialogBoxImage;
	}

	public static void setDialogBoxImage(BufferedImage dialogBoxImage) {
		DialogBox.dialogBoxImage = dialogBoxImage;
	}

	public static void loadDialogBoxImage() {
		try {
			setDialogBoxImage(ImageIO.read(new File(DialogBox.DIALOG_BOX_PATH)));
		} catch (IOException e) {
			System.err.println("Dialog Box Image Error");
			e.printStackTrace();
		}
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
