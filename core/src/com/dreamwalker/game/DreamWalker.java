package com.dreamwalker.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dreamwalker.game.tools.ScreenSwitcher;

public class DreamWalker extends Game {
	// Пакет спрайтов
	private SpriteBatch batch;
	public static final float PPM = 100;

	@Override
	public void create() {
		this.batch = new SpriteBatch();
		ScreenSwitcher.setGame(this);
		ScreenSwitcher.toMainMenu();
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
