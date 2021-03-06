package graphic;

import java.util.ArrayList;
import java.util.List;

import logic_fight.character.pokemon.Pokemon;
import manager.GUIFightGameManager;
import utility.Clock;

public class QueueBox implements IRenderable {

	public static final int ORIGIN_X = 396, ORIGIN_Y = 18, WIDTH = 68, HEIGHT = 40;
	public static final int BOX_X = 390, BOX_Y = 0;
	protected static final String QUEUE_BOX_PATH = "load\\img\\HUD\\queuebox.png";
	private static final int REMOVE_TIME = 34, REMOVE_RATE = 2;
	private static final int MOVE_TIME = 20, MOVE_RATE = 2;

	private static GUIFightGameManager currentFightManager;

	private static ArrayList<Pokemon> pokemonsOnQueue = new ArrayList<Pokemon>();
	private static List<Pokemon> pokemonsOnMap;
	private static int[][] delta = new int[10][2];
	private static int removeTimeCounter = 0, moveTimeCounter = 0;
	private static boolean remove = false, move = false, insert = false;
	private static boolean isQueue = true;
	private static boolean visible = true;

	public QueueBox(GUIFightGameManager currentFightManager) {
		QueueBox.currentFightManager = currentFightManager;
		pokemonsOnMap = new ArrayList<Pokemon>(QueueBox.currentFightManager.getFightMap().getPokemonsOnMap());
		clearQueue();
		for (Pokemon pokemon : pokemonsOnMap) {
			pokemonsOnQueue.add(pokemon);
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 2; j++) {
				delta[i][j] = 0;
			}
		}
	}

	@Override
	public void draw() {
		DrawingUtility.drawQueueBox();
	}

	@Override
	public int getDepth() {
		return 0;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void hide() {
		visible = false;
		IRenderableHolder.removeWorldObject(this);
	}

	@Override
	public void show() {
		IRenderableHolder.addWorldObject(this);
		visible = true;
	}

	public static void sort() {
		isQueue = false;
		while (!isQueue) {
			update();

			// MyCanvas.repaint();
			Clock.tick();
		}
	}

	public static void update() {
		pokemonsOnMap = currentFightManager.getFightMap().getPokemonsOnMap();
		boolean equal = true;
		if (pokemonsOnQueue.size() == pokemonsOnMap.size()) {
			for (int i = 0; i < pokemonsOnQueue.size(); i++) {
				if (pokemonsOnQueue.get(i) != pokemonsOnMap.get(i)) {
					equal = false;
				}
			}
		} else {
			equal = false;
		}
		if (!equal) {
			isQueue = false;
			animate();
		} else {
			isQueue = true;
		}
	}

	private static void animate() {
		if (!remove) {
			remove();
		} else if (!move) {
			move();
		}
		/*
		 * else if(!insert){ insert(); }
		 */
		else {
			pokemonsOnQueue.clear();
			for (Pokemon pokemon : pokemonsOnMap) {
				pokemonsOnQueue.add(pokemon);
			}
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 2; j++) {
					delta[i][j] = 0;
				}
			}
			remove = false;
			move = false;
		}
	}

	private static void remove() {
		if (removeTimeCounter == REMOVE_TIME) {
			removeTimeCounter = 0;
			remove = true;
		} else {
			for (Pokemon pokemon : pokemonsOnQueue) {
				if (!pokemonsOnMap.contains(pokemon)) {
					delta[pokemonsOnQueue.indexOf(pokemon)][0] += REMOVE_RATE;
				}
			}
			removeTimeCounter++;
		}
	}

	private static void move() {
		if (moveTimeCounter == MOVE_TIME) {
			moveTimeCounter = 0;
			move = true;
		} else {
			for (Pokemon pokemon : pokemonsOnQueue) {
				delta[pokemonsOnQueue.indexOf(pokemon)][1] += MOVE_RATE
						* (pokemonsOnMap.indexOf(pokemon) - pokemonsOnQueue.indexOf(pokemon));
			}
			moveTimeCounter++;
		}
	}

	/*
	 * private void insert(){
	 * 
	 * }
	 */

	public static void clearQueue() {
		pokemonsOnQueue.clear();
	}

	public static ArrayList<Pokemon> getPokemonsOnQueue() {
		return pokemonsOnQueue;
	}

	public static int[][] getDelta() {
		return delta;
	}

	public static boolean isQueue() {
		return isQueue;
	}

}