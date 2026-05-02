package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;

/**
 * A door that players must reach to finish the level. Opens when a player
 * stands on it.
 */
public class Door extends StaticEntity {
    private boolean isOpen;
    /** Creates a door at the given position with the given size. */
    public Door(Position position, double width, double height) {
        super(position, width, height);
        this.isOpen = false;
    }

    /** Returns true if a player is currently at this door. */
    public boolean isOpen() {
        return isOpen;
    }

    /** Sets whether this door is open or closed. */
    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    @Override
    protected void contactAction(IMovable movableEntity, CollisionSide side) {
        setOpen(true);
    }
}