package inf112.fireboys.view;

import javax.swing.JPanel;

import inf112.fireboys.coordinateSystem.Board;
import inf112.fireboys.model.ElementState;
import inf112.fireboys.model.GameState;
import inf112.fireboys.model.enemy.IEnemy;
import inf112.fireboys.model.entity.StaticEntity;
import inf112.fireboys.model.player.Player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class GameView extends JPanel {
    private ViewableGameModel viewableGameModel;
    private int windowWidth = 1000;
    private int windowHeight = 600;
    private Font font = new Font("Arial", Font.PLAIN, 12);
    private Font titleFont = new Font("Arial", Font.BOLD, 48);
    private Font menuFont = new Font("Arial", Font.PLAIN, 28);
    private Font selectedMenuFont = new Font("Arial", Font.BOLD, 32);
    public GameView(ViewableGameModel viewableGameModel) {
        this.viewableGameModel = viewableGameModel;
        this.setSize(windowWidth, windowHeight);
        this.setFont(font);
        this.setBackground(Color.decode("#35654d"));
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
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
        }
    }

    private void drawMainMenu(Graphics2D g2) {
        // Bakgrunn
        g2.setColor(Color.decode("#1a1a2e"));
        g2.fillRect(0, 0, windowWidth, windowHeight);
        // Tittel
        g2.setFont(titleFont);
        g2.setColor(Color.decode("#e94560"));
        String title = "FIREBOY & WATERGIRL";
        FontMetrics fm = g2.getFontMetrics();
        int titleX = (windowWidth - fm.stringWidth(title)) / 2;
        g2.drawString(title, titleX, 120);
        // Meny-valg
        String[] options = viewableGameModel.getMenuOptions();
        int selectedOption = viewableGameModel.getSelectedMenuOption();
        int startY = 250;
        int spacing = 60;
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                // Valgt element
                g2.setFont(selectedMenuFont);
                g2.setColor(Color.decode("#f5a623"));
                // Tegn markør
                String marker = "► ";
                fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(marker + options[i]);
                int x = (windowWidth - textWidth) / 2;
                g2.drawString(marker + options[i], x, startY + i * spacing);
            } else {
                // Ikke-valgt element
                g2.setFont(menuFont);
                g2.setColor(Color.WHITE);
                fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(options[i]);
                int x = (windowWidth - textWidth) / 2;
                g2.drawString(options[i], x, startY + i * spacing);
            }
        }
        // Instruksjoner
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
        // --- TEGNING ---
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

    private void drawBackground(Graphics2D g2) {
        g2.setColor(Color.decode("#35654d"));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawEntity(Graphics2D g2, StaticEntity entity, double scale, int diff_X, int diff_Y) {
        int x = (int) (diff_X + (entity.getPos().x() * scale));
        int y = (int) (diff_Y + (entity.getPos().y() * scale));
        int w = (int) (entity.getWidth() * scale);
        int h = (int) (entity.getHeight() * scale);
        g2.setColor(Color.GRAY);
        g2.fillRect(x, y, w, h);
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, w, h);
    }

    private void drawPlayer(Graphics2D g2, Player player, double scale, int diff_X, int diff_Y) {
        int x = (int) (diff_X + (player.getPos().x() * scale));
        int y = (int) (diff_Y + (player.getPos().y() * scale));
        int w = (int) (player.getWidth() * scale);
        int h = (int) (player.getHeight() * scale);
        if (player.getElementState() == ElementState.FIRE) {
            g2.setColor(Color.RED);
        } else {
            g2.setColor(Color.BLUE);
        }
        g2.fillRect(x, y, w, h);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(x, y, w, h);
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
        g2.drawString(pauseText, x, windowHeight / 2);
        g2.setFont(font);
        String hint = "Press ESC to resume";
        fm = g2.getFontMetrics();
        x = (windowWidth - fm.stringWidth(hint)) / 2;
        g2.drawString(hint, x, windowHeight / 2 + 50);
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
        g2.setColor(Color.decode("#1a1a2e"));
        g2.fillRect(0, 0, windowWidth, windowHeight);
        g2.setFont(titleFont);
        g2.setColor(Color.decode("#e94560"));
        String text = "GAME OVER";
        FontMetrics fm = g2.getFontMetrics();
        int x = (windowWidth - fm.stringWidth(text)) / 2;
        g2.drawString(text, x, windowHeight / 2);
    }
}
