package com.dreamwalker.game.entities.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.entities.controllers.MeeleAnimationController;
import com.dreamwalker.game.entities.player.Player;

public class Robber extends Enemy{



    /**
     * Конструктор
     *
     * @param x      - стартовая позиция врага по х
     * @param y      - стартовая позиция врага по у
     */
    public Robber (World world, float x, float y) {
        super(world, x, y);

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
    public Robber (World world, Vector2 enemySpawnPoint) {
        this(world, enemySpawnPoint.x, enemySpawnPoint.y);
    }

    @Override
    public void attack(Player player) {
        // Передлать перезарядку
        // System.out.println(this.isPlayerInArea());
        if (this.isPlayerInArea()) {
            this.setIsAttacking(true);
            if (attackSpeedCounter < attackSpeedMax) {
                attackSpeedCounter++;
            } else {
                attackSpeedCounter = 0;
                System.out.println("DAMAGED!" + this.damage);
                player.receiveDamage(this.damage);
            }
        } else {
            this.setIsAttacking(false);
        }
    }


    public static int rnd(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
