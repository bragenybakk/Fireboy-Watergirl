package inf112.fireboys.model;

import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import inf112.fireboys.controller.ControllableGameModel;
import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.enemy.IEnemy;
import inf112.fireboys.model.entity.*;
import inf112.fireboys.model.player.Player;
import inf112.fireboys.view.ViewableGameModel;

/**
 * The game model. Manages game state, physics, collisions, and level loading.
 * Implements both ControllableGameModel (for controller) and ViewableGameModel
 * (for view).
 */
public class GameModel implements ControllableGameModel, ViewableGameModel {
    private Board board;
    private List<Player> players;
    private List<StaticEntity> entities;
    private List<IEnemy> enemies;
    private final double GRAVITY = 0.1;
    private final double FRICTION = 0.9;
    // Menu fields
    private GameState gameState = GameState.MAIN_MENU;
    private int selectedMenuOption = 0;
    private String[] mainMenuOptions = { "START GAME", "How to Play", "Settings", "Exit" };
    private String[] pauseMenuOptions = { "Resume", "Main Menu" };
    private String[] gameOverMenuOptions = { "Respawn", "Main Menu" };
    private boolean testModeSinglePlayer = true;
    private String currentLevelFileName = null;
    private boolean adsBlocked = false;
    // Level file names
    private List<String> levelNames = null;
    private int unlockedLevelCount = 1;
    // Constructor for menu only (no board)
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
        applyGravityAll();
        updateAllPositions();
        handlePlayerCollisions();
        handleEntityCollisions();
        handleEnemyCollisions();
    }

    private void applyGravityAll() {
        for (Player player : players) {
            applyGravity(player);
        }
        for (IStaticEntity entity : entities) {
            if (entity instanceof IMovable movable) {
                applyGravity(movable);
            }
        }
        for (IEnemy enemy : enemies) {
            applyGravity(enemy);
            enemy.update();
        }
    }

    // After wall collision, adjust player position down into any overlapping pool
    private void applyPoolDepth(Player player) {
        // Don't pull player into pool while jumping
        if (player.getVelocityY() < 0)
            return;
        double playerCenterX = player.getPos().x() + player.getWidth() / 2.0;
        double playerBottom = player.getPos().y() + player.getHeight();
        for (IStaticEntity entity : entities) {
            if (entity instanceof Pool pool) {
                double depth = pool.getDepthAt(playerCenterX);
                if (depth <= 0)
                    continue;
                double poolSurface = pool.getPos().y();
                // Only apply if player is near the floor surface, not jumping above
                if (Math.abs(playerBottom - poolSurface) < 1.0) {
                    double newY = poolSurface + depth - player.getHeight();
                    player.setPos(new Position(player.getPos().x(), newY));
                    player.setVelocityY(0);
                    player.setOnGroundTRUE();
                    pool.whenContact(player);
                }
            }
        }
    }

    @Override
    public int getScore() {
        if (players == null || players.isEmpty())
            return 0;
        return players.stream().mapToInt(p -> p.getScore()).sum();
    }

    private void handlePlayerCollisions() {
        for (Player player : players) {
            double savedVelocityY = player.getVelocityY();
            double yBeforeCollisions = player.getPos().y();
            for (IStaticEntity entity : entities) {
                if (entity instanceof Gem gem && gem.isCollected())
                    continue;
                if (checkCollision(entity, player)) {
                    if (entity instanceof IMovable movable) {
                        handlePush(player, movable);
                    }
                    entity.whenContact(player);
                    if (entity instanceof Door) {
                        ((Door) entity).setOpen(areAllGemsCollected());
                        checkWinConditions();
                    }
                }
            }
            // If player was jumping from a pool, restore velocity so wall doesn't cancel
            // the jump — but not if the player hit a ceiling (which pushes Y downward)
            if (savedVelocityY < 0 && player.getPos().y() <= yBeforeCollisions) {
                player.setVelocityY(savedVelocityY);
                player.setOnGroundFALSE();
            }
            applyPoolDepth(player);
            for (IEnemy enemy : enemies) {
                if (checkCollision(enemy, player)) {
                    enemy.whenContact(player);
                }
            }
            if (!player.isAlive()) {
                setGameState(GameState.GAME_OVER);
                return;
            }
        }
    }

    private void handleEntityCollisions() {
        // Pass 1: resolve movable entities against walls/floors
        resolveWallCollisions();
        // Pass 2: resolve box-box collisions, reverting if pushed into a wall
        for (StaticEntity entity : entities) {
            if (entity instanceof IMovable movable) {
                for (StaticEntity otherEntity : entities) {
                    if (entity != otherEntity && otherEntity instanceof IMovable
                            && checkCollision(otherEntity, movable)) {
                        if (entity instanceof Box && otherEntity instanceof Box) {
                            transferBoxPush((Box) entity, (Box) otherEntity);
                        }
                        Position prevPos = movable.getPos();
                        otherEntity.whenContact(movable);
                        if (isInsideWall(movable, entity, otherEntity)) {
                            movable.setPos(prevPos);
                            movable.setVelocityX(0);
                        }
                    }
                }
            }
        }
        // Pass 3: re-resolve walls in case box-box left overlaps
        resolveWallCollisions();
    }

    private void resolveWallCollisions() {
        for (StaticEntity entity : entities) {
            if (entity instanceof IMovable movable) {
                for (StaticEntity otherEntity : entities) {
                    if (entity != otherEntity && !(otherEntity instanceof IMovable)
                            && checkCollision(otherEntity, movable)) {
                        otherEntity.whenContact(movable);
                    }
                }
            }
        }
    }

    private boolean isInsideWall(IMovable movable, StaticEntity self, StaticEntity other) {
        for (StaticEntity wall : entities) {
            if (wall instanceof Gem gem && gem.isCollected())
                continue;
            if (wall != self && wall != other && !(wall instanceof IMovable)
                    && checkCollision(wall, movable)) {
                return true;
            }
        }
        return false;
    }

    private void transferBoxPush(Box pusher, Box target) {
        double pusherVx = pusher.getVelocityX();
        if (pusherVx != 0) {
            double pushForce = pusherVx / target.getWeight();
            target.setVelocityX(target.getVelocityX() + pushForce);
        }
    }

    private void handleEnemyCollisions() {
        if (enemies == null)
            return;
        for (IEnemy enemy : enemies) {
            for (StaticEntity entity : entities) {
                if (entity instanceof Gem gem && gem.isCollected())
                    continue;
                if (checkCollision(entity, enemy)) {
                    entity.whenContact(enemy);
                }
            }
        }
    }

    // ============ Menu methods ============
    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public void setGameState(GameState state) {
        this.gameState = state;
        this.selectedMenuOption = 0; // Reset selection when state changes
        if (state == GameState.LEVEL_SELECT) {
            // Load available level files when entering level select
            try {
                this.levelNames = loadLevelNamesFromDisk();
                clampUnlockedLevelCount();
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
        if (gameState == GameState.PAUSED) {
            return pauseMenuOptions;
        }
        if (gameState == GameState.GAME_OVER) {
            return gameOverMenuOptions;
        }
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
            int unlocked = Math.min(levelNames.size(), unlockedLevelCount);
            maxIndex = Math.max(0, unlocked - 1);
        } else if (gameState == GameState.PAUSED) {
            maxIndex = pauseMenuOptions.length - 1;
        } else if (gameState == GameState.GAME_OVER) {
            maxIndex = gameOverMenuOptions.length - 1;
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
            case GAME_OVER:
                handleGameOverMenuSelection();
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
            case 1: // How to Play
                setGameState(GameState.HOW_TO_PLAY);
                break;
            case 2: // Settings
                setGameState(GameState.SETTINGS);
                break;
            case 3: // Exit
                System.exit(0);
                break;
        }
    }

    private void handleLevelSelect() {
        if (levelNames == null || levelNames.isEmpty()) {
            // nothing to load
            return;
        }
        int maxUnlockedIndex = Math.min(levelNames.size(), unlockedLevelCount) - 1;
        int idx = Math.max(0, Math.min(selectedMenuOption, maxUnlockedIndex));
        if (idx > maxUnlockedIndex) {
            return;
        }
        String chosen = levelNames.get(idx);
        loadLevel(chosen + ".txt");
    }

    private void checkWinConditions() {
        if (!areAllGemsCollected()) {
            return;
        }
        for (StaticEntity e : entities)
            if (e instanceof Door) {
                Door door = (Door) e;
                if (!door.isOpen()) {
                    return;
                }
            }
        unlockNextLevel();
        setGameState(GameState.LEVEL_SELECT);
    }
    // =============== LEVEL READER / LOADER ===============

    public void loadLevel(String levelFileName) {
        try {
            this.currentLevelFileName = levelFileName;
            this.board = GameReader.loadLevel(levelFileName);
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
                clampUnlockedLevelCount();
            } catch (Exception e) {
                levelNames = new ArrayList<>();
            }
        }
        return levelNames;
    }

    @Override
    public int getUnlockedLevelCount() {
        return unlockedLevelCount;
    }

    private List<String> loadLevelNamesFromDisk() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("levels.txt")) {
            if (is == null)
                return new ArrayList<>();
            return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty() && !line.startsWith("#"))
                    .collect(Collectors.toList());
        }
    }

    private boolean areAllGemsCollected() {
        if (entities == null)
            return true;
        for (StaticEntity entity : entities) {
            if (entity instanceof Gem gem && !gem.isCollected()) {
                return false;
            }
        }
        return true;
    }

    public int getTotalGems() {
        if (entities == null)
            return 0;
        int count = 0;
        for (StaticEntity entity : entities) {
            if (entity instanceof Gem) {
                count++;
            }
        }
        return count;
    }

    public int getCollectedGems() {
        if (entities == null)
            return 0;
        int count = 0;
        for (StaticEntity entity : entities) {
            if (entity instanceof Gem gem && gem.isCollected()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean isPlayerOnBoostPlateWithoutCharge() {
        if (players == null || players.isEmpty() || entities == null) {
            return false;
        }
        Player p = players.get(0);
        if (!p.isOnGround() || p.hasJumpBoost()) {
            return false;
        }
        return isStandingOnBoostPlatform(p);
    }

    private boolean isStandingOnBoostPlatform(Player p) {
        for (StaticEntity e : entities) {
            if (!(e instanceof BoostPlatform bp)) {
                continue;
            }
            double px = p.getPos().x();
            double pw = p.getWidth();
            double bx = bp.getPos().x();
            double bw = bp.getWidth();
            if (px + pw <= bx || px >= bx + bw) {
                continue;
            }
            double playerBottom = p.getPos().y() + p.getHeight();
            double platTop = bp.getPos().y();
            double platBottom = platTop + bp.getHeight();
            double platH = bp.getHeight();
            // Thin plates (e.g. h=1) on a floor: feet often sit on the floor just under the
            // AABB bottom, so allow more slack below; keep tighter for thick platforms.
            double slackBelow = Math.max(1.0, 6.0 - platH);
            double slackAbove = Math.max(0.2, 2.5 - platH * 0.3);
            if (playerBottom >= platTop - slackAbove && playerBottom <= platBottom + slackBelow) {
                return true;
            }
        }
        return false;
    }

    private void unlockNextLevel() {
        try {
            if (levelNames == null) {
                levelNames = loadLevelNamesFromDisk();
            }
        } catch (Exception e) {
            return;
        }
        if (levelNames == null || levelNames.isEmpty() || currentLevelFileName == null) {
            return;
        }
        String current = currentLevelFileName.replaceAll("\\.txt$", "");
        int currentIndex = levelNames.indexOf(current);
        if (currentIndex >= 0) {
            unlockedLevelCount = Math.max(unlockedLevelCount, currentIndex + 2);
            clampUnlockedLevelCount();
        }
    }

    private void clampUnlockedLevelCount() {
        if (levelNames == null || levelNames.isEmpty()) {
            unlockedLevelCount = Math.max(1, unlockedLevelCount);
            return;
        }
        unlockedLevelCount = Math.max(1, Math.min(unlockedLevelCount, levelNames.size()));
    }

    private void handlePauseMenuSelection() {
        switch (selectedMenuOption) {
            case 0: // Resume
                setGameState(GameState.PLAYING);
                break;
            case 1: // Main Menu
                setGameState(GameState.MAIN_MENU);
                break;
        }
    }

    private void handleGameOverMenuSelection() {
        switch (selectedMenuOption) {
            case 0: // Respawn
                resetLevel();
                break;
            case 1: // Main Menu
                setGameState(GameState.MAIN_MENU);
                break;
        }
    }

    public void resetLevel() {
        if (currentLevelFileName != null) {
            loadLevel(currentLevelFileName);
        }
    }

    @Override
    public boolean isAdsBlocked() {
        return adsBlocked;
    }

    @Override
    public void toggleAdsBlocked() {
        adsBlocked = !adsBlocked;
    }

    // ============ Player controls ============
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
        if (players == null || players.isEmpty() || !players.get(0).isOnGround()) {
            return;
        }
        Player p = players.get(0);
        if (isStandingOnBoostPlatform(p) && !p.hasJumpBoost()) {
            p.grantJumpBoost();
            return;
        }
        p.setVelocityY(p.getJumpImpulse());
        p.consumeJumpBoost();
        p.setOnGroundFALSE();
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
        double playerBottom = player.getPos().y() + player.getHeight();
        double playerTop = player.getPos().y();
        double boxTop = movable.getPos().y();
        double boxBottom = movable.getPos().y() + movable.getHeight();
        double margin = 1; // Can imagine this need change as we change sizes of players and so on (Remove)
        boolean isAbove = playerBottom < boxTop + margin;
        boolean isBelow = playerTop > boxBottom - margin;
        if (!isAbove && !isBelow) {
            if (movable instanceof Box) {
                double weight = ((Box) movable).getWeight();
                double pushForce = player.getVelocityX() / weight;
                movable.setVelocityX(pushForce);
            }
        }
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
        if (enemies != null) {
            for (IEnemy enemy : enemies) {
                moveObj(enemy);
                keepInsideBounds(enemy);
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

    private boolean checkCollision(IStaticEntity entity, IMovable movableEntity) {
        return movableEntity.getPos().x() < entity.getPos().x() + entity.getWidth() &&
                movableEntity.getPos().x() + movableEntity.getWidth() > entity.getPos().x() &&
                movableEntity.getPos().y() < entity.getPos().y() + entity.getHeight() &&
                movableEntity.getPos().y() + movableEntity.getHeight() > entity.getPos().y();
    }
}