package com.dreamwalker.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;       //?
import com.badlogic.gdx.utils.Disposable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Player extends Sprite{
    private World world;
    private Body box2DBody;

    private double health;
    private double damage;
    private double armor;
    private float speed;

    public Player(World world, float x, float y){
        this.speed = 70.5f;

        this.world = world;
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        this.box2DBody = this.world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();

        CircleShape shape = new CircleShape();
        shape.setRadius(15);

        fixtureDef.shape = shape;
        this.box2DBody.createFixture(fixtureDef);
        shape.dispose();
    }

    public Player(World world, Vector2 spawnPoint){
        this(world, spawnPoint.x, spawnPoint.y);
    }


    public void move(){
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            //this.box2DBody.applyLinearImpulse(new Vector2(0, this.speed), this.box2DBody.getWorldCenter(), true);
            this.box2DBody.setLinearVelocity(new Vector2(0, this.speed));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            //this.box2DBody.applyLinearImpulse(new Vector2(-this.speed, 0), this.box2DBody.getWorldCenter(), true);
            this.box2DBody.setLinearVelocity(new Vector2(-this.speed, 0));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            //this.box2DBody.applyLinearImpulse(new Vector2(this.speed, 0), this.box2DBody.getWorldCenter(), true);
            this.box2DBody.setLinearVelocity(new Vector2(this.speed, 0));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            //this.box2DBody.applyLinearImpulse(new Vector2(0, -this.speed), this.box2DBody.getWorldCenter(), true);
            this.box2DBody.setLinearVelocity(new Vector2(0, -this.speed));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)){
            this.box2DBody.setLinearVelocity(new Vector2(this.speed, this.speed));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A)){
            this.box2DBody.setLinearVelocity(new Vector2(-this.speed, this.speed));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)){
            this.box2DBody.setLinearVelocity(new Vector2(this.speed, -this.speed));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A)){
            this.box2DBody.setLinearVelocity(new Vector2(-this.speed, -this.speed));
        }
        boolean isMoving = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.A) ||
                            Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.D);
        boolean conflictX = Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.D);
        boolean conflictY = Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.S);
        if(!isMoving || conflictX || conflictY){
            this.box2DBody.setLinearVelocity(0,0);
        }


    }

    public void setBuff(){
        throw new NotImplementedException();
    }

    public void setLevel(World level) {
        if(level == null){
            throw new IllegalArgumentException();
        }
        this.world = level;
    }

    public World getLevel() {
        return world;
    }

    public double getHealth() {
        return health;
    }

    public double getDamage() {
        return damage;
    }

    public double getArmor() {
        return armor;
    }

    public float getSpeed() {
        return speed;
    }

    public Body getBox2DBody() {
        return box2DBody;
    }

    public float getX(){
        return this.box2DBody.getPosition().x;
    }

    public float getY(){
        return this.box2DBody.getPosition().y;
    }


}
