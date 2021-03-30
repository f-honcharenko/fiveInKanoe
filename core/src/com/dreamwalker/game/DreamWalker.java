package com.dreamwalker.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dreamwalker.game.screens.GameScreen;

public class DreamWalker extends Game {
	// Пакет спрайтов
	private  SpriteBatch batch;

	@Override
	public void create() {
		this.batch = new SpriteBatch();
		this.setScreen(new GameScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		this.getScreen().dispose();
	}

	public SpriteBatch getBatch() {
		return this.batch;
	}
}