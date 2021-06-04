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

// import com.badlogic.gdx.maps.MapObject;

public class Location implements Disposable {
    // Тайловая карта
    private TiledMap map;
    // Физический игровой мир
    private World world;
    private Vector2 spawnPoint;
    private ArrayList<Body> exits;
    private ArrayList<Body> enemiesSP;
    private ArrayList<Enemy> enemies;
    private ArrayList<ItemInWorld> items;

    /**
     * Конструктор
     */
    public Location(TiledMap map) {
        // Инициализация мира без гравитации
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

    public void setMap(TiledMap map) {
        this.map = map;
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

    /**
     * Метод, отвичающий за создание коллизий
     */
    private void initCollisions() {
        MapLayer objLayer = map.getLayers().get("collisions");
        MapObjects mapObjects = (objLayer != null) ? objLayer.getObjects() : null;
        Array<RectangleMapObject> collisionsObj = (mapObjects != null) ? mapObjects.getByType(RectangleMapObject.class)
                : null;
        if (collisionsObj != null) {
            for (RectangleMapObject object : collisionsObj) {
                // физические свойства для "областей" коллизий
                BodyDef bdef = new BodyDef();
                // Границы коллизий
                PolygonShape shape = new PolygonShape();
                FixtureDef fdef = new FixtureDef();
                // Тело коллизий
                Body body;
                Rectangle rect = (object).getRectangle();
                bdef.type = BodyDef.BodyType.StaticBody;
                // Размещение коллизий по крате
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / DreamWalker.PPM,
                        (rect.getY() + rect.getHeight() / 2) / DreamWalker.PPM);
                body = world.createBody(bdef);

                // Задача областей коллизий
                shape.setAsBox((rect.getWidth() / 2) / DreamWalker.PPM, (rect.getHeight() / 2) / DreamWalker.PPM);
                fdef.shape = shape;
                body.createFixture(fdef);
                // Удаляем фигуру, которая была создана для коллизии
                shape.dispose();
            }
        }
    }

    private void initSpawnPoint() {
        MapLayer objLayer = this.map.getLayers().get("spawnpoint");
        MapObjects mpaObjects = (objLayer != null) ? objLayer.getObjects() : null;
        MapObject object = (mpaObjects != null) ? mpaObjects.get(0) : null;
        if (object != null) {

            // физические свойства для точки спавна
            BodyDef bodyDef = new BodyDef();
            // Границы точки спавна
            PolygonShape shape = new PolygonShape();
            FixtureDef fixtureDef = new FixtureDef();
            // Тело точки спавна
            Body body;
            // Получение данных с самой карты
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            // Координаты точки спавна
            this.spawnPoint = new Vector2((rect.getX() + rect.getWidth() / 2) / DreamWalker.PPM,
                    (rect.getY() + rect.getHeight() / 2) / DreamWalker.PPM);

            // МОЖНО БУДЕТ УДАЛИТЬ НА ФИНАЛЬНОЙ СТАДИИ ПРОКТА
            // |
            // V
            // Размещение точки спавна на карте
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(this.spawnPoint);
            // Область будет проходимой
            fixtureDef.isSensor = true;
            body = this.world.createBody(bodyDef);
            // Задача области спавна
            shape.setAsBox((rect.getWidth() / 2) / DreamWalker.PPM, (rect.getHeight() / 2) / DreamWalker.PPM);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);

            // Удаляем фигуру, которая была создана для коллизии
            shape.dispose();
        }

    }

    private void initExits() {
        MapLayer objLayer = map.getLayers().get("exits");
        MapObjects mapObjects = (objLayer != null) ? objLayer.getObjects() : null;
        Array<RectangleMapObject> exitsObj = (mapObjects != null) ? mapObjects.getByType(RectangleMapObject.class)
                : null;
        if (exitsObj != null) {
            for (RectangleMapObject object : exitsObj) {
                // физические свойства для "областей" коллизий
                BodyDef bdef = new BodyDef();
                // Границы коллизий
                PolygonShape shape = new PolygonShape();
                FixtureDef fdef = new FixtureDef();
                // Тело коллизий
                Body body;
                Rectangle rect = object.getRectangle();
                bdef.type = BodyDef.BodyType.StaticBody;
                // Размещение коллизий по крате
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / DreamWalker.PPM,
                        (rect.getY() + rect.getHeight() / 2) / DreamWalker.PPM);
                body = world.createBody(bdef);
                // Задача областей коллизий
                shape.setAsBox((rect.getWidth() / 2) / DreamWalker.PPM, (rect.getHeight() / 2) / DreamWalker.PPM);
                fdef.shape = shape;
                fdef.isSensor = true;
                body.createFixture(fdef);
                body.getFixtureList().get(0).setUserData(this);
                this.exits.add(body);
                // Удаляем фигуру, которая была создана для коллизии
                shape.dispose();
            }
        }
    }

    private void initEnemiesSpawnPoint() {
        MapLayer objLayer = map.getLayers().get("enemies");
        MapObjects mapObjects = (objLayer != null) ? objLayer.getObjects() : null;
        Array<RectangleMapObject> enemiesSPObj = (mapObjects != null) ? mapObjects.getByType(RectangleMapObject.class)
                : null;
        if (enemiesSPObj != null) {
            for (RectangleMapObject object : enemiesSPObj) {
                BodyDef bdef = new BodyDef();
                PolygonShape shape = new PolygonShape();
                FixtureDef fdef = new FixtureDef();
                Body body;
                Rectangle rect = object.getRectangle();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / DreamWalker.PPM,
                        (rect.getY() + rect.getHeight() / 2) / DreamWalker.PPM);
                body = world.createBody(bdef);
                shape.setAsBox((rect.getWidth() / 2) / DreamWalker.PPM, (rect.getHeight() / 2) / DreamWalker.PPM);
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
            this.spawnPoint = new Vector2((rect.getX() + rect.getWidth() / 2) / DreamWalker.PPM,
                    (rect.getY() + rect.getHeight() / 2) / DreamWalker.PPM);
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(this.spawnPoint);
            fixtureDef.isSensor = true;
            body = this.world.createBody(bodyDef);
            shape.setAsBox((rect.getWidth() / 2) / DreamWalker.PPM, (rect.getHeight() / 2) / DreamWalker.PPM);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
            body.getFixtureList().get(0).setUserData("Exit"); // Сомнения по поводу такого варианта
            shape.dispose();
        }
    }

    private void initLoot() {
        MapLayer objLayer = map.getLayers().get("itemSpawnPoint");
        MapObjects mapObjects = (objLayer != null) ? objLayer.getObjects() : null;
        Array<RectangleMapObject> itemSpawnPoint = (mapObjects != null) ? mapObjects.getByType(RectangleMapObject.class)
                : null;
        if (itemSpawnPoint != null) {
            for (RectangleMapObject object : itemSpawnPoint) {
                BodyDef bdef = new BodyDef();
                PolygonShape shape = new PolygonShape();
                FixtureDef fdef = new FixtureDef();
                Body body;
                Rectangle rect = object.getRectangle();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / DreamWalker.PPM,
                        (rect.getY() + rect.getHeight() / 2) / DreamWalker.PPM);
                body = world.createBody(bdef);
                shape.setAsBox((rect.getWidth() / 2) / DreamWalker.PPM, (rect.getHeight() / 2) / DreamWalker.PPM);
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

            System.out.println(items.get(i).haveToDestroy);
            if (items.get(i).haveToDestroy) {
                items.get(i).dispose();
            }
        }
    }

    /**
     * Метод, отвечающий за инициализацию точки спавна
     * 
     * @return - координаты точки спавна
     */
    public Vector2 getSpawnPoint() {
        return this.spawnPoint;
    }

    public ArrayList<Body> getExits() {
        return this.exits;
    }

    public ArrayList<ItemInWorld> addItem(ItemInWorld item) {
        this.items.add(item);
        return this.items;
    }

    public void disposeItems() {
        this.items = new ArrayList<>();
    }

    public ArrayList<ItemInWorld> removeItem(ItemInWorld item) {
        this.items.remove(item);
        return this.items;
    }

    /**
     * Очистка памяти от ресурсов, используемых игрок
     */
    @Override
    public void dispose() {
        this.map.dispose();
        this.world.dispose();
    }
}
