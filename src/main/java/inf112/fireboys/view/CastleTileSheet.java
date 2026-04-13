package inf112.fireboys.view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Loads sub-images from {@code oppcastle-mod-tiles.png}.
 * Coordinates are pixel offsets into the 480x352 sheet — adjust the constants
 * below if a tile looks misaligned.
 */
public class CastleTileSheet {
    // Coordinates of individual tiles within the sheet (x, y, w, h).
    private static final int WALL_TILE_X = 96, WALL_TILE_Y = 80, WALL_TILE_W = 16, WALL_TILE_H = 16;
    private static final int DOOR_X = 384, DOOR_Y = 208, DOOR_W = 64, DOOR_H = 80;
    private BufferedImage sheet;
    public CastleTileSheet(String path) {
        try {
            sheet = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            System.err.println("Kunne ikke laste tile-sheet: " + path);
        }
    }

    public BufferedImage getWallTile() {
        return getSprite(WALL_TILE_X, WALL_TILE_Y, WALL_TILE_W, WALL_TILE_H);
    }

    public BufferedImage getDoor() {
        return getSprite(DOOR_X, DOOR_Y, DOOR_W, DOOR_H);
    }

    private BufferedImage getSprite(int x, int y, int width, int height) {
        if (sheet == null)
            return null;
        if (x + width > sheet.getWidth() || y + height > sheet.getHeight())
            return null;
        return sheet.getSubimage(x, y, width, height);
    }
}
