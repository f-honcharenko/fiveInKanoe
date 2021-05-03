package com.dreamwalker.game.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Animations {
    private enum State {
        STANDING_NORTH, STANDING_EAST, STANDING_SOUTH, STANDING_WEST, RUNNING_NORTH, RUNNING_EAST, RUNNING_SOUTH,
        RUNNING_WEST, MELEE_ATTACKING_NORTH, MELEE_ATTACKING_EAST, MELEE_ATTACKING_SOUTH, MELEE_ATTACKING_WEST
    };

    private final Player player;

    private State currentState;
    private State prevState;

    private TextureAtlas atlas;

    private final TextureRegion playerStandSouth;
    private final TextureRegion playerStandWest;
    private final TextureRegion playerStandNorth;
    private final TextureRegion playerStandEast;

    private final Animation playerRunSouth;
    private final Animation playerRunWest;
    private final Animation playerRunNorth;
    private final Animation playerRunEast;

    private final Animation playerMeleeSouth;
    private final Animation playerMeleeWest;
    private final Animation playerMeleeNorth;
    private final Animation playerMeleeEast;

    private float stateTimer;

    Animations(Player player, String atlasName) {
        this.player = player;

        this.atlas = new TextureAtlas(atlasName);
        this.player.setRegion(this.atlas.findRegion("player"));

        this.playerStandEast = new TextureRegion(this.player.getTexture(), 0, 23 * 64, 64, 64);
        this.playerStandSouth = new TextureRegion(this.player.getTexture(), 0, 21 * 64, 64, 64);
        this.playerStandWest = new TextureRegion(this.player.getTexture(), 0, 19 * 64, 64, 64);
        this.playerStandNorth = new TextureRegion(this.player.getTexture(), 0, 17 * 64, 64, 64);

        this.currentState = State.STANDING_SOUTH;
        this.prevState = State.STANDING_SOUTH;
        this.stateTimer = 0;

        this.playerRunNorth = initAnimation(18, 8);
        this.playerRunWest = initAnimation(20, 8);
        this.playerRunSouth = initAnimation(22, 8);
        this.playerRunEast = initAnimation(24, 8);

        this.playerMeleeNorth = initAnimation(26, 6);
        this.playerMeleeWest = initAnimation(28, 6);
        this.playerMeleeSouth = initAnimation(30, 6);
        this.playerMeleeEast = initAnimation(32, 6);

        this.player.setRegion(this.playerStandSouth);
    }

    private Animation initAnimation(int rowOfAtlas, int spritesCount) {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i < spritesCount; i++) {
            frames.add(new TextureRegion(this.player.getTexture(), i * 64, (rowOfAtlas - 1) * 64, 64, 64));
        }
        Animation newAnim = new Animation(0.1f, frames);
        frames.clear();
        return newAnim;
    }

    TextureRegion getFrame(float deltaTime) {
        this.currentState = this.getState();
        TextureRegion region;
        float speed = this.player.getSpeed() + this.stateTimer;
        float attackSpeed = this.player.getAttackSpeedCoefficient() * this.stateTimer;
        boolean attack;
        switch (this.currentState) {
            case RUNNING_NORTH:
                region = (TextureRegion) this.playerRunNorth.getKeyFrame(speed, true);
                break;
            case RUNNING_EAST:
                region = (TextureRegion) this.playerRunEast.getKeyFrame(speed, true);
                break;
            case RUNNING_SOUTH:
                region = (TextureRegion) this.playerRunSouth.getKeyFrame(speed, true);
                break;
            case RUNNING_WEST:
                region = (TextureRegion) this.playerRunWest.getKeyFrame(speed, true);
                break;
            case MELEE_ATTACKING_NORTH:
                region = (TextureRegion) this.playerMeleeNorth.getKeyFrame(attackSpeed, true);
                this.player.setDamageDealt(this.playerMeleeNorth
                        .getKeyFrames()[this.playerMeleeNorth.getKeyFrames().length - 1] == region);
                this.player.setIsAttacking(false);
                break;
            case MELEE_ATTACKING_EAST:
                region = (TextureRegion) this.playerMeleeEast.getKeyFrame(attackSpeed, true);
                this.player.setDamageDealt(
                        this.playerMeleeEast.getKeyFrames()[this.playerMeleeEast.getKeyFrames().length - 1] == region);
                this.player.setIsAttacking(false);
                break;
            case MELEE_ATTACKING_SOUTH:
                region = (TextureRegion) this.playerMeleeSouth.getKeyFrame(attackSpeed, true);
                this.player.setDamageDealt(this.playerMeleeSouth
                        .getKeyFrames()[this.playerMeleeSouth.getKeyFrames().length - 1] == region);
                this.player.setIsAttacking(false);
                break;
            case MELEE_ATTACKING_WEST:
                region = (TextureRegion) this.playerMeleeWest.getKeyFrame(attackSpeed, true);
                this.player.setDamageDealt(
                        this.playerMeleeWest.getKeyFrames()[this.playerMeleeWest.getKeyFrames().length - 1] == region);
                this.player.setIsAttacking(false);
                break;
            case STANDING_NORTH:
                region = this.playerStandNorth;
                break;
            case STANDING_EAST:
                region = this.playerStandEast;
                break;
            case STANDING_WEST:
                region = this.playerStandWest;
                break;
            case STANDING_SOUTH:
            default:
                region = this.playerStandSouth;
                break;
        }
        this.stateTimer = (this.currentState == this.prevState) ? this.stateTimer + deltaTime : 0;
        this.prevState = this.currentState;
        return region;
    }

    private State getState() {
        State currentState = null;
        float velocityX = this.player.getPlayersBody().getLinearVelocity().x;
        float velocityY = this.player.getPlayersBody().getLinearVelocity().y;
        double viewAngle = this.player.getViewAngle();
        boolean isAttacking = this.player.isAttacking();
        boolean isMoving = velocityX != 0 || velocityY != 0;
        if (isMoving && !isAttacking) {
            if ((viewAngle >= 315 && viewAngle <= 360) || (viewAngle >= 0 && viewAngle <= 45)) {
                currentState = State.RUNNING_EAST;
                if (velocityX > 0) {
                    this.playerRunEast.setPlayMode(Animation.PlayMode.NORMAL);
                } else {
                    this.playerRunEast.setPlayMode(Animation.PlayMode.REVERSED);
                }

            } else if (viewAngle > 135 && viewAngle < 225) {
                currentState = State.RUNNING_WEST;
                if (velocityX > 0) {
                    this.playerRunWest.setPlayMode(Animation.PlayMode.REVERSED);
                } else {
                    this.playerRunWest.setPlayMode(Animation.PlayMode.NORMAL);
                }
            } else if (viewAngle > 45 && viewAngle < 135) {
                currentState = State.RUNNING_NORTH;
                if (velocityY > 0) {
                    this.playerRunNorth.setPlayMode(Animation.PlayMode.NORMAL);
                } else {
                    this.playerRunNorth.setPlayMode(Animation.PlayMode.REVERSED);
                }
            } else if (viewAngle >= 225 && viewAngle <= 315) {
                currentState = State.RUNNING_SOUTH;
                if (velocityX > 0) {
                    this.playerRunSouth.setPlayMode(Animation.PlayMode.REVERSED);
                } else {
                    this.playerRunSouth.setPlayMode(Animation.PlayMode.NORMAL);
                }
            }
        } else {
            if (isAttacking) {
                if (viewAngle > 45 && viewAngle < 135) {
                    currentState = State.MELEE_ATTACKING_NORTH;
                } else if (viewAngle > 135 && viewAngle < 225) {
                    currentState = State.MELEE_ATTACKING_WEST;
                    this.playerMeleeWest.setPlayMode(Animation.PlayMode.REVERSED);
                } else if (viewAngle >= 225 && viewAngle <= 315) {
                    currentState = State.MELEE_ATTACKING_SOUTH;
                } else {
                    currentState = State.MELEE_ATTACKING_EAST;
                    this.playerMeleeEast.setPlayMode(Animation.PlayMode.REVERSED);
                }
            } else {
                if (viewAngle > 45 && viewAngle < 135) {
                    currentState = State.STANDING_NORTH;
                } else if (viewAngle > 135 && viewAngle < 225) {
                    currentState = State.STANDING_WEST;
                } else if (viewAngle >= 225 && viewAngle <= 315) {
                    currentState = State.STANDING_SOUTH;
                } else {
                    currentState = State.STANDING_EAST;
                }
            }
        }
        return currentState;
    }
}
