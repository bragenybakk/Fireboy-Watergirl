package inf112.fireboys.coordinateSystem;

/**
 * A decorative image placed on the board. Has a position and size,
 * but no collision or game logic. The sheet coordinates point to the
 * tile to draw from the tile sheet.
 */
public record Decoration(Position pos, double width, double height,
        int sheetX, int sheetY, int sheetW, int sheetH) {}
