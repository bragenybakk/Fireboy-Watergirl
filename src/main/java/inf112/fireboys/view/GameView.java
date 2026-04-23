package inf112.fireboys.view;

import javax.swing.JPanel;

import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.coordinateSystem.Decoration;
import inf112.fireboys.model.ElementState;
import inf112.fireboys.model.GameState;
import inf112.fireboys.model.enemy.EnemyState;
import inf112.fireboys.model.enemy.IEnemy;
import inf112.fireboys.model.entity.Door;
import inf112.fireboys.model.entity.Gem;
import inf112.fireboys.model.entity.BoostPlatform;
import inf112.fireboys.model.entity.Pool;
import inf112.fireboys.model.entity.StaticEntity;
import inf112.fireboys.model.entity.Wall;
import inf112.fireboys.model.player.Player;
import inf112.fireboys.view.theme.CastleTheme;
import inf112.fireboys.view.theme.Theme;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * Renders the game using Swing Graphics2D.
 * Draws menus, game entities, players, and enemies based on the current game
 * state.
 */
public class GameView extends JPanel {
    private static final Color ENEMY_PATROL = new Color(0x8B0000);
    private static final Color ENEMY_ALERT = new Color(0xFF8C00);
    private static final Color ENEMY_CHASE = new Color(0xFF0000);
    private ViewableGameModel viewableGameModel;
    private int windowWidth = 1100;
    private int windowHeight = 900;
    private Font font = new Font("Arial", Font.PLAIN, 12);
    private Font titleFont = new Font("Arial", Font.BOLD, 48);
    private Font menuFont = new Font("Arial", Font.PLAIN, 28);
    private Font selectedMenuFont = new Font("Arial", Font.BOLD, 32);
    private BufferedImage adLeft;
    private BufferedImage adRight;
    private Rectangle adBlockToggleBounds = new Rectangle();
    private final Theme theme;
    // Remembers last horizontal direction per player so sprite keeps facing
    // that way after they stop moving.
    private final Map<Player, Boolean> playerFacingLeft = new HashMap<>();
    private SkeletonSpriteSheet skeletonSheet;
    // Per-enemy animation tick counter; incremented each draw call.
    private final Map<IEnemy, Integer> enemyAnimTick = new HashMap<>();

    public GameView(ViewableGameModel viewableGameModel) {
        this(viewableGameModel, new CastleTheme());
    }

    public GameView(ViewableGameModel viewableGameModel, Theme theme) {
        this.viewableGameModel = viewableGameModel;
        this.theme = theme;
        this.setSize(windowWidth, windowHeight);
        this.setFont(font);
        this.setBackground(theme.getBackgroundColor());
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
        try {
            adLeft = ImageIO.read(getClass().getResourceAsStream("/Advertisement_1.png"));
            adRight = ImageIO.read(getClass().getResourceAsStream("/Advertisement_2.png"));
        } catch (IOException e) {
            adLeft = null;
            adRight = null;
        }
        skeletonSheet = new SkeletonSpriteSheet("/skeleton_enemy.png");
    }

    /**
     * Returns whether the given point is inside the ad blocker toggle button.
     * Used by the controller to detect clicks on the toggle.
     */
    public boolean isAdBlockToggleClicked(Point point) {
        return adBlockToggleBounds.contains(point);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GameState state = viewableGameModel.getGameState();
        switch (state) {
            case MAIN_MENU:
                drawMainMenu(g2);
                break;
            case PLAYING:
                drawGame(g2);
                break;
            case PAUSED:
                drawPauseMenu(g2);
                break;
            case LEVEL_SELECT:
                drawLevelSelect(g2);
                break;
            case SETTINGS:
                drawSettings(g2);
                break;
            case GAME_OVER:
                drawGameOver(g2);
                break;
            case HOW_TO_PLAY:
                drawHowToPlay(g2);
                break;
        }
    }

    private void drawMainMenu(Graphics2D g2) {
        // Background
        g2.setColor(Color.decode("#1a1a2e"));
        g2.fillRect(0, 0, windowWidth, windowHeight);
        // Menu characters
        int charSize = 120;
        int charY = 200;
        int fireboyX = 80;
        int watergirlX = windowWidth - 80 - charSize;
        BufferedImage fireboyHead = theme.getPlayerHead(ElementState.FIRE);
        BufferedImage watergirlHead = theme.getPlayerHead(ElementState.WATER);
        if (fireboyHead != null) {
            g2.drawImage(fireboyHead, fireboyX, charY, charSize, charSize, null);
        }
        if (watergirlHead != null) {
            g2.drawImage(watergirlHead, watergirlX, charY, charSize, charSize, null);
        }
        // Title
        g2.setFont(titleFont);
        g2.setColor(Color.decode("#e94560"));
        String title = "FIREBOY & WATERGIRL";
        FontMetrics fm = g2.getFontMetrics();
        int titleX = (windowWidth - fm.stringWidth(title)) / 2;
        g2.drawString(title, titleX, 120);
        // Menu options
        String[] options = viewableGameModel.getMenuOptions();
        int selectedOption = viewableGameModel.getSelectedMenuOption();
        int startY = 250;
        int spacing = 60;
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                // Selected item
                g2.setFont(selectedMenuFont);
                g2.setColor(Color.decode("#f5a623"));
                // Draw cursor
                String marker = "► ";
                fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(marker + options[i]);
                int x = (windowWidth - textWidth) / 2;
                g2.drawString(marker + options[i], x, startY + i * spacing);
            } else {
                // Unselected item
                g2.setFont(menuFont);
                g2.setColor(Color.WHITE);
                fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(options[i]);
                int x = (windowWidth - textWidth) / 2;
                g2.drawString(options[i], x, startY + i * spacing);
            }
        }
        // Instructions
        g2.setFont(font);
        g2.setColor(Color.GRAY);
        String instructions = "Use ↑↓ to navigate, ENTER to select";
        fm = g2.getFontMetrics();
        int instrX = (windowWidth - fm.stringWidth(instructions)) / 2;
        g2.drawString(instructions, instrX, windowHeight - 50);
    }

    private void drawGame(Graphics2D g2) {
        Board board = viewableGameModel.getBoard();
        if (board == null)
            return;
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        double screenRelation = Math.min((double) viewWidth / board.boardWidth(),
                (double) viewHeight / board.boardHeight());
        int diff_X = (int) (viewWidth - (board.boardWidth() * screenRelation)) / 2;
        int diff_Y = (int) (viewHeight - (board.boardHeight() * screenRelation)) / 2;
        double scale = screenRelation;
        // --- DRAWING ---
        drawBackground(g2);
        g2.setColor(Color.decode("#222034"));
        g2.fillRect(diff_X, diff_Y, (int) (board.boardWidth() * scale), (int) (board.boardHeight() * scale));
        for (Decoration decor : board.decorations()) {
            drawDecoration(g2, decor, scale, diff_X, diff_Y);
        }
        for (StaticEntity entity : board.entities()) {
            if (!(entity instanceof Pool)) {
                drawEntity(g2, entity, scale, diff_X, diff_Y);
            }
        }
        for (StaticEntity entity : board.entities()) {
            if (entity instanceof Pool) {
                drawEntity(g2, entity, scale, diff_X, diff_Y);
            }
        }
        if (viewableGameModel.getPlayers() != null) {
            for (Player player : viewableGameModel.getPlayers()) {
                drawPlayer(g2, player, scale, diff_X, diff_Y);
            }
        }
        if (viewableGameModel.getEnemies() != null) {
            for (IEnemy enemy : viewableGameModel.getEnemies()) {
                drawEnemy(g2, enemy, scale, diff_X, diff_Y);
            }
        }
        if (!viewableGameModel.isAdsBlocked()) {
            drawAds(g2, diff_X, viewWidth, viewHeight);
        }
        drawAdBlockToggle(g2);
        drawHUD(g2);
    }

    private String formatTime(int ticks) {
        int totalSeconds = ticks / 60;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    private void drawHUD(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.setColor(Color.WHITE);
        int collected = viewableGameModel.getCollectedGems();
        int total = viewableGameModel.getTotalGems();
        g2.drawString("Gems: " + collected + "/" + total, 20, 30);
        g2.drawString("Time: " + formatTime(viewableGameModel.getElapsedTicks()), 20, 65);
        if (viewableGameModel.isPlayerOnBoostPlateWithoutCharge()) {
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.setColor(Color.WHITE);
            String hint = "Hopp for å lade boost";
            FontMetrics hfm = g2.getFontMetrics();
            int hx = (getWidth() - hfm.stringWidth(hint)) / 2;
            g2.drawString(hint, hx, getHeight() - 48);
        }
    }

    private void drawEnemy(Graphics2D g2, IEnemy enemy, double scale, int diff_X, int diff_Y) {
        int x = (int) (diff_X + (enemy.getPos().x() * scale));
        int y = (int) (diff_Y + (enemy.getPos().y() * scale));
        int w = (int) (enemy.getWidth() * scale);
        int h = (int) (enemy.getHeight() * scale);
        int tick = enemyAnimTick.getOrDefault(enemy, 0);
        enemyAnimTick.put(enemy, tick + 1);
        // Advance one animation frame every 5 draw ticks (~12 fps at 60 fps game).
        int animRow, frameCount;
        switch (enemy.getState()) {
            case PATROL -> {
                animRow = SkeletonSpriteSheet.ROW_WALK;
                frameCount = SkeletonSpriteSheet.WALK_FRAMES;
            }
            case ALERT -> {
                animRow = SkeletonSpriteSheet.ROW_IDLE;
                frameCount = SkeletonSpriteSheet.IDLE_FRAMES;
            }
            case CHASE -> {
                animRow = SkeletonSpriteSheet.ROW_ATTACK;
                frameCount = SkeletonSpriteSheet.ATTACK_FRAMES;
            }
            default -> {
                animRow = SkeletonSpriteSheet.ROW_WALK;
                frameCount = SkeletonSpriteSheet.WALK_FRAMES;
            }
        }
        int col = (tick / 5) % frameCount;
        BufferedImage frame = skeletonSheet.getFrame(animRow, col);
        if (frame != null) {
            // Scale the frame so the content bounding box (non-transparent pixels)
            // lines up exactly with the enemy hitbox.
            int contentW = SkeletonSpriteSheet.CONTENT_RIGHT - SkeletonSpriteSheet.CONTENT_LEFT + 1;
            int contentH = SkeletonSpriteSheet.CONTENT_BOTTOM - SkeletonSpriteSheet.CONTENT_TOP + 1;
            float sx = (float) w / contentW;
            float sy = (float) h / contentH;
            int drawW = Math.round(SkeletonSpriteSheet.FRAME_SIZE * sx);
            int drawH = Math.round(SkeletonSpriteSheet.FRAME_SIZE * sy);
            int drawX = x - Math.round(SkeletonSpriteSheet.CONTENT_LEFT * sx);
            int drawY = y - Math.round(SkeletonSpriteSheet.CONTENT_TOP * sy);
            boolean facingLeft = enemy.getVelocityX() < 0;
            if (facingLeft) {
                g2.drawImage(frame, drawX + drawW, drawY, -drawW, drawH, null);
            } else {
                g2.drawImage(frame, drawX, drawY, drawW, drawH, null);
            }
        } else {
            // Fallback: colored rectangle if sprite is unavailable.
            Color enemyColor = switch (enemy.getState()) {
                case PATROL -> ENEMY_PATROL;
                case ALERT -> ENEMY_ALERT;
                case CHASE -> ENEMY_CHASE;
            };
            g2.setColor(enemyColor);
            g2.fillRect(x, y, w, h);
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, w, h);
        }
    }

    private void drawAds(Graphics2D g2, int sideMargin, int viewWidth, int viewHeight) {
        if (sideMargin < 10)
            return; // no room for ads
        int adWidth = sideMargin * 3 / 4;
        int adHeight = viewHeight * 4 / 5;
        int adY = (viewHeight - adHeight) / 2 + viewHeight / 40;
        if (adLeft != null) {
            g2.drawImage(adLeft, (sideMargin - adWidth) / 2, adY, adWidth, adHeight, null);
        }
        if (adRight != null) {
            g2.drawImage(adRight, viewWidth - sideMargin + (sideMargin - adWidth) / 2, adY, adWidth, adHeight, null);
        }
    }

    /**
     * Draws the ad blocker toggle button in the top-right corner.
     * Shows "ADS ON" or "ADS OFF" as a simple text label.
     */
    private void drawAdBlockToggle(Graphics2D g2) {
        boolean blocked = viewableGameModel.isAdsBlocked();
        String label;
        if (blocked) {
            label = "ADBLOCK ENABLED";
        } else {
            label = "ADBLOCK DISABLED";
        }
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        int padding = 10;
        int maxLabelWidth = Math.max(fm.stringWidth("ADBLOCK ENABLED"), fm.stringWidth("ADBLOCK DISABLED"));
        int w = maxLabelWidth + padding * 2;
        int h = fm.getHeight() + padding;
        int x = getWidth() - w - padding;
        int y = padding;
        adBlockToggleBounds.setBounds(x, y, w, h);
        g2.setColor(Color.RED);
        g2.fillRect(x, y, w, h);
        g2.setColor(Color.WHITE);
        g2.drawString(label, x + padding, y + padding / 2 + fm.getAscent());
    }

    private void drawBackground(Graphics2D g2) {
        g2.setColor(theme.getBackgroundColor());
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawEntity(Graphics2D g2, StaticEntity entity, double scale, int diff_X, int diff_Y) {
        if (entity instanceof Pool) {
            drawPool(g2, (Pool) entity, scale, diff_X, diff_Y);
            return;
        }
        if (entity instanceof Gem gem) {
            if (gem.isCollected())
                return;
            int x = (int) (diff_X + (gem.getPos().x() * scale));
            int y = (int) (diff_Y + (gem.getPos().y() * scale));
            int w = (int) (gem.getWidth() * scale);
            int h = (int) (gem.getHeight() * scale);
            boolean isFire = gem.getElement() == ElementState.FIRE;
            BufferedImage sprite = theme.getGem(gem.getElement());
            if (sprite != null) {
                g2.drawImage(sprite, x, y, w, h, null);
            } else {
                g2.setColor(isFire ? Color.RED : Color.CYAN);
                g2.fillRect(x, y, w, h);
            }
            return;
        }
        if (entity instanceof BoostPlatform pad) {
            int px = (int) (diff_X + (pad.getPos().x() * scale));
            int py = (int) (diff_Y + (pad.getPos().y() * scale));
            int pw = (int) (pad.getWidth() * scale);
            int ph = (int) (pad.getHeight() * scale);
            int arc = Math.max(2, Math.min(ph / 2 + 1, 6));
            g2.setColor(new Color(48, 50, 58));
            g2.fillRoundRect(px, py, Math.max(1, pw), Math.max(1, ph), arc, arc);
            int inset = Math.max(1, (int) Math.ceil(ph / 4.0));
            g2.setColor(new Color(108, 112, 124));
            g2.fillRoundRect(px + inset, py + inset, Math.max(1, pw - 2 * inset), Math.max(1, ph - 2 * inset),
                    Math.max(1, arc - inset), Math.max(1, arc - inset));
            g2.setColor(new Color(72, 145, 82));
            int stripeH = Math.max(1, ph / 2);
            g2.fillRoundRect(px + inset + 1, py + inset, Math.max(1, pw - 2 * inset - 2), stripeH,
                    1, 1);
            g2.setColor(new Color(210, 215, 225, 140));
            g2.fillRect(px + inset + 1, py + inset + 1, Math.max(1, pw - 2 * inset - 2), Math.max(1, stripeH / 2));
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(px, py, Math.max(1, pw), Math.max(1, ph), arc, arc);
            if (pw > 10) {
                int rivet = Math.max(2, Math.min(5, ph));
                g2.setColor(new Color(65, 68, 76));
                g2.fillOval(px + 2, py + ph / 2 - rivet / 2, rivet, rivet);
                g2.fillOval(px + pw - 2 - rivet, py + ph / 2 - rivet / 2, rivet, rivet);
            }
            return;
        }
        int x = (int) (diff_X + (entity.getPos().x() * scale));
        int y = (int) (diff_Y + (entity.getPos().y() * scale));
        int w = (int) (entity.getWidth() * scale);
        int h = (int) (entity.getHeight() * scale);
        BufferedImage doorSprite = theme.getDoor();
        if (entity instanceof Door && doorSprite != null) {
            g2.drawImage(doorSprite, x, y, w, h, null);
            return;
        }
        BufferedImage wallTile = theme.getWallTile();
        if (entity instanceof Wall && wallTile != null) {
            int tileSize = Math.max(8, (int) (8 * scale));
            TexturePaint tile = new TexturePaint(wallTile,
                    new Rectangle(x, y, tileSize, tileSize));
            g2.setPaint(tile);
            g2.fillRect(x, y, w, h);
            return;
        }
        if (entity instanceof Door) {
            g2.setColor(Color.decode("#8B4513"));
        } else {
            g2.setColor(Color.GRAY);
        }
        g2.fillRect(x, y, w, h);
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, w, h);
    }

    private void drawDecoration(Graphics2D g2, Decoration decor, double scale, int diff_X, int diff_Y) {
        BufferedImage sprite = theme.getDecorationSprite(decor.sheetX(), decor.sheetY(), decor.sheetW(), decor.sheetH());
        if (sprite == null)
            return;
        int x = (int) (diff_X + (decor.pos().x() * scale));
        int y = (int) (diff_Y + (decor.pos().y() * scale));
        int w = (int) (decor.width() * scale);
        int h = (int) (decor.height() * scale);
        g2.drawImage(sprite, x, y, w, h, null);
    }

    private void drawPool(Graphics2D g2, Pool pool, double scale, int diff_X, int diff_Y) {
        int x = (int) (diff_X + (pool.getPos().x() * scale));
        int y = (int) (diff_Y + (pool.getPos().y() * scale));
        int w = (int) (pool.getWidth() * scale);
        int h = (int) (pool.getHeight() * scale);
        int inset = w / 5;
        // Trapezoid: wide at top, narrow at bottom
        Polygon trap = new Polygon(
                new int[] { x, x + w, x + w - inset, x + inset },
                new int[] { y, y, y + h, y + h },
                4);
        g2.setColor(theme.getPoolColor(pool.getElement()));
        g2.fill(trap);
        // Outline
        g2.setColor(Color.BLACK);
        g2.draw(trap);
    }

    private void drawPlayer(Graphics2D g2, Player player, double scale, int diff_X, int diff_Y) {
        int x = (int) (diff_X + (player.getPos().x() * scale));
        int y = (int) (diff_Y + (player.getPos().y() * scale));
        int w = (int) (player.getWidth() * scale);
        int h = (int) (player.getHeight() * scale);
        // Update facing direction from velocity; keep previous when standing still
        double vx = player.getVelocityX();
        if (vx < -0.05) {
            playerFacingLeft.put(player, true);
        } else if (vx > 0.05) {
            playerFacingLeft.put(player, false);
        }
        boolean facingLeft = playerFacingLeft.getOrDefault(player, false);
        BufferedImage flame = theme.getFlame(player.getElementState());
        if (player.hasJumpBoost() && flame != null) {
            double flameAspect = (double) flame.getWidth() / flame.getHeight();
            int flameH = (int) (h * 1.6);
            int flameW = (int) (flameH * flameAspect);
            int flameX = x + w / 2 - flameW / 2;
            int flameY = y + h - flameH;
            g2.drawImage(flame, flameX, flameY, flameW, flameH, null);
        }
        boolean isFire = player.getElementState() == ElementState.FIRE;
        BufferedImage headSprite = theme.getPlayerHead(player.getElementState());
        BufferedImage bodySprite = theme.getPlayerBody(player.getElementState());
        int headH = (int) (h * (isFire ? 0.62 : 0.48));
        int overlap = isFire ? Math.max(2, h / 8) : Math.max(1, h / 20);
        int bodyExtend = isFire ? Math.max(1, h / 9) : Math.max(2, h / 2);
        int bodyH = h - headH + overlap + bodyExtend;
        if (headSprite != null) {
            if (facingLeft) {
                g2.drawImage(headSprite, x + w, y, -w, headH, null);
            } else {
                g2.drawImage(headSprite, x, y, w, headH, null);
            }
        } else {
            g2.setColor(isFire ? Color.RED : Color.BLUE);
            g2.fillRect(x, y, w, headH);
        }
        if (bodySprite != null) {
            if (facingLeft) {
                g2.drawImage(bodySprite, x + w, y + headH - overlap, -w, bodyH, null);
            } else {
                g2.drawImage(bodySprite, x, y + headH - overlap, w, bodyH, null);
            }
        } else {
            g2.setColor(isFire ? Color.RED : Color.BLUE);
            g2.fillRect(x, y + headH, w, bodyH - overlap);
        }
    }

    private void drawPauseMenu(Graphics2D g2) {
        // Semi-transparent overlay
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, windowWidth, windowHeight);
        g2.setFont(titleFont);
        g2.setColor(Color.WHITE);
        String pauseText = "PAUSED";
        FontMetrics fm = g2.getFontMetrics();
        int x = (windowWidth - fm.stringWidth(pauseText)) / 2;
        g2.drawString(pauseText, x, windowHeight / 3);
        // Menu options
        String[] options = viewableGameModel.getMenuOptions();
        int selectedOption = viewableGameModel.getSelectedMenuOption();
        int startY = windowHeight / 3 + 80;
        int spacing = 60;
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                g2.setFont(selectedMenuFont);
                g2.setColor(Color.decode("#f5a623"));
                String marker = "► ";
                fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(marker + options[i]);
                int tx = (windowWidth - textWidth) / 2;
                g2.drawString(marker + options[i], tx, startY + i * spacing);
            } else {
                g2.setFont(menuFont);
                g2.setColor(Color.WHITE);
                fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(options[i]);
                int tx = (windowWidth - textWidth) / 2;
                g2.drawString(options[i], tx, startY + i * spacing);
            }
        }
        g2.setFont(font);
        g2.setColor(Color.GRAY);
        String hint = "Use ↑↓ to navigate, ENTER to select, ESC to resume";
        fm = g2.getFontMetrics();
        x = (windowWidth - fm.stringWidth(hint)) / 2;
        g2.drawString(hint, x, windowHeight - 50);
    }

    private void drawLevelSelect(Graphics2D g2) {
        g2.setColor(Color.decode("#1a1a2e"));
        g2.fillRect(0, 0, windowWidth, windowHeight);
        g2.setFont(titleFont);
        g2.setColor(Color.WHITE);
        String text = "LEVEL SELECT";
        FontMetrics fm = g2.getFontMetrics();
        int x = (windowWidth - fm.stringWidth(text)) / 2;
        g2.drawString(text, x, 120);
        // Draw available levels
        java.util.List<String> levels = viewableGameModel.getLevelNames();
        Map<String, Integer> bestTicks = viewableGameModel.getLevelBestTicks();
        int selected = viewableGameModel.getSelectedMenuOption();
        int unlockedLevelCount = viewableGameModel.getUnlockedLevelCount();
        g2.setFont(menuFont);
        int startY = 220;
        int spacing = 60;
        for (int i = 0; i < levels.size(); i++) {
            String name = levels.get(i);
            boolean isUnlocked = i < unlockedLevelCount;
            String timeLabel = bestTicks.containsKey(name)
                    ? "Best: " + formatTime(bestTicks.get(name))
                    : "Not completed";
            if (!isUnlocked) {
                g2.setFont(menuFont);
                g2.setColor(Color.GRAY);
                fm = g2.getFontMetrics();
                String lockedName = "[LOCKED] " + name;
                int textWidth = fm.stringWidth(lockedName);
                int tx = (windowWidth - textWidth) / 2;
                g2.drawString(lockedName, tx, startY + i * spacing);
            } else if (i == selected) {
                g2.setFont(selectedMenuFont);
                g2.setColor(Color.decode("#f5a623"));
                String marker = "► ";
                fm = g2.getFontMetrics();
                String fullLabel = marker + name + "   " + timeLabel;
                int textWidth = fm.stringWidth(fullLabel);
                int tx = (windowWidth - textWidth) / 2;
                g2.drawString(fullLabel, tx, startY + i * spacing);
            } else {
                g2.setFont(menuFont);
                g2.setColor(Color.WHITE);
                fm = g2.getFontMetrics();
                String fullLabel = name + "   " + timeLabel;
                int textWidth = fm.stringWidth(fullLabel);
                int tx = (windowWidth - textWidth) / 2;
                g2.drawString(fullLabel, tx, startY + i * spacing);
            }
        }
        g2.setFont(font);
        g2.setColor(Color.GRAY);
        String hint = "Fullfør nivåer i rekkefølge for å låse opp neste";
        fm = g2.getFontMetrics();
        x = (windowWidth - fm.stringWidth(hint)) / 2;
        g2.drawString(hint, x, windowHeight - 50);
    }

    private void drawSettings(Graphics2D g2) {
        g2.setColor(Color.decode("#1a1a2e"));
        g2.fillRect(0, 0, windowWidth, windowHeight);
        g2.setFont(titleFont);
        g2.setColor(Color.WHITE);
        String text = "SETTINGS";
        FontMetrics fm = g2.getFontMetrics();
        int x = (windowWidth - fm.stringWidth(text)) / 2;
        g2.drawString(text, x, 120);
        String[] options = viewableGameModel.getMenuOptions();
        int selectedOption = viewableGameModel.getSelectedMenuOption();
        int startY = 250;
        int spacing = 70;
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                g2.setFont(selectedMenuFont);
                g2.setColor(Color.decode("#f5a623"));
                String marker = "► ";
                fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(marker + options[i]);
                int tx = (windowWidth - textWidth) / 2;
                g2.drawString(marker + options[i], tx, startY + i * spacing);
            } else {
                g2.setFont(menuFont);
                g2.setColor(Color.WHITE);
                fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(options[i]);
                int tx = (windowWidth - textWidth) / 2;
                g2.drawString(options[i], tx, startY + i * spacing);
            }
        }
        g2.setFont(font);
        g2.setColor(Color.GRAY);
        String hint = "Use ↑↓ to navigate, ENTER to toggle, ESC to go back";
        fm = g2.getFontMetrics();
        x = (windowWidth - fm.stringWidth(hint)) / 2;
        g2.drawString(hint, x, windowHeight - 50);
    }

    private void drawGameOver(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, windowWidth, windowHeight);
        g2.setFont(titleFont);
        g2.setColor(Color.decode("#e94560"));
        String text = "GAME OVER";
        FontMetrics fm = g2.getFontMetrics();
        int x = (windowWidth - fm.stringWidth(text)) / 2;
        g2.drawString(text, x, windowHeight / 3);
        // Menu options
        String[] options = viewableGameModel.getMenuOptions();
        int selectedOption = viewableGameModel.getSelectedMenuOption();
        int startY = windowHeight / 3 + 80;
        int spacing = 60;
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                g2.setFont(selectedMenuFont);
                g2.setColor(Color.decode("#f5a623"));
                String marker = "► ";
                fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(marker + options[i]);
                int tx = (windowWidth - textWidth) / 2;
                g2.drawString(marker + options[i], tx, startY + i * spacing);
            } else {
                g2.setFont(menuFont);
                g2.setColor(Color.WHITE);
                fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(options[i]);
                int tx = (windowWidth - textWidth) / 2;
                g2.drawString(options[i], tx, startY + i * spacing);
            }
        }
        g2.setFont(font);
        g2.setColor(Color.GRAY);
        String hint = "Use ↑↓ to navigate, ENTER to select";
        fm = g2.getFontMetrics();
        x = (windowWidth - fm.stringWidth(hint)) / 2;
        g2.drawString(hint, x, windowHeight - 50);
    }

    private void drawHowToPlay(Graphics2D g2) {
        // Dark background, same as the other menus
        g2.setColor(Color.decode("#1a1a2e"));
        g2.fillRect(0, 0, windowWidth, windowHeight);
        // Title at top
        g2.setFont(titleFont);
        g2.setColor(Color.decode("#e94560"));
        String title = "HOW TO PLAY";
        FontMetrics fm = g2.getFontMetrics();
        int x = (windowWidth - fm.stringWidth(title)) / 2;
        g2.drawString(title, x, 100);
        // Instructions as a list
        g2.setFont(menuFont);
        g2.setColor(Color.WHITE);
        String[] lines = {
                "Fireboy:  Move with A / D,  Jump with W",
                "Watergirl:  Move with ← →,  Jump with ↑",
                "",
                "Reach the doors to complete the level",
                "Avoid enemies and hazards",
                "Fireboy dies in water, Watergirl dies in fire"
        };
        int startY = 200;
        int spacing = 55;
        for (int i = 0; i < lines.length; i++) {
            fm = g2.getFontMetrics();
            int lineX = (windowWidth - fm.stringWidth(lines[i])) / 2;
            g2.drawString(lines[i], lineX, startY + i * spacing);
        }
        // Hint at bottom to go back
        g2.setFont(font);
        g2.setColor(Color.GRAY);
        String hint = "Press ESC to go back";
        fm = g2.getFontMetrics();
        x = (windowWidth - fm.stringWidth(hint)) / 2;
        g2.drawString(hint, x, windowHeight - 50);
    }
}
