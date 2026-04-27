package inf112.fireboys.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import inf112.fireboys.model.GameState;
import inf112.fireboys.view.AudioManager;
import inf112.fireboys.view.GameView;

import javax.swing.Timer;

/**
 * Handles keyboard and mouse input and runs the game loop at 60 FPS.
 * Routes input to the model based on the current game state.
 */
public class GameController implements KeyListener, MouseListener {
    private ControllableGameModel gameModel;
    private GameView gameView;
    private AudioManager audioManager;
    private Timer gameLoopTimer;
    private GameState previousState = null;
    // Track which keys are currently pressed
    // Watergirl: arrow keys, Fireboy: WASD
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean p2LeftPressed = false;
    private boolean p2RightPressed = false;
    public GameController(ControllableGameModel gameModel, GameView gameView, AudioManager audioManager) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.audioManager = audioManager;
        gameView.setFocusable(true);
        gameView.addKeyListener(this);
        gameView.addMouseListener(this);
        gameView.requestFocusInWindow();
        // Start game loop timer (60 FPS = ~16ms per frame)
        gameLoopTimer = new Timer(16, e -> updateGame());
        gameLoopTimer.start();
    }

    void stopTimer() {
        gameLoopTimer.stop();
    }

    void triggerUpdate() {
        updateGame();
    }

    private void updateGame() {
        GameState currentState = gameModel.getGameState();
        if (previousState == GameState.PLAYING && currentState == GameState.GAME_OVER) {
            audioManager.playSound("/blipp.ogg");
        }
        previousState = currentState;
        if (currentState == GameState.PLAYING) {
            if (leftPressed) gameModel.movePlayerLeft();
            if (rightPressed) gameModel.movePlayerRight();
            if (p2LeftPressed) gameModel.movePlayer2Left();
            if (p2RightPressed) gameModel.movePlayer2Right();
            gameModel.clockTick();
        } else {
            leftPressed = false;
            rightPressed = false;
            p2LeftPressed = false;
            p2RightPressed = false;
        }
        gameView.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        GameState keyState = gameModel.getGameState();
        int keyCode = e.getKeyCode();
        switch (keyState) {
            case MAIN_MENU:
            case LEVEL_SELECT:
            case SETTINGS:
            case HOW_TO_PLAY:
                handleMenuInput(keyCode);
                break;
            case PLAYING:
                handleGameInput(keyCode);
                break;
            case PAUSED:
                handlePauseInput(keyCode);
                break;
            case GAME_OVER:
                handleGameOverInput(keyCode);
                break;
        }
        gameView.repaint();
    }

    private void handleMenuInput(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                gameModel.menuUp();
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                gameModel.menuDown();
                break;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
                gameModel.menuSelect();
                syncAudioWithModel();
                break;
            case KeyEvent.VK_ESCAPE:
                // Go back to main menu from submenu
                if (gameModel.getGameState() != GameState.MAIN_MENU) {
                    gameModel.setGameState(GameState.MAIN_MENU);
                }
                break;
        }
    }

    private void syncAudioWithModel() {
        audioManager.setSoundEnabled(gameModel.isSoundEnabled());
        boolean musicShouldPlay = gameModel.isMusicEnabled();
        if (musicShouldPlay && !audioManager.isMusicEnabled()) {
            audioManager.restartMusic();
        } else if (!musicShouldPlay && audioManager.isMusicEnabled()) {
            audioManager.stop();
        }
    }

    private void handleGameInput(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_ESCAPE:
                gameModel.setGameState(GameState.PAUSED);
                break;
            // Watergirl: arrow keys
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = true;
                break;
            case KeyEvent.VK_UP:
                gameModel.playerJump();
                break;
            // Fireboy: WASD
            case KeyEvent.VK_A:
                p2LeftPressed = true;
                break;
            case KeyEvent.VK_D:
                p2RightPressed = true;
                break;
            case KeyEvent.VK_W:
                gameModel.player2Jump();
                break;
        }
    }

    private void handlePauseInput(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_ESCAPE:
                gameModel.setGameState(GameState.PLAYING);
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                gameModel.menuUp();
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                gameModel.menuDown();
                break;
            case KeyEvent.VK_ENTER:
                gameModel.menuSelect();
                break;
        }
    }

    private void handleGameOverInput(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                gameModel.menuUp();
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                gameModel.menuDown();
                break;
            case KeyEvent.VK_ENTER:
                gameModel.menuSelect();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        GameState currentState = gameModel.getGameState();
        if (currentState == GameState.PLAYING) {
            switch (keyCode) {
                case KeyEvent.VK_LEFT:
                    leftPressed = false;
                    if (!leftPressed && !rightPressed) gameModel.stopPlayer();
                    break;
                case KeyEvent.VK_RIGHT:
                    rightPressed = false;
                    if (!leftPressed && !rightPressed) gameModel.stopPlayer();
                    break;
                case KeyEvent.VK_A:
                    p2LeftPressed = false;
                    if (!p2LeftPressed && !p2RightPressed) gameModel.stopPlayer2();
                    break;
                case KeyEvent.VK_D:
                    p2RightPressed = false;
                    if (!p2LeftPressed && !p2RightPressed) gameModel.stopPlayer2();
                    break;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameModel.getGameState() == GameState.PLAYING
                && gameView.isAdBlockToggleClicked(e.getPoint())) {
            gameModel.toggleAdsBlocked();
            gameView.repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
