import graphic.Screen;
import logic.terrain.FightMap;
import logic.terrain.FightTerrain;
import logic.terrain.FightTerrain.TerrainType;
import utility.FileUtility;

public class Main {
	
	public static void main(String[] args) {
		new Screen();
		FightMap fightmap = new FightMap(5, 5);
		FightTerrain[][] map = {{new FightTerrain(0, 0, TerrainType.GRASS), new FightTerrain(0, 1, TerrainType.GRASS), new FightTerrain(0, 2, TerrainType.GRASS), new FightTerrain(0, 3, TerrainType.GRASS), new FightTerrain(0, 4, TerrainType.GRASS)},
				{new FightTerrain(1, 0, TerrainType.GRASS), new FightTerrain(1, 0, TerrainType.GRASS), new FightTerrain(1, 0, TerrainType.GRASS), new FightTerrain(1, 0, TerrainType.GRASS), new FightTerrain(1, 0, TerrainType.GRASS)},
				{new FightTerrain(2, 0, TerrainType.GRASS), new FightTerrain(2, 0, TerrainType.GRASS), new FightTerrain(2, 0, TerrainType.GRASS), new FightTerrain(2, 0, TerrainType.GRASS), new FightTerrain(2, 0, TerrainType.GRASS)},
				{new FightTerrain(3, 0, TerrainType.GRASS), new FightTerrain(3, 0, TerrainType.GRASS), new FightTerrain(3, 0, TerrainType.GRASS), new FightTerrain(3, 0, TerrainType.GRASS), new FightTerrain(3, 0, TerrainType.GRASS)},
				{new FightTerrain(4, 0, TerrainType.GRASS), new FightTerrain(4, 0, TerrainType.GRASS), new FightTerrain(4, 0, TerrainType.GRASS), new FightTerrain(4, 0, TerrainType.GRASS), new FightTerrain(4, 0, TerrainType.GRASS)}};
		fightmap.setMap(map);
		fightmap.draw();
	}

}
