package com.dreamwalker.game.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.entities.player.Player;

public class HedgehogAmmo extends Sprite{
    private float lifeTime;
    private World world;
    private Body hedgehogBody;
    private float damage;
    private float speed;
    private Vector2 direction;

    public HedgehogAmmo(Player player, float damage, Texture texture){
        super(texture);
        this.world = player.getWorld();
        this.damage = damage;
        this.lifeTime = 15;
        this.speed = 3.5f;
        this.direction = player.getBody().getPosition();

        BodyDef hedgehogBD = new BodyDef();
        hedgehogBD.angle = player.getBody().getAngle();
        hedgehogBD.position.set(player.getX(), player.getY());
        hedgehogBD.type = BodyDef.BodyType.DynamicBody;
        this.hedgehogBody = player.getWorld().createBody(hedgehogBD);

        FixtureDef hedgehogFD = new FixtureDef();
        hedgehogFD.isSensor = true;

        CircleShape shape = new CircleShape();
        shape.setRadius(7 / DreamWalker.PPM);
        hedgehogFD.shape = shape;
        this.hedgehogBody.createFixture(hedgehogFD);
        this.hedgehogBody.getFixtureList().get(0).setUserData(this);
        shape.dispose();

        this.setBounds(0, 0, 30 / DreamWalker.PPM, 30 / DreamWalker.PPM);
    }

    public void move(){
        this.direction.x = MathUtils.cos(this.hedgehogBody.getAngle()) * this.speed;
        this.direction.y = MathUtils.sin(this.hedgehogBody.getAngle()) * this.speed;
        this.hedgehogBody.setLinearVelocity(this.direction.x, this.direction.y);
        this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
        this.setCenter(this.hedgehogBody.getPosition().x, this.hedgehogBody.getPosition().y);
        this.rotate(this.lifeTime * 2f);

    }

    public float getLifeTime() {
        return this.lifeTime;
    }

    public World getWorld() {
        return this.world;
    }

    public Body getHedgehogBody() {
        return this.hedgehogBody;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setLifeTime(float lifeTime) {
        this.lifeTime = lifeTime;
    }

    public void decreaseLifeTime() {
        this.lifeTime -= 0.3;
    }
}
