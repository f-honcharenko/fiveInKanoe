package com.dreamwalker.game.entities.controllers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dreamwalker.game.entities.Entity;

public class MeeleAnimationController extends AnimationController {


    public MeeleAnimationController(Entity entity, String atlasName, String regionName) {

        this.entity = entity;

        this.atlas = new TextureAtlas(atlasName);
        this.entity.setRegion(this.atlas.findRegion(regionName));

        this.standEast = new TextureRegion(this.entity.getTexture(), 0, 23 * 64, 64, 64);
        this.standSouth = new TextureRegion(this.entity.getTexture(), 0, 21 * 64, 64, 64);
        this.standWest = new TextureRegion(this.entity.getTexture(), 0, 19 * 64, 64, 64);
        this.standNorth = new TextureRegion(this.entity.getTexture(), 0, 17 * 64, 64, 64);

        this.currentState = State.STANDING_SOUTH;
        this.prevState = State.STANDING_SOUTH;
        this.stateTimer = 0;

        this.runNorth = initAnimation(18, 8);
        this.runWest = initAnimation(20, 8);
        this.runSouth = initAnimation(22, 8);
        this.runEast = initAnimation(24, 8);

        this.meleeNorth = initAnimation(26, 6);
        this.meleeWest = initAnimation(28, 6);
        this.meleeSouth = initAnimation(30, 6);
        this.meleeEast = initAnimation(32, 6);

        this.entity.setRegion(this.standSouth);
    }


}
