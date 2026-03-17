package inf112.fireboys.model.enemy;

import inf112.fireboys.coordinateSystem.Position;
import inf112.fireboys.model.entity.IMovable;
import inf112.fireboys.model.player.IPlayer;

public class Enemy implements IEnemy {
    private Position position;
    private double velocityX, velocityY;
    private double weight;
    private double height;
    private double width;
    private boolean alive;
    private boolean isOnGround = false;
    private double patrolSpeed = 0.3;
    private boolean movingRight = true;
    private int jumpCooldown = 0;

    public Enemy(Position position, double width, double height) {
        this.position = position;
        this.velocityX = patrolSpeed;
        this.velocityY = 0;
        this.weight = 1.0;
        this.height = height;
        this.width = width;
        this.alive = true;
    }

    @Override
    public void update() {
        boolean wasOnGround = isOnGround;
        isOnGround = false;

        if (jumpCooldown > 0) jumpCooldown--;

        boolean blocked = Math.abs(velocityX) < patrolSpeed * 0.5;
        if (blocked) {
            if (wasOnGround && jumpCooldown == 0) {
                velocityY = -3.0;
                jumpCooldown = 60;
            } else if (jumpCooldown == 0) {
                movingRight = !movingRight;
            }
        }

        velocityX = movingRight ? patrolSpeed : -patrolSpeed;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public boolean isOnGround() {
        return isOnGround;
    }

    @Override
    public void setOnGroundTRUE() {
        this.isOnGround = true;
    }

    @Override
    public void setOnGroundFALSE() {
        this.isOnGround = false;
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
    public double getVelocityX() {
        return velocityX;
    }

    @Override
    public void setVelocityX(double vx) {
        this.velocityX = vx;
    }

    @Override
    public double getVelocityY() {
        return velocityY;
    }

    @Override
    public void setVelocityY(double vy) {
        this.velocityY = vy;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void whenContact(IMovable movableEntity) {
        if (movableEntity instanceof IPlayer) {
            IPlayer player = (IPlayer) movableEntity;
            player.kill();
        }
    }
}
