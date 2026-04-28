package inf112.fireboys.view;

import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

public interface ControllableGameView {
    void setFocusable(boolean focusable);
    void addKeyListener(KeyListener l);
    void addMouseListener(MouseListener l);
    boolean requestFocusInWindow();
    void repaint();
    boolean isAdBlockToggleClicked(Point point);
}
