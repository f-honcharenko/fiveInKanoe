package com.dreamwalker.game.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.dreamwalker.game.DreamWalker;

public class ItemInWorld implements Disposable {
    private float x;
    private float y;
    private Item item;
    private Texture texture;
    private World world;

    private Body box2DBody;

    public ItemInWorld(float x, float y, Item item, World world) {
        this.x = x;
        this.y = y;
        this.item = item;
        this.world = world;

        this.defineItemInWorld();
    }

    private void defineItemInWorld() {
        // Задача физических свойств для "тела" предмета
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(this.x, this.y);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        // Создаем физическое "тело" предмета в игровом мире на основе свойств
        this.box2DBody = this.world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        // Физические границы врага
        CircleShape shape = new CircleShape();
        shape.setRadius(8 / DreamWalker.PPM);

        fixtureDef.shape = shape;
        this.box2DBody.createFixture(fixtureDef);

        this.box2DBody.getFixtureList().get(0).setUserData(this);
        // Удаляем фигуру, которая была создана для "тела" предмета
        shape.dispose();
        this.texture = item.getTexture();

        // Добавить в список предметов отрисовки
        this.AddToAllItemList();
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public void draw(SpriteBatch sb) {
        sb.draw(this.texture, this.getX() - 15 / DreamWalker.PPM, this.getY() - 15 / DreamWalker.PPM,
                30 / DreamWalker.PPM, 30 / DreamWalker.PPM);
    }

    private void AddToAllItemList() {
        AllItemsInWorld.addItem(this);
    }

    public Item getItem() {
        return this.item;
    }

    @Override
    public void dispose() {
        // ItemInWorld temp = this;
        // this.box2DBody.destroyFixture(this.box2DBody.getFixtureList().get(0));
        // this.item.getTexture().dispose();
        AllItemsInWorld.removeItem(this);
        this.box2DBody.getFixtureList().get(0).setUserData(null);
    }
}
