package graphic;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class GraphicComponent extends JComponent {
	
	public GraphicComponent() {
		// TODO Auto-generated constructor stub
		setPreferredSize(new Dimension(800, 600));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		for (IRenderable object : DrawingUtility.getObjectOnScreen()) {
			object.draw();
		}
	}

}
