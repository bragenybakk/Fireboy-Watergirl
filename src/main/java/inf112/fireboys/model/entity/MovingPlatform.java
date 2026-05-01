package inf112.fireboys.model.entity;

import inf112.fireboys.coordinateSystem.Position;

/** A platform that moves back and forth between two points along a direction. */
public class MovingPlatform extends Wall {
    private final Position startPos;
    private final double dirX;
    private final double dirY;
    private final double speed;
    private final double distance;
    private double distanceTraveled = 0;
    private int travelDir = 1;
    private double deltaX = 0;
    private double deltaY = 0;
    /**
     * Creates a moving platform. It moves in the direction (dirX, dirY) at the
     * given speed, bouncing back when it has traveled the given distance.
     */
    public MovingPlatform(Position position, double width, double height,
            double dirX, double dirY, double speed, double distance) {
        super(position, width, height);
        this.startPos = position;
        double len = Math.sqrt(dirX * dirX + dirY * dirY);
        this.dirX = len > 0 ? dirX / len : 0;
        this.dirY = len > 0 ? dirY / len : 0;
        this.speed = speed;
        this.distance = distance;
    }

    /**
     * Advances the platform one step, updating its position and delta values.
     */
    public void tick() {
        deltaX = dirX * speed * travelDir;
        deltaY = dirY * speed * travelDir;
        distanceTraveled += speed * travelDir;
        if (distanceTraveled >= distance) {
            distanceTraveled = distance;
            travelDir = -1;
        } else if (distanceTraveled <= 0) {
            distanceTraveled = 0;
            travelDir = 1;
        }
        setPos(new Position(startPos.x() + dirX * distanceTraveled, startPos.y() + dirY * distanceTraveled));
    }

    /**
     * Returns how far the platform moved horizontally this tick.
     *
     * @return the horizontal displacement this tick
     */
    public double getDeltaX() {
        return deltaX;
    }

    /**
     * Returns how far the platform moved vertically this tick.
     *
     * @return the vertical displacement this tick
     */
    public double getDeltaY() {
        return deltaY;
    }
}
