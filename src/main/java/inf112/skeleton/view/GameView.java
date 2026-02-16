package inf112.skeleton.view;

import javax.swing.JPanel;

import inf112.skeleton.model.GameState;
import inf112.skeleton.model.player.Player;

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
        // Bakgrunn
        g2.setColor(Color.decode("#35654d"));
        g2.fillRect(0, 0, windowWidth, windowHeight);
        // Tegn spillere
        if (viewableGameModel.getBoard() != null && viewableGameModel.getPlayers() != null) {
            for (Player player : viewableGameModel.getPlayers()) {
                drawPlayer(g2, player);
            }
        }
        // Debug info
        g2.setFont(font);
        g2.setColor(Color.WHITE);
        g2.drawString("Game is running... (Press ESC for pause, Use LEFT/RIGHT arrows to move)", 20, 30);
    }

    private void drawPlayer(Graphics2D g2, Player player) {
        // Konverter spill-koordinater til pixel-koordinater
        int scale = 30; // 30 pixels per spill-enhet
        int x = (int) (player.getPos().x() * scale);
        int y = (int) (player.getPos().y() * scale);
        int width = (int) (player.getWidth() * scale);
        int height = (int) (player.getHeight() * scale);
        // Tegn svart firkant for spilleren
        g2.setColor(Color.BLACK);
        g2.fillRect(x, y, width, height);
        // Tegn border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x, y, width, height);
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
