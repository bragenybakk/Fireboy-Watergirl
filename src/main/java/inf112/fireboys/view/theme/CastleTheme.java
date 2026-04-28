package inf112.fireboys.view.theme;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import inf112.fireboys.model.ElementState;
import inf112.fireboys.view.CastleTileSheet;
import inf112.fireboys.view.SpriteSheet;

/**
 * Castle-themed sprites: loads the default sprite sheet, castle tiles, and
 * flame sheets.
 */
public class CastleTheme implements Theme {
    private final SpriteSheet spriteSheet;
    private final CastleTileSheet castleTiles;
    private final BufferedImage redFlame;
    private final BufferedImage blueFlame;

    public CastleTheme() {
        this.spriteSheet = new SpriteSheet("/spritesheet.png");
        this.castleTiles = new CastleTileSheet("/oppcastle-mod-tiles.png");
        this.redFlame = loadSubimage("/red_flame_spritesheet.png", 255, 606, 157, 335);
        this.blueFlame = loadSubimage("/blue_flame_spritesheet.png", 190, 400, 120, 255);
    }

    private BufferedImage loadSubimage(String path, int x, int y, int w, int h) {
        try {
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream(path));
            return sheet.getSubimage(x, y, w, h);
        } catch (IOException | NullPointerException e) {
            return null;
        }
    }

    @Override
    public BufferedImage getPlayerHead(ElementState element) {
        return element == ElementState.FIRE ? spriteSheet.getFireboyHead() : spriteSheet.getWatergirlHead();
    }

    @Override
    public BufferedImage getPlayerBody(ElementState element) {
        return element == ElementState.FIRE ? spriteSheet.getFireboyBody() : spriteSheet.getWatergirlBody();
    }

    @Override
    public BufferedImage getGem(ElementState element) {
        return element == ElementState.FIRE ? spriteSheet.getFireGem() : spriteSheet.getBlueGem();
    }

    @Override
    public BufferedImage getFlame(ElementState element) {
        return element == ElementState.FIRE ? redFlame : blueFlame;
    }

    @Override
    public BufferedImage getWallTile() {
        return castleTiles.getWallTile();
    }

    @Override
    public BufferedImage getBox() {
        return castleTiles.getBox();
    }

    @Override
    public BufferedImage getDoor() {
        return castleTiles.getDoor();
    }

    @Override
    public BufferedImage getOpenDoor() {
        return castleTiles.getOpenDoor();
    }

    @Override
    public BufferedImage getDecorationSprite(int sheetX, int sheetY, int sheetW, int sheetH) {
        return castleTiles.getSprite(sheetX, sheetY, sheetW, sheetH);
    }

    @Override
    public Color getPoolColor(ElementState element) {
        return element == ElementState.FIRE ? Color.decode("#e25822") : Color.decode("#1e90ff");
    }

    @Override
    public Color getBackgroundColor() {
        return Color.decode("#161624");
    }
}
