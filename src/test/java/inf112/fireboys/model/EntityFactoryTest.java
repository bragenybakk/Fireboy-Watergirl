package inf112.fireboys.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.model.entity.Box;
import inf112.fireboys.model.entity.Door;
import inf112.fireboys.model.entity.Gem;
import inf112.fireboys.model.entity.Pool;
import inf112.fireboys.model.entity.StaticEntity;

public class EntityFactoryTest {
    private Scanner scan(String input) {
        return new Scanner(input).useLocale(Locale.US);
    }

    @Test
    void parseUnknownTokenReturnsFalse() {
        EntityFactory factory = new EntityFactory();
        assertFalse(factory.parse("NOT_A_REAL_TOKEN", scan("")),
                "parse should return false for an unknown token");
    }

    @Test
    void parseKnownTokenReturnsTrue() {
        EntityFactory factory = new EntityFactory();
        assertTrue(factory.parse("WALL", scan("0 0 5 5")),
                "parse should return true for a known token");
    }

    @Test
    void emptyFactoryBuildsEmptyBoard() {
        Board board = new EntityFactory().build(50, 30);
        assertEquals(50, board.boardWidth());
        assertEquals(30, board.boardHeight());
        assertTrue(board.entities().isEmpty());
        assertTrue(board.players().isEmpty());
        assertTrue(board.enemies().isEmpty());
    }

    @Test
    void doorEndsUpInBothEntitiesAndDoorsLists() {
        EntityFactory factory = new EntityFactory();
        factory.parse("DOOR", scan("10 20 5 5"));
        Board board = factory.build(100, 100);
        assertEquals(1, board.doors().size(), "Door should be in doors list");
        assertEquals(1, board.entities().size(), "Door should also be in entities list");
        assertSame(board.doors().get(0), board.entities().get(0),
                "Same Door instance should appear in both lists");
        assertInstanceOf(Door.class, board.entities().get(0));
    }

    @Test
    void boxEndsUpInBothEntitiesAndMovablesLists() {
        EntityFactory factory = new EntityFactory();
        factory.parse("BOX", scan("0 0 5 5 3"));
        Board board = factory.build(100, 100);
        assertEquals(1, board.movables().size());
        assertEquals(1, board.entities().size());
        assertSame(board.movables().get(0), board.entities().get(0));
        assertInstanceOf(Box.class, board.entities().get(0));
    }

    @Test
    void poolAndGemReadElementBeforeCoordinates() {
        EntityFactory factory = new EntityFactory();
        factory.parse("POOL", scan("FIRE 10 10 5 5"));
        factory.parse("GEM", scan("WATER 20 20 3 3"));
        Board board = factory.build(100, 100);
        assertEquals(ElementState.FIRE, board.pools().get(0).getElement());
        assertEquals(ElementState.WATER, board.gems().get(0).getElement());
    }

    @Test
    void boostPlatformAliasesAllProduceBoostPlatforms() {
        for (String alias : new String[] { "BOOST_PLATFORM", "GRAVITY_POTION", "JUMP_PAD" }) {
            EntityFactory factory = new EntityFactory();
            factory.parse(alias, scan("0 0 5 1"));
            Board board = factory.build(100, 100);
            assertEquals(1, board.entities().size(),
                    alias + " should produce one entity");
            StaticEntity e = board.entities().get(0);
            assertFalse(e instanceof Pool || e instanceof Gem || e instanceof Door || e instanceof Box,
                    alias + " should produce a BoostPlatform, not another type");
        }
    }
}
