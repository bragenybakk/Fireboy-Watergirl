package inf112.fireboys.controller;

import inf112.fireboys.model.GameState;
import inf112.fireboys.view.AudioManager;
import inf112.fireboys.view.ControllableGameView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JLabel;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    private FakeGameModel fakeModel;
    private FakeAudioManager fakeAudio;
    private GameController controller;

    @BeforeEach
    void setUp() {
        fakeModel = new FakeGameModel();
        fakeAudio = new FakeAudioManager();
        controller = new GameController(fakeModel, new FakeGameView(), fakeAudio);
        controller.stopTimer();
    }

    private KeyEvent keyPress(int keyCode) {
        return new KeyEvent(new JLabel(), KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);
    }

    private KeyEvent keyRelease(int keyCode) {
        return new KeyEvent(new JLabel(), KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);
    }

    @Test
    void clockTickCalledWhenPlaying() {
        fakeModel.gameState = GameState.PLAYING;
        controller.triggerUpdate();
        assertEquals(1, fakeModel.clockTickCount);
    }

    @Test
    void clockTickNotCalledWhenPaused() {
        fakeModel.gameState = GameState.PAUSED;
        controller.triggerUpdate();
        assertEquals(0, fakeModel.clockTickCount);
    }

    @Test
    void movementKeysDispatchMoveCommandsToModel() {
        fakeModel.gameState = GameState.PLAYING;
        controller.keyPressed(keyPress(KeyEvent.VK_LEFT));
        controller.keyPressed(keyPress(KeyEvent.VK_RIGHT));
        controller.keyPressed(keyPress(KeyEvent.VK_A));
        controller.keyPressed(keyPress(KeyEvent.VK_D));
        controller.triggerUpdate();
        assertEquals(1, fakeModel.movePlayerLeftCount);
        assertEquals(1, fakeModel.movePlayerRightCount);
        assertEquals(1, fakeModel.movePlayer2LeftCount);
        assertEquals(1, fakeModel.movePlayer2RightCount);
    }

    @Test
    void movementFlagsResetWhenNotPlaying() {
        fakeModel.gameState = GameState.PLAYING;
        controller.keyPressed(keyPress(KeyEvent.VK_LEFT));
        fakeModel.gameState = GameState.PAUSED;
        controller.triggerUpdate();
        fakeModel.gameState = GameState.PLAYING;
        controller.triggerUpdate();
        assertEquals(0, fakeModel.movePlayerLeftCount);
    }

    @Test
    void releasingMoveKeyStopsPlayer() {
        fakeModel.gameState = GameState.PLAYING;
        controller.keyPressed(keyPress(KeyEvent.VK_LEFT));
        controller.keyReleased(keyRelease(KeyEvent.VK_LEFT));
        assertEquals(1, fakeModel.stopPlayerCount);
    }

    @Test
    void holdingBothKeysDoesNotStopPlayer() {
        fakeModel.gameState = GameState.PLAYING;
        controller.keyPressed(keyPress(KeyEvent.VK_LEFT));
        controller.keyPressed(keyPress(KeyEvent.VK_RIGHT));
        controller.keyReleased(keyRelease(KeyEvent.VK_LEFT));
        assertEquals(0, fakeModel.stopPlayerCount);
    }

    @Test
    void releasingP2MoveKeyStopsPlayer2() {
        fakeModel.gameState = GameState.PLAYING;
        controller.keyPressed(keyPress(KeyEvent.VK_A));
        controller.keyReleased(keyRelease(KeyEvent.VK_A));
        assertEquals(1, fakeModel.stopPlayer2Count);
    }

    @Test
    void holdingBothP2KeysDoesNotStopPlayer2() {
        fakeModel.gameState = GameState.PLAYING;
        controller.keyPressed(keyPress(KeyEvent.VK_A));
        controller.keyPressed(keyPress(KeyEvent.VK_D));
        controller.keyReleased(keyRelease(KeyEvent.VK_A));
        assertEquals(0, fakeModel.stopPlayer2Count);
    }

    @Test
    void keyReleaseIgnoredWhenNotPlaying() {
        fakeModel.gameState = GameState.PAUSED;
        controller.keyReleased(keyRelease(KeyEvent.VK_LEFT));
        assertEquals(0, fakeModel.stopPlayerCount);
    }

    @Test
    void jumpKeysCallJumpMethods() {
        fakeModel.gameState = GameState.PLAYING;
        controller.keyPressed(keyPress(KeyEvent.VK_UP));
        controller.keyPressed(keyPress(KeyEvent.VK_W));
        assertEquals(1, fakeModel.playerJumpCount);
        assertEquals(1, fakeModel.player2JumpCount);
    }

    @Test
    void upAndDownKeysNavigateMenu() {
        fakeModel.gameState = GameState.MAIN_MENU;
        controller.keyPressed(keyPress(KeyEvent.VK_UP));
        controller.keyPressed(keyPress(KeyEvent.VK_DOWN));
        assertEquals(1, fakeModel.menuUpCount);
        assertEquals(1, fakeModel.menuDownCount);
    }

    @Test
    void enterSelectsMenuOption() {
        fakeModel.gameState = GameState.MAIN_MENU;
        controller.keyPressed(keyPress(KeyEvent.VK_ENTER));
        assertEquals(1, fakeModel.menuSelectCount);
    }

    @Test
    void menuNavigationWorksWhilePaused() {
        fakeModel.gameState = GameState.PAUSED;
        controller.keyPressed(keyPress(KeyEvent.VK_UP));
        controller.keyPressed(keyPress(KeyEvent.VK_DOWN));
        assertEquals(1, fakeModel.menuUpCount);
        assertEquals(1, fakeModel.menuDownCount);
    }

    @Test
    void menuNavigationWorksOnGameOver() {
        fakeModel.gameState = GameState.GAME_OVER;
        controller.keyPressed(keyPress(KeyEvent.VK_UP));
        controller.keyPressed(keyPress(KeyEvent.VK_DOWN));
        controller.keyPressed(keyPress(KeyEvent.VK_ENTER));
        assertEquals(1, fakeModel.menuUpCount);
        assertEquals(1, fakeModel.menuDownCount);
        assertEquals(1, fakeModel.menuSelectCount);
    }

    @Test
    void escapeFromSubMenuGoesToMainMenu() {
        fakeModel.gameState = GameState.SETTINGS;
        controller.keyPressed(keyPress(KeyEvent.VK_ESCAPE));
        assertEquals(GameState.MAIN_MENU, fakeModel.lastSetGameState);
    }

    @Test
    void escapeFromMainMenuDoesNothing() {
        fakeModel.gameState = GameState.MAIN_MENU;
        controller.keyPressed(keyPress(KeyEvent.VK_ESCAPE));
        assertNull(fakeModel.lastSetGameState);
    }

    @Test
    void escapeWhilePlayingPausesGame() {
        fakeModel.gameState = GameState.PLAYING;
        controller.keyPressed(keyPress(KeyEvent.VK_ESCAPE));
        assertEquals(GameState.PAUSED, fakeModel.lastSetGameState);
    }

    @Test
    void escapeWhilePausedResumesGame() {
        fakeModel.gameState = GameState.PAUSED;
        controller.keyPressed(keyPress(KeyEvent.VK_ESCAPE));
        assertEquals(GameState.PLAYING, fakeModel.lastSetGameState);
    }

    @Test
    void playsBlippSoundWhenTransitioningToGameOver() {
        fakeModel.gameState = GameState.PLAYING;
        controller.triggerUpdate();
        fakeModel.gameState = GameState.GAME_OVER;
        controller.triggerUpdate();
        assertEquals("/blipp.ogg", fakeAudio.lastPlayedSound);
        assertEquals(1, fakeAudio.playSoundCount);
    }

    @Test
    void blippSoundNotPlayedOnSubsequentGameOverTicks() {
        fakeModel.gameState = GameState.PLAYING;
        controller.triggerUpdate();
        fakeModel.gameState = GameState.GAME_OVER;
        controller.triggerUpdate();
        controller.triggerUpdate();
        assertEquals(1, fakeAudio.playSoundCount);
    }

    @Test
    void syncAudioSetsSoundEnabledFromModel() {
        fakeModel.gameState = GameState.MAIN_MENU;
        fakeModel.soundEnabled = false;
        fakeModel.musicEnabled = false;
        fakeAudio.musicEnabled = false;
        controller.keyPressed(keyPress(KeyEvent.VK_ENTER));
        assertEquals(false, fakeAudio.soundEnabledValue);
    }

    @Test
    void syncAudioRestartsMusic() {
        fakeModel.gameState = GameState.MAIN_MENU;
        fakeModel.musicEnabled = true;
        fakeAudio.musicEnabled = false;
        controller.keyPressed(keyPress(KeyEvent.VK_ENTER));
        assertEquals(1, fakeAudio.restartMusicCount);
        assertEquals(0, fakeAudio.stopCount);
    }

    @Test
    void syncAudioStopsMusic() {
        fakeModel.gameState = GameState.MAIN_MENU;
        fakeModel.musicEnabled = false;
        fakeAudio.musicEnabled = true;
        controller.keyPressed(keyPress(KeyEvent.VK_ENTER));
        assertEquals(1, fakeAudio.stopCount);
        assertEquals(0, fakeAudio.restartMusicCount);
    }

    // Fake implementations used instead of mocks

    static class FakeGameModel implements ControllableGameModel {
        GameState gameState = GameState.MAIN_MENU;
        boolean soundEnabled = true;
        boolean musicEnabled = false;
        GameState lastSetGameState = null;

        int clockTickCount = 0;
        int menuUpCount = 0;
        int menuDownCount = 0;
        int menuSelectCount = 0;
        int movePlayerLeftCount = 0;
        int movePlayerRightCount = 0;
        int stopPlayerCount = 0;
        int playerJumpCount = 0;
        int movePlayer2LeftCount = 0;
        int movePlayer2RightCount = 0;
        int stopPlayer2Count = 0;
        int player2JumpCount = 0;

        @Override
        public GameState getGameState() {
            return gameState;
        }

        @Override
        public void setGameState(GameState state) {
            lastSetGameState = state;
        }

        @Override
        public void clockTick() {
            clockTickCount++;
        }

        @Override
        public void menuUp() {
            menuUpCount++;
        }

        @Override
        public void menuDown() {
            menuDownCount++;
        }

        @Override
        public void menuSelect() {
            menuSelectCount++;
        }

        @Override
        public void movePlayerLeft() {
            movePlayerLeftCount++;
        }

        @Override
        public void movePlayerRight() {
            movePlayerRightCount++;
        }

        @Override
        public void stopPlayer() {
            stopPlayerCount++;
        }

        @Override
        public void playerJump() {
            playerJumpCount++;
        }

        @Override
        public void movePlayer2Left() {
            movePlayer2LeftCount++;
        }

        @Override
        public void movePlayer2Right() {
            movePlayer2RightCount++;
        }

        @Override
        public void stopPlayer2() {
            stopPlayer2Count++;
        }

        @Override
        public void player2Jump() {
            player2JumpCount++;
        }

        @Override
        public boolean isSoundEnabled() {
            return soundEnabled;
        }

        @Override
        public boolean isMusicEnabled() {
            return musicEnabled;
        }

        @Override
        public void toggleAdsBlocked() {}

        @Override
        public void toggleMusicEnabled() {}

        @Override
        public void toggleSoundEnabled() {}
    }

    static class FakeGameView implements ControllableGameView {
        @Override
        public void setFocusable(boolean focusable) {}

        @Override
        public void addKeyListener(KeyListener l) {}

        @Override
        public void addMouseListener(MouseListener l) {}

        @Override
        public boolean requestFocusInWindow() {
            return false;
        }

        @Override
        public void repaint() {}

        @Override
        public boolean isAdBlockToggleClicked(Point point) {
            return false;
        }
    }

    static class FakeAudioManager extends AudioManager {
        int playSoundCount = 0;
        String lastPlayedSound = null;
        boolean soundEnabledValue = true;
        int restartMusicCount = 0;
        int stopCount = 0;
        boolean musicEnabled = false;

        @Override
        public void playSound(String path) {
            playSoundCount++;
            lastPlayedSound = path;
        }

        @Override
        public void setSoundEnabled(boolean enabled) {
            soundEnabledValue = enabled;
        }

        @Override
        public void restartMusic() {
            restartMusicCount++;
        }

        @Override
        public void stop() {
            stopCount++;
        }

        @Override
        public boolean isMusicEnabled() {
            return musicEnabled;
        }
    }
}
