package inf112.fireboys.view.theme;

import java.awt.Color;
import java.awt.image.BufferedImage;

import inf112.fireboys.model.ElementState;

/**
 * Abstract factory for all thematic sprites used by GameView. Swapping the
 * concrete Theme changes the whole visual family in one place.
 */
public interface Theme {
    BufferedImage getPlayerHead(ElementState element);

    BufferedImage getPlayerBody(ElementState element);

    BufferedImage getGem(ElementState element);

    BufferedImage getFlame(ElementState element);

    BufferedImage getWallTile();

    BufferedImage getDoor();

    BufferedImage getOpenDoor();

    BufferedImage getDecorationSprite(int sheetX, int sheetY, int sheetW, int sheetH);

    Color getPoolColor(ElementState element);

    Color getBackgroundColor();
}
