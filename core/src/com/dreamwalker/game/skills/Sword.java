package com.dreamwalker.game.skills;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.player.Player;

public class Sword extends Sprite implements Disposable {
    private final Player player;
    private final World world;
    private final Body swordBody;
    private final float damage;
    private float lifeTime;
    private Vector2 direction;
    private float speed;

    Sword(Player player, float damage, float lifeTime) {
        super(new Texture("flying_sword.png"));
        this.player = player;
        this.world = player.getWorld();
        this.damage = damage;
        this.lifeTime = lifeTime;
        this.direction = this.player.getPlayersBody().getPosition();
        this.speed = 3.5f;

        BodyDef swordBodyDef = new BodyDef();
        swordBodyDef.angle = this.player.getPlayersBody().getAngle();
        swordBodyDef.position.set(this.player.getX(), this.player.getY());
        swordBodyDef.type = BodyDef.BodyType.DynamicBody;
        this.swordBody = this.world.createBody(swordBodyDef);

        FixtureDef swordFixtureDef = new FixtureDef();
        swordFixtureDef.isSensor = true;
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / DreamWalker.PPM);
        swordFixtureDef.shape = shape;
        this.swordBody.createFixture(swordFixtureDef);
        this.swordBody.getFixtureList().get(0).setUserData(this);
        shape.dispose();

        this.setBounds(0, 0, 75 / DreamWalker.PPM, 75 / DreamWalker.PPM);
    }

    public void move() {
        this.direction.x = MathUtils.cos(this.swordBody.getAngle()) * this.speed;
        this.direction.y = MathUtils.sin(this.swordBody.getAngle()) * this.speed;
        this.swordBody.setLinearVelocity(this.direction.x, this.direction.y);
        this.setRotation((float) Math.toDegrees(this.swordBody.getAngle()));
        this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
        this.setCenter(this.swordBody.getPosition().x, this.swordBody.getPosition().y);
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
        return this.swordBody;
    }

    public World getWorld() {
        return this.world;
    }
}
