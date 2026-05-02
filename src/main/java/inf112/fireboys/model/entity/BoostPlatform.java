package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;

/**
 * Pressure plate: wall-like collision. First jump on the plate only charges
 * (purple glow); the next jump elsewhere uses boost (see GameModel.playerJump).
 */
public class BoostPlatform extends StaticEntity {
    /** Creates a boost platform at the given position with the given size. */
    public BoostPlatform(Position position, double width, double height) {
        super(position, width, height);
    }
}
