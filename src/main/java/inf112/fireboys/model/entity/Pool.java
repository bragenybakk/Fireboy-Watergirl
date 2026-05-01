package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.ElementState;

/** A pool of liquid that kills players of the opposite element type. */
public class Pool extends StaticEntity {
    private ElementState element;

    /** Creates a pool at the given position with the given size and element type. */
    public Pool(Position position, double width, double height, ElementState element) {
        super(position, width, height);
        this.element = element;
    }

    /**
     * Returns the element type of this pool (FIRE or WATER).
     *
     * @return the element state of this pool
     */
    public ElementState getElement() {
        return element;
    }

    /**
     * Sets the element type of this pool.
     *
     * @param element
     *            the new element state
     */
    public void setElement(ElementState element) {
        this.element = element;
    }

    /**
     * Returns the y-coordinate of the pool floor at the given x.
     *
     * @param x
     *            the x position to query
     * @return the y-coordinate of the pool floor at x
     */
    public double floorYAt(double x) {
        return getPos().y() + getDepthAt(x);
    }

    /**
     * True if the foot-point is inside the water (below surface, within x-range).
     *
     * @param centerX
     *            the horizontal center of the movable
     * @param bottomY
     *            the bottom y-coordinate (foot) of the movable
     * @return true if the foot is inside the water
     */
    public boolean footIsInWater(double centerX, double bottomY) {
        double poolLeft = getPos().x();
        double poolRight = poolLeft + getWidth();
        return centerX >= poolLeft && centerX <= poolRight
                && bottomY >= getPos().y() && bottomY <= floorYAt(centerX);
    }

    /**
     * True if the player straddles the pool floor (top above it, bottom at/below).
     *
     * @param centerX
     *            the horizontal center of the movable
     * @param topY
     *            the top y-coordinate of the movable
     * @param bottomY
     *            the bottom y-coordinate (foot) of the movable
     * @return true if the movable is straddling the pool floor
     */
    public boolean footOnFloor(double centerX, double topY, double bottomY) {
        double poolLeft = getPos().x();
        double poolRight = poolLeft + getWidth();
        double floor = floorYAt(centerX);
        return centerX >= poolLeft && centerX <= poolRight && topY < floor && bottomY >= floor;
    }

    /**
     * Returns how deep the pool is at the given x position, following the trapezoid shape.
     * Returns 0 if x is outside the pool.
     *
     * @param x
     *            the x position to query
     * @return the depth of the pool at x, or 0 if outside the pool
     */
    public double getDepthAt(double x) {
        double poolLeft = getPos().x();
        double poolRight = poolLeft + getWidth();
        double inset = getWidth() / 5.0;
        if (x < poolLeft || x > poolRight) {
            return 0;
        }
        // Left slope
        if (x < poolLeft + inset) {
            return height * (x - poolLeft) / inset;
        }
        // Right slope
        if (x > poolRight - inset) {
            return height * (poolRight - x) / inset;
        }
        // Center: full depth
        return height;
    }

    @Override
    protected void contactAction(IMovable movableEntity, CollisionSide side) {
        // Handled in GameModel.handlePoolInteraction (foot-point based).
    }
}
