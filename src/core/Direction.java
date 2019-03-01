package core;

public enum Direction {

	// Clockwise order
	NORTH, EAST, SOUTH, WEST;

	public boolean isNorth() {
		return Direction.NORTH == this;
	}

	public boolean isEast() {
		return Direction.EAST == this;
	}

	public boolean isSouth() {
		return Direction.SOUTH == this;
	}

	public boolean isWest() {
		return Direction.WEST == this;
	}

	public boolean isNorthSouth() {
		return isNorth() || isSouth();
	}

	public boolean isEastWest() {
		return isEast() || isWest();
	}

	// Static copy to avoid many copies
	private static Direction[] vals = values();
	public Direction next() {
		return vals[(this.ordinal() + 1) % vals.length];
	}

	public Direction next(int n) {
		return vals[(this.ordinal() + n) % vals.length];
	}
}