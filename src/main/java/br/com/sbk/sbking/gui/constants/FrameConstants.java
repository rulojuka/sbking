package br.com.sbk.sbking.gui.constants;

import java.util.HashMap;
import java.util.Map;
import java.awt.Point;

import br.com.sbk.sbking.core.Direction;

public final class FrameConstants {
    public static final java.awt.Color TABLE_COLOR = new java.awt.Color(0, 100, 0); // Tablecloth green

    public static final int ORIGINAL_TABLE_WIDTH = 2200;
    public static final int ORIGINAL_TABLE_HEIGHT = 1400;

    public static int tableWidth;
    public static int tableHeight;
    public static int halfWidth;
    public static int halfHeight;

    private static double scaleX;
    private static double scaleY;

    public static Map<Direction, Point> pointOfDirection = new HashMap<Direction, Point>();

    public static void initFrameConstants() {
        computeConstants(1400, 820);
    }

    public static double getScreenScale() {
        return Math.max(0.05, Math.min(scaleX, scaleY));
    }

    public static void computeConstants(int newWidth, int newHeight) {
        tableWidth = newWidth;
        tableHeight = newHeight;
        halfWidth = tableWidth / 2;
        halfHeight = tableHeight / 2;
        scaleX = (double) (Math.min(tableWidth, ORIGINAL_TABLE_WIDTH)) / ORIGINAL_TABLE_WIDTH;
        scaleY = (double) (Math.min(tableHeight, ORIGINAL_TABLE_HEIGHT)) / ORIGINAL_TABLE_HEIGHT;
        double scale = getScreenScale();

        double handToTrickSpacingVertical = scale * 60;
        double handToTrickSpacingHorizontal = scale * 400;
        double approximateCardWidth = scale * 182;
        double approximateCardHeight = scale * 247;

        int widthOffset = (int) ((3 * approximateCardWidth + 2 * handToTrickSpacingHorizontal) / 2);
        int heightOffset = (int) ((3 * approximateCardHeight + 2 * handToTrickSpacingVertical) / 2);

        int northX = halfWidth;
        int northY = halfHeight - heightOffset;
        int southX = halfWidth;
        int southY = halfHeight + heightOffset;
        int eastX = halfWidth + widthOffset;
        int eastY = halfHeight;
        int westX = halfWidth - widthOffset;
        int westY = halfHeight;

        // TODO: cached references to the points stored in pointOfDirection may
        // be invalid. We must prevent caching of those references.
        pointOfDirection.clear();
        pointOfDirection.put(Direction.NORTH, new Point(northX, northY));
        pointOfDirection.put(Direction.EAST, new Point(eastX, eastY));
        pointOfDirection.put(Direction.SOUTH, new Point(southX, southY));
        pointOfDirection.put(Direction.WEST, new Point(westX, westY));
    }
}
