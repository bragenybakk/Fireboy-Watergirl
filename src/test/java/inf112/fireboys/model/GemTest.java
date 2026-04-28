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

    @Test
    void notCollectedOnCreation() {
        assertFalse(fireGem.isCollected());
        assertFalse(waterGem.isCollected());
    }

    @Test
    void fireboyCollectsFireGem() {
        fireGem.whenContact(fireboy);
        assertTrue(fireGem.isCollected());
    }

    @Test
    void watergirlCollectsWaterGem() {
        waterGem.whenContact(watergirl);
        assertTrue(waterGem.isCollected());
    }

    @Test
    void fireboyCannotCollectWaterGem() {
        waterGem.whenContact(fireboy);
        assertFalse(waterGem.isCollected());
    }

    @Test
    void watergirlCannotCollectFireGem() {
        fireGem.whenContact(watergirl);
        assertFalse(fireGem.isCollected());
    }

    @Test
    void cannotCollectTwice() {
        fireGem.whenContact(fireboy);
        fireGem.whenContact(fireboy);
        assertTrue(fireGem.isCollected());
    }
}
