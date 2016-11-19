package graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JComponent;

public class ScreenComponent extends JComponent {

	private static CopyOnWriteArrayList<IRenderable> objectOnScreen = new CopyOnWriteArrayList<IRenderable>();
	public static Graphics2D g2;

	public ScreenComponent() {
		// TODO Auto-generated constructor stub
		setPreferredSize(new Dimension(800, 600));
		setDoubleBuffered(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		g2 = (Graphics2D) g;

		// anti aliasing
		/*
		 * g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		 * RenderingHints.VALUE_ANTIALIAS_ON);
		 * g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		 * RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		 */

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
