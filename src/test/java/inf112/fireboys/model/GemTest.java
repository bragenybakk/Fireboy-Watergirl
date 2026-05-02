package inf112.fireboys.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.entity.Gem;
import inf112.fireboys.model.entity.StaticEntity;
import inf112.fireboys.model.entity.Wall;
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

    private GameModel makeModel(List<Player> players, List<Gem> gems) {
        List<StaticEntity> entities = new ArrayList<>();
        entities.add(new Wall(new Position(0, 95), 100, 5));
        entities.addAll(gems);
        Board board = new Board(100, 100, players, entities, new ArrayList<>(),
                List.of(), List.of(), gems, List.of(), List.of(), List.of());
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        return model;
    }

    @Test
    void collectedGemsStartsAtZero() {
        GameModel model = makeModel(List.of(fireboy), List.of(fireGem));
        assertEquals(0, model.getCollectedGems());
    }

    @Test
    void collectingGemIncreasesCount() {
        Player player = new Player(new Position(0, 0), ElementState.FIRE);
        Gem gem = new Gem(new Position(0, 0), 5, 5, ElementState.FIRE);
        GameModel model = makeModel(List.of(player), List.of(gem));
        model.clockTick();
        assertEquals(1, model.getCollectedGems());
    }

    @Test
    void collectingSameGemTwiceCountsOnce() {
        Player player = new Player(new Position(0, 0), ElementState.FIRE);
        Gem gem = new Gem(new Position(0, 0), 5, 5, ElementState.FIRE);
        GameModel model = makeModel(List.of(player), List.of(gem));
        model.clockTick();
        model.clockTick();
        assertEquals(1, model.getCollectedGems());
    }

    @Test
    void collectingTwoGemsCountsBoth() {
        Player boy = new Player(new Position(0, 0), ElementState.FIRE);
        Player girl = new Player(new Position(10, 0), ElementState.WATER);
        Gem fGem = new Gem(new Position(0, 0), 5, 5, ElementState.FIRE);
        Gem wGem = new Gem(new Position(10, 0), 5, 5, ElementState.WATER);
        GameModel model = makeModel(List.of(boy, girl), List.of(fGem, wGem));
        model.clockTick();
        assertEquals(2, model.getCollectedGems());
    }
}
