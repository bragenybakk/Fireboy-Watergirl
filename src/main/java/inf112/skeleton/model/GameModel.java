package inf112.skeleton.model;

import java.util.List;
import java.util.ArrayList;

import inf112.skeleton.controller.ControllableGameModel;
import inf112.skeleton.coordinateSystem.Board;
import inf112.skeleton.coordinateSystem.Position;
import inf112.skeleton.model.entity.*;
import inf112.skeleton.model.player.Player;
import inf112.skeleton.view.ViewableGameModel;

public class GameModel implements ControllableGameModel, ViewableGameModel {
    private Board board;
    private List<Player> players;
    private List<StaticEntity> entities;
    private final double GRAVITY = 0.4;
    private final double FRICTION = 0.9;
    // Meny-relaterte felt
    private GameState gameState = GameState.MAIN_MENU;
    private int selectedMenuOption = 0;
    private String[] mainMenuOptions = { "Start Game", "Level Select", "Settings", "Exit" };
    private boolean testModeSinglePlayer = true;
    // Konstruktør for kun meny (uten brett)
    public GameModel() {
        this.board = null;
        this.players = null;
        this.entities = null;
    }

    public GameModel(Board board) {
        this.board = board;
        this.players = board.players();
        this.entities = board.entities();
    }

    // ============ Meny-metoder ============
    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public void setGameState(GameState state) {
        this.gameState = state;
        this.selectedMenuOption = 0; // Reset valg når tilstand endres
    }

    @Override
    public int getSelectedMenuOption() {
        return selectedMenuOption;
    }

    @Override
    public String[] getMenuOptions() {
        return mainMenuOptions;
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public Board getBoard() {
        return this.board;
    }

    @Override
    public void menuUp() {
        if (selectedMenuOption > 0) {
            selectedMenuOption--;
        }
    }

    @Override
    public void menuDown() {
        if (selectedMenuOption < mainMenuOptions.length - 1) {
            selectedMenuOption++;
        }
    }

    @Override
    public void menuSelect() {
        switch (gameState) {
            case MAIN_MENU:
                handleMainMenuSelection();
                break;
            case PAUSED:
                handlePauseMenuSelection();
                break;
            default:
                break;
        }
    }

    private void handleMainMenuSelection() {
        switch (selectedMenuOption) {
            case 0: // Start Game
                // initializeTestLevel();src/main/java/
                loadLevel("level2.txt");
                setGameState(GameState.PLAYING);
                break;
            case 1: // Level Select
                setGameState(GameState.LEVEL_SELECT);
                break;
            case 2: // Settings
                setGameState(GameState.SETTINGS);
                break;
            case 3: // Exit
                System.exit(0);
                break;
        }
    }

    // =============== LEVEL READER / LOADER ===============
    private void initializeTestLevel() {
        // Laster et testbrett med en spiller
        try {
            this.board = GameReader.loadLevel("src/main/java/inf112/skeleton/model/data/level2.txt");
            this.players = board.players();
            this.entities = board.entities();
        } catch (Exception e) {
            // Hvis fil ikke finnes, lag et enkelt testbrett
            createSimpleTestBoard();
        }
    }

    private void createSimpleTestBoard() {
        // Lag et enkelt testbrett med 20x15 og en spiller
        this.board = new Board(20, 15, List.of(new Player(new Position(2, 5), ElementState.FIRE)), new ArrayList<>());
        this.players = board.players();
        this.entities = board.entities();
    }

    public void loadLevel(String levelFileName) {
        try {
            String path = "src/main/java/inf112/skeleton/data/" + levelFileName;
            this.board = GameReader.loadLevel(path);
            List<Player> allPlayers = board.players();
            if (testModeSinglePlayer && !allPlayers.isEmpty()) {
                this.players = new java.util.ArrayList<>();
                this.players.add(allPlayers.get(0));
            } else {
                this.players = allPlayers;
            }
            this.entities = board.entities();
            setGameState(GameState.PLAYING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handlePauseMenuSelection() {
        // Kan utvides for pause-meny
    }

    // ============ Spiller-kontroll ============
    public void movePlayerLeft() {
        if (players != null && !players.isEmpty()) {
            players.get(0).setVelocityX(-0.5);
        }
    }

    public void movePlayerRight() {
        if (players != null && !players.isEmpty()) {
            players.get(0).setVelocityX(0.5);
        }
    }

    @Override
    public void playerJump() {
        if (players != null && !players.isEmpty()) {
            players.get(0).setVelocityY(-2.5);
        }
    }

    public void stopPlayer() {
        if (players != null && !players.isEmpty()) {
            players.get(0).setVelocityX(0);
        }
    }

    public void clockTick() {
        if (entities == null || players == null || gameState != GameState.PLAYING) {
            return;
        }
        for (IStaticEntity entity : entities)
            if (entity instanceof IMovable movable) {
                applyGravity(movable);
            }
        updateAllPositions();
        for (Player player : players) {
            applyGravity(player);
            for (IStaticEntity entity : entities) {
                if (checkCollision(entity, player)) {
                    entity.whenContact(player);
                }
            }
        }
    }

    private void applyGravity(IMovable obj) {
        obj.setVelocityY(obj.getVelocityY() + GRAVITY);
    }

    private void handlePush(Player player, IMovable movable) {
        double weightFactor = 1.0 / (1.0 + movable.getWeight());
        double pushForce = player.getVelocityX() * weightFactor;
        movable.setVelocityX(movable.getVelocityX() + pushForce);
        player.setVelocityX(pushForce);
    }

    private void updateAllPositions() {
        if (players != null) {
            for (Player p : players) {
                moveObj(p);
                keepInsideBounds(p);
            }
        }
    }

    private void moveObj(IMovable obj) {
        double newX = obj.getPos().x() + obj.getVelocityX();
        double newY = obj.getPos().y() + obj.getVelocityY();
        obj.setVelocityX(obj.getVelocityX() * FRICTION);
        obj.setPos(new Position(newX, newY));
    }

    private void keepInsideBounds(IMovable obj) {
        double x = obj.getPos().x();
        double y = obj.getPos().y();
        double w = obj.getWidth();
        double h = obj.getHeight();
        if (x < 0) {
            x = 0;
            obj.setVelocityX(0);
        } else if (x + w > board.boardWidth()) {
            x = board.boardWidth() - w;
            obj.setVelocityX(0);
        }
        if (y < 0) {
            y = 0;
            obj.setVelocityY(0);
        } else if (y + h > board.boardHeight()) {
            y = board.boardHeight() - h;
            obj.setVelocityY(0);
        }
        obj.setPos(new Position(x, y));
    }

    private boolean checkCollision(IStaticEntity entity, Player player) {
        return player.getPos().x() < entity.getPos().x() + entity.getWidth() &&
                player.getPos().x() + player.getWidth() > entity.getPos().x() &&
                player.getPos().y() < entity.getPos().y() + entity.getHeight() &&
                player.getPos().y() + player.getHeight() > entity.getPos().y();
    }

    // ------------ to do functions --------------
    private void restartLevel() {
    }
}