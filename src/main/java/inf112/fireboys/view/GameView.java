package inf112.fireboys.view;

import javax.swing.JPanel;

import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.coordinateSystem.Decoration;
import inf112.fireboys.model.ElementState;
import inf112.fireboys.model.GameState;
import inf112.fireboys.model.enemy.EnemyState;
import inf112.fireboys.model.enemy.IEnemy;
import inf112.fireboys.model.entity.Box;
import inf112.fireboys.model.entity.Button;
import inf112.fireboys.model.entity.Door;
import inf112.fireboys.model.entity.LaserWall;
import inf112.fireboys.model.entity.Gem;
import inf112.fireboys.model.entity.BoostPlatform;
import inf112.fireboys.model.entity.Pool;
import inf112.fireboys.model.entity.StaticEntity;
import inf112.fireboys.model.entity.Wall;
import inf112.fireboys.model.player.Player;
import inf112.fireboys.view.theme.CastleTheme;
import inf112.fireboys.view.theme.Theme;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.Polygon;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.Point2D;
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
public class GameView extends JPanel implements ControllableGameView {
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
        // --- BACKGROUND ---
        g2.setColor(Color.decode("#0d0d1f"));
        g2.fillRect(0, 0, windowWidth, windowHeight);

        // Fire glow bottom-left
        RadialGradientPaint fireGlow = new RadialGradientPaint(
            new Point2D.Float(0, windowHeight),
            windowHeight * 0.9f,
            new float[]{0f, 1f},
            new Color[]{new Color(200, 50, 0, 110), new Color(0, 0, 0, 0)}
        );
        g2.setPaint(fireGlow);
        g2.fillRect(0, 0, windowWidth, windowHeight);

        // Water glow bottom-right
        RadialGradientPaint waterGlow = new RadialGradientPaint(
            new Point2D.Float(windowWidth, windowHeight),
            windowHeight * 0.9f,
            new float[]{0f, 1f},
            new Color[]{new Color(0, 80, 200, 110), new Color(0, 0, 0, 0)}
        );
        g2.setPaint(waterGlow);
        g2.fillRect(0, 0, windowWidth, windowHeight);

        // --- CHARACTERS (large, bottom corners) ---
        int charH = 320;
        BufferedImage fireboySprite = theme.getPlayerHead(ElementState.FIRE);
        BufferedImage watergirlSprite = theme.getPlayerHead(ElementState.WATER);

        int charY = windowHeight - charH - 100;
        if (fireboySprite != null) {
            int charW = charH * fireboySprite.getWidth() / fireboySprite.getHeight();
            g2.drawImage(fireboySprite, 20, charY, charW, charH, null);
        }
        if (watergirlSprite != null) {
            int charW = charH * watergirlSprite.getWidth() / watergirlSprite.getHeight();
            g2.drawImage(watergirlSprite, windowWidth - 20 - charW, charY, charW, charH, null);
        }

        // --- TITLE ---
        Font bigTitle = new Font("Impact", Font.PLAIN, 82);
        g2.setFont(bigTitle);
        String title = "FIREBOY & WATERGIRL";
        FontMetrics fm = g2.getFontMetrics();
        int titleX = (windowWidth - fm.stringWidth(title)) / 2;
        int titleY = 130;

        // Drop shadow (layered for depth)
        for (int i = 5; i >= 1; i--) {
            g2.setColor(new Color(0, 0, 0, 40 + i * 20));
            g2.drawString(title, titleX + i, titleY + i);
        }

        // Gradient: fire orange → white → water blue
        GradientPaint titleGrad = new GradientPaint(
            titleX, titleY, Color.decode("#ff4400"),
            titleX + fm.stringWidth(title), titleY, Color.decode("#0099ff")
        );
        g2.setPaint(titleGrad);
        g2.drawString(title, titleX, titleY);

        // --- DECORATIVE DIVIDER ---
        int divY = titleY + 18;
        g2.setStroke(new BasicStroke(2f));
        GradientPaint divGrad = new GradientPaint(
            150, divY, Color.decode("#ff4400"),
            windowWidth - 150, divY, Color.decode("#0099ff")
        );
        g2.setPaint(divGrad);
        g2.drawLine(150, divY, windowWidth - 150, divY);
        g2.setStroke(new BasicStroke(1f));

        // --- MENU OPTIONS ---
        Font menuF = new Font("Arial", Font.PLAIN, 30);
        Font selectedF = new Font("Arial", Font.BOLD, 34);
        String[] options = viewableGameModel.getMenuOptions();
        int selectedOption = viewableGameModel.getSelectedMenuOption();
        int startY = divY + 75;
        int spacing = 62;

        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                g2.setFont(selectedF);
                fm = g2.getFontMetrics();
                String label = "► " + options[i];
                int tw = fm.stringWidth(label);
                int ox = (windowWidth - tw) / 2;
                int oy = startY + i * spacing;

                // Glowing selection box
                g2.setColor(new Color(245, 166, 35, 25));
                g2.fillRoundRect(ox - 24, oy - fm.getAscent() - 6, tw + 48, fm.getHeight() + 12, 12, 12);
                g2.setColor(new Color(245, 166, 35, 90));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(ox - 24, oy - fm.getAscent() - 6, tw + 48, fm.getHeight() + 12, 12, 12);
                g2.setStroke(new BasicStroke(1f));

                g2.setColor(Color.decode("#f5a623"));
                g2.drawString(label, ox, oy);
            } else {
                g2.setFont(menuF);
                fm = g2.getFontMetrics();
                int tw = fm.stringWidth(options[i]);
                g2.setColor(new Color(190, 190, 210));
                g2.drawString(options[i], (windowWidth - tw) / 2, startY + i * spacing);
            }
        }

        // --- INSTRUCTIONS ---
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.setColor(new Color(90, 90, 110));
        String instructions = "Use ↑↓ to navigate, ENTER to select";
        fm = g2.getFontMetrics();
        g2.drawString(instructions, (windowWidth - fm.stringWidth(instructions)) / 2, windowHeight - 20);
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
        drawWinFade(g2);
    }

    private void drawWinFade(Graphics2D g2) {
        double progress = viewableGameModel.getWinFadeProgress();
        if (progress <= 0) return;
        int alpha = (int) Math.min(255, progress * 255);
        g2.setColor(new Color(255, 255, 255, alpha));
        g2.fillRect(0, 0, getWidth(), getHeight());
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
        if (entity instanceof Door door) {
            BufferedImage doorSprite;
            if (door.isOpen()) {
                doorSprite = theme.getOpenDoor();
            } else {
                doorSprite = theme.getDoor();
            }
            if (doorSprite == null) doorSprite = theme.getDoor();
            if (doorSprite != null) {
                g2.drawImage(doorSprite, x, y, w, h, null);
                return;
            }
        }
        if (entity instanceof Button button) {
            g2.setColor(button.isPressed() ? new Color(0, 80, 200) : new Color(0, 140, 255));
            g2.fillRect(x, y, w, h);
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, w, h);
            drawLaser(g2, button, scale, diff_X, diff_Y);
            return;
        }
        if (entity instanceof LaserWall) return;
        if (entity instanceof Box) {
            BufferedImage boxSprite = theme.getBox();
            if (boxSprite != null) {
                g2.drawImage(boxSprite, x, y, w, h, null);
                return;
            }
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

    private void drawLaser(Graphics2D g2, Button button, double scale, int diff_X, int diff_Y) {
        int tx = (int) (diff_X + button.getTrapPos().x() * scale);
        int ty = (int) (diff_Y + button.getTrapPos().y() * scale);
        int tw = Math.max(2, (int) (button.getTrapWidth() * scale));
        int th = (int) (button.getTrapHeight() * scale);

        if (button.isPressed()) {
            g2.setColor(new Color(255, 30, 30, 25));
            g2.fillRect(tx - 1, ty, tw + 2, th);
            g2.setColor(new Color(255, 80, 80, 255));
            g2.fillRect(tx, ty, tw, th);
        }

        g2.setColor(Color.WHITE);
        g2.fillRect(tx, ty, tw, 1);
        g2.fillRect(tx, ty + th - 1, tw, 1);
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
        BufferedImage sprite = theme.getPlayerHead(player.getElementState());
        if (sprite != null) {
            int drawW = w * 3 / 4;
            int drawH = drawW * sprite.getHeight() / sprite.getWidth();
            int drawX = x + (w - drawW) / 2;
            int drawY = y + h - drawH;
            if (facingLeft)
                g2.drawImage(sprite, drawX + drawW, drawY, -drawW, drawH, null);
            else
                g2.drawImage(sprite, drawX, drawY, drawW, drawH, null);
        } else {
            g2.setColor(isFire ? Color.RED : Color.BLUE);
            g2.fillRect(x, y, w, h);
        }
    }

    // --- Shared menu helpers ---

    private void drawMenuBackground(Graphics2D g2) {
        g2.setColor(Color.decode("#0d0d1f"));
        g2.fillRect(0, 0, windowWidth, windowHeight);
    }

    private void drawMenuTitle(Graphics2D g2, String title, int y, Color left, Color right) {
        Font f = new Font("Impact", Font.PLAIN, 72);
        g2.setFont(f);
        FontMetrics fm = g2.getFontMetrics();
        int tx = (windowWidth - fm.stringWidth(title)) / 2;
        for (int i = 4; i >= 1; i--) {
            g2.setColor(new Color(0, 0, 0, 40 + i * 20));
            g2.drawString(title, tx + i, y + i);
        }
        g2.setPaint(new GradientPaint(tx, y, left, tx + fm.stringWidth(title), y, right));
        g2.drawString(title, tx, y);
        int divY = y + 18;
        g2.setStroke(new BasicStroke(2f));
        g2.setPaint(new GradientPaint(150, divY, left, windowWidth - 150, divY, right));
        g2.drawLine(150, divY, windowWidth - 150, divY);
        g2.setStroke(new BasicStroke(1f));
    }

    private void drawMenuOptions(Graphics2D g2, String[] options, int selected, int startY, int spacing) {
        Font normalF = new Font("Arial", Font.PLAIN, 30);
        Font selF = new Font("Arial", Font.BOLD, 34);
        for (int i = 0; i < options.length; i++) {
            if (i == selected) {
                g2.setFont(selF);
                FontMetrics fm = g2.getFontMetrics();
                String label = "► " + options[i];
                int tw = fm.stringWidth(label);
                int ox = (windowWidth - tw) / 2;
                int oy = startY + i * spacing;
                g2.setColor(new Color(245, 166, 35, 25));
                g2.fillRoundRect(ox - 24, oy - fm.getAscent() - 6, tw + 48, fm.getHeight() + 12, 12, 12);
                g2.setColor(new Color(245, 166, 35, 90));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(ox - 24, oy - fm.getAscent() - 6, tw + 48, fm.getHeight() + 12, 12, 12);
                g2.setStroke(new BasicStroke(1f));
                g2.setColor(Color.decode("#f5a623"));
                g2.drawString(label, ox, oy);
            } else {
                g2.setFont(normalF);
                FontMetrics fm = g2.getFontMetrics();
                int tw = fm.stringWidth(options[i]);
                g2.setColor(new Color(190, 190, 210));
                g2.drawString(options[i], (windowWidth - tw) / 2, startY + i * spacing);
            }
        }
    }

    private void drawMenuHint(Graphics2D g2, String hint) {
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.setColor(new Color(90, 90, 110));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(hint, (windowWidth - fm.stringWidth(hint)) / 2, windowHeight - 20);
    }

    // --- Menu screens ---

    private void drawPauseMenu(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRect(0, 0, windowWidth, windowHeight);
        int titleY = windowHeight / 3;
        drawMenuTitle(g2, "PAUSED", titleY, Color.decode("#aaaaff"), Color.decode("#ffffff"));
        drawMenuOptions(g2, viewableGameModel.getMenuOptions(),
                viewableGameModel.getSelectedMenuOption(), titleY + 80, 62);
        drawMenuHint(g2, "Use ↑↓ to navigate, ENTER to select, ESC to resume");
    }

    private void drawLevelSelect(Graphics2D g2) {
        drawMenuBackground(g2);
        drawMenuTitle(g2, "LEVEL SELECT", 110, Color.decode("#ff4400"), Color.decode("#0099ff"));

        java.util.List<String> levels = viewableGameModel.getLevelNames();
        Map<String, Integer> bestTicks = viewableGameModel.getLevelBestTicks();
        int selected = viewableGameModel.getSelectedMenuOption();
        int unlockedLevelCount = viewableGameModel.getUnlockedLevelCount();
        Font normalF = new Font("Arial", Font.PLAIN, 30);
        Font selF = new Font("Arial", Font.BOLD, 34);
        int startY = 200;
        int spacing = 62;

        for (int i = 0; i < levels.size(); i++) {
            String name = levels.get(i);
            boolean isUnlocked = i < unlockedLevelCount;
            String timeLabel = bestTicks.containsKey(name)
                    ? "   Best: " + formatTime(bestTicks.get(name))
                    : "   Not completed";
            if (!isUnlocked) {
                g2.setFont(normalF);
                g2.setColor(new Color(80, 80, 100));
                FontMetrics fm = g2.getFontMetrics();
                String locked = "[LOCKED] " + name;
                g2.drawString(locked, (windowWidth - fm.stringWidth(locked)) / 2, startY + i * spacing);
            } else if (i == selected) {
                g2.setFont(selF);
                FontMetrics fm = g2.getFontMetrics();
                String label = "► " + name + timeLabel;
                int tw = fm.stringWidth(label);
                int ox = (windowWidth - tw) / 2;
                int oy = startY + i * spacing;
                g2.setColor(new Color(245, 166, 35, 25));
                g2.fillRoundRect(ox - 24, oy - fm.getAscent() - 6, tw + 48, fm.getHeight() + 12, 12, 12);
                g2.setColor(new Color(245, 166, 35, 90));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(ox - 24, oy - fm.getAscent() - 6, tw + 48, fm.getHeight() + 12, 12, 12);
                g2.setStroke(new BasicStroke(1f));
                g2.setColor(Color.decode("#f5a623"));
                g2.drawString(label, ox, oy);
            } else {
                g2.setFont(normalF);
                FontMetrics fm = g2.getFontMetrics();
                String label = name + timeLabel;
                g2.setColor(new Color(190, 190, 210));
                g2.drawString(label, (windowWidth - fm.stringWidth(label)) / 2, startY + i * spacing);
            }
        }
        drawMenuHint(g2, "Fullfør nivåer i rekkefølge for å låse opp neste");
    }

    private void drawSettings(Graphics2D g2) {
        drawMenuBackground(g2);
        drawMenuTitle(g2, "SETTINGS", 110, Color.decode("#ff4400"), Color.decode("#0099ff"));
        drawMenuOptions(g2, viewableGameModel.getMenuOptions(),
                viewableGameModel.getSelectedMenuOption(), 210, 70);
        drawMenuHint(g2, "Use ↑↓ to navigate, ENTER to toggle, ESC to go back");
    }

    private void drawGameOver(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRect(0, 0, windowWidth, windowHeight);
        int titleY = windowHeight / 3;
        drawMenuTitle(g2, "GAME OVER", titleY, Color.decode("#ff2200"), Color.decode("#ff6600"));
        drawMenuOptions(g2, viewableGameModel.getMenuOptions(),
                viewableGameModel.getSelectedMenuOption(), titleY + 80, 62);
        drawMenuHint(g2, "Use ↑↓ to navigate, ENTER to select");
    }

    private void drawHowToPlay(Graphics2D g2) {
        drawMenuBackground(g2);
        drawMenuTitle(g2, "HOW TO PLAY", 100, Color.decode("#ff4400"), Color.decode("#0099ff"));

        Font bodyF = new Font("Arial", Font.PLAIN, 26);
        g2.setFont(bodyF);
        String[][] sections = {
            {"FIREBOY", "Move with A / D   •   Jump with W"},
            {"WATERGIRL", "Move with ← →   •   Jump with ↑"},
        };
        String[] rules = {
            "Reach the doors to complete the level",
            "Fireboy dies in water  •  Watergirl dies in fire",
            "Avoid enemies and hazards",
        };

        int y = 200;
        for (String[] section : sections) {
            // Character label
            g2.setFont(new Font("Arial", Font.BOLD, 22));
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(section[0].equals("FIREBOY") ? Color.decode("#ff4400") : Color.decode("#0099ff"));
            g2.drawString(section[0], (windowWidth - fm.stringWidth(section[0])) / 2, y);
            y += 34;
            g2.setFont(bodyF);
            fm = g2.getFontMetrics();
            g2.setColor(new Color(200, 200, 220));
            g2.drawString(section[1], (windowWidth - fm.stringWidth(section[1])) / 2, y);
            y += 60;
        }

        // Divider
        g2.setColor(new Color(60, 60, 90));
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(250, y - 15, windowWidth - 250, y - 15);

        g2.setFont(bodyF);
        for (String rule : rules) {
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(new Color(170, 170, 200));
            g2.drawString(rule, (windowWidth - fm.stringWidth(rule)) / 2, y + 10);
            y += 52;
        }

        drawMenuHint(g2, "Press ESC to go back");
    }
}
