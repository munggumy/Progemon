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
}
