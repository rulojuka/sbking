package br.com.sbk.sbking.gui.constants;

public final class FrameConstants {
	public static final java.awt.Color TABLE_COLOR = new java.awt.Color(0, 100, 0); // Tablecloth green
	public static final int TABLE_WIDTH = 1024;
	public static final int TABLE_HEIGHT = 768;

	public static final int HALF_WIDTH = TABLE_WIDTH / 2;
	public static final int HALF_HEIGHT = TABLE_HEIGHT / 2;
	private static final int FIFTH_WIDTH = TABLE_WIDTH / 5;
	private static final int FIFTH_HEIGHT = TABLE_HEIGHT / 5;

	public static final int NORTH_X_CENTER = HALF_WIDTH;
	public static final int NORTH_Y_CENTER = FIFTH_HEIGHT;

	public static final int SOUTH_X_CENTER = HALF_WIDTH;
	public static final int SOUTH_Y_CENTER = FIFTH_HEIGHT * 4;

	public static final int EAST_X_CENTER = FIFTH_WIDTH * 4;
	public static final int EAST_Y_CENTER = HALF_HEIGHT;

	public static final int WEST_X_CENTER = FIFTH_WIDTH;
	public static final int WEST_Y_CENTER = HALF_HEIGHT;
}
