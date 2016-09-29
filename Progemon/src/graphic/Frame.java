package graphic;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame {
	
	private static JFrame frame;
	private JPanel panel;
	private static GraphicComponent graphicComponent;
	
	public Frame() {
		// TODO Auto-generated constructor stub
		frame = new JFrame("Progemon");
		panel = new JPanel();
		graphicComponent = new GraphicComponent();
		graphicComponent.setPreferredSize(new Dimension(800, 600));
		panel.add(graphicComponent);
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.pack();
	}
	
	public static JFrame getFrame() {
		return frame;
	}
	
	public static GraphicComponent getGraphicComponent() {
		return graphicComponent;
	}

}
