package inf112.fireboys.model;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private int collectedGems = 0;
    private final double GRAVITY = 0.049;
    private final double FRICTION = 0.9;
    // Menu fields
    private GameState gameState = GameState.MAIN_MENU;
    private int selectedMenuOption = 0;
    private String[] mainMenuOptions = { "START GAME", "How to Play", "Settings", "Exit" };
    private String[] pauseMenuOptions = { "Resume", "Main Menu" };
    private String[] gameOverMenuOptions = { "Respawn", "Main Menu" };
    private String currentLevelFileName = null;
    private boolean adsBlocked = false;
    private boolean musicEnabled = true;
    private boolean soundEnabled = true;
    // Level file names
    private List<String> levelNames = null;
    private int unlockedLevelCount = 99;
    // Time tracking
    private int levelTicks = 0;
    private final Map<String, Integer> levelBestTicks = new HashMap<>();
    private static final int WIN_DELAY_TICKS = 60;
    private int winDelayTicks = 0;

    @Override
    public double getWinFadeProgress() {
        return Math.min(1.0, (double) winDelayTicks / WIN_DELAY_TICKS);
    }
    /** Creates an empty game model used to show the main menu before any level is loaded. */
    public GameModel() {
        this.board = null;
        this.players = null;
        this.entities = null;
        this.enemies = null;
    }

    /** Creates a game model with an already-loaded board. */
    public GameModel(Board board) {
        this.board = board;
        this.players = board.players();
        this.entities = board.entities();
        this.enemies = board.enemies();
        this.collectedGems = 0;
    }

    /** Advances the game by one frame — runs physics, collisions, and win/death checks. */
    public void clockTick() {
        if (entities == null || players == null || gameState != GameState.PLAYING) {
            return;
        }
        levelTicks++;
        resetButtonStates();
        tickMovingEntities();
        carryPlayersOnMovingPlatforms();
        applyGravityAll();
        updateAllPositions();
        handlePlayerCollisions();
        handleEntityCollisions();
        handleEnemyCollisions();
        updateDoorOpenStates();
        tickWinDelay();
        checkButton();
    }

    private void updateDoorOpenStates() {
        for (Door door : board.doors()) {
            boolean hasPlayer = false;
            for (Player player : players) {
                if (playerIsAtDoor(door, player)) {
                    hasPlayer = true;
                    break;
                }
            }
            door.setOpen(hasPlayer);
        }
    }

    private void tickWinDelay() {
        if (areAllGemsCollected() && allDoorsHavePlayer()) {
            winDelayTicks++;
            if (winDelayTicks >= WIN_DELAY_TICKS) {
                saveBestTime();
                unlockNextLevel();
                setGameState(GameState.LEVEL_SELECT);
                winDelayTicks = 0;
            }
        } else {
            winDelayTicks = 0;
        }
    }

    private void tickMovingEntities() {
        for (MovingPlatform mp : board.movingPlatforms()) {
            mp.tick();
        }
    }

    private void carryPlayersOnMovingPlatforms() {
        for (Player player : players) {
            if (!player.isOnGround())
                continue;
            double playerBottom = player.getPos().y() + player.getHeight();
            double playerCenterX = player.getPos().x() + player.getWidth() / 2.0;
            for (MovingPlatform mp : board.movingPlatforms()) {
                double platLeft = mp.getPos().x();
                double platRight = platLeft + mp.getWidth();
                double platTop = mp.getPos().y();
                if (playerCenterX >= platLeft && playerCenterX <= platRight
                        && Math.abs(playerBottom - platTop) < 2.0) {
                    player.setPos(new Position(
                            player.getPos().x() + mp.getDeltaX(),
                            player.getPos().y() + mp.getDeltaY()));
                    break;
                }
            }
        }
    }

    private void applyGravityAll() {
        for (Player player : players) {
            applyGravity(player);
        }
        for (StaticEntity e : board.movables()) {
            applyGravity((IMovable) e);
        }
        for (IEnemy enemy : enemies) {
            applyGravity(enemy);
            enemy.update();
        }
    }

    /** Sinks a movable to the pool floor if it straddles it while falling. Returns true if it was placed on the floor. */
    private boolean sinkOnPoolFloor(IMovable movable, Pool pool) {
        double centerX = movable.getPos().x() + movable.getWidth() / 2.0;
        double topY = movable.getPos().y();
        double bottomY = topY + movable.getHeight();
        if (pool.footOnFloor(centerX, topY, bottomY) && movable.getVelocityY() >= 0) {
            double floorY = pool.floorYAt(centerX);
            movable.setPos(new Position(movable.getPos().x(), floorY - movable.getHeight()));
            movable.setVelocityY(0);
            return true;
        }
        return false;
    }

    /** Returns true if the movable's foot is below a pool surface. If requiredElement is non-null, only pools of that element count. */
    private boolean isInPoolWater(IMovable movable, ElementState requiredElement) {
        double centerX = movable.getPos().x() + movable.getWidth() / 2.0;
        double bottomY = movable.getPos().y() + movable.getHeight();
        for (Pool pool : board.pools()) {
            if (requiredElement != null && pool.getElement() != requiredElement)
                continue;
            if (pool.footIsInWater(centerX, bottomY))
                return true;
        }
        return false;
    }

    private void handlePoolInteraction(Player player) {
        double centerX = player.getPos().x() + player.getWidth() / 2.0;
        double topY = player.getPos().y();
        double bottomY = topY + player.getHeight();
        for (Pool pool : board.pools()) {
            if (player.getElementState() != pool.getElement()) {
                if (pool.footIsInWater(centerX, bottomY)) {
                    player.kill();
                }
            } else if (sinkOnPoolFloor(player, pool)) {
                player.setOnGroundTRUE();
            }
        }
    }

    private void handleBoxPoolInteractions() {
        for (StaticEntity entity : entities) {
            if (!(entity instanceof Box box))
                continue;
            for (Pool pool : board.pools()) {
                sinkOnPoolFloor(box, pool);
            }
        }
    }

    private void handlePlayerCollisions() {
        for (Player player : players) {
            double savedVelocityY = player.getVelocityY();
            double yBeforeCollisions = player.getPos().y();
            handlePoolInteraction(player);
            boolean inMatchingPool = isInPoolWater(player, player.getElementState());
            for (IStaticEntity entity : entities) {
                if (entity instanceof Gem gem && gem.isCollected())
                    continue;
                if (entity instanceof Wall && inMatchingPool)
                    continue;
                if (checkCollision(entity, player)) {
                    if (entity instanceof IMovable movable) {
                        handlePush(player, movable);
                    }
                    if (entity instanceof Gem gem) {
                        boolean wasCollected = gem.isCollected();
                        gem.whenContact(player);
                        if (!wasCollected && gem.isCollected()) collectedGems++;
                    } else {
                        entity.whenContact(player);
                    }
                }
            }
            if (savedVelocityY < 0 && player.getPos().y() <= yBeforeCollisions) {
                player.setVelocityY(savedVelocityY);
                player.setOnGroundFALSE();
            }
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
        handleBoxPoolInteractions();
        resolveWallCollisions();
        for (StaticEntity entity : board.movables()) {
            IMovable movable = (IMovable) entity;
            for (StaticEntity otherEntity : board.movables()) {
                if (entity != otherEntity && checkCollision(otherEntity, movable)) {
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
        resolveWallCollisions();
    }

    private void resolveWallCollisions() {
        for (StaticEntity entity : board.movables()) {
            IMovable movable = (IMovable) entity;
            boolean inPool = (movable instanceof Box) && isInPoolWater(movable, null);
            for (StaticEntity otherEntity : entities) {
                if (entity != otherEntity && !(otherEntity instanceof IMovable)
                        && checkCollision(otherEntity, movable)) {
                    if (inPool && otherEntity instanceof Wall)
                        continue;
                    otherEntity.whenContact(movable);
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
                if (entity instanceof Pool)
                    continue;
                if (checkCollision(entity, enemy)) {
                    entity.whenContact(enemy);
                }
            }
        }
    }

    // ============ Menu methods ============
    /** Returns the current game state (e.g. PLAYING, PAUSED, MAIN_MENU). */
    @Override
    public GameState getGameState() {
        return gameState;
    }

    /** Sets the game state and resets menu selection. Also loads level names when entering level select. */
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

    /** Returns the index of the currently highlighted menu option. */
    @Override
    public int getSelectedMenuOption() {
        return selectedMenuOption;
    }

    /** Returns the menu options for the current game state. */
    @Override
    public String[] getMenuOptions() {
        if (gameState == GameState.PAUSED) {
            return pauseMenuOptions;
        }
        if (gameState == GameState.GAME_OVER) {
            return gameOverMenuOptions;
        }
        if (gameState == GameState.SETTINGS) {
            return new String[] {
                    "Music: " + (musicEnabled ? "ON" : "OFF"),
                    "Sound Effects: " + (soundEnabled ? "ON" : "OFF"),
                    "Ad Blocker: " + (adsBlocked ? "ON" : "OFF"),
                    "Back"
            };
        }
        return mainMenuOptions;
    }

    /** Returns the list of players in the current level. */
    @Override
    public List<Player> getPlayers() {
        return players;
    }

    /** Returns the list of static entities (walls, doors, gems, etc.) in the current level. */
    public List<StaticEntity> getStaticEntities() {
        return entities;
    }

    /** Returns the list of enemies in the current level. */
    @Override
    public List<IEnemy> getEnemies() {
        return enemies;
    }

    /** Returns the current board (level layout). */
    @Override
    public Board getBoard() {
        return this.board;
    }

    /** Moves the menu selection up by one. */
    @Override
    public void menuUp() {
        if (selectedMenuOption > 0) {
            selectedMenuOption--;
        }
    }

    /** Moves the menu selection down by one. */
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
        } else if (gameState == GameState.SETTINGS) {
            maxIndex = 3; // Music, Sound Effects, Ad Blocker, Back
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
            case SETTINGS:
                handleSettingsMenuSelection();
                break;
            default:
                break;
        }
    }

    private void handleSettingsMenuSelection() {
        switch (selectedMenuOption) {
            case 0:
                musicEnabled = !musicEnabled;
                break;
            case 1:
                soundEnabled = !soundEnabled;
                break;
            case 2:
                adsBlocked = !adsBlocked;
                break;
            case 3:
                setGameState(GameState.MAIN_MENU);
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

    /** Clears the pressed flag on all buttons before collisions run, so that the flag reflects only what is on the button this tick. */
    private void resetButtonStates() {
        for (StaticEntity entity : entities) {
            if (entity instanceof Button button) {
                button.resetPressed();
            }
        }
    }

    /** Adds the laser barrier when no one is on the button, removes it while someone is. */
    private void checkButton() {
        List<StaticEntity> toAdd = new ArrayList<>();
        List<StaticEntity> toRemove = new ArrayList<>();
        for (StaticEntity entity : entities) {
            if (!(entity instanceof Button button))
                continue;
            if (button.isPressed() && button.getLaserWall() != null) {
                toRemove.add(button.getLaserWall());
                button.setLaserWall(null);
            } else if (!button.isPressed() && button.getLaserWall() == null) {
                LaserWall laser = new LaserWall(button.getTrapPos(), button.getTrapWidth(), button.getTrapHeight());
                button.setLaserWall(laser);
                toAdd.add(laser);
            }
        }
        entities.removeAll(toRemove);
        entities.addAll(toAdd);
    }

    private boolean allDoorsHavePlayer() {
        for (Door door : board.doors()) {
            boolean hasPlayer = false;
            for (Player player : players) {
                if (playerIsAtDoor(door, player)) {
                    hasPlayer = true;
                    break;
                }
            }
            if (!hasPlayer)
                return false;
        }
        return true;
    }

    private boolean playerIsAtDoor(Door door, Player player) {
        double playerCenterX = player.getPos().x() + player.getWidth() / 2.0;
        double playerCenterY = player.getPos().y() + player.getHeight() / 2.0;
        double doorLeft = door.getPos().x();
        double doorRight = doorLeft + door.getWidth();
        double doorTop = door.getPos().y();
        double doorBottom = doorTop + door.getHeight();
        return playerCenterX >= doorLeft && playerCenterX <= doorRight
                && playerCenterY >= doorTop && playerCenterY <= doorBottom;
    }

    // =============== LEVEL READER / LOADER ===============

    /** Loads the level from the given file name and starts the game. */
    public void loadLevel(String levelFileName) {
        try {
            this.currentLevelFileName = levelFileName;
            this.levelTicks = 0;
            this.winDelayTicks = 0;
            this.board = GameReader.loadLevel(levelFileName);
            this.players = board.players();
            this.entities = board.entities();
            this.enemies = new ArrayList<>(board.enemies());
            this.collectedGems = 0;
            checkButton();
            setGameState(GameState.PLAYING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Returns the list of available level names read from levels.txt. */
    @Override
    public List<String> getLevelNames() {
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

    /** Returns the number of levels the player has unlocked. */
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
        return collectedGems >= board.gems().size();
    }

    /** Returns the total number of gems in the current level. */
    public int getTotalGems() {
        return board == null ? 0 : board.gems().size();
    }

    /** Returns the number of gems collected so far in the current level. */
    public int getCollectedGems() {
        return collectedGems;
    }

    /** Returns true if any player is standing on a boost platform without a boost already charged. */
    @Override
    public boolean isPlayerOnBoostPlateWithoutCharge() {
        if (players == null || players.isEmpty() || entities == null) {
            return false;
        }
        for (Player p : players) {
            if (p.isOnGround() && !p.hasJumpBoost() && isStandingOnBoostPlatform(p)) {
                return true;
            }
        }
        return false;
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

    private void saveBestTime() {
        if (currentLevelFileName == null)
            return;
        String key = currentLevelFileName.replaceAll("\\.txt$", "");
        int prev = levelBestTicks.getOrDefault(key, Integer.MAX_VALUE);
        if (levelTicks < prev) {
            levelBestTicks.put(key, levelTicks);
        }
    }

    /** Returns how many ticks have passed since the current level started. */
    @Override
    public int getElapsedTicks() {
        return levelTicks;
    }

    /** Returns a map from level name to the player's best completion time in ticks. */
    @Override
    public Map<String, Integer> getLevelBestTicks() {
        return levelBestTicks;
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

    /** Reloads the current level from scratch. */
    public void resetLevel() {
        if (currentLevelFileName != null) {
            loadLevel(currentLevelFileName);
        }
    }

    /** Returns true if the ad blocker is enabled. */
    @Override
    public boolean isAdsBlocked() {
        return adsBlocked;
    }

    /** Toggles the ad blocker on or off. */
    @Override
    public void toggleAdsBlocked() {
        adsBlocked = !adsBlocked;
    }

    /** Returns true if background music is enabled. */
    @Override
    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    /** Returns true if sound effects are enabled. */
    @Override
    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    /** Toggles background music on or off. */
    @Override
    public void toggleMusicEnabled() {
        musicEnabled = !musicEnabled;
    }

    /** Toggles sound effects on or off. */
    @Override
    public void toggleSoundEnabled() {
        soundEnabled = !soundEnabled;
    }

    // ============ Player controls ============
    private Player getWatergirl() {
        if (players == null)
            return null;
        return players.stream()
                .filter(p -> p.getElementState() == ElementState.WATER)
                .findFirst().orElse(null);
    }

    private Player getFireboy() {
        if (players == null)
            return null;
        return players.stream()
                .filter(p -> p.getElementState() == ElementState.FIRE)
                .findFirst().orElse(null);
    }

    /** Moves Watergirl to the left. */
    public void movePlayerLeft() {
        Player p = getWatergirl();
        if (p != null)
            p.setVelocityX(-0.7);
    }

    /** Moves Watergirl to the right. */
    public void movePlayerRight() {
        Player p = getWatergirl();
        if (p != null)
            p.setVelocityX(0.7);
    }

    @Override
    public void playerJump() {
        Player p = getWatergirl();
        if (p == null || !p.isOnGround())
            return;
        if (isStandingOnBoostPlatform(p) && !p.hasJumpBoost()) {
            p.grantJumpBoost();
            return;
        }
        p.setVelocityY(p.getJumpImpulse());
        p.consumeJumpBoost();
        p.setOnGroundFALSE();
    }

    /** Stops Watergirl's horizontal movement. */
    public void stopPlayer() {
        Player p = getWatergirl();
        if (p != null)
            p.setVelocityX(0);
    }

    /** Moves Fireboy to the left. */
    @Override
    public void movePlayer2Left() {
        Player p = getFireboy();
        if (p != null)
            p.setVelocityX(-0.7);
    }

    /** Moves Fireboy to the right. */
    @Override
    public void movePlayer2Right() {
        Player p = getFireboy();
        if (p != null)
            p.setVelocityX(0.7);
    }

    /** Stops Fireboy's horizontal movement. */
    @Override
    public void stopPlayer2() {
        Player p = getFireboy();
        if (p != null)
            p.setVelocityX(0);
    }

    @Override
    public void player2Jump() {
        Player p = getFireboy();
        if (p == null || !p.isOnGround())
            return;
        if (isStandingOnBoostPlatform(p) && !p.hasJumpBoost()) {
            p.grantJumpBoost();
            return;
        }
        p.setVelocityY(p.getJumpImpulse());
        p.consumeJumpBoost();
        p.setOnGroundFALSE();
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
        for (StaticEntity e : board.movables()) {
            IMovable movable = (IMovable) e;
            moveObj(movable);
            keepInsideBounds(movable);
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
        double eX = entity.getPos().x();
        double eY = entity.getPos().y();
        double eW = entity.getWidth();
        double eH = entity.getHeight();
        if (entity instanceof Wall) {
            double playerMidX = movableEntity.getPos().x() + movableEntity.getWidth() / 2;
            for (Pool pool : board.pools()) {
                if (playerMidX >= pool.getPos().x() && playerMidX <= pool.getPos().x() + pool.getWidth()) {
                    if (Math.abs(pool.getPos().y() - entity.getPos().y()) < 5) {
                        double basinDepth = pool.getDepthAt(playerMidX);
                        double newTop = pool.getPos().y() + basinDepth;
                        if (newTop > eY) {
                            double bottomY = eY + eH;
                            eY = newTop;
                            eH = Math.max(0, bottomY - newTop);
                        }
                    }
                }
            }
        }
        return movableEntity.getPos().x() < eX + eW &&
                movableEntity.getPos().x() + movableEntity.getWidth() > eX &&
                movableEntity.getPos().y() < eY + eH &&
                movableEntity.getPos().y() + movableEntity.getHeight() > eY;
    }
}