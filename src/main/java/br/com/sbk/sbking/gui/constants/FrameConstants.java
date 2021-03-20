package br.com.sbk.sbking.gui.constants;

import java.util.HashMap;
import java.util.Map;
import java.awt.Point;

import br.com.sbk.sbking.core.Direction;

public final class FrameConstants {
    public static final java.awt.Color TABLE_COLOR = new java.awt.Color(0, 100, 0); // Tablecloth green

    public static final int ORIGINAL_TABLE_WIDTH = 2200;
    public static final int ORIGINAL_TABLE_HEIGHT = 1400;

    public static int TABLE_WIDTH;
    public static int TABLE_HEIGHT;
    public static int HALF_WIDTH;
    public static int HALF_HEIGHT;

    private static double scaleX;
    private static double scaleY;

    @SuppressWarnings("serial")
    public static Map<Direction, Point> pointOfDirection = new HashMap<Direction, Point>();

    public static void initFrameConstants() {
        computeConstants(1400, 820);
    }

    public static double getScreenScale() {
        return Math.max(0.05, Math.min(scaleX, scaleY));
    }

    public static void computeConstants(int newWidth, int newHeight) {
        TABLE_WIDTH = newWidth;
        TABLE_HEIGHT = newHeight;
        HALF_WIDTH = TABLE_WIDTH / 2;
        HALF_HEIGHT = TABLE_HEIGHT / 2;
        scaleX = (double) (Math.min(TABLE_WIDTH, ORIGINAL_TABLE_WIDTH)) / ORIGINAL_TABLE_WIDTH;
        scaleY = (double) (Math.min(TABLE_HEIGHT, ORIGINAL_TABLE_HEIGHT)) / ORIGINAL_TABLE_HEIGHT;
        double scale = getScreenScale();

        double handToTrickSpacingVertical = scale * 60;
        double handToTrickSpacingHorizontal = scale * 400;
        double approximateCardWidth = scale * 182;
        double approximateCardHeight = scale * 247;

        int widthOffset = (int) ((3 * approximateCardWidth + 2 * handToTrickSpacingHorizontal) / 2);
        int heightOffset = (int) ((3 * approximateCardHeight + 2 * handToTrickSpacingVertical) / 2);

        int northX = HALF_WIDTH;
        int northY = HALF_HEIGHT - heightOffset;
        int southX = HALF_WIDTH;
        int southY = HALF_HEIGHT + heightOffset;
        int eastX = HALF_WIDTH + widthOffset;
        int eastY = HALF_HEIGHT;
        int westX = HALF_WIDTH - widthOffset;
        int westY = HALF_HEIGHT;

        // TODO: cached references to the points stored in pointOfDirection may
        // be invalid. We must prevent caching of those references.
        pointOfDirection.clear();
        pointOfDirection.put(Direction.NORTH, new Point(northX, northY));
        pointOfDirection.put(Direction.EAST, new Point(eastX, eastY));
        pointOfDirection.put(Direction.SOUTH, new Point(southX, southY));
        pointOfDirection.put(Direction.WEST, new Point(westX, westY));
    }
}
