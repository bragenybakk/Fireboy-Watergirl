package inf112.fireboys.controller;

import inf112.fireboys.model.ElementState;
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
        assertEquals(1, fakeModel.moveLeftWaterCount);
        assertEquals(1, fakeModel.moveRightWaterCount);
        assertEquals(1, fakeModel.moveLeftFireCount);
        assertEquals(1, fakeModel.moveRightFireCount);
    }

    @Test
    void movementFlagsResetWhenNotPlaying() {
        fakeModel.gameState = GameState.PLAYING;
        controller.keyPressed(keyPress(KeyEvent.VK_LEFT));
        fakeModel.gameState = GameState.PAUSED;
        controller.triggerUpdate();
        fakeModel.gameState = GameState.PLAYING;
        controller.triggerUpdate();
        assertEquals(0, fakeModel.moveLeftWaterCount);
    }

    @Test
    void releasingMoveKeyStopsPlayer() {
        fakeModel.gameState = GameState.PLAYING;
        controller.keyPressed(keyPress(KeyEvent.VK_LEFT));
        controller.keyReleased(keyRelease(KeyEvent.VK_LEFT));
        assertEquals(1, fakeModel.stopWaterCount);
    }

    @Test
    void holdingBothKeysDoesNotStopPlayer() {
        fakeModel.gameState = GameState.PLAYING;
        controller.keyPressed(keyPress(KeyEvent.VK_LEFT));
        controller.keyPressed(keyPress(KeyEvent.VK_RIGHT));
        controller.keyReleased(keyRelease(KeyEvent.VK_LEFT));
        assertEquals(0, fakeModel.stopWaterCount);
    }

    @Test
    void releasingP2MoveKeyStopsPlayer2() {
        fakeModel.gameState = GameState.PLAYING;
        controller.keyPressed(keyPress(KeyEvent.VK_A));
        controller.keyReleased(keyRelease(KeyEvent.VK_A));
        assertEquals(1, fakeModel.stopFireCount);
    }

    @Test
    void holdingBothP2KeysDoesNotStopPlayer2() {
        fakeModel.gameState = GameState.PLAYING;
        controller.keyPressed(keyPress(KeyEvent.VK_A));
        controller.keyPressed(keyPress(KeyEvent.VK_D));
        controller.keyReleased(keyRelease(KeyEvent.VK_A));
        assertEquals(0, fakeModel.stopFireCount);
    }

    @Test
    void keyReleaseIgnoredWhenNotPlaying() {
        fakeModel.gameState = GameState.PAUSED;
        controller.keyReleased(keyRelease(KeyEvent.VK_LEFT));
        assertEquals(0, fakeModel.stopWaterCount);
    }

    @Test
    void jumpKeysCallJumpMethods() {
        fakeModel.gameState = GameState.PLAYING;
        controller.keyPressed(keyPress(KeyEvent.VK_UP));
        controller.keyPressed(keyPress(KeyEvent.VK_W));
        assertEquals(1, fakeModel.jumpWaterCount);
        assertEquals(1, fakeModel.jumpFireCount);
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
        int moveLeftWaterCount = 0;
        int moveLeftFireCount = 0;
        int moveRightWaterCount = 0;
        int moveRightFireCount = 0;
        int stopWaterCount = 0;
        int stopFireCount = 0;
        int jumpWaterCount = 0;
        int jumpFireCount = 0;

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
        public void moveLeft(ElementState element) {
            if (element == ElementState.WATER) moveLeftWaterCount++;
            else moveLeftFireCount++;
        }

        @Override
        public void moveRight(ElementState element) {
            if (element == ElementState.WATER) moveRightWaterCount++;
            else moveRightFireCount++;
        }

        @Override
        public void stop(ElementState element) {
            if (element == ElementState.WATER) stopWaterCount++;
            else stopFireCount++;
        }

        @Override
        public void jump(ElementState element) {
            if (element == ElementState.WATER) jumpWaterCount++;
            else jumpFireCount++;
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
