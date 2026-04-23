package inf112.fireboys.view.theme;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import inf112.fireboys.model.ElementState;

public class ThemeTest {

    @Test
    void castleThemeReturnsSprites() {
        Theme theme = new CastleTheme();
        assertNotNull(theme.getPlayerHead(ElementState.FIRE));
        assertNotNull(theme.getPlayerHead(ElementState.WATER));
        assertNotNull(theme.getGem(ElementState.FIRE));
        assertNotNull(theme.getWallTile());
        assertNotNull(theme.getDoor());
    }

    @Test
    void nullThemeReturnsNull() {
        Theme theme = new NullTheme();
        assertNull(theme.getPlayerHead(ElementState.FIRE));
        assertNull(theme.getGem(ElementState.WATER));
        assertNull(theme.getWallTile());
        assertNull(theme.getDoor());
        assertNull(theme.getFlame(ElementState.FIRE));
    }
}
