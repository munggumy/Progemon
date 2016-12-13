package logic_world.terrain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import graphic.DrawingUtility;
import graphic.IRenderable;
import graphic.IRenderableHolder;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import logic_world.player.Character;
import logic_world.player.NPCCharacter;

public class WorldMap implements IRenderable {

	private static final String DEFAULT_MUSIC = "littleroot";
	private static final String DEFAULT_TILE_SET_PATH = "load\\img\\world\\tileset.png";
	private static List<Image> tilesetList = new ArrayList<Image>();

	private String name;
	private int[][] map;
	private boolean visible = true;

	private List<WorldObject> worldObjects = new ArrayList<>();
	private List<NPCCharacter> worldCharacters = new ArrayList<>();
	private List<IRenderable> visibleWorldObjects = new ArrayList<>();

	private List<SpawningPokemonEntry> pokemonSpawningChance = new ArrayList<>();

	private static Properties defaultProperties = new Properties();
	private Properties mapProperties = new Properties(defaultProperties);
	static {
		defaultProperties.setProperty("music", DEFAULT_MUSIC);
		defaultProperties.setProperty("spawning_pokemons", "[pidgey/VERY_COMMON/2/5]");
	}

	private WorldObject space = WorldObject.createWorldObject("000", -1, -1, new ArrayList<>(), this);

	public WorldMap(String mapName) throws WorldMapException {
		this.name = mapName;
		String propertiesFilePath = "load\\worldmap\\" + mapName + "\\" + mapName + ".properties";
		loadProperties(propertiesFilePath);
		String worldMapFilePath = "load\\worldmap\\" + mapName + "\\" + mapName + "_map.csv";
		loadWorldMap(worldMapFilePath);
		if (mapName.equals("littleroot_house1")) {
			NPCCharacter mom = new NPCCharacter("load\\img\\player\\npc1.png", 3, 6, WorldDirection.EAST);
			visibleWorldObjects.add(mom);
			worldCharacters.add(mom);
		}
		else{
			worldCharacters.clear();
		}

		space.hide();
		space.addOnEnter("-");
		space.addOnExit("-");
	}

	@Override
	public void draw() {
		DrawingUtility.drawWorldMap(this);
	}

	@Override
	public int getDepth() {
		return Integer.MIN_VALUE;
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

	public int getTerrainAt(int x, int y) throws WorldMapException {
		try {
			return map[y][x];
		} catch (NullPointerException ex) {
			throw new WorldMapException("Map not loaded ", ex);
		} catch (IndexOutOfBoundsException ex) {
			throw new WorldMapException("Cannot find Terrain at [x=" + x + ", y=" + y + "]", ex);
		}
	}
	
	public void setTerrainAt(int x, int y, int value) throws WorldMapException {
		try {
			map[y][x] = value;
		} catch (NullPointerException ex) {
			throw new WorldMapException("Map not loaded ", ex);
		} catch (IndexOutOfBoundsException ex) {
			throw new WorldMapException("Cannot find Terrain at [x=" + x + ", y=" + y + "]", ex);
		}
	}

	public int getTerrainAt(int x, int y, WorldDirection direction) throws WorldMapException {
		return getTerrainAt(x + direction.getX(), y + direction.getY());
	}

	public static Image getImage(int tileCode) {
		try {
			return tilesetList.get(tileCode);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new UnknownTileSetException(ex);
		}
	}

	public static List<Image> getTileset() {
		return tilesetList;
	}

	public int[][] getMap() {
		return map;
	}

	public int getHeight() {
		return map.length;
	}

	public int getWidth() {
		return map[0].length;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	// worldObject List

	public void addWorldObjects(WorldObject worldObject) {
		worldObjects.add(worldObject);
	}

	public WorldObject getObjectAt(int x, int y) {
		for (WorldObject worldObject : worldObjects) {
			if (worldObject.getBlockX() == x && worldObject.getBlockY() == y) {
				return worldObject;
			}
		}
		return space;
	}

	public void clearWorldObjects() {
		worldObjects.clear();
	}

	// Load

	/** Loads the tileset image 
	 * @throws WorldMapException */
	public static void loadTileset() throws WorldMapException {
		try {
			Image img = new Image(new File(DEFAULT_TILE_SET_PATH).toURI().toString());
			for (int i = 0; i < 50; i++) {
				tilesetList.add(DrawingUtility.resize(
						new WritableImage(img.getPixelReader(), (i % 10) * 16, Math.floorDiv(i, 10) * 16, 16, 16), 2));
			}
		} catch (Exception e) {
			throw new WorldMapException("cannot load world tileset", e);
		} 
	}

	private void loadWorldMap(String mapFilePath) throws WorldMapException {
		try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(mapFilePath)))) {
			String delimiter = ",";
			String[] widthAndHeight = scanner.nextLine().split(delimiter);
			int width = Integer.parseInt(widthAndHeight[0]);
			int height = Integer.parseInt(widthAndHeight[1]);
			Pattern pattern = Pattern.compile("[" + delimiter + "\\s+-?\\d+]+");
			Matcher matcher;
			map = new int[height][width];
			int[] mapLine = new int[width];
			int lineCounter = 0;
			// System.out.println("Map [filePath=" + filePath + "width=" + width
			// + ", height=" + height + "]");
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.matches("^#+.*")) { // comment line ##hey hey heys
					continue;
				}
				matcher = pattern.matcher(line);
				if (matcher.find()) {
					int digitCounter = 0;
					for (String digit : line.trim().split("\\s*" + delimiter + "\\s*")) {
						mapLine[digitCounter] = Integer.parseInt(digit);
						digitCounter++;
					}
					map[lineCounter] = mapLine.clone();
					lineCounter++;
				} else {
					throw new WorldMapException("loadMap(): Unmatched pattern : \"" + line + "\", filePath="
							+ mapFilePath + ", lineCounter=" + lineCounter);
				}
			}
		} catch (FileNotFoundException ex) {
			throw new WorldMapException("World Map Load Error [filePath=" + mapFilePath + " not found]", ex);
		} catch (NumberFormatException ex) {
			throw new WorldMapException("World Map Load Error [filePath=" + mapFilePath + " ]", ex);
		}
	}

	private void loadProperties(String filePath) throws WorldMapException {
		try (FileInputStream in = new FileInputStream(filePath)) {
			mapProperties.load(in);
			String spawnProperty = mapProperties.getProperty("spawning_pokemons");
			if (spawnProperty == null || spawnProperty.isEmpty()) {
				throw new WorldMapException("spawnProperty not set for map=" + name);
			}
			String secondaryDelimiter = "/";
			Pattern pattern = Pattern.compile(String.join(secondaryDelimiter, "\\[(?<pokemonName>\\w+)",
					"(?<rareness>\\w+)", "(?<minLevel>\\d+)", "(?<maxLevel>\\d+)\\]"));
			Matcher matcher = pattern.matcher(spawnProperty);
			while (matcher.find()) {
				pokemonSpawningChance.add(new SpawningPokemonEntry(matcher.group("pokemonName"),
						Integer.parseInt(matcher.group("minLevel")), Integer.parseInt(matcher.group("maxLevel")),
						WildRareness.valueOf(matcher.group("rareness").trim())));
			}

		} catch (FileNotFoundException ex) {
			throw new WorldMapException("World Map Properties Load Error [filePath=" + filePath + " not found]", ex);
		} catch (IOException ex) {
			throw new WorldMapException("IOException on " + filePath, ex);
		} catch (IllegalArgumentException ex) {
			throw new WorldMapException("Illegal Argument in WorldMap.loadProperties");
		}
	}

	public Properties getMapProperties() {
		return mapProperties;
	}

	public String getName() {
		return name;
	}

	public List<WorldObject> getWorldObjects() {
		return worldObjects;
	}

	public final List<IRenderable> getVisibleWorldObjects() {
		return visibleWorldObjects;
	}

	public void addVisibleWorldObject(WorldObject object) {
		visibleWorldObjects.add(object);
	}

	public void addAllVisibleWorldObject(List<IRenderable> objects) {
		visibleWorldObjects.addAll(objects);
	}

	public final List<SpawningPokemonEntry> getSortedPokemonSpawningChance() {
		Collections.sort(pokemonSpawningChance);
		return pokemonSpawningChance;
	}
	
	public List<NPCCharacter> getWorldCharacters() {
		return worldCharacters;
	}

}