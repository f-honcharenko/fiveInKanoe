package com.dreamwalker.game.entities.enemy;

import com.badlogic.gdx.math.Vector2;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.entities.controllers.MeeleAnimationController;
import com.dreamwalker.game.entities.player.Player;
import com.dreamwalker.game.location.Location;

public class Archer extends Enemy {

    // private Boolean idleFlag = true;


    /**
     * РљРѕРЅСЃС‚СЂСѓРєС‚РѕСЂ
     *
     * @param x      - СЃС‚Р°СЂС‚РѕРІР°СЏ РїРѕР·РёС†РёСЏ РІСЂР°РіР° РїРѕ С…
     * @param y      - СЃС‚Р°СЂС‚РѕРІР°СЏ РїРѕР·РёС†РёСЏ РІСЂР°РіР° РїРѕ Сѓ
     */
    public Archer(Location location, float x, float y) {
        super(location, x, y);

        this.animationController = new MeeleAnimationController(this, "archer.atlas", "archer");
        this.setBoundsCustom(50f, 50f); // 54
        // this.setBounds(0, 0, 54, 54);
        this.spawnX = x;
        this.spawnY = y;
        this.speed = 0.2f;
        this.damage = 10;
        this.health = 100;
        this.attackSpeedMax = 20;
        this.attackSpeedCounter = 0;
        this.idleTimerMax = rnd(50, 200);
        this.waitingTimerMax = rnd(50, 100);
        this.agroTimerMax = 150;
        this.idleRadius = 250 / DreamWalker.PPM;
        this.agroRadius = 200 / DreamWalker.PPM;
        this.respawnTime = 550;
    }

    /**
     * РљРѕРЅСЃС‚СЂСѓРєС‚РѕСЂ
     *
     * @param enemySpawnPoint - РїРѕР·РёС†РёСЏ РІСЂР°РіР°
     */
    public Archer(Location location, Vector2 enemySpawnPoint) {
        this(location, enemySpawnPoint.x, enemySpawnPoint.y);
    }

    @Override
    public void attack(Player player) {
        if (this.isPlayerInArea()) {
            this.setIsAttacking(true);
            if (attackSpeedCounter < attackSpeedMax) {
                attackSpeedCounter++;
            } else {
                attackSpeedCounter = 0;
                player.receiveDamage(this.damage);
            }
        } else {
            this.setIsAttacking(false);
        }
    }



}
