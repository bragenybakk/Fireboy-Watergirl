package inf112.fireboys.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.entity.BoostPlatform;
import inf112.fireboys.model.entity.StaticEntity;
import inf112.fireboys.model.entity.Wall;
import inf112.fireboys.model.player.Player;

public class BoostPlatformTest {
    private static final Position PLAYER_POS = new Position(12, 20);
    private static final Position PLATFORM_POS = new Position(10, 28);
    private Player fireboy;
    private Player watergirl;

    @BeforeEach
    void setUp() {
        fireboy = new Player(PLAYER_POS, ElementState.FIRE);
        watergirl = new Player(PLAYER_POS, ElementState.WATER);
    }

    private GameModel modelWith(Player p, StaticEntity... entities) {
        ArrayList<Player> players = new ArrayList<>();
        players.add(p);
        ArrayList<StaticEntity> ents = new ArrayList<>();
        for (StaticEntity e : entities) ents.add(e);
        Board board = new Board(50, 50, players, ents, new ArrayList<>());
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        return model;
    }

    @Test
    void contactAloneDoesNotGrantBoost() {
        BoostPlatform platform = new BoostPlatform(PLATFORM_POS, 8, 2);
        platform.whenContact(fireboy);
        platform.whenContact(watergirl);
        assertFalse(fireboy.hasJumpBoost());
        assertFalse(watergirl.hasJumpBoost());
    }

    @Test
    void noBoostFromSideHit() {
        BoostPlatform platform = new BoostPlatform(PLATFORM_POS, 8, 2);
        Player side = new Player(new Position(20, 21), ElementState.FIRE);
        platform.whenContact(side);
        assertFalse(side.hasJumpBoost());
    }

    @Test
    void standingOnPlateSetsChargeReady() {
        Player p = new Player(PLAYER_POS, ElementState.FIRE);
        p.setOnGroundTRUE();
        GameModel model = modelWith(p, new BoostPlatform(PLATFORM_POS, 8, 2));
        assertTrue(model.isPlayerOnBoostPlateWithoutCharge());
    }

    @Test
    void jumpingOnPlateChargesBoostWithoutMoving() {
        Player p = new Player(PLAYER_POS, ElementState.FIRE);
        p.setOnGroundTRUE();
        GameModel model = modelWith(p, new BoostPlatform(PLATFORM_POS, 8, 2));
        model.jump(ElementState.FIRE);
        assertTrue(p.hasJumpBoost());
        assertEquals(0.0, p.getVelocityY(), 0.0001);
    }

    @Test
    void jumpingOffPlateUsesBoostAndConsumesIt() {
        Player p = new Player(PLAYER_POS, ElementState.FIRE);
        p.setOnGroundTRUE();
        GameModel model = modelWith(p,
                new Wall(new Position(0, 30), 50, 8),
                new BoostPlatform(PLATFORM_POS, 10, 2));
        model.jump(ElementState.FIRE);
        assertTrue(p.hasJumpBoost());
        p.setPos(new Position(5, 22));
        p.setOnGroundTRUE();
        double normalImpulse = new Player(new Position(0, 0), ElementState.FIRE).getJumpImpulse();
        model.jump(ElementState.FIRE);
        assertTrue(p.getVelocityY() < normalImpulse - 0.01);
        assertFalse(p.hasJumpBoost());
    }

    @Test
    void canRechargeOnPlateAfterBoostUsed() {
        Player p = new Player(PLAYER_POS, ElementState.FIRE);
        p.setOnGroundTRUE();
        GameModel model = modelWith(p,
                new Wall(new Position(0, 30), 50, 8),
                new BoostPlatform(PLATFORM_POS, 10, 2));
        model.jump(ElementState.FIRE);
        p.setPos(new Position(5, 22));
        p.setOnGroundTRUE();
        model.jump(ElementState.FIRE);
        assertFalse(p.hasJumpBoost());
        p.setVelocityY(0);
        p.setPos(PLAYER_POS);
        p.setOnGroundTRUE();
        model.jump(ElementState.FIRE);
        assertTrue(p.hasJumpBoost());
        assertEquals(0.0, p.getVelocityY(), 0.0001);
    }

    @Test
    void boostedJumpImpulseIs1Point5xNormal() {
        double normal = fireboy.getJumpImpulse();
        fireboy.grantJumpBoost();
        assertEquals(normal * 1.5, fireboy.getJumpImpulse(), 0.001);
    }

    @Test
    void boostConsumedAfterJump() {
        fireboy.grantJumpBoost();
        fireboy.consumeJumpBoost();
        assertFalse(fireboy.hasJumpBoost());
        assertEquals(fireboy.getJumpImpulse(), new Player(PLAYER_POS, ElementState.FIRE).getJumpImpulse());
    }
}
