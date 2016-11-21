package graphic;

import java.util.concurrent.CopyOnWriteArrayList;

import javafx.scene.canvas.Canvas;

public class GameScreen extends Canvas {

	private static CopyOnWriteArrayList<IRenderable> objectOnScreen = new CopyOnWriteArrayList<IRenderable>();
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	public GameScreen() {
		super(WIDTH, HEIGHT);
		DrawingUtility.setGC(getGraphicsContext2D());
	}

	public static void repaint() {
		DrawingUtility.getGC().clearRect(0, 0, WIDTH, HEIGHT);
		objectOnScreen.stream().forEach(renderable -> renderable.draw());
	}

	public static CopyOnWriteArrayList<IRenderable> getObjectOnScreen() {
		return objectOnScreen;
	}

	public static void addObject(IRenderable object) {
		objectOnScreen.add(object);
	}

	public static void removeObject(IRenderable object) {
		objectOnScreen.remove(object);
	}

}
