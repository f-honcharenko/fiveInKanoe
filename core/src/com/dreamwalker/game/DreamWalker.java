package com.dreamwalker.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dreamwalker.game.tools.ScreenSwitcher;

public class DreamWalker extends Game {
	// Пакет спрайтов
	private SpriteBatch batch;
	private ScreenSwitcher screenSwitcher;

	@Override
	public void create() {
		this.batch = new SpriteBatch();
		this.screenSwitcher = new ScreenSwitcher(this);
		this.screenSwitcher.ToMainMenu();
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
