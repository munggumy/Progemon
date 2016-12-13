package manager;

import java.util.HashMap;
import java.util.Map;

import audio.MusicUtility;
import audio.SFXUtility;
import graphic.IRenderableHolder;
import item.Item;
import item.Items;
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

	// private static ArrayList<WorldObject> worldObjects = new ArrayList<>();
	private static WorldMap currentWorldMap;
	private static Map<WorldDirection, WorldMap> nextWorldMaps = new HashMap<>();
	private static WorldMap worldMapBuffer;
	private static PlayerCharacter player;

	public WorldManager() {
		try {
			GlobalPhase.setCurrentPhase(GlobalPhase.WORLD);
			new Clock();
			player = PlayerCharacter.instance;
			if (player.getNumberOfPokemons() <= 0) {
				throw new IllegalStateException("PlayerCharacter has no pokemons!");
			}
			player.show();

			/*
			 * WorldObject.loadMapObjects(
			 * "load\\worldmap\\littleroot\\littleroot_object.txt"); worldMap =
			 * new WorldMap("load\\worldmap\\littleroot\\littleroot_map.txt");
			 */

			WorldMap map = loadWorld("oldale");
			System.out.println(map.getName() + ", size=" + map.getWorldObjects().size());
			useWorld(map, 10, 10);
		} catch (WorldMapException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
					processPlayer(WorldDirection.SOUTH);
				} else if (InputUtility.getKeyPressed(KeyCode.LEFT)) {
					processPlayer(WorldDirection.WEST);
				} else if (InputUtility.getKeyPressed(KeyCode.UP)) {
					processPlayer(WorldDirection.NORTH);
				} else if (InputUtility.getKeyPressed(KeyCode.RIGHT)) {
					processPlayer(WorldDirection.EAST);
				} else if (!player.isStucking() && !player.isWalking()) {
					player.setMoving(false);
				}
				if (InputUtility.getKeyTriggered(KeyCode.Z)) {
					currentWorldMap.getObjectAt((int) player.getBlockX() + player.getDirection().getX(),
							(int) player.getBlockY() + player.getDirection().getY()).interacted();
				}
				if (InputUtility.getKeyTriggered(KeyCode.R)) {
					Item repel = Items.getItem("super_repel");
					repel.getOnTrainerUse().use(player);
					System.out.println(repel.getName() + " used");
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
		WorldObject.loadMapObjects("load\\worldmap\\" + temp.getName() + "\\" + temp.getName() + "_object.csv", temp, 0,
				0, 0, 0, temp.getWidth(), temp.getHeight());
		System.out.println("WorldManager.java size=" + temp.getWorldObjects().size());
		return temp;
	}

	public static WorldMap loadNeighbourWorld(String mapName) throws WorldMapException {
		WorldMap temp = new WorldMap(mapName);
		return temp;
	}

	public static void setWorldMapBuffer(WorldMap worldMapBuffer) {
		WorldManager.worldMapBuffer = worldMapBuffer;
	}

	public static void useBufferedWorld(int playerStartX, int playerStartY) throws WorldMapException {
		if (worldMapBuffer == null) {
			throw new WorldMapException("(WorldManager.java) : Buffer not setted.");
		}
		useWorld(worldMapBuffer, playerStartX, playerStartY);
	}

	public static void useWorld(WorldMap centerWorldMap, int playerStartX, int playerStartY) throws WorldMapException {
		// currentWorldMap.clearWorldObjects();
		// nextWorldMaps.values().forEach(WorldMap::clearWorldObjects);
		IRenderableHolder.getWritelock().lock();
		try {
			for (WorldDirection wd : WorldDirection.values()) {
				String neighbourMapName = centerWorldMap.getMapProperties().getProperty(wd.toString().toLowerCase());
				if (neighbourMapName != null) {
					WorldMap neighbourMap = loadNeighbourWorld(neighbourMapName);
					int offsetX = 0, offsetY = 0, minX = 0, maxX = neighbourMap.getWidth(), minY = 0,
							maxY = neighbourMap.getHeight();

					switch (wd) {
					case SOUTH:
						minY += Integer.parseInt(centerWorldMap.getMapProperties().getProperty("south_trim", "0"));
						offsetX = Integer.parseInt(centerWorldMap.getMapProperties().getProperty("south_offset", "0"));
						offsetY = centerWorldMap.getHeight() - minY;
						System.out.println("WorldManager.java offset case DOWN : " + offsetX + ", " + offsetY);
						break;
					case WEST:
						maxX -= Integer.parseInt(centerWorldMap.getMapProperties().getProperty("west_trim", "0"));
						offsetX = -maxX;
						offsetY = Integer.parseInt(centerWorldMap.getMapProperties().getProperty("west_offset", "0"));
						break;
					case EAST:
						minX += Integer.parseInt(centerWorldMap.getMapProperties().getProperty("east_trim", "0"));
						offsetX = centerWorldMap.getWidth() - minX;
						offsetY = Integer.parseInt(centerWorldMap.getMapProperties().getProperty("east_offset", "0"));
						break;
					case NORTH:
						maxY -= Integer.parseInt(centerWorldMap.getMapProperties().getProperty("north_trim", "0"));
						offsetX = Integer.parseInt(centerWorldMap.getMapProperties().getProperty("north_offset", "0"));
						offsetY = -maxY;
						break;
					}

					WorldObject.loadMapObjects(
							"load\\worldmap\\" + neighbourMap.getName() + "\\" + neighbourMap.getName() + "_object.csv",
							neighbourMap, offsetX, offsetY, minX, minY, maxX, maxY);
					nextWorldMaps.put(wd, neighbourMap);
					centerWorldMap.addAllVisibleWorldObject(neighbourMap.getVisibleWorldObjects());
				} else {
					nextWorldMaps.put(wd, null);

				}
			}
			MusicUtility.playMusic(centerWorldMap.getMapProperties().getProperty("music"));
			centerWorldMap.getVisibleWorldObjects().add(centerWorldMap);
			IRenderableHolder.setWorldObjects(centerWorldMap.getVisibleWorldObjects());
			currentWorldMap = centerWorldMap;
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

	public static final WorldMap getNextWorldMaps(WorldDirection wd) {
		// /*
		// * if (nextWorldMaps.get(wd) == null) { throw new
		// * WorldMapException("WorldManager.getNextWorldMaps() : map is null
		// [wd="
		// * + wd + ", currentMap=" + currentWorldMap.getName() + "]"); }
		// */
		return nextWorldMaps.get(wd);
	}

	public static PlayerCharacter getPlayer() {
		return player;
	}
	
	public static PlayerCharacter getCharacterAt(int blockX, int blockY) {
		if (player.getY() == blockY * 32) {
			if (player.isWalking() && player.getX() >= (blockX - 1) * 32 && player.getX() <= (blockX + 1) * 32) {
				return player;
			}
		}
		else if (player.getX() == blockX * 32) {
			if (player.isWalking() && player.getY() >= (blockY - 1) * 32 && player.getY() <= (blockY + 1) * 32) {
				return player;
			}
		}
		return null;
	}

}
