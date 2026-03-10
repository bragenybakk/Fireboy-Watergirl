package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.player.IPlayer;

/**
 * Abstract base class for static entities (buttons, doors, levers, pools,
 * etc.).
 * Provides common functionality so subclasses only need to implement specific
 * behavior.
 */
public abstract class StaticEntity implements IStaticEntity {
    protected Position position;
    protected double width;
    protected double height;
    /**
     * Creates a new static entity.
     * 
     * @param position
     *            the position of the entity
     * @param width
     *            the width of the entity
     * @param height
     *            the height of the entity
     */
    public StaticEntity(Position position, double width, double height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    @Override
    public Position getPos() {
        return position;
    }

    @Override
    public void setPos(Position pos) {
        this.position = pos;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void whenContact(IMovable movableEntity) {
        CollisionSide side = calculateCollisionSide(movableEntity);
        contactAction(movableEntity, side);
    }

    private CollisionSide calculateCollisionSide(IMovable movableEntity) {
        double pX = movableEntity.getPos().x();
        double pY = movableEntity.getPos().y();
        double pW = movableEntity.getWidth();
        double pH = movableEntity.getHeight();
        double eX = this.getPos().x();
        double eY = this.getPos().y();
        double eW = this.getWidth();
        double eH = this.getHeight();
        double overlapLeft = (pX + pW) - eX;
        double overlapRight = (eX + eW) - pX;
        double overlapTop = (pY + pH) - eY;
        double overlapBottom = (eY + eH) - pY;
        double min = Math.min(Math.min(overlapLeft, overlapRight), Math.min(overlapTop, overlapBottom));
        if (min == overlapLeft)
            return CollisionSide.LEFT;
        if (min == overlapRight)
            return CollisionSide.RIGHT;
        if (min == overlapTop)
            return CollisionSide.TOP;
        if (min == overlapBottom)
            return CollisionSide.BOTTOM;
        return CollisionSide.NONE;
    }

    protected abstract void contactAction(IMovable movableEntity, CollisionSide side);
}
