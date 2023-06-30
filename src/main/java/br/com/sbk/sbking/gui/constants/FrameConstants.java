package br.com.sbk.sbking.gui.constants;

import java.awt.Color;
import java.awt.Point;
import java.util.EnumMap;
import java.util.Map;

import br.com.sbk.sbking.core.Direction;

public final class FrameConstants {

    private FrameConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final Color TABLE_COLOR = new Color(0, 100, 0); // Tablecloth green
    public static final Color TEXT_COLOR = new Color(255, 255, 255); // White
    public static final Color RED_SUIT_COLOR = new Color(255, 0, 0); // Red
    public static final Color BLACK_SUIT_COLOR = new Color(0, 0, 0); // Black

    public static final int ORIGINAL_TABLE_WIDTH = 2200;
    public static final int ORIGINAL_TABLE_HEIGHT = 1400;

    private static int tableWidth;
    private static int tableHeight;
    private static int halfWidth;
    private static int halfHeight;

    public static int getTableWidth() {
        return tableWidth;
    }

    public static int getTableHeight() {
        return tableHeight;
    }

    public static int getHalfWidth() {
        return halfWidth;
    }

    public static int getHalfHeight() {
        return halfHeight;
    }

    private static double scaleX;
    private static double scaleY;

    private static Map<Direction, Point> pointOfDirection = new EnumMap<Direction, Point>(Direction.class);

    public static Point getPointOfDirection(Direction direction) {
        return pointOfDirection.get(direction);
    }

    private static Point spectatorNamesPosition = new Point();

    public static Point getSpectatorNamesPosition() {
        return spectatorNamesPosition;
    }

    private static Point rulesetPosition = new Point();

    public static Point getRulesetPosition() {
        return rulesetPosition;
    }

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

        spectatorNamesPosition = new Point((int) FrameConstants.pointOfDirection.get(Direction.EAST).getX(),
                (int) FrameConstants.pointOfDirection.get(Direction.SOUTH).getY());
        rulesetPosition = new Point((int) FrameConstants.pointOfDirection.get(Direction.WEST).getX(),
                (int) FrameConstants.pointOfDirection.get(Direction.NORTH).getY());
    }
}
