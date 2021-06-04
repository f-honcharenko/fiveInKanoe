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
    private final float x;
    private final float y;
    private final Item item;
    private Texture texture;
    private final World world;
    private final Location location;

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
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(this.x, this.y);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        this.box2DBody = this.world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;

        CircleShape shape = new CircleShape();
        shape.setRadius(8 / DreamWalker.PPM);

        fixtureDef.shape = shape;
        this.box2DBody.createFixture(fixtureDef);
        this.box2DBody.getFixtureList().get(0).setUserData(this);
        shape.dispose();
        this.texture = new Texture(item.getTexture().getTextureData());

        this.location.addItem(this);
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public void draw(SpriteBatch sb) {
        sb.draw(
                this.texture,
                this.getX() - 15 / DreamWalker.PPM,
                this.getY() - 15 / DreamWalker.PPM,
                30 / DreamWalker.PPM,
                30 / DreamWalker.PPM
        );
    }

    public Item getItem() {
        return this.item;
    }

    public void render(SpriteBatch sb) {

        this.draw(sb);
    }

    public Body getBody() {
        return this.box2DBody;
    }

    @Override
    public void dispose() {
        this.location.getWorld().destroyBody(this.box2DBody);
        this.location.removeItem(this);
        this.texture.dispose();
    }
}
