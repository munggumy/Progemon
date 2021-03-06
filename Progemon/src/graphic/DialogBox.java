package graphic;

import java.io.File;
import java.util.ArrayList;

import audio.SFXUtility;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import utility.Clock;
import utility.InputUtility;

public class DialogBox implements IRenderable {

	public static final DialogBox instance = new DialogBox();
	public static final KeyCode advancingKey = KeyCode.Z;
	public static final Color FONT_COLOR = Color.BLACK;
	protected static final String DIALOG_BOX_PATH = "load\\img\\dialogbox\\Theme2.png";
	private static final int x = 2, y = 294;

	private Image dialogBoxImage = null;
	private String line = "", nextWord = "";
	private ArrayList<String> messages = new ArrayList<>(), lines = new ArrayList<>();
	private String[] messageOnScreen = { "", "" };
	private Font font = new Font("Tahoma", 20);
	private int textDelay = 5, textDelayCounter = 5, newLineDelay = 0, newLineDelayCounter = 0, currentLine = 0;
	private int yShift = 0;
	private boolean hasSentDialog = true, hasSentLine = true;
	private int endLineWidth;
	private boolean visible = false, showSign = false;


	public DialogBox() {
		hide();
	}

	@Override
	public void draw() {
		DrawingUtility.drawDialogBox();
	}

	@Override
	public int getDepth() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void hide() {
		visible = false;
		IRenderableHolder.removeFightObject(this);
		IRenderableHolder.removeWorldObject(this);
	}

	@Override
	public void show() {
		IRenderableHolder.addFightObject(this);
		IRenderableHolder.addWorldObject(this);
		visible = true;
	}

	public void sentWord() {
		// KeyEvent kEvent = InputUtility.getLastKeyEvent();
		// if (kEvent != null && kEvent.getCode().equals(advancingKey)) {
		// textDelay = 0;
		// }
		textDelay = 5;
		if (InputUtility.getKeyPressed(advancingKey)) {
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
		} else if (line.length() > 0) {
			if (DrawingUtility.computeStringWidth(messageOnScreen[currentLine] + line.split(" ")[0], font) > 360) {
				toNewLine();
			} else {
				nextWord = line.split(" ")[0];
				line = line.substring(nextWord.length(), line.length());
				if (line.split(" ").length > 0 && line.length() > 0) {
					line = line.substring(1, line.length());
					nextWord += " ";
				}
			}
			// } else if (kEvent != null &&
			// kEvent.getCode().equals(advancingKey)) {
		} else {
			hasSentLine = true;
		}
	}

	private void toNewLine() {
		if (currentLine < 1) {
			currentLine += 1;
			return;
		}
		waitForResponse(true);
		while (messageOnScreen[1].length() > 0) {
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
			Clock.tick();
		}
	}

	private void waitForResponse(boolean showSign) {
		endLineWidth = (int) DrawingUtility.computeStringWidth(messageOnScreen[currentLine], font);
		this.showSign = showSign;
		while (!InputUtility.getKeyTriggered(advancingKey)) {
			Clock.tick();
		}
		SFXUtility.playSound("dialog_box_next");
		this.showSign = false;
	}

	public Image getDialogBoxImage() {
		if (dialogBoxImage == null) {
			loadDialogBoxImage();
		}
		return dialogBoxImage;
	}

	public void setDialogBoxImage(Image dialogBoxImage) {
		this.dialogBoxImage = dialogBoxImage;
	}

	public void loadDialogBoxImage() {
		File file = new File(DialogBox.DIALOG_BOX_PATH);
		dialogBoxImage = DrawingUtility.resize(new Image(file.toURI().toString()), 2);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void sentDialog(String dialog) {
		hasSentDialog = false;
		for (String string : dialog.split("(@@@n)+")) {
			if (!string.equals("")) {
				messages.add(string);
			}
		}
		while (messages.size() > 0) {
			sentMessage(messages.get(0));
			messages.remove(0);
			waitForResponse(messages.size() > 0);
			clear();
		}
		hasSentDialog = true;
	}

	private void sentMessage(String message) {
		for (String string : message.split("(@@@t)+")) {
			if (!string.equals("")) {
				lines.add(string);
			}
		}
		while (lines.size() > 0) {
			sentLine(lines.get(0));
			lines.remove(0);
			if (lines.size() > 0) {
				toNewLine();
			}
		}
	}

	public void sentLine(String line) {
		this.line = line.trim();
		hasSentLine = false;
		while (!hasSentLine) {
			sentWord();
			Clock.tick();
		}
	}

	public void clear() {
		messageOnScreen[0] = "";
		messageOnScreen[1] = "";
		currentLine = 0;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public int getTextDelay() {
		return textDelay;
	}

	public void setTextDelay(int textDelay) {
		this.textDelay = textDelay;
	}

	public String[] getMessageOnScreen() {
		return messageOnScreen;
	}

	public int getyShift() {
		return yShift;
	}

	public boolean hasSentMessage() {
		return hasSentDialog;
	}

	public int getEndLineWidth() {
		return endLineWidth;
	}

	public int getCurrentLine() {
		return currentLine;
	}

	public boolean isShowSign() {
		return showSign;
	}

}
