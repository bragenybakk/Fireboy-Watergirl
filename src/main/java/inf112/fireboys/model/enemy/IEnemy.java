package inf112.fireboys.model.enemy;

import inf112.fireboys.model.entity.IMovable;

public interface IEnemy extends IMovable {
    void update(); // * AI */

    boolean isAlive();

    boolean isOnGround();

    void setOnGroundTRUE();

    void setOnGroundFALSE();
}
