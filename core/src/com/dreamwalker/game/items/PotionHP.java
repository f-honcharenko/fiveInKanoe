package com.dreamwalker.game.items;

import com.badlogic.gdx.graphics.Texture;

public class PotionHP extends Item {
    private int ID;
    private int count;
    private String name;
    private String description;

    public PotionHP(int count) {
        super(0, "HP Potion", "Regenerate health points.", count);
        this.setTexture(new Texture("HPpot_icon.png"));
    }

    @Override
    public void dispose() {

    }
}
