package inf112.fireboys.view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/** Loads character and gem sprites from the sprite sheet images. */
public class SpriteSheet {
    private BufferedImage sheet;
    private BufferedImage charSheet;

    /** Loads the sprite sheet from the given resource path, and the character sheet. */
    public SpriteSheet(String path) {
        try {
            sheet = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            System.err.println("Kunne ikke laste spritesheet: " + path);
        }
        try {
            charSheet = ImageIO.read(getClass().getResourceAsStream("/fireboy&watergirl_char.png"));
        } catch (IOException e) {
            System.err.println("Kunne ikke laste karakter-sprites");
        }
    }

    /**
     * Returns the Fireboy head sprite.
     *
     * @return the Fireboy head image, or null if the sheet was not loaded
     */
    public BufferedImage getFireboyHead() {
        if (charSheet == null) return null;
        return charSheet.getSubimage(1, 2, 274, 469);
    }

    /**
     * Returns the Watergirl head sprite.
     *
     * @return the Watergirl head image, or null if the sheet was not loaded
     */
    public BufferedImage getWatergirlHead() {
        if (charSheet == null) return null;
        return charSheet.getSubimage(275, 50, 268, 421);
    }

    /**
     * Returns the blue (water) gem sprite.
     *
     * @return the blue gem image, or null if the sheet was not loaded
     */
    public BufferedImage getBlueGem() {
        return getSprite(980, 1375, 70, 70);
    }

    /**
     * Returns the red (fire) gem sprite.
     *
     * @return the red gem image, or null if the sheet was not loaded
     */
    public BufferedImage getFireGem() {
        return getSprite(1095, 1375, 70, 70);
    }

    private BufferedImage getSprite(int x, int y, int width, int height) {
        if (sheet == null)
            return null;
        if (x + width > sheet.getWidth() || y + height > sheet.getHeight())
            return null;
        return sheet.getSubimage(x, y, width, height);
    }
}
