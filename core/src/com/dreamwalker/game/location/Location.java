package com.dreamwalker.game.location;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
// import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.generator.LevelGraph;
import com.dreamwalker.game.generator.Vertex;
import com.dreamwalker.game.tools.MapChanger;
// import com.badlogic.gdx.maps.objects.RectangleMapObject;
// import com.badlogic.gdx.math.Rectangle;

public class Location implements Disposable {
    // Тайловая карта
    private TiledMap map;
    // Физический игровой мир
    private World world;
    private Vector2 spawnPoint;


    /**
     * Конструктор
     */
    public Location(TiledMap map) {
        // Инициализация мира без гравитации
        this.world = new World(new Vector2(0, 0), true);
        this.map = map;
        this.spawnPoint = new Vector2(2, 2);
        this.initAreas();
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

    private void initAreas(){
        //this.initCollisions();
        //this.initSpawnPoint();
        this.initExits();
    }

    /**
     * Метод, отвичающий за создание коллизий
     */
    private void initCollisions() {
        // физические свойства для "областей" коллизий
        BodyDef bdef = new BodyDef();
        // Границы коллизий
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        // Тело коллизий
        Body body;
        for (MapObject object : map.getLayers().get("collisions").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            // Размещение коллизий по крате
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / DreamWalker.PPM, (rect.getY() + rect.getHeight() / 2) / DreamWalker.PPM);
            body = world.createBody(bdef);

            // Задача областей коллизий
            shape.setAsBox((rect.getWidth() / 2) / DreamWalker.PPM, (rect.getHeight() / 2) / DreamWalker.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
        // Удаляем фигуру, которая была создана для коллизии
        shape.dispose();
    }

    private void initSpawnPoint(){
        // физические свойства для точки спавна
        BodyDef bodyDef = new BodyDef();
        // Границы точки спавна
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        // Тело точки спавна
        Body body;

        // Получение данных с самой карты
        MapObject object = this.map.getLayers().get("spawnPoint").getObjects().get(0);
        Rectangle rect = ((RectangleMapObject) object).getRectangle();

        // Координаты точки спавна
        this.spawnPoint = new Vector2((rect.getX() + rect.getWidth() / 2) / DreamWalker.PPM, (rect.getY() + rect.getHeight() / 2) / DreamWalker.PPM);

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

    private void initExits(){
        // физические свойства для "областей" коллизий
        BodyDef bdef = new BodyDef();
        // Границы коллизий
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        // Тело коллизий
        Body body;

        for (RectangleMapObject object : map.getLayers().get("exits").getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = object.getRectangle();
                bdef.type = BodyDef.BodyType.StaticBody;
                // Размещение коллизий по крате
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / DreamWalker.PPM, (rect.getY() + rect.getHeight() / 2) / DreamWalker.PPM);
                body = world.createBody(bdef);
                // Задача областей коллизий
                shape.setAsBox((rect.getWidth() / 2) / DreamWalker.PPM, (rect.getHeight() / 2) / DreamWalker.PPM);
                fdef.shape = shape;
                fdef.isSensor = true;
                body.createFixture(fdef);
                body.getFixtureList().get(0).setUserData(this);
        }


        // Удаляем фигуру, которая была создана для коллизии
        shape.dispose();
    }

    /**
     * Метод, отвечающий за инициализацию точки спавна
     * 
     * @return - координаты точки спавна
     */
    public Vector2 getSpawnPoint() {
        return this.spawnPoint;
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
