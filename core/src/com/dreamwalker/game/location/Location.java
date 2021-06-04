package com.dreamwalker.game.location;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.entities.enemy.Archer;
import com.dreamwalker.game.entities.enemy.Enemy;
import com.dreamwalker.game.entities.enemy.Goblin;
import com.dreamwalker.game.entities.enemy.Robber;
import com.dreamwalker.game.entities.player.Player;
import com.dreamwalker.game.items.Hedgehog;
import com.dreamwalker.game.items.ItemInWorld;

import java.util.ArrayList;


public class Location implements Disposable {

    private final TiledMap map;
    private final World world;
    private Vector2 spawnPoint;
    private final ArrayList<Body> exits;
    private final ArrayList<Body> enemiesSP;
    private final ArrayList<Enemy> enemies;
    private final ArrayList<ItemInWorld> items;

    public Location(TiledMap map) {
        this.world = new World(new Vector2(0, 0), true);
        this.map = map;
        this.spawnPoint = new Vector2(2, 2);
        this.exits = new ArrayList<>();
        this.enemiesSP = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.items = new ArrayList<>();
        this.initCollisions();
        this.initSpawnPoint();
        this.initEnemiesSpawnPoint();
        this.initExits();
        this.initEnemies();
        this.initFinalExit();
        this.initLoot();
    }

    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return this.map;
    }

    private void initEnemies() {
        for (int i = 0; i < this.enemiesSP.size(); i++) {
            Enemy newEnemy;
            if (i % 2 == 0) {
                newEnemy = new Goblin(this, enemiesSP.get(i).getPosition());
            } else if (i % 3 == 0) {
                newEnemy = new Archer(this, enemiesSP.get(i).getPosition());
            } else {
                newEnemy = new Robber(this, enemiesSP.get(i).getPosition());
            }
            this.enemies.add(newEnemy);
        }
    }


    private void initCollisions() {
        MapLayer objLayer = map.getLayers().get("collisions");
        MapObjects mapObjects = (objLayer != null) ? objLayer.getObjects() : null;
        Array<RectangleMapObject> collisionsObj = (mapObjects != null) ? mapObjects.getByType(RectangleMapObject.class)
                : null;
        if (collisionsObj != null) {
            for (RectangleMapObject object : collisionsObj) {
                BodyDef bdef = new BodyDef();
                PolygonShape shape = new PolygonShape();
                FixtureDef fdef = new FixtureDef();
                Body body;
                Rectangle rect = (object).getRectangle();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set(
                        (rect.getX() + rect.getWidth() / 2) / DreamWalker.PPM,
                        (rect.getY() + rect.getHeight() / 2) / DreamWalker.PPM
                );
                body = world.createBody(bdef);

                shape.setAsBox(
                        (rect.getWidth() / 2) / DreamWalker.PPM,
                        (rect.getHeight() / 2) / DreamWalker.PPM)
                ;
                fdef.shape = shape;
                body.createFixture(fdef);
                shape.dispose();
            }
        }
    }

    private void initSpawnPoint() {
        MapLayer objLayer = this.map.getLayers().get("spawnpoint");
        MapObjects mpaObjects = (objLayer != null) ? objLayer.getObjects() : null;
        MapObject object = (mpaObjects != null) ? mpaObjects.get(0) : null;
        if (object != null) {
            BodyDef bodyDef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            FixtureDef fixtureDef = new FixtureDef();
            Body body;
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            this.spawnPoint = new Vector2(
                    (rect.getX() + rect.getWidth() / 2) / DreamWalker.PPM,
                    (rect.getY() + rect.getHeight() / 2) / DreamWalker.PPM)
            ;

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(this.spawnPoint);
            fixtureDef.isSensor = true;
            body = this.world.createBody(bodyDef);
            shape.setAsBox(
                    (rect.getWidth() / 2) / DreamWalker.PPM,
                    (rect.getHeight() / 2) / DreamWalker.PPM
            );
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
            shape.dispose();
        }

    }

    private void initExits() {
        MapLayer objLayer = map.getLayers().get("exits");
        MapObjects mapObjects = (objLayer != null) ? objLayer.getObjects() : null;
        Array<RectangleMapObject> exitsObj = (mapObjects != null) ? mapObjects.getByType(RectangleMapObject.class) : null;
        if (exitsObj != null) {
            for (RectangleMapObject object : exitsObj) {
                BodyDef bdef = new BodyDef();
                PolygonShape shape = new PolygonShape();
                FixtureDef fdef = new FixtureDef();
                Body body;
                Rectangle rect = object.getRectangle();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set(
                        (rect.getX() + rect.getWidth() / 2) / DreamWalker.PPM,
                        (rect.getY() + rect.getHeight() / 2) / DreamWalker.PPM
                );
                body = world.createBody(bdef);
                shape.setAsBox(
                        (rect.getWidth() / 2) / DreamWalker.PPM,
                        (rect.getHeight() / 2) / DreamWalker.PPM
                );
                fdef.shape = shape;
                fdef.isSensor = true;
                body.createFixture(fdef);
                body.getFixtureList().get(0).setUserData(this);
                this.exits.add(body);
                shape.dispose();
            }
        }
    }

    private void initEnemiesSpawnPoint() {
        MapLayer objLayer = map.getLayers().get("enemies");
        MapObjects mapObjects = (objLayer != null) ? objLayer.getObjects() : null;
        Array<RectangleMapObject> enemiesSPObj = (mapObjects != null) ? mapObjects.getByType(RectangleMapObject.class) : null;
        if (enemiesSPObj != null) {
            for (RectangleMapObject object : enemiesSPObj) {
                BodyDef bdef = new BodyDef();
                PolygonShape shape = new PolygonShape();
                FixtureDef fdef = new FixtureDef();
                Body body;
                Rectangle rect = object.getRectangle();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set(
                        (rect.getX() + rect.getWidth() / 2) / DreamWalker.PPM,
                        (rect.getY() + rect.getHeight() / 2) / DreamWalker.PPM
                );
                body = world.createBody(bdef);
                shape.setAsBox(
                        (rect.getWidth() / 2) / DreamWalker.PPM,
                        (rect.getHeight() / 2) / DreamWalker.PPM
                );
                fdef.shape = shape;
                fdef.isSensor = true;
                body.createFixture(fdef);
                this.enemiesSP.add(body);
                shape.dispose();
            }
        }
    }

    private void initFinalExit() {
        MapLayer objLayer = this.map.getLayers().get("finalExit");
        MapObjects mpaObjects = (objLayer != null) ? objLayer.getObjects() : null;
        MapObject object = (mpaObjects != null) ? mpaObjects.get(0) : null;
        if (object != null) {
            BodyDef bodyDef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            FixtureDef fixtureDef = new FixtureDef();
            Body body;
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            this.spawnPoint = new Vector2(
                    (rect.getX() + rect.getWidth() / 2) / DreamWalker.PPM,
                    (rect.getY() + rect.getHeight() / 2) / DreamWalker.PPM)
            ;
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(this.spawnPoint);
            fixtureDef.isSensor = true;
            body = this.world.createBody(bodyDef);
            shape.setAsBox(
                    (rect.getWidth() / 2) / DreamWalker.PPM,
                    (rect.getHeight() / 2) / DreamWalker.PPM
            );
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
            body.getFixtureList().get(0).setUserData("Exit");
            shape.dispose();
        }
    }

    private void initLoot() {
        MapLayer objLayer = map.getLayers().get("itemSpawnPoint");
        MapObjects mapObjects = (objLayer != null) ? objLayer.getObjects() : null;
        Array<RectangleMapObject> itemSpawnPoint = (mapObjects != null) ? mapObjects.getByType(RectangleMapObject.class) : null;
        if (itemSpawnPoint != null) {
            for (RectangleMapObject object : itemSpawnPoint) {
                BodyDef bdef = new BodyDef();
                PolygonShape shape = new PolygonShape();
                FixtureDef fdef = new FixtureDef();
                Body body;
                Rectangle rect = object.getRectangle();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set(
                        (rect.getX() + rect.getWidth() / 2) / DreamWalker.PPM,
                        (rect.getY() + rect.getHeight() / 2) / DreamWalker.PPM
                );
                body = world.createBody(bdef);
                shape.setAsBox(
                        (rect.getWidth() / 2) / DreamWalker.PPM,
                        (rect.getHeight() / 2) / DreamWalker.PPM
                );
                fdef.shape = shape;
                fdef.isSensor = true;
                body.createFixture(fdef);
                new ItemInWorld(body.getPosition().x, body.getPosition().y, new Hedgehog(1), this);
                shape.dispose();
            }
        }
    }

    public void enemiesResp() {
        for (Enemy enemy : this.enemies) {
            enemy.respawn();
        }
    }

    public void enemiesUpdate(float deltaTime, Player player) {
        for (Enemy enemy : this.enemies) {
            enemy.update(deltaTime, player);
        }
    }

    public void enemiesRender(SpriteBatch spriteBatch) {
        for (Enemy enemy : this.enemies) {
            enemy.render(spriteBatch);
        }
    }

    public void itemsRender(SpriteBatch spriteBatch) {
        for (int i = 0; i < this.items.size(); i++) {
            items.get(i).render(spriteBatch);

            if (items.get(i).haveToDestroy) {
                items.get(i).dispose();
            }
        }
    }

    public Vector2 getSpawnPoint() {
        return this.spawnPoint;
    }

    public ArrayList<Body> getExits() {
        return this.exits;
    }

    public void addItem(ItemInWorld item) {
        this.items.add(item);
    }

    public void removeItem(ItemInWorld item) {
        this.items.remove(item);
    }

    @Override
    public void dispose() {
        this.map.dispose();
        this.world.dispose();
    }
}
