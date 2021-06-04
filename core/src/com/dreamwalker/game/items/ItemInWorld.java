package com.dreamwalker.game.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.location.Location;

public class ItemInWorld implements Disposable {
    private float x;
    private float y;
    private Item item;
    private Texture texture;
    private World world;
    private Location location;

    public boolean haveToDestroy;

    private Body box2DBody;

    public ItemInWorld(float x, float y, Item item, Location loc) {
        this.x = x;
        this.y = y;
        this.item = item;
        this.location = loc;
        this.world = location.getWorld();
        this.haveToDestroy = false;

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
        this.texture = new Texture(item.getTexture().getTextureData());

        // Добавить в список предметов отрисовки
        // this.AddToAllItemList();
        this.location.addItem(this);
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

    public Item getItem() {
        return this.item;
    }

    public void render(SpriteBatch sb) {

        this.draw(sb);
    }

    public Location getLocation() {
        return this.location;
    }

    public Body getBody() {
        return this.box2DBody;
    }

    @Override
    public void dispose() {
        this.location.getWorld().destroyBody(this.box2DBody);
        //this.location.disposeItems();
        this.location.removeItem(this);
        // this.box2DBody.getFixtureList().get(0).setUserData(null);
        // this.item.getTexture().dispose();
        this.texture.dispose();

    }
}
