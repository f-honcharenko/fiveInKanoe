package com.dreamwalker.game.entities.enemy;

import com.badlogic.gdx.math.Vector2;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.entities.controllers.MeeleAnimationController;
import com.dreamwalker.game.location.Location;

public class Robber extends Enemy{


    public Robber (Location location, float x, float y) {
        super(location, x, y);

        this.animationController = new MeeleAnimationController(this, "Robber.atlas", "Robber");
        this.setBoundsCustom(60f, 60f);
        this.spawnX = x;
        this.spawnY = y;

        this.speed = 0.3f;
        this.damage = 35;
        this.health = 100;
        this.attackSpeedMax = 60;

        this.idleTimerMax = rnd(20, 150);
        this.waitingTimerMax = rnd(20, 120);
        this.agroTimerMax = 200;
        this.idleRadius = 200 / DreamWalker.PPM;
        this.agroRadius = 150 / DreamWalker.PPM;
    }

    public Robber (Location location, Vector2 enemySpawnPoint) {
        this(location, enemySpawnPoint.x, enemySpawnPoint.y);
    }


    public static int rnd(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
