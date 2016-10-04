package graphic;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;

import utility.InputUtility;

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

		frame.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				InputUtility.addInputEvents(e);
			}

		});
		frame.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				super.mouseMoved(e);
				InputUtility.addInputEvents(e);
			}

		});
	}

	public static JFrame getFrame() {
		return frame;
	}

	public static ScreenComponent getGraphicComponent() {
		return graphicComponent;
	}

}
