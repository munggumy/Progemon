package logic_world.terrain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import graphic.Animation;
import graphic.DialogBox;
import graphic.DrawingUtility;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import manager.WorldManager;
import utility.Clock;
import utility.exception.FileWrongFormatException;

public class WorldObject extends Animation implements Cloneable {

	private static final String WORLD_OBJECTS_PROP_FILE = "load\\worldobjects_list.csv";
	private static final String DEFAULT_IMG_PATH = "load\\img\\world\\worldobjects.png";
	private static final String DEFAULT_IMGPOS_PATH = "load\\img\\world\\imageposition.csv";

	public static Map<String, WorldObject> allWorldObjects = new HashMap<>();
	public static Map<String, WorldObjectAction> allObjectFunctions = new HashMap<>();
	public static Map<String, ArrayList<Image>> objectImagesSet = new HashMap<>();

	protected int blockX, blockY;
	protected String objectCode;
	private ArrayList<WorldObjectAction> onEnter = new ArrayList<>(), onExit = new ArrayList<>(),
			onStep = new ArrayList<>(), onInteract = new ArrayList<>();
	/** Use to tell graphicDepth if objects overlap in worldMap. */
	private int specialDepth = 0;
	private ArrayList<ArrayList<String>> functionParameter = new ArrayList<>(4);
	private int actionType = 0, parameterCounter = 0;

	public static WorldObject createWorldObject(String objectCode, int blockX, int blockY, ArrayList<String> parameters,
			WorldMap owner) {
		try {
			WorldObject worldObject;

			worldObject = (WorldObject) allWorldObjects.get(objectCode).clone();
			worldObject.blockX = blockX;
			worldObject.blockY = blockY;
			// if (objectCode.equals("008")) {
			// System.err.println("01245678913215621321");
			// System.out.println("x = " + blockX);
			// System.out.println("y = " + blockY);
			// // System.out.println("param = " + parameters.get(0));
			// }
			for (int i = 0; i < 4; i++) {
				worldObject.functionParameter.add(new ArrayList<>());
			}
			if (!parameters.isEmpty()) {
				for (String string : parameters) {
					worldObject.functionParameter.get(Integer.parseInt(string.substring(0, 1)))
							.add(string.substring(string.indexOf("[") + 1, string.indexOf("]")));
				}
			}
			if (allWorldObjects.get(objectCode).isPlaying()) {
				worldObject.play();
			}
			if (!allWorldObjects.get(objectCode).isVisible()) {
				worldObject.visible = false;
			} else {
				worldObject.visible = true;
				owner.addVisibleWorldObject(worldObject);
			}
			owner.addWorldObjects(worldObject);
			return worldObject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public WorldObject(String objectCode, int frameNumber, int frameDelay, boolean loop, boolean autostop,
			int specialDepth) {
		super(frameNumber, frameDelay, loop, autostop);
		this.objectCode = objectCode;
		this.specialDepth = specialDepth;
		addOnEnter("-");
		addOnStep("-");
		addOnExit("-");
		addOnInteract("-");
	}

	public WorldObject(String objectCode, String onEnter, String onStep, String onExit, String onInteract,
			int frameNumber, int frameDelay, boolean loop, boolean autostop) {
		super(frameNumber, frameDelay, loop, autostop);
		this.objectCode = objectCode;
		addOnEnter(onEnter);
		addOnStep(onStep);
		addOnExit(onExit);
		addOnInteract(onInteract);
	}

	public WorldObject(String[] args) {
		super(Integer.parseInt(args[5]), Integer.parseInt(args[6]), Boolean.parseBoolean(args[7]),
				Boolean.parseBoolean(args[8]));
		currentFrame = 0;
		objectCode = args[0];
		addOnEnter(args[1]);
		addOnStep(args[2]);
		addOnExit(args[3]);
		addOnInteract(args[4]);
		specialDepth = Integer.parseInt(args[11]);
		super.setPlaying(Boolean.parseBoolean(args[9]));
		super.setVisible(Boolean.parseBoolean(args[10]));
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		WorldObject worldObject = new WorldObject(objectCode, amountOfFrame, frameDelay, loop, autostop, specialDepth);
		worldObject.setOnEnter(onEnter);
		worldObject.setOnInteract(onInteract);
		worldObject.setOnStep(onStep);
		worldObject.setOnExit(onExit);
		return worldObject;
	}

	@Override
	public Image getCurrentImage() {
		if (objectImagesSet.get(objectCode) == null) {
			throw new NullPointerException("getCurrentImage : Can't find objectCode=" + objectCode);
		}
		return objectImagesSet.get(objectCode).get(currentFrame);
	}

	@Override
	public void draw() {
		DrawingUtility.drawWorldObject(this);
	}

	@Override
	public int getDepth() {
		return blockY * 32 + specialDepth;
	}

	public int getBlockX() {
		return blockX;
	}

	public void setBlockX(int blockX) {
		this.blockX = blockX;
	}

	public int getBlockY() {
		return blockY;
	}

	public void setBlockY(int blockY) {
		this.blockY = blockY;
	}

	public void setSpecialDepth(int specialDepth) {
		this.specialDepth = specialDepth;
	}

	// Functions

	public void addOnEnter(String actionCodes) {
		for (String actionCode : actionCodes.split("/")) {
			this.onEnter.add(allObjectFunctions.get(actionCode));
		}
	}

	public void setOnEnter(ArrayList<WorldObjectAction> onEnter) {
		this.onEnter = onEnter;
	}

	public void entered() {
		parameterCounter = 0;
		actionType = 0;
		for (WorldObjectAction function : onEnter) {
			function.execute(this);
		}
	}

	public void addOnInteract(String actionCodes) {
		for (String actionCode : actionCodes.split("/")) {
			this.onInteract.add(allObjectFunctions.get(actionCode));
		}
	}

	public void setOnInteract(ArrayList<WorldObjectAction> onInteract) {
		this.onInteract = onInteract;
	}

	public void interacted() {
		parameterCounter = 0;
		actionType = 1;
		for (WorldObjectAction action : onInteract) {
			action.execute(this);
		}
	}

	public void addOnStep(String actionCodes) {
		for (String actionCode : actionCodes.split("/")) {
			this.onStep.add(allObjectFunctions.get(actionCode));
		}
	}

	public void setOnStep(ArrayList<WorldObjectAction> onStep) {
		this.onStep = onStep;
	}

	public void step() {
		parameterCounter = 0;
		actionType = 2;
		for (WorldObjectAction action : onStep) {
			action.execute(this);
		}
	}

	public void addOnExit(String actionCodes) {
		for (String actionCode : actionCodes.split("/")) {
			this.onExit.add(allObjectFunctions.get(actionCode));
		}
	}

	public void setOnExit(ArrayList<WorldObjectAction> onExit) {
		this.onExit = onExit;
	}

	public void exit() {
		parameterCounter = 0;
		actionType = 3;
		for (WorldObjectAction action : onExit) {
			action.execute(this);
		}
	}

	// WorldDirection use incase owner map is not current center.
	/** load objects on map */
	public static void loadMapObjects(String datapath, WorldMap owner, int offsetX, int offsetY, int minX, int minY,
			int maxX, int maxY) {
		String delimiter = "\\s*,\\s*";
		/*
		 * if (true) { System.err.println(
		 * "kuykuykuykuykukuykuykuykuykuykuykuykuykuykuyykuykkuykuykuykuykuykuykuykuykuykuyuykuykuykuy"
		 * ); }
		 */
		try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(datapath)))) {
			Pattern pattern = Pattern.compile(String.join(delimiter, "(?<objectCode>\\d+)", "(?<blockX>\\d+)",
					"(?<blockY>\\d+)(?<functionParam>(", "\"?\\d\\[.+\\]\"?)+)?"));
			Matcher matcher;
			ArrayList<String> functionsStr = new ArrayList<String>();
			loop: while (scanner.hasNextLine()) {
				functionsStr.clear();
				String line = scanner.nextLine();
				if (line.matches("^#+.*")) { // comment line ##hey hey heys
					continue loop;
				}
				matcher = pattern.matcher(line);
				if (matcher.find()) {
					String objectCode = String.format("%03d", Integer.parseInt(matcher.group("objectCode")));
					int blockX = Integer.parseInt(matcher.group("blockX"));
					int blockY = Integer.parseInt(matcher.group("blockY"));
					if (blockX < minX || blockX > maxX || blockY < minY || blockY > maxY) {
						continue loop;
					}
					blockX += offsetX;
					blockY += offsetY;

					functionsStr.clear();
					if (matcher.group("functionParam") != null && !matcher.group("functionParam").isEmpty()) {
						for (String string : matcher.group("functionParam").trim().split(delimiter)) {

							ArrayList<String> str = new ArrayList<>();
							if (string.matches("\".+\"")) {
								while (true) {
									str.add(string.substring(string.indexOf("\"") + 1, string.indexOf("\"", 1)));
									if (string.indexOf("\"", 1) == string.lastIndexOf("\"")) {
										break;
									}
									string = string.substring(string.indexOf("\"", 1) + 1, string.length());
								}
							} else if (string != null && !string.isEmpty()) {
								functionsStr.add(string);
							}
							if (str.size() != 0) {
								String buffer = "";
								for (String string2 : str) {
									if (!buffer.equals("")) {
										buffer = buffer + "\"" + string2;
									} else {
										buffer = buffer + string2;
									}
								}
								functionsStr.add(buffer);
							}
						}
					}
					createWorldObject(objectCode, blockX, blockY, functionsStr, owner);
				} else {
					System.err.println("WARNING : Unmatched Pattern In WorldObject.java : line=" + line + "\npattern="
							+ pattern + "\nfile=" + datapath);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * load all object template
	 * 
	 * @throws WorldMapException
	 */
	public static void loadWorldObjects() throws WorldMapException {
		try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(WORLD_OBJECTS_PROP_FILE)))) {
			String delimiter = "\\s*,\\s*";
			Pattern pattern = Pattern.compile(String.join(delimiter, "(?<objectCode>\\d+)",
					"(?<onEnter>\\w+[/\\w+]*|-)", "(?<onStep>\\w+[/\\w+]*|-)", "(?<onExit>\\w+[/\\w+]*|-)",
					"(?<onInteract>\\w+[/\\w+]*|-)", "(?<frameNumber>\\d+)", "(?<frameDelay>\\d+)", "(?<loop>\\w+)",
					"(?<autoStop>\\w+)", "(?<playing>\\w+)", "(?<showing>\\w+)", "(?<specialDepth>-?\\d+)"));
			Matcher matcher;
			String[] args = new String[12];
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.matches("^#+.*")) { // comment line ##hey hey heys
					continue;
				}
				matcher = pattern.matcher(line);
				if (matcher.find()) {
					for (int i = 0; i < 12; i++) {
						args[i] = matcher.group(i + 1);
					}
					String objectCode = String.format("%03d", Integer.parseInt(args[0]));
					args[0] = objectCode;
					allWorldObjects.put(objectCode, new WorldObject(args));
				} else {
					throw new WorldMapException("loadWorldObjects() : Unmatched pattern=" + line);
				}
			}
		} catch (FileNotFoundException e) {
			throw new WorldMapException("loadWorldObjects() : file not found " + WORLD_OBJECTS_PROP_FILE, e);
		}
	}

	public static void loadObjectImages() throws WorldMapException {
		try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(DEFAULT_IMGPOS_PATH)))) {
			Image img = new Image(new File(DEFAULT_IMG_PATH).toURI().toString());

			String delimiter = "\\s*,\\s*";
			Pattern pattern = Pattern.compile(String.join(delimiter, "(?<objectCode>\\d+)", "(?<xPos>\\d+)",
					"(?<yPos>\\d+)", "(?<width>\\d+)", "(?<height>\\d+)"));
			Matcher matcher;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.matches("^#+.*")) { // comment line ##hey hey heys
					continue;
				}
				matcher = pattern.matcher(line);
				if (matcher.find()) {

					String objectCode = String.format("%03d", Integer.parseInt(matcher.group("objectCode")));
					if (allWorldObjects.get(objectCode) != null) {
						int amountOfFrame = allWorldObjects.get(objectCode).amountOfFrame;
						ArrayList<Image> array = new ArrayList<>();
						int xPos = Integer.parseInt(matcher.group("xPos")),
								yPos = Integer.parseInt(matcher.group("yPos")),
								width = Integer.parseInt(matcher.group("width")),
								height = Integer.parseInt(matcher.group("height"));
						for (int i = 0; i < amountOfFrame; i++) {
							array.add(DrawingUtility.resize(new WritableImage(img.getPixelReader(),
									xPos + (width / amountOfFrame) * i, yPos, (width / amountOfFrame), height), 2));
						}
						objectImagesSet.put(objectCode, array);
					}

				} else {
					throw new FileWrongFormatException("Unmatched pattern : " + line);
				}
			}
		} catch (NullPointerException e) {
			System.err.println("error in loadObjectImage()");
			// e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorldMapException(e);
		}
	}

	// All Object Function
	public static void loadObjectFunctions() {
		allObjectFunctions.put("-", target -> {

		});

		allObjectFunctions.put("play", target -> {
			target.show();
			target.play();
			return;
		});

		allObjectFunctions.put("playback", target -> {
			target.show();
			target.setPlayback(true);
			target.play();
		});

		allObjectFunctions.put("playforward", target -> {
			target.show();
			target.setPlayback(false);
			target.play();
		});

		allObjectFunctions.put("delay", target -> {

			String parameter = target.functionParameter.get(target.actionType).get(target.parameterCounter);
			int delay = Integer.parseInt(parameter);
			while (delay > 0) {
				delay--;
				Clock.tick();
			}
			target.parameterCounter++;
		});

		allObjectFunctions.put("hide", target -> {
			target.hide();
		});

		allObjectFunctions.put("hideplayer", target -> {
			WorldManager.getPlayer().hide();
			// PlayerCharacter.instance.hide();
		});

		allObjectFunctions.put("showplayer", target -> {
			WorldManager.getPlayer().show();
			// PlayerCharacter.instance.show();
		});

		allObjectFunctions.put("spawn", target -> {
			SpawningUtility.trySpawnPokemon(WorldManager.getWorldMap());

		});

		allObjectFunctions.put("changemap", target -> {
			String[] parameters = target.functionParameter.get(target.actionType).get(target.parameterCounter)
					.split("/");
			target.parameterCounter++;
			System.out.println("parameter passed [1] and [2]" + parameters[1] + " " + parameters[2]);
			WorldManager.changeWorld(parameters[0], Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]),
					Boolean.parseBoolean(parameters[3]), parameters[4]);
		});

		allObjectFunctions.put("playerpause", target -> {
			WorldManager.getPlayer().pause();
			// PlayerCharacter.instance.pause();
		});

		allObjectFunctions.put("playerunpause", object -> {
			WorldManager.getPlayer().unpause();
			// PlayerCharacter.instance.unpause();
		});

		allObjectFunctions.put("playerwalk", target -> {
			WorldManager.getPlayer().walk();
			// PlayerCharacter.instance.walk();
		});

		allObjectFunctions.put("loadmap", target -> {
			String[] parameters = target.functionParameter.get(target.actionType).get(target.parameterCounter)
					.split("/");
			target.parameterCounter++;
			try {
				WorldManager.setWorldMapBuffer(WorldManager.loadWorld(parameters[0]));
			} catch (WorldMapException ex) {
				ex.printStackTrace();
			}
		});

		allObjectFunctions.put("usemap", target -> {
			String[] parameters = target.functionParameter.get(target.actionType).get(target.parameterCounter)
					.split("/");
			target.parameterCounter++;
			try {
				WorldManager.useBufferedWorld(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]));
			} catch (NumberFormatException | WorldMapException ex) {
				ex.printStackTrace();
			}
		});

		allObjectFunctions.put("showdialog", object -> {
			DialogBox.instance.show();
		});

		allObjectFunctions.put("sentmessage", object -> {
			System.out.println("sentmessage");
			System.out.println("WorldObj (463) : x = " + object.getBlockX());
			System.out.println("WorldObj (464) : y = " + object.getBlockY());
			String[] parameters = object.processParameters();
			DialogBox.instance.sentDialog(parameters[0]);
		});

		allObjectFunctions.put("hidedialog", object -> {
			DialogBox.instance.hide();
		});

	}

	private final String[] processParameters() {
		try {
			return functionParameter.get(actionType).get(parameterCounter).split("/");
		} finally {
			// TODO: handle finally clause
			parameterCounter++;
		}
	}

	public String getObjectCode() {
		return objectCode;
	}

}
