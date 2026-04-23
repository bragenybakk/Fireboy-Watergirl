package inf112.fireboys.view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteSheet {
    private BufferedImage sheet;
    public SpriteSheet(String path) {
        try {
            sheet = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            System.err.println("Kunne ikke laste spritesheet: " + path);
        }
    }

    public BufferedImage getFireboyHead() {
        return getSprite(1900, 1000, 70, 70);
    }

    public BufferedImage getFireboyBody() {
        return getSprite(150, 420, 75, 60);
    }

    public BufferedImage getWatergirlHead() {
        return getSprite(1900, 500, 70, 70);
    }

    public BufferedImage getWatergirlBody() {
        return getSprite(910, 310, 75, 60);
    }

    public BufferedImage getBlueGem() {
        return getSprite(980, 1375, 70, 70);
    }

    public BufferedImage getFireGem() {
        return getSprite(1095, 1375, 70, 70);
    }

    /**
     * Extract a sprite region from the sheet.
     */
    private BufferedImage getSprite(int x, int y, int width, int height) {
        if (sheet == null)
            return null;
        if (x + width > sheet.getWidth() || y + height > sheet.getHeight())
            return null;
        return sheet.getSubimage(x, y, width, height);
    }
}
