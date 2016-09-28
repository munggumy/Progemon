package graphic;

import javax.swing.JFrame;

public class Screen {
	
	private static final int SCREEN_WIDTH = 800, SCREEN_HEIGHT = 600;
	private static JFrame screen = new JFrame("Progemon");
	
	public Screen() {
		// TODO Auto-generated constructor stub
		screen.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		screen.setVisible(true);
		screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static JFrame getScreen(){
		return screen;
	}

}
