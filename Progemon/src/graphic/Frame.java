package graphic;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;

import utility.InputUtility;

@Deprecated
public class Frame {

	public static final int OFFSET_X = 8, OFFSET_Y = 31;

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
				// InputUtility.addInputEvents(e);
			}

		});
		frame.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				super.mouseMoved(e);
				// InputUtility.addInputEvents(e);
				InputUtility.setMouseX(e.getX() - OFFSET_X);
				InputUtility.setMouseY(e.getY() - OFFSET_Y);
			}

		});
		frame.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyPressed(e);
				// InputUtility.addInputEvents(e);
				// InputUtility.addHoldingKeys(e);
				// InputUtility.setLastKeyEvent(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyReleased(e);
				// InputUtility.addInputEvents(e);
				// InputUtility.removeHoldingKeys(e);
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyTyped(e);
				// InputUtility.addTypeKeys(e);
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
