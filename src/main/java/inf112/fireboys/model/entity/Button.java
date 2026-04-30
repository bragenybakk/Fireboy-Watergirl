package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;

/** A pressure button that toggles a laser barrier — laser is on when nothing is on the button, off while something is. */
public class Button extends Wall {
    private boolean isPressed = false;
    private LaserWall laserWall = null;
    private final Position trapPos;
    private final double trapWidth;
    private final double trapHeight;

    /** Creates a button at the given position with a laser barrier at trapPos. */
    public Button(Position position, double width, double height,
                  Position trapPos, double trapWidth, double trapHeight) {
        super(position, width, height);
        this.trapPos = trapPos;
        this.trapWidth = trapWidth;
        this.trapHeight = trapHeight;
    }

    /** Returns true if something is currently standing on this button. */
    public boolean isPressed() { return isPressed; }
    /** Resets the pressed flag — called each tick before collisions are checked. */
    public void resetPressed() { isPressed = false; }
    /** Returns the laser wall associated with this button, or null if none. */
    public LaserWall getLaserWall() { return laserWall; }
    /** Stores a reference to the spawned laser wall. */
    public void setLaserWall(LaserWall laserWall) { this.laserWall = laserWall; }
    /** Returns the position where the laser barrier should be. */
    public Position getTrapPos() { return trapPos; }
    /** Returns the width of the laser barrier. */
    public double getTrapWidth() { return trapWidth; }
    /** Returns the height of the laser barrier. */
    public double getTrapHeight() { return trapHeight; }

    @Override
    protected void contactAction(IMovable movableEntity, CollisionSide side) {
        super.contactAction(movableEntity, side);
        if (side == CollisionSide.TOP) {
            isPressed = true;
        }
    }
}
