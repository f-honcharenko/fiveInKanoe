package com.dreamwalker.game.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.dreamwalker.game.entities.controllers.MeeleAnimationController;
import com.dreamwalker.game.entities.controllers.SoundController;

public abstract class Entity extends Sprite {
    protected World world;
    protected Body entityBody;
    protected Body attackArea;

    protected MeeleAnimationController animationController;
    protected SoundController soundController;

    protected double health;
    protected double healthMax;
    protected double damage;

    protected float speed;
    protected float attackSpeedCoefficient;

    protected double viewAngle;

    protected boolean isAlive;
    protected boolean isAttacking;
    protected boolean isDamageDealt;

    protected Vector2 spawnPoint;

    protected Entity(){
        this.soundController = new SoundController("sounds/");
    }

    protected abstract void render(SpriteBatch batch);

    protected abstract void receiveDamage(double damage);

    /**
     *
     * @return - позиция игрока по х
     */
    public float getX() {
        return this.entityBody.getPosition().x;
    }

    /**
     *
     * @return - позиция игрока по у
     */
    public float getY() {
        return this.entityBody.getPosition().y;
    }

    public World getWorld() {
        return this.world;
    }

    public double getCurrentHealth() {
        return this.health;
    }

    public double getMaxHealth() {
        return this.healthMax;
    }

    public double getDamage() {
        return this.damage;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setDamageDealt(boolean damageDealt) {
        this.isDamageDealt = damageDealt;
    }

    public Body getBody() {
        return this.entityBody;
    }

    public boolean isAttacking() {
        return this.isAttacking;
    }

    public void setIsAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    public float getAttackSpeedCoefficient() {
        return this.attackSpeedCoefficient;
    }

    public double getViewAngle() {
        return this.viewAngle;
    }

    public void setViewAngle(double viewAngle) {
        this.viewAngle = viewAngle;
    }

    public Body getAttackArea() {
        return this.attackArea;
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public SoundController getSoundController() {
        return this.soundController;
    }
}
