package com.dreamwalker.game.location;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;

public class Location implements Disposable {
    private TiledMap map;
    private World world;

    public Location(TiledMap map){
        this.map = map;
        this.world = new World(new Vector2(0,0), true);

    }

    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return this.map;
    }

    public void setMap(TiledMap map) {
        this.map = map;
    }

    public Vector2 getSpawnPoint(){
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape(); //Нужно диспоузить после createFixture()
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        MapObject object = this.map.getLayers().get(8).getObjects().get(0);
        Rectangle rect = ((RectangleMapObject) object).getRectangle();

        Vector2 spawnPoint = new Vector2(rect.getX() + rect.getWidth() / 2,
                                         rect.getY() + rect.getHeight() / 2);

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(spawnPoint);

        //fixtureDef.isSensor = true;
        body = this.world.createBody(bodyDef);

        shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
        shape.dispose();

        return spawnPoint;
    }

    @Override
    public void dispose() {
        this.map.dispose();
        this.world.dispose();
    }
}
