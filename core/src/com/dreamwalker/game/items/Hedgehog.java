package com.dreamwalker.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.entities.Entity;

public class Hedgehog extends Item {
    private float damage;

    public Hedgehog(int count){
        this.damage = 13.5f;
        this.id = 2;
        this.name = "Hedgehog";
        this.description = "Deals " + this.damage + " to the enemy";
        this.count = count;
        this.setTexture(new Texture("hedgehog_icon.png"));
    }

    @Override
    public void dispose() {

    }

    @Override
    public void usage(Entity entity) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.X)){
            BodyDef hedgehogBD = new BodyDef();
            hedgehogBD.angle = entity.getBody().getAngle();
            hedgehogBD.position.set(entity.getX(), entity.getY());
            hedgehogBD.type = BodyDef.BodyType.KinematicBody;
            Body hedgehogBody = entity.getWorld().createBody(hedgehogBD);

            FixtureDef hedgehogFD = new FixtureDef();
            hedgehogFD.isSensor = true;

            CircleShape shape = new CircleShape();
            shape.setRadius(7 / DreamWalker.PPM);
            hedgehogFD.shape = shape;
            hedgehogBody.createFixture(hedgehogFD);
            hedgehogBody.getFixtureList().get(0).setUserData(this);
            shape.dispose();
        }
    }
}
