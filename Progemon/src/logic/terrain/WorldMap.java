package logic.terrain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import graphic.DrawingUtility;
import graphic.IRenderable;
import graphic.IRenderableHolder;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import logic.character.ActiveSkill;

public class WorldMap implements IRenderable {
	
	private int[][] map;
	private static ArrayList<Image> tileset = new ArrayList<>();
	private static final String DEFAULT_PATH = "load\\img\\world\\tileset.png";
	private static WritableImage wimg;
	private boolean visible = true;
	
	public WorldMap(String filePath) {
		// TODO Auto-generated constructor stub
		loadMap(filePath);
		IRenderableHolder.addWorldObject(this);
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		DrawingUtility.drawWorldMap(this);
	}

	@Override
	public int getDepth() {
		// TODO Auto-generated method stub
		return Integer.MIN_VALUE;
	}
	
	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return visible;
	}
	
	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		this.visible = visible;
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		visible = false;
		IRenderableHolder.removeWorldObject(this);
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		IRenderableHolder.addWorldObject(this);
		visible = true;
	}
	
	public int getTerrainAt(int x, int y) {
		return map[y][x];
	}
	
	public static Image getImage(int tileCode) {
		return tileset.get(tileCode - 1);
	}
	
	public static ArrayList<Image> getTileset() {
		return tileset;
	}
	
	public int[][] getMap() {
		return map;
	}
	
	public void setMap(int[][] map) {
		this.map = map;
	}
	
	public static void loadTileset() {
		Image img = new Image(new File(DEFAULT_PATH).toURI().toString());
		tileset.add(DrawingUtility.resize(new WritableImage(img.getPixelReader(), 0, 0, 16, 16), 2));
		tileset.add(DrawingUtility.resize(new WritableImage(img.getPixelReader(), 16, 0, 16, 16), 2));
	}
	
	public void loadMap(String filePath) {
		try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(filePath)))) {
			int width = scanner.nextInt();
			int height = scanner.nextInt();
			scanner.nextLine();
			Pattern pattern = Pattern.compile("[\\s+-?\\d+]+");
			Matcher matcher;
			map = new int[height][width];
			int[] mapLine = new int[width];
			int lineCounter = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.matches("^#+.*")) { // comment line ##hey hey heys
					continue;
				}
				matcher = pattern.matcher(line);
				if (matcher.find()) {
					int digitCounter = 0;
					for (String digit : line.trim().split("\\s+")) {
						mapLine[digitCounter] = Integer.parseInt(digit);
						digitCounter++;
					}
					map[lineCounter] = mapLine.clone();
					lineCounter++;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}