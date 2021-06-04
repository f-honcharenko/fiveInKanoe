package com.dreamwalker.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.dreamwalker.game.entities.player.Player;

public class PotionHP extends Item {
    private final float regenHealth;

    public PotionHP(int count) {
        this.regenHealth = 8.2f;
        this.id = 0;
        this.name = "HP Potion";
        this.description = "Regenerate health points.";
        this.count = count;
        this.setTexture(new Texture("HPpot_icon.png"));
    }

    @Override
    public void dispose() {
        this.getTexture().dispose();
    }

    @Override
    public void usage(Player player) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.H) && this.count != 0) {
            player.addHealth(this.regenHealth);
            this.count--;
            if (this.count < 0) {
                this.dispose();
            }
        }
    }
}
