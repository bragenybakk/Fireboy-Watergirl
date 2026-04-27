package inf112.fireboys.controller;

import inf112.fireboys.model.GameState;
import inf112.fireboys.view.AudioManager;
import inf112.fireboys.view.GameView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GameControllerTest {

    private ControllableGameModel gameModel;
    private GameView gameView;
    private AudioManager audioManager;
    private GameController controller;

    @BeforeEach
    void setUp() {
        gameModel = mock(ControllableGameModel.class);
        gameView = mock(GameView.class);
        audioManager = mock(AudioManager.class);
        when(gameModel.getGameState()).thenReturn(GameState.MAIN_MENU);
        controller = new GameController(gameModel, gameView, audioManager);
        controller.stopTimer();
    }

    private KeyEvent keyEvent(int keyCode) {
        return new KeyEvent(mock(Component.class), KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);
    }

    private KeyEvent keyReleasedEvent(int keyCode) {
        return new KeyEvent(mock(Component.class), KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);
    }

    private MouseEvent mouseEvent(Point point) {
        MouseEvent e = mock(MouseEvent.class);
        when(e.getPoint()).thenReturn(point);
        return e;
    }

    @Test
    void constructorSetsUpView() {
        verify(gameView).setFocusable(true);
        verify(gameView).addKeyListener(controller);
        verify(gameView).addMouseListener(controller);
        verify(gameView).requestFocusInWindow();
    }

    @Test
    void clockTickCalledWhenPlaying() {
        when(gameModel.getGameState()).thenReturn(GameState.PLAYING);
        controller.triggerUpdate();
        verify(gameModel).clockTick();
    }

    @Test
    void clockTickNotCalledWhenPaused() {
        when(gameModel.getGameState()).thenReturn(GameState.PAUSED);
        controller.triggerUpdate();
        verify(gameModel, never()).clockTick();
    }

    @Test
    void movementKeysDispatchMoveCommandsToModel() {
        when(gameModel.getGameState()).thenReturn(GameState.PLAYING);
        controller.keyPressed(keyEvent(KeyEvent.VK_LEFT));
        controller.keyPressed(keyEvent(KeyEvent.VK_RIGHT));
        controller.keyPressed(keyEvent(KeyEvent.VK_A));
        controller.keyPressed(keyEvent(KeyEvent.VK_D));
        controller.triggerUpdate();
        verify(gameModel).movePlayerLeft();
        verify(gameModel).movePlayerRight();
        verify(gameModel).movePlayer2Left();
        verify(gameModel).movePlayer2Right();
    }

    @Test
    void movementFlagsResetWhenNotPlaying() {
        when(gameModel.getGameState()).thenReturn(GameState.PLAYING);
        controller.keyPressed(keyEvent(KeyEvent.VK_LEFT));
        when(gameModel.getGameState()).thenReturn(GameState.PAUSED);
        controller.triggerUpdate();
        when(gameModel.getGameState()).thenReturn(GameState.PLAYING);
        controller.triggerUpdate();
        verify(gameModel, never()).movePlayerLeft();
    }

    @Test
    void repaintsEveryTick() {
        controller.triggerUpdate();
        verify(gameView).repaint();
    }

    @Test
    void playsBlippSoundWhenTransitioningToGameOver() {
        when(gameModel.getGameState()).thenReturn(GameState.PLAYING);
        controller.triggerUpdate();
        when(gameModel.getGameState()).thenReturn(GameState.GAME_OVER);
        controller.triggerUpdate();
        verify(audioManager).playSound("/blipp.ogg");
    }

    @Test
    void blippSoundNotPlayedOnSubsequentGameOverTicks() {
        when(gameModel.getGameState()).thenReturn(GameState.PLAYING);
        controller.triggerUpdate();
        when(gameModel.getGameState()).thenReturn(GameState.GAME_OVER);
        controller.triggerUpdate();
        controller.triggerUpdate();
        verify(audioManager, times(1)).playSound("/blipp.ogg");
    }

    @Test
    void upAndDownKeysNavigateMenu() {
        when(gameModel.getGameState()).thenReturn(GameState.MAIN_MENU);
        controller.keyPressed(keyEvent(KeyEvent.VK_UP));
        controller.keyPressed(keyEvent(KeyEvent.VK_DOWN));
        verify(gameModel).menuUp();
        verify(gameModel).menuDown();
    }

    @Test
    void enterSelectsMenuOption() {
        when(gameModel.getGameState()).thenReturn(GameState.MAIN_MENU);
        when(gameModel.isSoundEnabled()).thenReturn(true);
        when(gameModel.isMusicEnabled()).thenReturn(false);
        when(audioManager.isMusicEnabled()).thenReturn(false);
        controller.keyPressed(keyEvent(KeyEvent.VK_ENTER));
        verify(gameModel).menuSelect();
    }

    @Test
    void escapeFromSubMenuGoesToMainMenu() {
        when(gameModel.getGameState()).thenReturn(GameState.SETTINGS);
        controller.keyPressed(keyEvent(KeyEvent.VK_ESCAPE));
        verify(gameModel).setGameState(GameState.MAIN_MENU);
    }

    @Test
    void escapeFromMainMenuDoesNothing() {
        when(gameModel.getGameState()).thenReturn(GameState.MAIN_MENU);
        controller.keyPressed(keyEvent(KeyEvent.VK_ESCAPE));
        verify(gameModel, never()).setGameState(any());
    }

    @Test
    void escapeWhilePlayingPausesGame() {
        when(gameModel.getGameState()).thenReturn(GameState.PLAYING);
        controller.keyPressed(keyEvent(KeyEvent.VK_ESCAPE));
        verify(gameModel).setGameState(GameState.PAUSED);
    }

    @Test
    void jumpKeysCallJumpMethods() {
        when(gameModel.getGameState()).thenReturn(GameState.PLAYING);
        controller.keyPressed(keyEvent(KeyEvent.VK_UP));
        controller.keyPressed(keyEvent(KeyEvent.VK_W));
        verify(gameModel).playerJump();
        verify(gameModel).player2Jump();
    }

    @Test
    void escapeWhilePausedResumesGame() {
        when(gameModel.getGameState()).thenReturn(GameState.PAUSED);
        controller.keyPressed(keyEvent(KeyEvent.VK_ESCAPE));
        verify(gameModel).setGameState(GameState.PLAYING);
    }

    @Test
    void menuNavigationWorksWhilePaused() {
        when(gameModel.getGameState()).thenReturn(GameState.PAUSED);
        controller.keyPressed(keyEvent(KeyEvent.VK_UP));
        controller.keyPressed(keyEvent(KeyEvent.VK_DOWN));
        verify(gameModel).menuUp();
        verify(gameModel).menuDown();
    }

    @Test
    void menuNavigationWorksOnGameOver() {
        when(gameModel.getGameState()).thenReturn(GameState.GAME_OVER);
        controller.keyPressed(keyEvent(KeyEvent.VK_UP));
        controller.keyPressed(keyEvent(KeyEvent.VK_DOWN));
        controller.keyPressed(keyEvent(KeyEvent.VK_ENTER));
        verify(gameModel).menuUp();
        verify(gameModel).menuDown();
        verify(gameModel).menuSelect();
    }

    @Test
    void releasingMoveKeyStopsPlayer() {
        when(gameModel.getGameState()).thenReturn(GameState.PLAYING);
        controller.keyPressed(keyEvent(KeyEvent.VK_LEFT));
        controller.keyReleased(keyReleasedEvent(KeyEvent.VK_LEFT));
        verify(gameModel).stopPlayer();
    }

    @Test
    void holdingBothKeysDoesNotStopPlayer() {
        when(gameModel.getGameState()).thenReturn(GameState.PLAYING);
        controller.keyPressed(keyEvent(KeyEvent.VK_LEFT));
        controller.keyPressed(keyEvent(KeyEvent.VK_RIGHT));
        controller.keyReleased(keyReleasedEvent(KeyEvent.VK_LEFT));
        verify(gameModel, never()).stopPlayer();
    }

    @Test
    void releasingP2MoveKeyStopsPlayer2() {
        when(gameModel.getGameState()).thenReturn(GameState.PLAYING);
        controller.keyPressed(keyEvent(KeyEvent.VK_A));
        controller.keyReleased(keyReleasedEvent(KeyEvent.VK_A));
        verify(gameModel).stopPlayer2();
    }

    @Test
    void holdingBothP2KeysDoesNotStopPlayer2() {
        when(gameModel.getGameState()).thenReturn(GameState.PLAYING);
        controller.keyPressed(keyEvent(KeyEvent.VK_A));
        controller.keyPressed(keyEvent(KeyEvent.VK_D));
        controller.keyReleased(keyReleasedEvent(KeyEvent.VK_A));
        verify(gameModel, never()).stopPlayer2();
    }

    @Test
    void keyReleaseIgnoredWhenNotPlaying() {
        when(gameModel.getGameState()).thenReturn(GameState.PAUSED);
        controller.keyReleased(keyReleasedEvent(KeyEvent.VK_LEFT));
        verify(gameModel, never()).stopPlayer();
    }

    @Test
    void adBlockToggleWorksWhenPlaying() {
        when(gameModel.getGameState()).thenReturn(GameState.PLAYING);
        when(gameView.isAdBlockToggleClicked(any())).thenReturn(true);
        controller.mouseClicked(mouseEvent(new Point(0, 0)));
        verify(gameModel).toggleAdsBlocked();
    }

    @Test
    void adBlockToggleIgnoredWhenNotPlaying() {
        when(gameModel.getGameState()).thenReturn(GameState.PAUSED);
        when(gameView.isAdBlockToggleClicked(any())).thenReturn(true);
        controller.mouseClicked(mouseEvent(new Point(0, 0)));
        verify(gameModel, never()).toggleAdsBlocked();
    }

    @Test
    void syncAudioSetsSoundEnabledFromModel() {
        when(gameModel.getGameState()).thenReturn(GameState.MAIN_MENU);
        when(gameModel.isSoundEnabled()).thenReturn(false);
        when(gameModel.isMusicEnabled()).thenReturn(false);
        when(audioManager.isMusicEnabled()).thenReturn(false);
        controller.keyPressed(keyEvent(KeyEvent.VK_ENTER));
        verify(audioManager).setSoundEnabled(false);
    }

    @Test
    void syncAudioRestartsOrStopsMusic() {
        when(gameModel.getGameState()).thenReturn(GameState.MAIN_MENU);
        when(gameModel.isSoundEnabled()).thenReturn(true);
        when(gameModel.isMusicEnabled()).thenReturn(true);
        when(audioManager.isMusicEnabled()).thenReturn(false);
        controller.keyPressed(keyEvent(KeyEvent.VK_ENTER));
        verify(audioManager).restartMusic();

        reset(audioManager);
        when(gameModel.isMusicEnabled()).thenReturn(false);
        when(audioManager.isMusicEnabled()).thenReturn(true);
        controller.keyPressed(keyEvent(KeyEvent.VK_ENTER));
        verify(audioManager).stop();
    }
}
