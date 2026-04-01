package inf112.fireboys.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.entity.Gem;
import inf112.fireboys.model.player.Player;

public class GemTest {
    private Gem fireGem;
    private Gem waterGem;
    private Player fireboy;
    private Player watergirl;
    @BeforeEach
    void setUp() {
        fireGem = new Gem(new Position(0, 0), 1.0, 1.0, ElementState.FIRE);
        waterGem = new Gem(new Position(0, 0), 1.0, 1.0, ElementState.WATER);
        fireboy = new Player(new Position(0, 0), ElementState.FIRE);
        watergirl = new Player(new Position(0, 0), ElementState.WATER);
    }

    /** Gems start as not collected. */
    @Test
    void notCollectedOnCreation() {
        assertFalse(fireGem.isCollected());
        assertFalse(waterGem.isCollected());
    }

    /** Fireboy collects fire gem and gets score. */
    @Test
    void fireboyCollectsFireGem() {
        fireGem.whenContact(fireboy);
        assertTrue(fireGem.isCollected());
        assertEquals(1, fireboy.getScore());
    }

    /** Watergirl collects water gem and gets score. */
    @Test
    void watergirlCollectsWaterGem() {
        waterGem.whenContact(watergirl);
        assertTrue(waterGem.isCollected());
        assertEquals(1, watergirl.getScore());
    }

    /** Fireboy cannot collect water gem. */
    @Test
    void fireboyCannotCollectWaterGem() {
        waterGem.whenContact(fireboy);
        assertFalse(waterGem.isCollected());
        assertEquals(0, fireboy.getScore());
    }

    /** Watergirl cannot collect fire gem. */
    @Test
    void watergirlCannotCollectFireGem() {
        fireGem.whenContact(watergirl);
        assertFalse(fireGem.isCollected());
        assertEquals(0, watergirl.getScore());
    }

    /** Collected gem cannot be collected again. */
    @Test
    void cannotCollectTwice() {
        fireGem.whenContact(fireboy);
        fireGem.whenContact(fireboy);
        assertEquals(1, fireboy.getScore());
    }
}
