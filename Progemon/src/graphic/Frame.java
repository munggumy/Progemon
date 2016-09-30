package graphic;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Frame {
	
	private static JFrame frame;
	private static ScreenComponent graphicComponent;
	
	public Frame() {
		// TODO Auto-generated constructor stub
		frame = new JFrame("Progemon");
		graphicComponent = new ScreenComponent();
		frame.getContentPane().add(graphicComponent);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.pack();
	}
	
	public static JFrame getFrame() {
		return frame;
	}
	
	public static ScreenComponent getGraphicComponent() {
		return graphicComponent;
	}

}