package com.dreamwalker.game.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Animations {
    private enum State {
        STANDING_NORTH, STANDING_EAST, STANDING_SOUTH, STANDING_WEST, RUNNING_NORTH, RUNNING_EAST, RUNNING_SOUTH,
        RUNNING_WEST, MELEE_ATTACKING_NORTH, MELEE_ATTACKING_EAST, MELEE_ATTACKING_SOUTH, MELEE_ATTACKING_WEST
    };

    private final Enemy enemy;

    private State currentState;
    private State prevState;

    private TextureAtlas atlas;

    private final TextureRegion enemyStandSouth;
    private final TextureRegion enemyStandWest;
    private final TextureRegion enemyStandNorth;
    private final TextureRegion enemyStandEast;

    private final Animation enemyRunSouth;
    private final Animation enemyRunWest;
    private final Animation enemyRunNorth;
    private final Animation enemyRunEast;

    private final Animation enemyMeleeSouth;
    private final Animation enemyMeleeWest;
    private final Animation enemyMeleeNorth;
    private final Animation enemyMeleeEast;

    private float stateTimer;

    /**
     * Добавляет анимации объекту
     * 
     * @param enemy      - обьект
     * @param atlasName  -название атласа
     * @param regionName - название региона
     */
    Animations(Enemy enemy, String atlasName, String regionName) {
        this.enemy = enemy;

        this.atlas = new TextureAtlas(atlasName);
        this.enemy.setRegion(this.atlas.findRegion(regionName));

        this.enemyStandEast = new TextureRegion(this.enemy.getTexture(), 0, 23 * 64, 64, 64);
        this.enemyStandSouth = new TextureRegion(this.enemy.getTexture(), 0, 21 * 64, 64, 64);
        this.enemyStandWest = new TextureRegion(this.enemy.getTexture(), 0, 19 * 64, 64, 64);
        this.enemyStandNorth = new TextureRegion(this.enemy.getTexture(), 0, 17 * 64, 64, 64);

        this.currentState = State.STANDING_SOUTH;
        this.prevState = State.STANDING_SOUTH;
        this.stateTimer = 0;

        this.enemyRunNorth = initAnimation(18, 8);
        this.enemyRunWest = initAnimation(20, 8);
        this.enemyRunSouth = initAnimation(22, 8);
        this.enemyRunEast = initAnimation(24, 8);

        this.enemyMeleeNorth = initAnimation(26, 6);
        this.enemyMeleeWest = initAnimation(28, 6);
        this.enemyMeleeSouth = initAnimation(30, 6);
        this.enemyMeleeEast = initAnimation(32, 6);

        this.enemy.setRegion(this.enemyStandSouth);
    }

    private Animation initAnimation(int rowOfAtlas, int spritesCount) {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i < spritesCount; i++) {
            frames.add(new TextureRegion(this.enemy.getTexture(), i * 64, (rowOfAtlas - 1) * 64, 64, 64));
        }
        Animation newAnim = new Animation(0.1f, frames);
        frames.clear();
        return newAnim;
    }

    TextureRegion getFrame(float deltaTime) {
        this.currentState = this.getState();
        TextureRegion region;
        float speed = this.enemy.getSpeed() + this.stateTimer;
        float attackSpeed = this.enemy.getAttackSpeedCoefficient() * this.stateTimer;
        boolean attack;
        switch (this.currentState) {
            case RUNNING_NORTH:
                region = (TextureRegion) this.enemyRunNorth.getKeyFrame(speed, true);
                break;
            case RUNNING_EAST:
                region = (TextureRegion) this.enemyRunEast.getKeyFrame(speed, true);
                break;
            case RUNNING_SOUTH:
                region = (TextureRegion) this.enemyRunSouth.getKeyFrame(speed, true);
                break;
            case RUNNING_WEST:
                region = (TextureRegion) this.enemyRunWest.getKeyFrame(speed, true);
                break;
            case MELEE_ATTACKING_NORTH:
                region = (TextureRegion) this.enemyMeleeNorth.getKeyFrame(attackSpeed, true);
                // this.enemy.setDamageDealt(
                // this.enemyMeleeNorth.getKeyFrames()[this.enemyMeleeNorth.getKeyFrames().length
                // - 1] == region);
                // this.enemy.setIsAttacking(false);
                break;
            case MELEE_ATTACKING_EAST:
                region = (TextureRegion) this.enemyMeleeEast.getKeyFrame(attackSpeed, true);
                // this.enemy.setDamageDealt(
                // this.enemyMeleeEast.getKeyFrames()[this.enemyMeleeEast.getKeyFrames().length
                // - 1] == region);
                // this.enemy.setIsAttacking(false);
                break;
            case MELEE_ATTACKING_SOUTH:
                region = (TextureRegion) this.enemyMeleeSouth.getKeyFrame(attackSpeed, true);
                // this.enemy.setDamageDealt(
                // this.enemyMeleeSouth.getKeyFrames()[this.enemyMeleeSouth.getKeyFrames().length
                // - 1] == region);
                // this.enemy.setIsAttacking(false);
                break;
            case MELEE_ATTACKING_WEST:
                region = (TextureRegion) this.enemyMeleeWest.getKeyFrame(attackSpeed, true);
                // this.enemy.setDamageDealt(
                // this.enemyMeleeWest.getKeyFrames()[this.enemyMeleeWest.getKeyFrames().length
                // - 1] == region);
                // this.enemy.setIsAttacking(false);
                break;
            case STANDING_NORTH:
                region = this.enemyStandNorth;
                break;
            case STANDING_EAST:
                region = this.enemyStandEast;
                break;
            case STANDING_WEST:
                region = this.enemyStandWest;
                break;
            case STANDING_SOUTH:
            default:
                region = this.enemyStandSouth;
                break;
        }
        this.stateTimer = (this.currentState == this.prevState) ? this.stateTimer + deltaTime : 0;
        this.prevState = this.currentState;
        return region;
    }

    private State getState() {
        State currentState = null;
        float velocityX = this.enemy.getEnemysBody().getLinearVelocity().x;
        float velocityY = this.enemy.getEnemysBody().getLinearVelocity().y;
        double viewAngle = this.enemy.getViewAngle();
        boolean isAttacking = this.enemy.isAttacking();
        boolean isMoving = velocityX != 0 || velocityY != 0;
        if (isMoving && !isAttacking) {
            if ((viewAngle >= 315 && viewAngle <= 360) || (viewAngle >= 0 && viewAngle <= 45)) {
                // currentState = State.RUNNING_EAST;
                if (velocityX > 0) {
                    this.enemyRunEast.setPlayMode(Animation.PlayMode.NORMAL);
                } else {
                    this.enemyRunEast.setPlayMode(Animation.PlayMode.REVERSED);
                }
                return State.RUNNING_EAST;

            } else if (viewAngle >= 135 && viewAngle < 225) {
                // currentState = State.RUNNING_WEST;
                if (velocityX > 0) {
                    this.enemyRunWest.setPlayMode(Animation.PlayMode.REVERSED);
                } else {
                    this.enemyRunWest.setPlayMode(Animation.PlayMode.NORMAL);
                }
                return State.RUNNING_WEST;
            } else if (viewAngle > 45 && viewAngle < 135) {
                // currentState = State.RUNNING_NORTH;
                if (velocityY > 0) {
                    this.enemyRunNorth.setPlayMode(Animation.PlayMode.NORMAL);
                } else {
                    this.enemyRunNorth.setPlayMode(Animation.PlayMode.REVERSED);
                }
                return State.RUNNING_NORTH;
            } else if (viewAngle >= 225 && viewAngle <= 315) {
                // currentState = State.RUNNING_SOUTH;
                if (velocityX > 0) {
                    this.enemyRunSouth.setPlayMode(Animation.PlayMode.REVERSED);
                } else {
                    this.enemyRunSouth.setPlayMode(Animation.PlayMode.NORMAL);
                }
                return State.RUNNING_SOUTH;
            }
        } else {
            if (isAttacking) {
                if (viewAngle > 45 && viewAngle < 135) {
                    // currentState = State.MELEE_ATTACKING_NORTH;
                    return State.MELEE_ATTACKING_NORTH;
                } else if (viewAngle >= 135 && viewAngle < 225) {
                    // currentState = State.MELEE_ATTACKING_WEST;
                    this.enemyMeleeWest.setPlayMode(Animation.PlayMode.REVERSED);
                    return State.MELEE_ATTACKING_WEST;
                } else if (viewAngle >= 225 && viewAngle <= 315) {
                    // currentState = State.MELEE_ATTACKING_SOUTH;
                    return State.MELEE_ATTACKING_SOUTH;
                } else {
                    // currentState = State.MELEE_ATTACKING_EAST;
                    this.enemyMeleeEast.setPlayMode(Animation.PlayMode.REVERSED);
                    return State.MELEE_ATTACKING_EAST;
                }
            } else {
                if (viewAngle > 45 && viewAngle < 135) {
                    // currentState = State.STANDING_NORTH;
                    return State.STANDING_NORTH;
                } else if (viewAngle >= 135 && viewAngle < 225) {
                    // currentState = State.STANDING_WEST;
                    return State.STANDING_WEST;
                } else if (viewAngle >= 225 && viewAngle <= 315) {
                    // currentState = State.STANDING_SOUTH;
                    return State.STANDING_SOUTH;
                } else {
                    // currentState = State.STANDING_EAST;
                    return State.STANDING_EAST;
                }
            }
        }
        return State.RUNNING_SOUTH;
    }
}
