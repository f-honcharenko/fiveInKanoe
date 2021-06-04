package com.dreamwalker.game.entities.attacktypes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.entities.player.Player;

public class Arrow extends Sprite implements Disposable {
    private final Player player;
    private final World world;
    private final Body arrowBody;
    private final float damage;
    private float lifeTime;
    private Vector2 direction;
    private float speed;

    Arrow(Player player, float damage, float lifeTime) {
        super(new Texture("flying_arrow.png"));
        this.player = player;
        this.world = player.getWorld();
        this.damage = damage;
        this.lifeTime = lifeTime;
        this.direction = this.player.getBody().getPosition();
        this.speed = 3.5f;

        BodyDef arrowBodyDef = new BodyDef();
        arrowBodyDef.angle = this.player.getBody().getAngle();
        arrowBodyDef.position.set(this.player.getX(), this.player.getY());
        arrowBodyDef.type = BodyDef.BodyType.DynamicBody;
        this.arrowBody = this.world.createBody(arrowBodyDef);

        FixtureDef swordFixtureDef = new FixtureDef();
        swordFixtureDef.isSensor = true;
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / DreamWalker.PPM);
        swordFixtureDef.shape = shape;
        this.arrowBody.createFixture(swordFixtureDef);
        this.arrowBody.getFixtureList().get(0).setUserData(this);
        shape.dispose();

        this.setBounds(0, 0, 75 / DreamWalker.PPM, 75 / DreamWalker.PPM);
    }

    public void move() {
        this.direction.x = MathUtils.cos(this.arrowBody.getAngle()) * this.speed;
        this.direction.y = MathUtils.sin(this.arrowBody.getAngle()) * this.speed;
        this.arrowBody.setLinearVelocity(this.direction.x, this.direction.y);
        this.setRotation((float) Math.toDegrees(this.arrowBody.getAngle()));
        this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
        this.setCenter(this.arrowBody.getPosition().x, this.arrowBody.getPosition().y);
    }

    public void decreaseLifeTime() {
        this.lifeTime -= 0.3;
    }

    public float getLifeTime() {
        return this.lifeTime;
    }

    public float getDamage() {
        return this.damage;
    }

    @Override
    public void dispose() {
        this.getTexture().dispose();
    }

    public Body getBody() {
        return this.arrowBody;
    }

    public World getWorld() {
        return this.world;
    }
}

