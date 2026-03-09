package inf112.fireboys.model;

import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import inf112.fireboys.controller.ControllableGameModel;
import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.enemy.IEnemy;
import inf112.fireboys.model.entity.*;
import inf112.fireboys.model.player.Player;
import inf112.fireboys.view.ViewableGameModel;

public class GameModel implements ControllableGameModel, ViewableGameModel {
    private Board board;
    private List<Player> players;
    private List<StaticEntity> entities;
    private List<IEnemy> enemies;
    private final double GRAVITY = 0.2;
    private final double FRICTION = 0.9;
    // Meny-relaterte felt
    private GameState gameState = GameState.MAIN_MENU;
    private int selectedMenuOption = 0;
    private String[] mainMenuOptions = { "START GAME", "Settings", "Exit" };
    private boolean testModeSinglePlayer = true;
    // Filnavn for nivåer
    private List<String> levelNames = null;
    // Konstruktør for kun meny (uten brett)
    public GameModel() {
        this.board = null;
        this.players = null;
        this.entities = null;
        this.enemies = null;
    }

    public GameModel(Board board) {
        this.board = board;
        this.players = board.players();
        this.entities = board.entities();
        this.enemies = board.enemies();
    }

    public void clockTick() {
        if (entities == null || players == null || gameState != GameState.PLAYING) {
            return;
        }
        for (IStaticEntity entity : entities)
            if (entity instanceof IMovable movable) {
                applyGravity(movable);
            }
        for (IEnemy enemy : enemies) {
            applyGravity(enemy);
            enemy.update();
        }
        updateAllPositions();
        for (Player player : players) {
            applyGravity(player);
            for (IStaticEntity entity : entities) {
                if (checkCollision(entity, player)) {
                    entity.whenContact(player);
                    if (entity instanceof Door) {
                        ((Door) entity).setOpen(true);
                        checkWinConditions();
                    }
                    if (entity instanceof IMovable movable) {
                        handlePush(player, movable);
                    }
                }
            }
            for (IEnemy enemy : enemies) {
                if (checkCollision(enemy, player)) {
                    enemy.whenContact(player);
                }
            }
        }
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
        if (state == GameState.LEVEL_SELECT) {
            // Load available level files when entering level select
            try {
                this.levelNames = loadLevelNamesFromDisk();
            } catch (Exception e) {
                this.levelNames = new ArrayList<>();
            }
        }
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

    public List<StaticEntity> getStaticEntities() {
        return entities;
    }

    @Override
    public List<IEnemy> getEnemies() {
        return enemies;
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
        int maxIndex = mainMenuOptions.length - 1;
        if (gameState == GameState.LEVEL_SELECT && levelNames != null) {
            maxIndex = Math.max(0, levelNames.size() - 1);
        }
        if (selectedMenuOption < maxIndex) {
            selectedMenuOption++;
        }
    }

    @Override
    public void menuSelect() {
        switch (gameState) {
            case MAIN_MENU:
                handleMainMenuSelection();
                break;
            case LEVEL_SELECT:
                handleLevelSelect();
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
            case 0: // Level Select
                setGameState(GameState.LEVEL_SELECT);
                break;
            case 1: // Settings
                setGameState(GameState.SETTINGS);
                break;
            case 2: // Exit
                System.exit(0);
                break;
        }
    }

    private void handleLevelSelect() {
        if (levelNames == null || levelNames.isEmpty()) {
            // nothing to load
            return;
        }
        int idx = Math.max(0, Math.min(selectedMenuOption, levelNames.size() - 1));
        String chosen = levelNames.get(idx);
        loadLevel(chosen + ".txt");
    }

    private void checkWinConditions() {
        for (StaticEntity e : entities)
            if (e instanceof Door) {
                Door door = (Door) e;
                if (!door.isOpen()) {
                    return;
                }
                door.setOpen(false);
            }
        setGameState(GameState.LEVEL_SELECT);
    }

    // =============== LEVEL READER / LOADER ===============
    private void initializeTestLevel() {
        // Laster et testbrett med en spiller
        try {
            this.board = GameReader.loadLevel("src/main/resources/level2.txt");
            this.players = board.players();
            this.entities = board.entities();
        } catch (Exception e) {
            // Hvis fil ikke finnes, lag et enkelt testbrett
            createSimpleTestBoard();
        }
    }

    private void createSimpleTestBoard() {
        // Lag et enkelt testbrett med 20x15 og en spiller
        this.board = new Board(20, 15, List.of(new Player(new Position(2, 5), ElementState.FIRE)), new ArrayList<>(),
                new ArrayList<>());
        this.players = board.players();
        this.entities = board.entities();
    }

    public void loadLevel(String levelFileName) {
        try {
            String path = "src/main/resources/" + levelFileName;
            this.board = GameReader.loadLevel(path);
            List<Player> allPlayers = board.players();
            if (testModeSinglePlayer && !allPlayers.isEmpty()) {
                this.players = new ArrayList<>();
                this.players.add(allPlayers.get(0));
            } else {
                this.players = allPlayers;
            }
            this.entities = board.entities();
            this.enemies = new ArrayList<>(board.enemies());
            setGameState(GameState.PLAYING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public java.util.List<String> getLevelNames() {
        if (levelNames == null) {
            try {
                levelNames = loadLevelNamesFromDisk();
            } catch (Exception e) {
                levelNames = new ArrayList<>();
            }
        }
        return levelNames;
    }

    private List<String> loadLevelNamesFromDisk() throws Exception {
        Path dir = Paths.get("src/main/resources");
        if (!Files.exists(dir) || !Files.isDirectory(dir))
            return new ArrayList<>();
        return Files.list(dir)
                .filter(p -> Files.isRegularFile(p) && p.getFileName().toString().toLowerCase().startsWith("level")
                        && p.getFileName().toString().toLowerCase().endsWith(".txt"))
                .map(p -> p.getFileName().toString().replaceAll("\\.txt$", ""))
                .sorted()
                .collect(Collectors.toList());
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
        if (players != null && !players.isEmpty() && players.get(0).isOnGround()) {
            players.get(0).setVelocityY(-2.5);
            players.get(0).setOnGroundFALSE();
        }
    }

    public void stopPlayer() {
        if (players != null && !players.isEmpty()) {
            players.get(0).setVelocityX(0);
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
        if (entities != null) {
        for (StaticEntity e : entities) {
            if (e instanceof IMovable movable) {
                moveObj(movable);
                keepInsideBounds(movable);
            }
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
}