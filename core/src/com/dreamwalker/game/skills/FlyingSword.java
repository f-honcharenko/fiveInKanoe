package com.dreamwalker.game.skills;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.dreamwalker.game.player.Player;

import java.util.ArrayList;

public class FlyingSword extends ActiveSkill {
    private final World world;
    private final Player player;

    private final ArrayList<Sword> swordsOnScreen;

    private float damage;
    private float lifeTime;
    private int maxSwordsCount;
    private int manaCost;

    public FlyingSword(int hotKey, Player player) {
        super(hotKey);
        this.player = player;
        this.world = player.getWorld();
        this.swordsOnScreen = new ArrayList<>();

        this.mousePosition = null;

        this.maxSwordsCount = 3;
        this.damage = 15;
        this.lifeTime = 35;
        this.manaCost = 15;
    }

    @Override
    public void render(SpriteBatch batch) {
        for(Sword sword : this.swordsOnScreen){
            sword.draw(batch);
        }
    }

    @Override
    public void usage() {
        if (Gdx.input.isKeyJustPressed(super.hotKey)) {

            if (this.player.getCurrentMana() >= this.manaCost) {
                this.player.manaSpend(this.manaCost);
                if (this.swordsOnScreen.size() <= this.maxSwordsCount) {
                    Sword newSword = new Sword(this.player, this.damage, this.lifeTime);
                    this.swordsOnScreen.add(newSword);
                }
            }
        }
        for (int i = 0; i < this.swordsOnScreen.size(); i++) {
            Sword currentSword = this.swordsOnScreen.get(i);
            if (currentSword.getLifeTime() <= 0) {
                this.swordsOnScreen.remove(i);
                currentSword.dispose();
            } else {
                currentSword.move();
                currentSword.decreaseLifeTime();
            }
        }

    }

    public ArrayList<Sword> getSwordsOnScreen() {
        return this.swordsOnScreen;
    }


}
