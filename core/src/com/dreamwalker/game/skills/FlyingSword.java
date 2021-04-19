package com.dreamwalker.game.skills;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.dreamwalker.game.player.Player;

public class FlyingSword extends ActiveSkill{
    private final World world;
    private final Player player;

    private Body swordBody;

    public FlyingSword(int hotKey, Player player, World world){
        super(hotKey);
        this.player = player;
        this.world = world;
    }

    @Override
    public void usage(){
        if(Gdx.input.isKeyPressed(this.hotKey)){

        }
    }


}
