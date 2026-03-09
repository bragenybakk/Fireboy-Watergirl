package inf112.fireboys.controller;

import inf112.fireboys.model.GameState;

public interface ControllableGameModel {
    void setGameState(GameState state);

    GameState getGameState();

    void menuUp();

    void menuDown();

    void menuSelect();

    void clockTick();

    void movePlayerRight();

    void movePlayerLeft();

    void stopPlayer();

    void playerJump();
}
