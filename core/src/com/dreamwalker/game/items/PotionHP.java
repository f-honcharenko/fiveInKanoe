package com.dreamwalker.game.items;

import com.badlogic.gdx.graphics.Texture;
import com.dreamwalker.game.entities.Entity;

public class PotionHP extends Item {
    public PotionHP(int count) {
        this.id = 0;
        this.name = "HP Potion";
        this.description = "Regenerate health points.";
        this.count = count;
        this.setTexture(new Texture("HPpot_icon.png"));
    }

    @Override
    public void dispose() {

    }

    @Override
    public void usage(Entity entity) {

    }
}
