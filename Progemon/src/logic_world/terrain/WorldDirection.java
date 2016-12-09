package logic_world.terrain;

public enum WorldDirection {
	DOWN(0, 1), LEFT(-1, 0), UP(0, -1), RIGHT(1, 0);

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
