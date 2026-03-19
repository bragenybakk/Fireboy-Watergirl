package inf112.fireboys.controller;

import java.awt.event.KeyListener;

import inf112.fireboys.model.GameState;
import inf112.fireboys.view.GameView;

import java.awt.event.KeyEvent;
import javax.swing.Timer;

public class GameController implements KeyListener {
    private ControllableGameModel gameModel;
    private GameView gameView;
    private Timer gameLoopTimer;
    // Track which keys are currently pressed
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    public GameController(ControllableGameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        gameView.setFocusable(true);
        gameView.addKeyListener(this);
        gameView.requestFocusInWindow();
        // Start game loop timer (60 FPS = ~16ms per frame)
        gameLoopTimer = new Timer(16, e -> updateGame());
        gameLoopTimer.start();
    }

    private void updateGame() {
        if (gameModel.getGameState() == GameState.PLAYING) {
            if (leftPressed) {
                gameModel.movePlayerLeft();
            }
            if (rightPressed) {
                gameModel.movePlayerRight();
            }
            gameModel.clockTick();
        } else {
            leftPressed = false;
            rightPressed = false;
        }
        gameView.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        GameState currentState = gameModel.getGameState();
        int keyCode = e.getKeyCode();
        switch (currentState) {
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
                break;
            case KeyEvent.VK_ESCAPE:
                // Gå tilbake til hovedmeny fra undermeny
                if (gameModel.getGameState() != GameState.MAIN_MENU) {
                    gameModel.setGameState(GameState.MAIN_MENU);
                }
                break;
        }
    }

    private void handleGameInput(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_ESCAPE:
                gameModel.setGameState(GameState.PAUSED);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                leftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                rightPressed = true;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                gameModel.playerJump();
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
                case KeyEvent.VK_A:
                    leftPressed = false;
                    if (!leftPressed && !rightPressed) {
                        gameModel.stopPlayer();
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    rightPressed = false;
                    if (!leftPressed && !rightPressed) {
                        gameModel.stopPlayer();
                    }
                    break;
            }
        }
    }
}
