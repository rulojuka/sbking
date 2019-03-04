package core;

public enum Direction {

	// Clockwise order
	NORTH("North", 'N'), EAST("East", 'E'), SOUTH("South", 'S'), WEST("West", 'E');

	private final String completeName;
	private final char abbreviation;

	private Direction(String completeName, char abbreviation) {
		this.completeName = completeName;
		this.abbreviation = abbreviation;
	}

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

	public String getCompleteName() {
		return completeName;
	}

	public char getAbbreviation() {
		return abbreviation;
	}
}