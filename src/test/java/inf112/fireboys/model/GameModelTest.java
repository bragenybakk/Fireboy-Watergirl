package inf112.fireboys.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.coordinateSystem.Position;

public class GameModelTest {
    private Board readGameEasy() {
        GameReader reader = new GameReader();
        try {
            Board board = reader.loadLevel("src/test/resources/easy.txt");
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
    void testGameModelInitialization() {
        GameModel model = new GameModel();
        assertNotNull(model);
        // Add more assertions to check the initial state of the model
    }

    @Test
    void testPlayerMovement() {
        GameModel model = new GameModel();
        // Simulate player movement and assert the expected outcomes
    }

    @Test
    void testCollisionDetection() {
        GameModel model = new GameModel();
        // Simulate collisions and assert the expected outcomes
    }

    @Test
    void testLevelCompletion() {
        GameModel model = new GameModel();
        // Simulate level completion and assert the expected outcomes
    }

    @Test
    void testGameOverCondition() {
        GameModel model = new GameModel();
        // Simulate game over condition and assert the expected outcomes
    }
}
