package com.dreamwalker.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dreamwalker.game.generator.LevelGraph;
import com.dreamwalker.game.generator.Vertex;
import com.dreamwalker.game.tools.MapChanger;
import com.dreamwalker.game.tools.ScreenSwitcher;

public class DreamWalker extends Game {
	// Пакет спрайтов
	private SpriteBatch batch;
	private ScreenSwitcher screenSwitcher;

	private Vertex currentVertex;


	public static final float PPM = 100;

	@Override
	public void create() {
		MapChanger.setLevelGraph(new LevelGraph("TestMapPool/", 14));
		MapChanger.getLevelGraph().print();
		MapChanger.setCurrentVertex(MapChanger.getLevelGraph().getStart());

		this.batch = new SpriteBatch();
		this.screenSwitcher = new ScreenSwitcher(this);
		this.screenSwitcher.toMainMenu(MapChanger.getCurrentVertex().getMap(), new Vector2(0,0)); //!

		MapChanger.setScreenSwitcher(this.screenSwitcher);
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
