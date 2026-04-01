package inf112.fireboys.view;

import javax.swing.JPanel;

import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.model.ElementState;
import inf112.fireboys.model.GameState;
import inf112.fireboys.model.enemy.IEnemy;
import inf112.fireboys.model.entity.Door;
import inf112.fireboys.model.entity.Gem;
import inf112.fireboys.model.entity.Pool;
import inf112.fireboys.model.entity.StaticEntity;
import inf112.fireboys.model.player.Player;

import java.awt.image.BufferedImage;
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
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * Renders the game using Swing Graphics2D.
 * Draws menus, game entities, players, and enemies based on the current game
 * state.
 */
public class GameView extends JPanel {
    private ViewableGameModel viewableGameModel;
    private SpriteSheet spriteSheet = new SpriteSheet("/bildepakke.png");
    private int windowWidth = 1000;
    private int windowHeight = 600;
    private Font font = new Font("Arial", Font.PLAIN, 12);
    private Font titleFont = new Font("Arial", Font.BOLD, 48);
    private Font menuFont = new Font("Arial", Font.PLAIN, 28);
    private Font selectedMenuFont = new Font("Arial", Font.BOLD, 32);
    private BufferedImage fireboySprite;
    private BufferedImage watergirlSprite;
    private BufferedImage adLeft;
    private BufferedImage adRight;
    private Rectangle adBlockToggleBounds = new Rectangle();
    public GameView(ViewableGameModel viewableGameModel) {
        this.viewableGameModel = viewableGameModel;
        this.setSize(windowWidth, windowHeight);
        this.setFont(font);
        this.setBackground(Color.decode("#35654d"));
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
        SpriteSheet spriteSheet = new SpriteSheet("/spritesheet.png");
        fireboySprite = spriteSheet.getFireboyHead();
        watergirlSprite = spriteSheet.getWatergirlHead();
        try {
            adLeft = ImageIO.read(getClass().getResourceAsStream("/Advertisement_1.png"));
            adRight = ImageIO.read(getClass().getResourceAsStream("/Advertisement_2.png"));
        } catch (IOException e) {
            adLeft = null;
            adRight = null;
        }
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
        if (fireboySprite != null) {
            g2.drawImage(fireboySprite, fireboyX, charY, charSize, charSize, null);
        }
        if (watergirlSprite != null) {
            g2.drawImage(watergirlSprite, watergirlX, charY, charSize, charSize, null);
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
        g2.setColor(Color.decode("#2d5440"));
        g2.fillRect(diff_X, diff_Y, (int) (board.boardWidth() * scale), (int) (board.boardHeight() * scale));
        for (StaticEntity entity : board.entities()) {
            drawEntity(g2, entity, scale, diff_X, diff_Y);
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

    private void drawHUD(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.setColor(Color.WHITE);
        g2.drawString("Score: " + viewableGameModel.getScore(), 20, 30);
    }

    private void drawEnemy(Graphics2D g2, IEnemy enemy, double scale, int diff_X, int diff_Y) {
        int x = (int) (diff_X + (enemy.getPos().x() * scale));
        int y = (int) (diff_Y + (enemy.getPos().y() * scale));
        int w = (int) (enemy.getWidth() * scale);
        int h = (int) (enemy.getHeight() * scale);
        g2.setColor(Color.decode("#8B0000"));
        g2.fillRect(x, y, w, h);
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, w, h);
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
        g2.setColor(Color.decode("#35654d"));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawEntity(Graphics2D g2, StaticEntity entity, double scale, int diff_X, int diff_Y) {
        if (entity instanceof Pool) {
            drawPool(g2, (Pool) entity, scale, diff_X, diff_Y);
            return;
        }
        if (entity instanceof Gem gem && gem.isCollected())
            return;
        int x = (int) (diff_X + (entity.getPos().x() * scale));
        int y = (int) (diff_Y + (entity.getPos().y() * scale));
        int w = (int) (entity.getWidth() * scale);
        int h = (int) (entity.getHeight() * scale);
        if (entity instanceof Gem) {
            BufferedImage diamond = spriteSheet.getFrame("diamond", 0);
            if (diamond != null) {
                g2.drawImage(diamond, x, y, w, h, null);
            } else {
                g2.setColor(Color.CYAN);
                g2.fillRect(x, y, w, h);
            }
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
        // Fill based on element type
        if (pool.getElement() == ElementState.FIRE) {
            g2.setColor(Color.decode("#e25822"));
        } else {
            g2.setColor(Color.decode("#1e90ff"));
        }
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
        BufferedImage sprite = player.getElementState() == ElementState.FIRE ? fireboySprite : watergirlSprite;
        if (sprite != null) {
            g2.drawImage(sprite, x, y, w, h, null);
        } else {
            g2.setColor(player.getElementState() == ElementState.FIRE ? Color.RED : Color.BLUE);
            g2.fillRect(x, y, w, h);
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
        int selected = viewableGameModel.getSelectedMenuOption();
        g2.setFont(menuFont);
        int startY = 220;
        int spacing = 48;
        for (int i = 0; i < levels.size(); i++) {
            String name = levels.get(i);
            if (i == selected) {
                g2.setFont(selectedMenuFont);
                g2.setColor(Color.decode("#f5a623"));
                String marker = "► ";
                fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(marker + name);
                int tx = (windowWidth - textWidth) / 2;
                g2.drawString(marker + name, tx, startY + i * spacing);
            } else {
                g2.setFont(menuFont);
                g2.setColor(Color.WHITE);
                fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(name);
                int tx = (windowWidth - textWidth) / 2;
                g2.drawString(name, tx, startY + i * spacing);
            }
        }
        g2.setFont(font);
        g2.setColor(Color.GRAY);
        String hint = "Press ESC to go back";
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
        g2.setFont(font);
        g2.setColor(Color.GRAY);
        String hint = "Press ESC to go back";
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
