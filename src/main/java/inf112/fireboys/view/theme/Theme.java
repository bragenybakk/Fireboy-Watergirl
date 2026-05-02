package inf112.fireboys.view.theme;

import java.awt.Color;
import java.awt.image.BufferedImage;

import inf112.fireboys.model.ElementState;

/**
 * Abstract factory for all thematic sprites used by GameView. Swapping the
 * concrete Theme changes the whole visual family in one place.
 */
public interface Theme {
    /** Returns the head sprite for the given player element. */
    BufferedImage getPlayerHead(ElementState element);

    /** Returns the body sprite for the given player element, or null if not available. */
    BufferedImage getPlayerBody(ElementState element);

    /** Returns the gem sprite for the given element. */
    BufferedImage getGem(ElementState element);

    /** Returns the flame animation sprite for the given element. */
    BufferedImage getFlame(ElementState element);

    /** Returns the wall tile sprite. */
    BufferedImage getWallTile();

    /** Returns the box sprite. */
    BufferedImage getBox();

    /** Returns the closed door sprite. */
    BufferedImage getDoor();

    /** Returns the open door sprite. */
    BufferedImage getOpenDoor();

    /** Returns a decoration sprite cropped from the tile sheet at the given coordinates. */
    BufferedImage getDecorationSprite(int sheetX, int sheetY, int sheetW, int sheetH);

    /** Returns the pool color for the given element. */
    Color getPoolColor(ElementState element);

    /** Returns the background color for this theme. */
    Color getBackgroundColor();
}
