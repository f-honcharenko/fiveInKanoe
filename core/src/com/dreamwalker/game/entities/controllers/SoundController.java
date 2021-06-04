package com.dreamwalker.game.entities.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

public class SoundController implements Disposable {

    private Sound stepSound;
    private int stepSoundDelayCtr;
    private int soundDelay;

    private Sound swordAttack;
    private int attackSoundDelayCtr;
    private int attackSoundDelay;

    public SoundController(String path){
        this.soundDelay = 22;
        this.stepSoundDelayCtr = this.soundDelay;
        this.stepSound = Gdx.audio.newSound(Gdx.files.internal(path + "step.mp3"));

        this.attackSoundDelay = 19;
        this.attackSoundDelayCtr = this.attackSoundDelay;
        this.swordAttack = Gdx.audio.newSound(Gdx.files.internal(path + "swordAttack.wav"));

    }

    public void playStepSound(float volume){
        if(this.stepSoundDelayCtr == soundDelay){
            this.stepSoundDelayCtr = 0;
            this.stepSound.play(volume);
        }
        else{
            this.stepSoundDelayCtr++;
        }
    }

    public void playAttackSound(float volume){
        if(this.attackSoundDelayCtr == attackSoundDelay){
            this.attackSoundDelayCtr = 0;
            this.swordAttack.play(volume);
        }
        else{
            this.attackSoundDelayCtr++;
        }
    }

    @Override
    public void dispose() {
        this.stepSound.dispose();
        this.swordAttack.dispose();
    }
}
