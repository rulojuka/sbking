package br.com.sbk.sbking.gui.constants;

import java.util.HashMap;
import java.util.Map;
import java.awt.Point;

import br.com.sbk.sbking.core.Direction;

// (TODO) refactor this class to reduce instruction duplication. 
public final class FrameConstants {
	public static final java.awt.Color TABLE_COLOR = new java.awt.Color(0, 100, 0); // Tablecloth green
	public static int TABLE_WIDTH = 1024;
	public static int TABLE_HEIGHT = 768;

	public static int HALF_WIDTH = TABLE_WIDTH / 2;
	public static int HALF_HEIGHT = TABLE_HEIGHT / 2;
	private static int FIFTH_WIDTH = TABLE_WIDTH / 5;
	private static int FIFTH_HEIGHT = TABLE_HEIGHT / 5;

	private static int NORTH_X_CENTER = HALF_WIDTH;
	private static int NORTH_Y_CENTER = FIFTH_HEIGHT;

	private static int SOUTH_X_CENTER = HALF_WIDTH;
	private static int SOUTH_Y_CENTER = FIFTH_HEIGHT * 4;

	private static int EAST_X_CENTER = FIFTH_WIDTH * 4;
	private static int EAST_Y_CENTER = HALF_HEIGHT;

	private static int WEST_X_CENTER = FIFTH_WIDTH;
	private static int WEST_Y_CENTER = HALF_HEIGHT;

	@SuppressWarnings("serial")
	public static Map<Direction, Point> pointOfDirection = new HashMap<Direction, Point>() {
		{
			put(Direction.NORTH, new Point(NORTH_X_CENTER, NORTH_Y_CENTER));
			put(Direction.EAST, new Point(EAST_X_CENTER, EAST_Y_CENTER));
			put(Direction.SOUTH, new Point(SOUTH_X_CENTER, SOUTH_Y_CENTER));
			put(Direction.WEST, new Point(WEST_X_CENTER, WEST_Y_CENTER));
		}
	};

	public static void computeConstants(int newWidth, int newHeight) {
		TABLE_WIDTH = newWidth;
		TABLE_HEIGHT = newHeight;

		HALF_WIDTH = TABLE_WIDTH / 2;
		HALF_HEIGHT = TABLE_HEIGHT / 2;
		FIFTH_WIDTH = TABLE_WIDTH / 5;
		FIFTH_HEIGHT = TABLE_HEIGHT / 5;

		NORTH_X_CENTER = HALF_WIDTH;
		NORTH_Y_CENTER = FIFTH_HEIGHT;

		SOUTH_X_CENTER = HALF_WIDTH;
		SOUTH_Y_CENTER = FIFTH_HEIGHT * 4;

		EAST_X_CENTER = FIFTH_WIDTH * 4;
		EAST_Y_CENTER = HALF_HEIGHT;

		WEST_X_CENTER = FIFTH_WIDTH;
		WEST_Y_CENTER = HALF_HEIGHT;

		pointOfDirection = new HashMap<Direction, Point>() {
			{
				put(Direction.NORTH, new Point(NORTH_X_CENTER, NORTH_Y_CENTER));
				put(Direction.EAST, new Point(EAST_X_CENTER, EAST_Y_CENTER));
				put(Direction.SOUTH, new Point(SOUTH_X_CENTER, SOUTH_Y_CENTER));
				put(Direction.WEST, new Point(WEST_X_CENTER, WEST_Y_CENTER));
			}
		};
	}
}
