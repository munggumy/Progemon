package graphic;

import java.util.Queue;

import logic.terrain.FightMap;

public class QueueBox implements IRenderable {
	
	protected static final String QUEUE_BOX_PATH = "load\\img\\queuebox\\Theme1.png";
	
	private static final int x = 320, y = 0;
	private static FightMap fightMap;
	
	public QueueBox(FightMap fightMap) {
		// TODO Auto-generated constructor stub
		QueueBox.fightMap = fightMap;
	}
	
	@Override
	public void draw() {
		// TODO Auto-generated method stub
		DrawingUtility.drawQueueBox();
	}
	
	@Override
	public void getDepth() {
		// TODO Auto-generated method stub
		
	}
	
	public static int getX() {
		return x;
	}
	
	public static int getY() {
		return y;
	}
	
	public static FightMap getFightMap() {
		return fightMap;
	}

}