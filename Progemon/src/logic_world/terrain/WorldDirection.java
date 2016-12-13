package logic_world.terrain;

public enum WorldDirection {
	SOUTH(0, 1), WEST(-1, 0), NORTH(0, -1), EAST(1, 0);

	private int x, y;

	WorldDirection(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}
	
	public static WorldDirection getDirection(int x, int y) {
		if (x == 0 && y == 1) {
			return SOUTH;
		}
		if (x == -1 && y == 0) {
			return WEST;
		}
		if (x == 0 && y == -1) {
			return NORTH;
		}
		if (x == 1 && y == 0) {
			return EAST;
		}
		return null;
	}
}
