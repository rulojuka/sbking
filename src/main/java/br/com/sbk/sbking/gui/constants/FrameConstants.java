package br.com.sbk.sbking.gui.constants;

import java.util.HashMap;
import java.util.Map;
import java.awt.Point;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.main.ClientApplicationState;

public final class FrameConstants {
	public static final java.awt.Color TABLE_COLOR = new java.awt.Color(0, 100, 0); // Tablecloth green

	public static final int ORIGINAL_TABLE_WIDTH = 1600;
	public static final int ORIGINAL_TABLE_HEIGHT = 1400;

	public static int TABLE_WIDTH;
	public static int TABLE_HEIGHT;
	public static int HALF_WIDTH;
	public static int HALF_HEIGHT;

	private static double scaleX = 1.0;
	private static double scaleY = 1.0;

	@SuppressWarnings("serial")
	public static Map<Direction, Point> pointOfDirection = new HashMap<Direction, Point>();

	public static void initFrameConstants() {
		computeConstants(1400, 820);		
	}

	public static double getScreenScale() {
		// return Math.max(0.05, Math.min(scaleX, scaleY));
		return 0.7;
	}

	public static void computeConstants(int newWidth, int newHeight) {
		TABLE_WIDTH = newWidth;
		TABLE_HEIGHT = newHeight;
		HALF_WIDTH = TABLE_WIDTH / 2;
		HALF_HEIGHT = TABLE_HEIGHT / 2;
		scaleX = (double)(TABLE_WIDTH)/ORIGINAL_TABLE_WIDTH;
		scaleY = (double)(TABLE_HEIGHT)/ORIGINAL_TABLE_HEIGHT;
		double scale = getScreenScale();

		double handToTrickSpacingVertical = scale * 60;
		double handToTrickSpacingHorizontal = scale * 400;
		double approximateCardWidth = scale * 182;
		double approximateCardHeight = scale * 247;

		int widthOffset = (int)((3*approximateCardWidth + 2*handToTrickSpacingHorizontal)/2);
		int heightOffset = (int)((3*approximateCardHeight + 2*handToTrickSpacingVertical)/2);

		int NORTH_X_CENTER = HALF_WIDTH;
		int NORTH_Y_CENTER = HALF_HEIGHT - heightOffset;
		int SOUTH_X_CENTER = HALF_WIDTH;
		int SOUTH_Y_CENTER = HALF_HEIGHT + heightOffset;
		int EAST_X_CENTER = HALF_WIDTH + widthOffset;
		int EAST_Y_CENTER = HALF_HEIGHT;
		int WEST_X_CENTER = HALF_WIDTH - widthOffset;
		int WEST_Y_CENTER = HALF_HEIGHT;

		// TODO: cached references to the points stored in pointOfDirection may
		// be invalid. We must prevent caching of those references.
		pointOfDirection.clear();
		pointOfDirection.put(Direction.NORTH, new Point(NORTH_X_CENTER, NORTH_Y_CENTER));
		pointOfDirection.put(Direction.EAST, new Point(EAST_X_CENTER, EAST_Y_CENTER));
		pointOfDirection.put(Direction.SOUTH, new Point(SOUTH_X_CENTER, SOUTH_Y_CENTER));
		pointOfDirection.put(Direction.WEST, new Point(WEST_X_CENTER, WEST_Y_CENTER));

		ClientApplicationState.setGUIHasChanged(true);
	}
}
