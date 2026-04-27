package inf112.fireboys.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.coordinateSystem.Position;

public class GameModelTest {
    private Board readGameEasy() {
        try {
            Board board = GameReader.loadLevel("easy.txt");
            return board;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Board readGameFalling() {
        try {
            Board board = GameReader.loadLevel("falling.txt");
            return board;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    void testLoadLevel() {
        Board board = readGameEasy();
        assertNotNull(board, "Board should not be null");
        assertEquals(1, board.players().size(), "There should be one player on the board");
        assertEquals(2, board.entities().size(), "There should be two entities on the board");
    }

    @Test
    void testPlayerJump() {
        Board board = readGameEasy();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        Position initialPosition = model.getPlayers().get(0).getPos();
        model.playerJump();
        for (int i = 0; i < 10; i++) {
            model.clockTick();
        }
        assertTrue(model.getPlayers().get(0).getPos().y() != initialPosition.y(), "Player should have jumped");
    }

    @Test
    void testPlayerMoveLeft() {
        Board board = readGameEasy();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        Position initialPosition = model.getPlayers().get(0).getPos();
        model.movePlayerLeft();
        model.clockTick();
        assertTrue(model.getPlayers().get(0).getPos().x() < initialPosition.x(), "Player should have moved left");
    }

    @Test
    void testPlayerMoveRight() {
        Board board = readGameEasy();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        Position initialPosition = model.getPlayers().get(0).getPos();
        model.movePlayerRight();
        model.clockTick();
        assertTrue(model.getPlayers().get(0).getPos().x() > initialPosition.x(), "Player should have moved right");
    }

    @Test
    void testFalling() {
        Board board = readGameFalling();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        Position initialPosition = model.getPlayers().get(0).getPos();
        for (int i = 0; i < 10; i++) {
            model.clockTick();
        }
        assertTrue(model.getPlayers().get(0).getPos().y() > initialPosition.y(), "Player should have fallen");
        for (int i = 0; i < 19; i++) {
            model.clockTick();
        }
        double playerBottom = model.getPlayers().get(0).getPos().y() + model.getPlayers().get(0).getHeight();
        double platformTop = model.getStaticEntities().get(0).getPos().y();
        assertTrue(Math.abs(playerBottom - platformTop) < 0.1,
                "Player should land on platform");
    }

    @Test
    void testPlayerDoorInteraction() {
        Board board = readGameEasy();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        for (int i = 0; i < 70; i++) {
            model.getPlayers().get(0).setPos(new Position(80, 80));
            model.getPlayers().get(0).setVelocityY(0);
            model.clockTick();
        }
        assertEquals(GameState.LEVEL_SELECT, model.getGameState(),
                "Game state should be LEVEL_SELECT after completing the level");
    }

    @Test
    void testMeetingEnemy() {
        Board board = readGameEasy();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        model.getPlayers().get(0).setPos(new Position(0, 0));
        model.clockTick();
        assertEquals(GameState.GAME_OVER, model.getGameState(),
                "Game state should be GAME_OVER after player meets enemy");
        assertFalse(model.getPlayers().get(0).isAlive(),
                "Player should not be alive after meeting enemy");
    }
}
