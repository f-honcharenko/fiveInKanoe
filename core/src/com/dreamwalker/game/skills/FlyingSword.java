package com.dreamwalker.game.skills;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.dreamwalker.game.player.Player;

import java.util.ArrayList;

public class FlyingSword extends ActiveSkill{
    private final World world;
    private final Player player;

    private final ArrayList<Sword> swordsOnScreen;

    private float damage;
    private float lifeTime;
    private int maxSwordsCount;

    public FlyingSword(int hotKey, Player player, World world){
        super(hotKey);
        this.player = player;
        this.world = world;
        this.swordsOnScreen = new ArrayList<>();

        this.mousePosition = null;

        this.maxSwordsCount = 5;
        this.damage = 15;
        this.lifeTime = 35;
    }



    @Override
    public void usage(){
        if(Gdx.input.isKeyJustPressed(super.hotKey)){
            if(this.swordsOnScreen.size() <= this.maxSwordsCount){
                Sword newSword = new Sword(this.player, this.world, this.damage, this.lifeTime);
                this.swordsOnScreen.add(newSword);
            }
        }
        for(int i = 0; i < this.swordsOnScreen.size(); i++){
            Sword currentSword = this.swordsOnScreen.get(i);
            if(currentSword.getLifeTime() <= 0){
                //sword.dispose();
                this.swordsOnScreen.remove(i);
            }
            else {
                currentSword.move();
                currentSword.decreaseLifeTime();
            }
        }
    }

    public ArrayList<Sword> getSwordsOnScreen() {
        return this.swordsOnScreen;
    }
}
