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
    private static final int DOOR_X = 384, DOOR_Y = 208, DOOR_W = 64, DOOR_H = 80;
    private static final int TORCH_X = 96, TORCH_Y = 288, TORCH_W = 32, TORCH_H = 32;
    private static final int WINDOW_X = 96, WINDOW_Y = 256, WINDOW_W = 32, WINDOW_H = 32;
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

    public BufferedImage getTorch() {
        return getSprite(TORCH_X, TORCH_Y, TORCH_W, TORCH_H);
    }

    public BufferedImage getWindow() {
        return getSprite(WINDOW_X, WINDOW_Y, WINDOW_W, WINDOW_H);
    }

    public BufferedImage getSprite(int x, int y, int width, int height) {
        if (sheet == null)
            return null;
        if (x + width > sheet.getWidth() || y + height > sheet.getHeight())
            return null;
        return sheet.getSubimage(x, y, width, height);
    }
}
