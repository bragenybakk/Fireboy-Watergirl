package inf112.fireboys.view.theme;

import java.awt.image.BufferedImage;

import inf112.fireboys.model.ElementState;

/**
 * Returns null for every sprite. GameView falls back to solid colored
 * rectangles when sprites are missing, so this renders the game without art.
 * Useful as a fallback and to demonstrate theme swapping.
 */
public class NullTheme implements Theme {
    @Override
    public BufferedImage getPlayerHead(ElementState element) {
        return null;
    }

    @Override
    public BufferedImage getPlayerBody(ElementState element) {
        return null;
    }

    @Override
    public BufferedImage getGem(ElementState element) {
        return null;
    }

    @Override
    public BufferedImage getFlame(ElementState element) {
        return null;
    }

    @Override
    public BufferedImage getWallTile() {
        return null;
    }

    @Override
    public BufferedImage getDoor() {
        return null;
    }

    @Override
    public BufferedImage getDecorationSprite(int sheetX, int sheetY, int sheetW, int sheetH) {
        return null;
    }
}
