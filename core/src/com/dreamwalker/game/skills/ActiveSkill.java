package com.dreamwalker.game.skills;

public abstract class ActiveSkill implements Skill{
    protected int hotKey;

    protected ActiveSkill(int hotKey){
        this.hotKey = hotKey;
    }

    public abstract void usage();
}
