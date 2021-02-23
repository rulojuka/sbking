package br.com.sbk.sbking.gui.constants;

import java.util.HashMap;
import java.util.Map;
import java.awt.Point;

import br.com.sbk.sbking.core.Direction;

public final class FrameConstants {
	public static final java.awt.Color TABLE_COLOR = new java.awt.Color(0, 100, 0); // Tablecloth green
	public static final int TABLE_WIDTH = 1024;
	public static final int TABLE_HEIGHT = 768;

	public static final int HALF_WIDTH = TABLE_WIDTH / 2;
	public static final int HALF_HEIGHT = TABLE_HEIGHT / 2;
	private static final int FIFTH_WIDTH = TABLE_WIDTH / 5;
	private static final int FIFTH_HEIGHT = TABLE_HEIGHT / 5;

	private static final int NORTH_X_CENTER = HALF_WIDTH;
	private static final int NORTH_Y_CENTER = FIFTH_HEIGHT;

	private static final int SOUTH_X_CENTER = HALF_WIDTH;
	private static final int SOUTH_Y_CENTER = FIFTH_HEIGHT * 4;

	private static final int EAST_X_CENTER = FIFTH_WIDTH * 4;
	private static final int EAST_Y_CENTER = HALF_HEIGHT;

	private static final int WEST_X_CENTER = FIFTH_WIDTH;
	private static final int WEST_Y_CENTER = HALF_HEIGHT;

	@SuppressWarnings("serial")
	public static final Map<Direction, Point> pointOfDirection = new HashMap<Direction, Point>() {
		{
			put(Direction.NORTH, new Point(NORTH_X_CENTER, NORTH_Y_CENTER));
			put(Direction.EAST, new Point(EAST_X_CENTER, EAST_Y_CENTER));
			put(Direction.SOUTH, new Point(SOUTH_X_CENTER, SOUTH_Y_CENTER));
			put(Direction.WEST, new Point(WEST_X_CENTER, WEST_Y_CENTER));
		}
	};
}
