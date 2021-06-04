package com.dreamwalker.game.entities.controllers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.dreamwalker.game.entities.Entity;


abstract public class AnimationController {
    protected enum State {
        STANDING_NORTH, STANDING_EAST, STANDING_SOUTH, STANDING_WEST, RUNNING_NORTH, RUNNING_EAST, RUNNING_SOUTH,
        RUNNING_WEST, MELEE_ATTACKING_NORTH, MELEE_ATTACKING_EAST, MELEE_ATTACKING_SOUTH, MELEE_ATTACKING_WEST
    }

    protected Entity entity;

    protected State currentState;
    protected State prevState;

    protected TextureAtlas atlas;

    protected TextureRegion standSouth;
    protected TextureRegion standWest;
    protected TextureRegion standNorth;
    protected TextureRegion standEast;

    protected Animation runSouth;
    protected Animation runWest;
    protected Animation runNorth;
    protected Animation runEast;

    protected Animation meleeSouth;
    protected Animation meleeWest;
    protected Animation meleeNorth;
    protected Animation meleeEast;

    protected float stateTimer;

    protected Animation initAnimation(int rowOfAtlas, int spritesCount) {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i < spritesCount; i++) {
            frames.add(new TextureRegion(this.entity.getTexture(), i * 64, (rowOfAtlas - 1) * 64, 64, 64));
        }
        Animation newAnim = new Animation(0.1f, frames);
        frames.clear();
        return newAnim;
    }

    public TextureRegion getFrame(float deltaTime) {
        this.currentState = this.getState();
        TextureRegion region;
        float speed = this.entity.getSpeed() + this.stateTimer;
        float attackSpeed = this.entity.getAttackSpeedCoefficient() * this.stateTimer;
        switch (this.currentState) {
            case RUNNING_NORTH:
                region = (TextureRegion) this.runNorth.getKeyFrame(speed, true);
                break;
            case RUNNING_EAST:
                region = (TextureRegion) this.runEast.getKeyFrame(speed, true);
                break;
            case RUNNING_SOUTH:
                region = (TextureRegion) this.runSouth.getKeyFrame(speed, true);
                break;
            case RUNNING_WEST:
                region = (TextureRegion) this.runWest.getKeyFrame(speed, true);
                break;
            case MELEE_ATTACKING_NORTH:
                region = (TextureRegion) this.meleeNorth.getKeyFrame(attackSpeed, true);
                this.entity.setDamageDealt(this.meleeNorth
                        .getKeyFrames()[this.meleeNorth.getKeyFrames().length - 1] == region);
                this.entity.setIsAttacking(false);
                break;
            case MELEE_ATTACKING_EAST:
                region = (TextureRegion) this.meleeEast.getKeyFrame(attackSpeed, true);
                this.entity.setDamageDealt(
                        this.meleeEast.getKeyFrames()[this.meleeEast.getKeyFrames().length - 1] == region);
                this.entity.setIsAttacking(false);
                break;
            case MELEE_ATTACKING_SOUTH:
                region = (TextureRegion) this.meleeSouth.getKeyFrame(attackSpeed, true);
                this.entity.setDamageDealt(this.meleeSouth
                        .getKeyFrames()[this.meleeSouth.getKeyFrames().length - 1] == region);
                this.entity.setIsAttacking(false);
                break;
            case MELEE_ATTACKING_WEST:
                region = (TextureRegion) this.meleeWest.getKeyFrame(attackSpeed, true);
                this.entity.setDamageDealt(
                        this.meleeWest.getKeyFrames()[this.meleeWest.getKeyFrames().length - 1] == region);
                this.entity.setIsAttacking(false);
                break;
            case STANDING_NORTH:
                region = this.standNorth;
                break;
            case STANDING_EAST:
                region = this.standEast;
                break;
            case STANDING_WEST:
                region = this.standWest;
                break;
            case STANDING_SOUTH:
            default:
                region = this.standSouth;
                break;
        }
        this.stateTimer = (this.currentState == this.prevState) ? this.stateTimer + deltaTime : 0;
        this.prevState = this.currentState;
        return region;
    }

    private State getState() {
        float velocityX = this.entity.getBody().getLinearVelocity().x;
        float velocityY = this.entity.getBody().getLinearVelocity().y;
        double viewAngle = this.entity.getViewAngle();
        boolean isAttacking = this.entity.isAttacking();
        boolean isMoving = velocityX != 0 || velocityY != 0;
        if (isMoving && !isAttacking) {
            if ((viewAngle >= 315 && viewAngle <= 360) || (viewAngle >= 0 && viewAngle <= 45)) {
                if (velocityX > 0) {
                    this.runEast.setPlayMode(Animation.PlayMode.NORMAL);
                } else {
                    this.runEast.setPlayMode(Animation.PlayMode.REVERSED);
                }
                return State.RUNNING_EAST;

            } else if (viewAngle > 135 && viewAngle < 225) {
                if (velocityX > 0) {
                    this.runWest.setPlayMode(Animation.PlayMode.REVERSED);
                } else {
                    this.runWest.setPlayMode(Animation.PlayMode.NORMAL);
                }
                return State.RUNNING_WEST;
            } else if (viewAngle > 45 && viewAngle < 135) {
                if (velocityY > 0) {
                    this.runNorth.setPlayMode(Animation.PlayMode.NORMAL);
                } else {
                    this.runNorth.setPlayMode(Animation.PlayMode.REVERSED);
                }
                return State.RUNNING_NORTH;
            } else if (viewAngle >= 225 && viewAngle <= 315) {
                if (velocityY > 0) {
                    this.runSouth.setPlayMode(Animation.PlayMode.REVERSED);
                } else {
                    this.runSouth.setPlayMode(Animation.PlayMode.NORMAL);
                }
                return State.RUNNING_SOUTH;
            }
        } else {
            if (isAttacking) {
                if (viewAngle > 45 && viewAngle < 135) {
                    return State.MELEE_ATTACKING_NORTH;
                } else if (viewAngle > 135 && viewAngle < 225) {
                    this.meleeWest.setPlayMode(Animation.PlayMode.REVERSED);
                    return State.MELEE_ATTACKING_WEST;
                } else if (viewAngle >= 225 && viewAngle <= 315) {
                    return State.MELEE_ATTACKING_SOUTH;
                } else {
                    this.meleeEast.setPlayMode(Animation.PlayMode.REVERSED);
                    return State.MELEE_ATTACKING_EAST;
                }
            } else {
                if (viewAngle > 45 && viewAngle < 135) {
                    return State.STANDING_NORTH;
                } else if (viewAngle > 135 && viewAngle < 225) {
                    return State.STANDING_WEST;
                } else if (viewAngle >= 225 && viewAngle <= 315) {
                    return State.STANDING_SOUTH;
                } else {
                    return State.STANDING_EAST;
                }
            }
        }
        return State.RUNNING_SOUTH;
    }

}
