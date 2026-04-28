package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;

/** A pressure button that spawns a trap wall when stepped on. */
public class Button extends Wall {
    private boolean isPressed = false;
    private boolean trapSpawned = false;
    private final Position trapPos;
    private final double trapWidth;
    private final double trapHeight;

    /**
     * Creates a button at the given position. When pressed, it spawns a trap wall
     * at trapPos with the given trap dimensions.
     */
    public Button(Position position, double width, double height,
                  Position trapPos, double trapWidth, double trapHeight) {
        super(position, width, height);
        this.trapPos = trapPos;
        this.trapWidth = trapWidth;
        this.trapHeight = trapHeight;
    }

    /** Returns true if something is currently standing on this button. */
    public boolean isPressed() { return isPressed; }
    /** Returns true if the trap wall has already been spawned. */
    public boolean isTrapSpawned() { return trapSpawned; }
    /** Marks the trap wall as spawned so it is not spawned again. */
    public void markTrapSpawned() { trapSpawned = true; }
    /** Returns the position where the trap wall should be spawned. */
    public Position getTrapPos() { return trapPos; }
    /** Returns the width of the trap wall. */
    public double getTrapWidth() { return trapWidth; }
    /** Returns the height of the trap wall. */
    public double getTrapHeight() { return trapHeight; }

    @Override
    protected void contactAction(IMovable movableEntity, CollisionSide side) {
        super.contactAction(movableEntity, side);
        if (side == CollisionSide.TOP) {
            isPressed = true;
        }
    }
}
