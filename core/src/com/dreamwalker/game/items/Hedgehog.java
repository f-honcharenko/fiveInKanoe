package com.dreamwalker.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dreamwalker.game.entities.player.Player;

import java.util.ArrayList;

public class Hedgehog extends Item {
    private final float damage;
    private final ArrayList<HedgehogAmmo> ammoList;

    public Hedgehog(int count) {
        this.damage = 20.5f;
        this.id = 2;
        this.name = "Hedgehog";
        this.description = "Deals " + this.damage + " to the enemy";
        this.count = count;
        this.setTexture(new Texture("hedgehog_icon.png"));
        this.ammoList = new ArrayList<>();
    }

    @Override
    public void dispose() {
        this.getTexture().dispose();
    }

    @Override
    public void usage(Player player) {
        System.out.println(this.count);
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q) && this.count != 0) {
            HedgehogAmmo ammo = new HedgehogAmmo(player, this.damage, "hedgehog_icon.png");
            this.ammoList.add(ammo);
            this.count--;
        }

        for (int i = 0; i < this.ammoList.size(); i++) {
            HedgehogAmmo ammo = this.ammoList.get(i);
            if (ammo.getLifeTime() <= 0) {
                this.ammoList.remove(i);
                ammo.getWorld().destroyBody(ammo.getHedgehogBody());
            } else {
                ammo.move();
                ammo.decreaseLifeTime();
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (HedgehogAmmo ammo : this.ammoList) {
            ammo.draw(batch);
        }
    }

    public int getAmmoCount() {
        return this.ammoList.size();
    }
}
