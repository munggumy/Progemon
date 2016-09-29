package main;
import java.awt.FlowLayout;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;

import graphic.FightMapComponent;
import logic.terrain.FightMap;
import logic.terrain.FightTerrain;
import logic.terrain.FightTerrain.TerrainType;
import utility.fileUtility;

public class Main {
	
	public static final int SCREEN_WIDTH = 800, SCREEN_HEIGHT = 600;
	private static JFrame screen = new JFrame("Progemon");
	
	public Main() {
		// TODO Auto-generated constructor stub
		screen.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		screen.setVisible(true);
		screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		new Main();
		
		FightMap fightMap = null;
		
		FightTerrain[][] map = {{new FightTerrain(0, 0, TerrainType.GRASS)}};
		fightMap = new FightMap(map);
		
		FightMapComponent component = new FightMapComponent(fightMap);
		screen.add(component);
		
		/*try {
			fightMap = new FightMap(fileUtility.loadFightMap());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		FightTerrain[][] map2 = {{new FightTerrain(0, 0, TerrainType.GRASS), new FightTerrain(0, 1, TerrainType.GRASS), new FightTerrain(0, 2, TerrainType.GRASS), new FightTerrain(0, 3, TerrainType.GRASS), new FightTerrain(0, 4, TerrainType.GRASS)},
				{new FightTerrain(1, 0, TerrainType.GRASS), new FightTerrain(1, 0, TerrainType.GRASS), new FightTerrain(1, 0, TerrainType.GRASS), new FightTerrain(1, 0, TerrainType.GRASS), new FightTerrain(1, 0, TerrainType.GRASS)},
				{new FightTerrain(2, 0, TerrainType.GRASS), new FightTerrain(2, 0, TerrainType.GRASS), new FightTerrain(2, 0, TerrainType.GRASS), new FightTerrain(2, 0, TerrainType.GRASS), new FightTerrain(2, 0, TerrainType.GRASS)},
				{new FightTerrain(3, 0, TerrainType.GRASS), new FightTerrain(3, 0, TerrainType.GRASS), new FightTerrain(3, 0, TerrainType.GRASS), new FightTerrain(3, 0, TerrainType.GRASS), new FightTerrain(3, 0, TerrainType.GRASS)},
				{new FightTerrain(4, 0, TerrainType.GRASS), new FightTerrain(4, 0, TerrainType.GRASS), new FightTerrain(4, 0, TerrainType.GRASS), new FightTerrain(4, 0, TerrainType.GRASS), new FightTerrain(4, 0, TerrainType.GRASS)}};
		fightMap = new FightMap(map2);
		FightMapComponent component2 = new FightMapComponent(fightMap);
		//screen.add(component);
	}
}
