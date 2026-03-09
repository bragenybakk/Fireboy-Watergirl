package inf112.skeleton.model.enemy;

import inf112.skeleton.model.entity.IMovable;

public interface IEnemy extends IMovable {
    void update(); // * AI */

    boolean isAlive();
}
