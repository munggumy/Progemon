package logic.terrain;

public class FightMap {
	public static final int SIZE_X = 8;
	public static final int SIZE_Y = 6;
	private static FightTerrain[][] map = new FightTerrain[6][8];

	public static final FightTerrain[][] getMap() {
		return map;
	}

	public static final void setMap(FightTerrain[][] map) {
		FightMap.map = map;
	}
}
