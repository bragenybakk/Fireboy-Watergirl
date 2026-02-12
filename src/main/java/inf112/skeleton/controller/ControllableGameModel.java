package inf112.skeleton.controller;

import inf112.skeleton.model.GameState;

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
}
