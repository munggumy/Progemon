package graphic;

import javafx.scene.canvas.Canvas;

public class GameScreen extends Canvas {

	// private static CopyOnWriteArrayList<IRenderable> objectOnScreen = new
	// CopyOnWriteArrayList<IRenderable>();
	public static final int WIDTH = 480;
	public static final int HEIGHT = 384;

	public GameScreen() {
		super(WIDTH, HEIGHT);
		DrawingUtility.setGC(getGraphicsContext2D());
	}

	public static void repaint() {
		DrawingUtility.getGC().clearRect(0, 0, WIDTH, HEIGHT);
		IRenderableHolder.getObjectsOnScreen().stream().forEach(renderable -> renderable.draw());
		IRenderableHolder.getScreenTransitions().stream().forEach(renderable -> renderable.draw());
	}

}
