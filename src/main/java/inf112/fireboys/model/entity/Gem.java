package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.ElementState;
import inf112.fireboys.model.player.IPlayer;

/**
 * A collectible gem. Each gem has an element type — only the matching player
 * can collect it.
 */
public class Gem extends StaticEntity {
    private boolean collected;
    private ElementState element;
    /** Creates a gem at the given position with the given size and element type. */
    public Gem(Position position, double width, double height, ElementState element) {
        super(position, width, height);
        this.collected = false;
        this.element = element;
    }

    /**
     * Returns the element type of this gem (FIRE or WATER).
     *
     * @return the element state of this gem
     */
    public ElementState getElement() {
        return element;
    }

    /**
     * Returns true if this gem has already been collected.
     *
     * @return true if this gem has been collected
     */
    public boolean isCollected() {
        return collected;
    }

    @Override
    protected void contactAction(IMovable movableEntity, CollisionSide side) {
        if (!collected && movableEntity instanceof IPlayer player && player.getElementState() == element) {
            this.collected = true;
        }
    }
}
