package inf112.fireboys.view;

import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/** Interface for controlling the view — used by GameController to set up input listeners. */
public interface ControllableGameView {
    /** Sets whether the view can receive keyboard focus. */
    void setFocusable(boolean focusable);
    /** Adds a key listener to the view. */
    void addKeyListener(KeyListener l);
    /** Adds a mouse listener to the view. */
    void addMouseListener(MouseListener l);
    /** Requests that the view receives keyboard focus. */
    boolean requestFocusInWindow();
    /** Triggers a repaint of the view. */
    void repaint();
    /** Returns true if the given mouse point is over the ad-block toggle button. */
    boolean isAdBlockToggleClicked(Point point);
}
