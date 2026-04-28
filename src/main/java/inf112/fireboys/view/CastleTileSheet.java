package inf112.fireboys.view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Loads tiles from the castle tile sheet image.
 * Each tile has its pixel coordinates defined as constants below.
 */
public class CastleTileSheet {
    // Coordinates of individual tiles within the sheet (x, y, w, h).
    private static final int WALL_TILE_X = 128, WALL_TILE_Y = 68, WALL_TILE_W = 32, WALL_TILE_H = 32;
    private static final int DOOR_X = 320, DOOR_Y = 208, DOOR_W = 64, DOOR_H = 80;
    private static final int OPEN_DOOR_X = 256, OPEN_DOOR_Y = 208, OPEN_DOOR_W = 64, OPEN_DOOR_H = 80;
    private static final int TORCH_X = 96, TORCH_Y = 288, TORCH_W = 32, TORCH_H = 32;
    private static final int WINDOW_X = 96, WINDOW_Y = 256, WINDOW_W = 32, WINDOW_H = 32;
    private static final int BOX_X = 34, BOX_Y = 66, BOX_W = 26, BOX_H = 27;
    private BufferedImage sheet;
    /** Loads the tile sheet image from the given resource path. */
    public CastleTileSheet(String path) {
        try {
            sheet = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            System.err.println("Kunne ikke laste tile-sheet: " + path);
        }
    }

    /** Returns the wall tile sprite. */
    public BufferedImage getWallTile() {
        return getSprite(WALL_TILE_X, WALL_TILE_Y, WALL_TILE_W, WALL_TILE_H);
    }

    /** Returns the closed door sprite. */
    public BufferedImage getDoor() {
        return getSprite(DOOR_X, DOOR_Y, DOOR_W, DOOR_H);
    }

    /** Returns the open door sprite. */
    public BufferedImage getOpenDoor() {
        return getSprite(OPEN_DOOR_X, OPEN_DOOR_Y, OPEN_DOOR_W, OPEN_DOOR_H);
    }

    /** Returns the torch decoration sprite. */
    public BufferedImage getTorch() {
        return getSprite(TORCH_X, TORCH_Y, TORCH_W, TORCH_H);
    }

    /** Returns the window decoration sprite. */
    public BufferedImage getWindow() {
        return getSprite(WINDOW_X, WINDOW_Y, WINDOW_W, WINDOW_H);
    }

    /** Returns the box sprite. */
    public BufferedImage getBox() {
        return getSprite(BOX_X, BOX_Y, BOX_W, BOX_H);
    }

    /** Returns a sub-image from the sheet at the given pixel coordinates and size. */
    public BufferedImage getSprite(int x, int y, int width, int height) {
        if (sheet == null)
            return null;
        if (x + width > sheet.getWidth() || y + height > sheet.getHeight())
            return null;
        return sheet.getSubimage(x, y, width, height);
    }
}
