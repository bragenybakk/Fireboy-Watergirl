package inf112.fireboys.view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Extracts animation frames from the skeleton enemy sprite sheet (832x320).
 *
 * Sheet layout: 13 columns × 5 rows, each frame is 64×64 px.
 * Row 0 (y= 0): Attack animation — 11 frames
 * Row 1 (y= 64): Death animation — 11 frames
 * Row 2 (y=128): Walk animation — 11 frames
 * Row 3 (y=192): Idle animation — 4 frames
 * Row 4 (y=256): Spawn animation — 2 frames
 */
public class SkeletonSpriteSheet {
    public static final int FRAME_SIZE = 64;
    public static final int ROW_ATTACK = 0;
    public static final int ROW_DEATH = 1;
    public static final int ROW_WALK = 2;
    public static final int ROW_IDLE = 3;
    public static final int ROW_SPAWN = 4;
    public static final int ATTACK_FRAMES = 11;
    public static final int DEATH_FRAMES = 11;
    public static final int WALK_FRAMES = 11;
    public static final int IDLE_FRAMES = 4;
    public static final int SPAWN_FRAMES = 2;
    // Bounding box of non-transparent pixels within a 64×64 frame (walk row).
    // Used to align the sprite content precisely over the enemy hitbox.
    public static final int CONTENT_TOP = 17;
    public static final int CONTENT_BOTTOM = 48;
    public static final int CONTENT_LEFT = 5;
    public static final int CONTENT_RIGHT = 40;
    private final BufferedImage sheet;

    /**
     * Loads the skeleton sprite sheet from the given resource path.
     *
     * @param path
     *            the classpath resource path to the sprite sheet image
     */
    public SkeletonSpriteSheet(String path) {
        BufferedImage loaded = null;
        try {
            loaded = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Could not load skeleton sprite sheet: " + path);
        }
        this.sheet = loaded;
    }

    /**
     * Returns the sprite frame at the given row and column, or null if out of bounds.
     *
     * @param row
     *            the animation row (use ROW_* constants)
     * @param col
     *            the frame column index within that row
     * @return the sprite frame image, or null if out of bounds or sheet not loaded
     */
    public BufferedImage getFrame(int row, int col) {
        if (sheet == null)
            return null;
        int x = col * FRAME_SIZE;
        int y = row * FRAME_SIZE;
        if (x + FRAME_SIZE > sheet.getWidth() || y + FRAME_SIZE > sheet.getHeight())
            return null;
        return sheet.getSubimage(x, y, FRAME_SIZE, FRAME_SIZE);
    }
}
