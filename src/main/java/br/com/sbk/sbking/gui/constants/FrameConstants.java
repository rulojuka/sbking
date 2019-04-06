package br.com.sbk.sbking.gui.constants;

public final class FrameConstants {
	public static final java.awt.Color TABLE_COLOR = new java.awt.Color(0, 100, 0); // Tablecloth green
	public static final int TABLE_WIDTH = 1024;
	public static final int TABLE_HEIGHT = 768;
	public static final int BETWEEN_CARDS_WIDTH = 26; /* 26 is good. 12 pixels to hide pictures */
	
	public static final int NORTH_X = 1024 / 2 - BETWEEN_CARDS_WIDTH * 7; /* Ideal would be 6,5 (half of the cards) */
	public static final int SOUTH_X = NORTH_X;
	public static final int EAST_X = NORTH_X + 300;
	public static final int WEST_X = NORTH_X - 300;
	public static final int NORTH_Y = 120;
	public static final int SOUTH_Y = 470;
	public static final int EAST_Y = 300;
	public static final int WEST_Y = EAST_Y;
}
