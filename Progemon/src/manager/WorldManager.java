package manager;

import java.util.ArrayList;
import java.util.HashMap;

import audio.MusicUtility;
import audio.SFXUtility;
import graphic.IRenderable;
import graphic.IRenderableHolder;
import javafx.scene.input.KeyCode;
import logic_world.player.PlayerCharacter;
import logic_world.terrain.WorldDirection;
import logic_world.terrain.WorldMap;
import logic_world.terrain.WorldMapException;
import logic_world.terrain.WorldObject;
import utility.AnimationUtility;
import utility.Clock;
import utility.GlobalPhase;
import utility.InputUtility;

public class WorldManager {

	private static PlayerCharacter player;
	// private static ArrayList<WorldObject> worldObjects = new ArrayList<>();
	private static WorldMap currentWorldMap;
	private static HashMap<WorldDirection, WorldMap> nextWorldMaps = new HashMap<>();
	private static WorldMap worldMapBuffer;

	public static PlayerCharacter getPlayer() {
		return player;
	}

	public WorldManager() {

		GlobalPhase.setCurrentPhase(GlobalPhase.WORLD);
		new AnimationUtility();
		try {
			WorldObject.loadObjectFunctions();
			WorldObject.loadWorldObjects();
			WorldObject.loadObjectImages();
		} catch (WorldMapException e) {
			e.printStackTrace();
		}

		WorldMap.loadTileset();

		new Clock();
		player = new PlayerCharacter();
		/*
		 * WorldObject.loadMapObjects(
		 * "load\\worldmap\\littleroot\\littleroot_object.txt"); worldMap = new
		 * WorldMap("load\\worldmap\\littleroot\\littleroot_map.txt");
		 */
		try {
			WorldMap map = loadWorld("route_101");
			System.out.println(map.getName() + ", size=" + map.getWorldObjects().size());
			useWorld(map, 18, 19);
		} catch (WorldMapException ex) {
			ex.printStackTrace();
		}
		player.show();
		run();
	}

	public static void run() {
		/*
		 * player.turn(0); player.walk(); player.walk(); player.walk();
		 * player.turn(3); player.walk(); player.walk(); player.walk();
		 * player.turn(2); player.walk(); player.walk(); player.walk();
		 */
		while (true) {
			try {
				if (InputUtility.getKeyPressed(KeyCode.DOWN)) {
					processPlayer(WorldDirection.DOWN);
				} else if (InputUtility.getKeyPressed(KeyCode.LEFT)) {
					processPlayer(WorldDirection.LEFT);
				} else if (InputUtility.getKeyPressed(KeyCode.UP)) {
					processPlayer(WorldDirection.UP);
				} else if (InputUtility.getKeyPressed(KeyCode.RIGHT)) {
					processPlayer(WorldDirection.RIGHT);
				} else if (!player.isStucking() && !player.isWalking()) {
					player.setMoving(false);
				}
			} catch (WorldMapException ex) {
				ex.printStackTrace();
			}
			Clock.tick();
		}
	}

	private static void processPlayer(WorldDirection wd) throws WorldMapException {
		if (player.getDirection() == wd) {
			if (!player.isPlaying()) {
				if (currentWorldMap.getTerrainAt(player.getBlockX(), player.getBlockY(), wd) <= 0) {
					player.stuck();
				} else {
					player.walk();
				}
			}
		} else if (player.isMoving() && !player.isWalking()) {
			if (currentWorldMap.getTerrainAt(player.getBlockX(), player.getBlockY(), wd) <= 0) {
				player.setDirection(wd);
				player.stuck();
			} else {
				player.setDirection(wd);
				player.walk();
			}
		} else if (!player.isPlaying()) {
			player.turn(wd);
		}

	}
	//
	// public static void addWorldObjects(WorldObject worldObject) {
	// worldObjects.add(worldObject);
	// }

	// public static WorldObject getObjectAt(int x, int y) {
	// for (WorldObject worldObject : worldObjects) {
	// if (worldObject.getBlockX() == x && worldObject.getBlockY() == y) {
	// return worldObject;
	// }
	// }
	// return space;
	// }

	public static WorldMap loadWorld(String mapName) throws WorldMapException {
		WorldMap temp = new WorldMap(mapName);
		try {
			System.out.println(
					"loading properties at : " + "load\\worldmap\\" + mapName + "\\" + mapName + ".properties");
			temp.loadProperties("load\\worldmap\\" + mapName + "\\" + mapName + ".properties");
		} catch (WorldMapException ex) {
			ex.printStackTrace();
		}
		WorldObject.loadMapObjects("load\\worldmap\\" + temp.getName() + "\\" + temp.getName() + "_object.csv", temp, 0,
				0, 0, 0, temp.getWidth(), temp.getHeight());
		System.out.println("WorldManager.java size=" + temp.getWorldObjects().size());
		return temp;
	}

	public static void setWorldMapBuffer(WorldMap worldMapBuffer) {
		WorldManager.worldMapBuffer = worldMapBuffer;
	}

	public static WorldMap loadSideWorld(String mapName) throws WorldMapException {
		WorldMap temp = new WorldMap(mapName);
		try {
			System.out.println(
					"loading properties at : " + "load\\worldmap\\" + mapName + "\\" + mapName + ".properties");
			temp.loadProperties("load\\worldmap\\" + mapName + "\\" + mapName + ".properties");
		} catch (WorldMapException ex) {
			ex.printStackTrace();
		}
		return temp;
	}

	public static void useBufferedWorld(int playerStartX, int playerStartY) throws WorldMapException {
		if (worldMapBuffer == null) {
			throw new WorldMapException("(WorldManager.java) : Buffer not setted.");
		}
		useWorld(worldMapBuffer, playerStartX, playerStartY);
	}

	public static void useWorld(WorldMap worldMap, int playerStartX, int playerStartY) throws WorldMapException {
		// currentWorldMap.clearWorldObjects();
		// nextWorldMaps.values().forEach(WorldMap::clearWorldObjects);
		IRenderableHolder.getWritelock().lock();
		try {
			for (WorldDirection wd : WorldDirection.values()) {
				String nextMapName = worldMap.getMapProperties().getProperty(wd.toString().toLowerCase());
				if (nextMapName != null) {
					WorldMap nextMap = loadSideWorld(nextMapName);
					int offsetX = 0, offsetY = 0, minX = 0, maxX = nextMap.getWidth(), minY = 0, maxY = nextMap.getHeight();

					switch (wd) {
					case DOWN:
						minY += Integer.parseInt(worldMap.getMapProperties().getProperty("down_trim", "0"));
						offsetX = Integer.parseInt(nextMap.getMapProperties().getProperty("down_offset", "0"));
						offsetY = worldMap.getHeight() - minY;
						break;
					case LEFT:
						maxX -= Integer.parseInt(worldMap.getMapProperties().getProperty("left_trim", "0"));
						offsetX = -maxX;
						offsetY = Integer.parseInt(nextMap.getMapProperties().getProperty("left_offset", "0"));
						break;
					case RIGHT:
						minX += Integer.parseInt(worldMap.getMapProperties().getProperty("right_trim", "0"));
						offsetX = worldMap.getWidth() - minX;
						offsetY = Integer.parseInt(nextMap.getMapProperties().getProperty("right_offset", "0"));
						break;
					case UP:
						maxY -= Integer.parseInt(worldMap.getMapProperties().getProperty("up_trim", "0"));
						offsetX = Integer.parseInt(nextMap.getMapProperties().getProperty("up_offset", "0"));
						offsetY = -maxY;
						break;
					}

					WorldObject.loadMapObjects(
							"load\\worldmap\\" + nextMap.getName() + "\\" + nextMap.getName() + "_object.csv", nextMap,
							offsetX, offsetY, minX, minY, maxX, maxY);
					nextWorldMaps.put(wd, nextMap);
					worldMap.addAllVisibleWorldObject(nextMap.getVisibleWorldObjects());
				} else {
					nextWorldMaps.put(wd, null);

				}
			}
			MusicUtility.playMusic(worldMap.getMapProperties().getProperty("music"));
			worldMap.getVisibleWorldObjects().add(worldMap);
			IRenderableHolder.setWorldObjects(worldMap.getVisibleWorldObjects());
			currentWorldMap = worldMap;
			player.setX(playerStartX * 32);
			player.setY(playerStartY * 32);
			player.setBlockX(playerStartX);
			player.setBlockY(playerStartY);
			IRenderableHolder.addWorldObject(player);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} finally {
			IRenderableHolder.getWritelock().unlock();
		}
	}

	public static void changeWorld(String nextWorldName, int playerStartX, int playerStartY, boolean playTransition,
			String soundEffectName) {
		if (soundEffectName != null && !soundEffectName.isEmpty() && !soundEffectName.equals("none")) {
			SFXUtility.playSound(soundEffectName);
		}
		if (playTransition) {
			AnimationUtility.getLoadScreen00().show();
			AnimationUtility.getLoadScreen00().play();
			while (AnimationUtility.getLoadScreen00().isPlaying()) {
				Clock.tick();
			}
		}
		try {
			WorldMap nextWorld = loadWorld(nextWorldName);
			useWorld(nextWorld, playerStartX, playerStartY);
		} catch (WorldMapException ex) {
			ex.printStackTrace();
		}
		if (playTransition) {
			AnimationUtility.getLoadScreen00().setPlayback(true);
			AnimationUtility.getLoadScreen00().play();
			while (AnimationUtility.getLoadScreen00().isPlaying()) {
				Clock.tick();
			}
			AnimationUtility.getLoadScreen00().setPlayback(false);
			AnimationUtility.getLoadScreen00().hide();
		}
	}

	public static final WorldMap getWorldMap() {
		return currentWorldMap;
	}

	public static final WorldMap getNextWorldMaps(WorldDirection wd) throws WorldMapException {
		if (nextWorldMaps.get(wd) == null) {
			throw new WorldMapException("WorldManager.getNextWorldMaps() : map is null [wd=" + wd + ", currentMap="
					+ currentWorldMap.getName() + "]");
		}
		return nextWorldMaps.get(wd);
	}

}
