package com.dreamwalker.game.items;

import com.badlogic.gdx.graphics.Texture;
import com.dreamwalker.game.entities.Entity;

public class PotionMP extends Item {

    public PotionMP(int count) {
        this.id = 1;
        this.name = "MP Potion";
        this.description = "Regenerate mana points.";
        this.count = count;
        this.setTexture(new Texture("MPpot_icon.png"));
    }

    @Override
    public void dispose() {

    }

    @Override
    public void usage(Entity entity) {

    }
}
