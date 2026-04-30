package inf112.fireboys.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.coordinateSystem.Position;

public class GameStateNavigationTest {
    private Board readGameEasy() {
        try {
            return GameReader.loadLevel("easy.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Board readGemGateLevel() {
        try {
            return GameReader.loadLevel("gem_gate.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // ============ Main Menu Tests ============

    @Test
    void testMainMenuSelectStartGame() {
        GameModel model = new GameModel();
        model.menuSelect();
        assertEquals(GameState.LEVEL_SELECT, model.getGameState(),
                "Selecting START GAME should go to LEVEL_SELECT");
    }

    @Test
    void testMainMenuSelectSettings() {
        GameModel model = new GameModel();
        model.menuDown();
        model.menuDown();
        model.menuSelect();
        assertEquals(GameState.SETTINGS, model.getGameState(),
                "Selecting Settings should go to SETTINGS");
    }

    @Test
    void testMenuNavigationStaysInBounds() {
        GameModel model = new GameModel();
        model.menuUp();
        assertEquals(0, model.getSelectedMenuOption(),
                "Selection should not go below 0");
        model.menuDown();
        model.menuDown();
        model.menuDown();
        model.menuDown();
        assertEquals(3, model.getSelectedMenuOption(),
                "Selection should not exceed max index");
    }
    // ============ Pause Screen Tests ============

    @Test
    void testPauseResumeGoesBackToPlaying() {
        Board board = readGameEasy();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        model.setGameState(GameState.PAUSED);
        model.menuSelect();
        assertEquals(GameState.PLAYING, model.getGameState(),
                "Selecting Resume should go back to PLAYING");
    }

    @Test
    void testPauseMainMenuGoesBackToMainMenu() {
        Board board = readGameEasy();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        model.setGameState(GameState.PAUSED);
        model.menuDown();
        model.menuSelect();
        assertEquals(GameState.MAIN_MENU, model.getGameState(),
                "Selecting Main Menu should go back to MAIN_MENU");
    }

    @Test
    void testClockTickDoesNothingWhenPaused() {
        Board board = readGameEasy();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        Position posBeforePause = model.getPlayers().get(0).getPos();
        model.setGameState(GameState.PAUSED);
        model.clockTick();
        assertEquals(posBeforePause, model.getPlayers().get(0).getPos(),
                "Player position should not change while paused");
    }
    // ============ Death / Game Over Screen Tests ============

    @Test
    void testGameOverOnEnemyContact() {
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
    void testGameOverSelectMainMenu() {
        Board board = readGameEasy();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.GAME_OVER);
        model.menuDown();
        model.menuSelect();
        assertEquals(GameState.MAIN_MENU, model.getGameState(),
                "Selecting Main Menu should go to MAIN_MENU");
    }

    @Test
    void testGameOverRespawnResetsLevel() {
        GameModel model = new GameModel();
        model.loadLevel("level1.txt");
        model.setGameState(GameState.PLAYING);
        Position startPos = model.getPlayers().get(0).getPos();
        model.movePlayerRight();
        model.clockTick();
        assertNotEquals(startPos, model.getPlayers().get(0).getPos(),
                "Player should have moved from start position");
        model.setGameState(GameState.GAME_OVER);
        model.menuSelect();
        assertEquals(GameState.PLAYING, model.getGameState(),
                "Game state should be PLAYING after respawn");
        assertEquals(startPos, model.getPlayers().get(0).getPos(),
                "Player should be back at start position after respawn");
    }

    @Test
    void testClockTickDoesNothingWhenGameOver() {
        Board board = readGameEasy();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        model.getPlayers().get(0).setPos(new Position(0, 0));
        model.clockTick();
        Position posAfterDeath = model.getPlayers().get(0).getPos();
        model.clockTick();
        assertEquals(posAfterDeath, model.getPlayers().get(0).getPos(),
                "Player position should not change during GAME_OVER");
    }
    // ============ Level Select Tests ============

    @Test
    void testLevelSelectLoadsLevelNames() {
        GameModel model = new GameModel();
        model.setGameState(GameState.LEVEL_SELECT);
        assertNotNull(model.getLevelNames(),
                "Level names should not be null after entering LEVEL_SELECT");
    }

    @Test
    void testLevelsUnlockInOrder() {
        GameModel model = new GameModel();
        model.setGameState(GameState.LEVEL_SELECT);
        model.menuDown();
        assertEquals(1, model.getSelectedMenuOption(),
                "All levels should be selectable");
        model.loadLevel("level1.txt");
        model.getPlayers().get(0).setPos(new Position(15, 105));
        model.clockTick();
        model.getPlayers().get(0).setPos(new Position(15, 75));
        model.clockTick();
        model.getPlayers().get(0).setPos(new Position(45, 45));
        model.clockTick();
        model.getPlayers().get(0).setPos(new Position(43, 90));
        model.clockTick();
        model.getPlayers().get(1).setPos(new Position(55, 105));
        model.clockTick();
        model.getPlayers().get(1).setPos(new Position(56, 90));
        model.clockTick();
        model.getPlayers().get(1).setPos(new Position(85, 75));
        model.clockTick();
        model.getPlayers().get(1).setPos(new Position(62, 45));
        model.clockTick();
        for (int i = 0; i < 100; i++) {
            model.getPlayers().get(0).setPos(new Position(34, 45));
            model.getPlayers().get(1).setPos(new Position(52, 45));
            model.getPlayers().get(0).setVelocityY(0);
            model.getPlayers().get(1).setVelocityY(0);
            model.clockTick();
        }
        assertEquals(GameState.LEVEL_SELECT, model.getGameState(),
                "Completing level 1 should return to LEVEL_SELECT");
        model.menuDown();
        assertEquals(1, model.getSelectedMenuOption(),
                "Level 2 should be unlocked after finishing level 1");
    }

    @Test
    void testDoorRequiresAllGemsCollected() {
        Board board = readGemGateLevel();
        GameModel model = new GameModel(board);
        model.setGameState(GameState.PLAYING);
        model.getPlayers().get(0).setPos(new Position(80, 78));
        model.clockTick();
        assertEquals(GameState.PLAYING, model.getGameState(),
                "Door should stay locked until all gems are collected");
        model.getPlayers().get(0).setPos(new Position(40, 84));
        model.clockTick();
        for (int i = 0; i < 70; i++) {
            model.getPlayers().get(0).setPos(new Position(80, 78));
            model.getPlayers().get(0).setVelocityY(0);
            model.clockTick();
        }
        assertEquals(GameState.LEVEL_SELECT, model.getGameState(),
                "After collecting all gems, door should complete the level");
    }
}
