package inf112.fireboys.view;

import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/** Interface for controlling the view — used by GameController to set up input listeners. */
public interface ControllableGameView {
    /**
     * Sets whether the view can receive keyboard focus.
     *
     * @param focusable
     *            true to allow keyboard focus, false to disallow
     */
    void setFocusable(boolean focusable);

    /**
     * Adds a key listener to the view.
     *
     * @param l
     *            the key listener to add
     */
    void addKeyListener(KeyListener l);

    /**
     * Adds a mouse listener to the view.
     *
     * @param l
     *            the mouse listener to add
     */
    void addMouseListener(MouseListener l);

    /**
     * Requests that the view receives keyboard focus.
     *
     * @return true if the focus request was likely to succeed
     */
    boolean requestFocusInWindow();

    /**
     * Triggers a repaint of the view.
     */
    void repaint();

    /**
     * Returns true if the given mouse point is over the ad-block toggle button.
     *
     * @param point
     *            the mouse point to test
     * @return true if the point is over the ad-block toggle button
     */
    boolean isAdBlockToggleClicked(Point point);
}
