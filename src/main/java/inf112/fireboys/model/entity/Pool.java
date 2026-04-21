package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.ElementState;
import inf112.fireboys.model.player.IPlayer;

public class Pool extends StaticEntity {
    private ElementState element;
    public Pool(Position position, double width, double height, ElementState element) {
        super(position, width, height);
        this.element = element;
    }

    public ElementState getElement() {
        return element;
    }

    public void setElement(ElementState element) {
        this.element = element;
    }

    /**
     * Returns how deep the pool is at the given x position, following the trapezoid
     * shape.
     * Returns 0 if x is outside the pool.
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
        if (movableEntity instanceof IPlayer player) {
            // Vannoverflaten er helt flat og ligger på bassengets Y-posisjon
            double poolSurfaceY = getPos().y();
            double playerBottom = player.getPos().y() + player.getHeight();
            // ER DE FAKTISK I VANNET?
            // Hvis føttene er over overflaten (i luften), avbryt kollisjonen!
            if (playerBottom < poolSurfaceY + 0.5) {
                return;
            }
            // Standard drepe-logikk hvis de tok på feil vann
            if (player.getElementState() != this.element) {
                player.kill();
            }
        }
    }
}
