package com.dreamwalker.game.entities.enemy;

import com.badlogic.gdx.math.Vector2;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.entities.controllers.MeeleAnimationController;
import com.dreamwalker.game.location.Location;

public class Robber extends Enemy{



    /**
     * Конструктор
     *
     * @param x      - стартовая позиция врага по х
     * @param y      - стартовая позиция врага по у
     */
    public Robber (Location location, float x, float y) {
        super(location, x, y);

        this.animationController = new MeeleAnimationController(this, "Robber.atlas", "Robber");
        this.setBoundsCustom(60f, 60f); // 54
        // this.setBounds(0, 0, 54, 54);
        this.spawnX = x;
        this.spawnY = y;

        this.speed = 0.3f;          //пускай они будут быстрее но наносить меньший урон
        this.damage = 20;           //я еще подумаю на счет скорости, сначала хочеться глянуть как оно будет выглядеть с такой
        this.health = 100;
        this.attackSpeedMax = 130; //возможно нужно будет еще поменять

        this.idleTimerMax = rnd(20, 150);
        this.waitingTimerMax = rnd(20, 120);
        this.agroTimerMax = 200;
        this.idleRadius = 200 / DreamWalker.PPM;
        this.agroRadius = 150 / DreamWalker.PPM;
    }

    /**
     * Конструктор
     *
     * @param enemySpawnPoint - позиция врага
     */
    public Robber (Location location, Vector2 enemySpawnPoint) {
        this(location, enemySpawnPoint.x, enemySpawnPoint.y);
    }



    public static int rnd(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
