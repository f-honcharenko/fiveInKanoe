package com.dreamwalker.game;

import com.badlogic.gdx.Game;
// import com.badlogic.gdx.graphics.Camera;
// import com.badlogic.gdx.graphics.Texture;
// import com.badlogic.gdx.graphics.g2d.SpriteBatch;
// import com.badlogic.gdx.utils.ScreenUtils;
import com.dreamwalker.game.screens.GameScreen;

public class DreamWalker extends Game {


	@Override
	public void create() {
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
}