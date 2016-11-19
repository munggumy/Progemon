package graphic;

import java.util.concurrent.CopyOnWriteArrayList;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import utility.InputUtility;

public class MyCanvas extends Canvas {
	
	private static CopyOnWriteArrayList<IRenderable> objectOnScreen = new CopyOnWriteArrayList<IRenderable>();
	
	public MyCanvas() {
		// TODO Auto-generated constructor stub
		super(800, 600);
		
		DrawingUtility.setGC(getGraphicsContext2D());
	}
	
	public static void repaint(){
		DrawingUtility.getGc().clearRect(0, 0, 800, 600);
		objectOnScreen.stream().forEach((IRenderable object) -> object.draw());
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
