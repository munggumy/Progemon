package terrain;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class FightTerrain {
	private int x, y;
	private boolean isShadowed;
	private Type type;

	private enum Type {
		GRASS, ROCK, WATER, TREE;
	}

	private ArrayList<FightTerrain> toArrayList() {
		ArrayList<FightTerrain> temp = new ArrayList<FightTerrain>();
		temp.add(this);
		return temp;
	}

	@Override
	public String toString() {
		return "FightTerrain [x=" + x + ", y=" + y + ", isShadowed=" + isShadowed + ", type=" + type + "]";
	}

}
