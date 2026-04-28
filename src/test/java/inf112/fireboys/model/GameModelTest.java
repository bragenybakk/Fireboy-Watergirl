package inf112.fireboys.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.player.Player;

public class GameModelTest {
    private Board readGameEasy() {
        try {
            return GameReader.loadLevel("easy.txt");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Board readGameFalling() {
        try {
            return GameReader.loadLevel("falling.txt");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Board readTwoPlayers() {
        try {
            return GameReader.loadLevel("two_players.txt");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
    void testStopPlayer() {
        Board board = readGameEasy();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        model.movePlayerLeft();
        model.stopPlayer();
        assertEquals(0.0, model.getPlayers().get(0).getVelocityX(), 0.001);
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

    @Test
    void testGetGemsOnBoardWithoutGems() {
        Board board = readGameEasy();
        GameModel model = new GameModel(board);
        assertEquals(0, model.getTotalGems());
        assertEquals(0, model.getCollectedGems());
    }

    @Test
    void testToggleAdsBlocked() {
        GameModel model = new GameModel();
        assertFalse(model.isAdsBlocked());
        model.toggleAdsBlocked();
        assertTrue(model.isAdsBlocked());
    }

    @Test
    void testToggleMusicEnabled() {
        GameModel model = new GameModel();
        assertTrue(model.isMusicEnabled());
        model.toggleMusicEnabled();
        assertFalse(model.isMusicEnabled());
    }

    @Test
    void testToggleSoundEnabled() {
        GameModel model = new GameModel();
        assertTrue(model.isSoundEnabled());
        model.toggleSoundEnabled();
        assertFalse(model.isSoundEnabled());
    }

    @Test
    void testMenuOptionsPaused() {
        GameModel model = new GameModel();
        model.setGameState(GameState.PAUSED);
        assertEquals(2, model.getMenuOptions().length);
    }

    @Test
    void testMenuOptionsGameOver() {
        GameModel model = new GameModel();
        model.setGameState(GameState.GAME_OVER);
        assertEquals(2, model.getMenuOptions().length);
    }

    @Test
    void testMenuOptionsSettings() {
        GameModel model = new GameModel();
        model.setGameState(GameState.SETTINGS);
        assertEquals(4, model.getMenuOptions().length);
    }

    @Test
    void testSettingsMenuToggleMusic() {
        GameModel model = new GameModel();
        model.setGameState(GameState.SETTINGS);
        assertTrue(model.isMusicEnabled());
        model.menuSelect(); // option 0 = Music
        assertFalse(model.isMusicEnabled());
    }

    @Test
    void testSettingsMenuBack() {
        GameModel model = new GameModel();
        model.setGameState(GameState.SETTINGS);
        model.menuDown();
        model.menuDown();
        model.menuDown(); // option 3 = Back
        model.menuSelect();
        assertEquals(GameState.MAIN_MENU, model.getGameState());
    }

    @Test
    void testLevelSelectLoadsLevel() {
        GameModel model = new GameModel();
        model.setGameState(GameState.LEVEL_SELECT);
        model.menuSelect(); // selects first unlocked level
        assertEquals(GameState.PLAYING, model.getGameState());
    }

    @Test
    void testGetWinFadeProgressInitial() {
        GameModel model = new GameModel();
        assertEquals(0.0, model.getWinFadeProgress(), 0.001);
    }

    @Test
    void testGetLevelNames() {
        GameModel model = new GameModel();
        assertNotNull(model.getLevelNames());
        assertFalse(model.getLevelNames().isEmpty());
    }

    @Test
    void testPlayer2MoveLeft() {
        Board board = readTwoPlayers();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        Player fireboy = model.getPlayers().get(1);
        Position initialPos = fireboy.getPos();
        model.movePlayer2Left();
        model.clockTick();
        assertTrue(fireboy.getPos().x() < initialPos.x());
    }

    @Test
    void testPlayer2MoveRight() {
        Board board = readTwoPlayers();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        Player fireboy = model.getPlayers().get(1);
        Position initialPos = fireboy.getPos();
        model.movePlayer2Right();
        model.clockTick();
        assertTrue(fireboy.getPos().x() > initialPos.x());
    }

    @Test
    void testStopPlayer2() {
        Board board = readTwoPlayers();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        Player fireboy = model.getPlayers().get(1);
        model.movePlayer2Left();
        model.stopPlayer2();
        assertEquals(0.0, fireboy.getVelocityX(), 0.001);
    }

    @Test
    void testPlayer2Jump() {
        Board board = readTwoPlayers();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        Player fireboy = model.getPlayers().get(1);
        for (int i = 0; i < 30; i++) {
            model.clockTick();
        }
        Position posBeforeJump = fireboy.getPos();
        model.player2Jump();
        for (int i = 0; i < 10; i++) {
            model.clockTick();
        }
        assertTrue(fireboy.getPos().y() != posBeforeJump.y());
    }
}
