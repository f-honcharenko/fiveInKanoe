package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
// import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
// import com.badlogic.gdx.graphics.Texture;
// import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
// import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MyGdxGame extends ApplicationAdapter {

	TiledMap tiledMap;
	OrthogonalTiledMapRenderer ortMapRender;
	OrthographicCamera camera;
	FitViewport viewport;
	int[] map;

	@Override
	public void create() {
		camera = new OrthographicCamera();
		tiledMap = new TmxMapLoader().load("Maps/Exit.tmx");
		ortMapRender = new OrthogonalTiledMapRenderer(tiledMap);
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// camera.translate(256, 0);
		camera.update();
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT); // фикс мерцания экрана при изменении
																				// размеров окна
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			camera.position.y++;
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			camera.position.y--;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			camera.position.x--;
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			camera.position.x++;
		}

		// SWITCH MAP [start.tmx]
		if (Gdx.input.isKeyPressed(Keys.Q)) {
			tiledMap = new TmxMapLoader().load("Maps/start.tmx");
			ortMapRender = new OrthogonalTiledMapRenderer(tiledMap);
		}
		// SWITCH MAP [room.tmx]
		if (Gdx.input.isKeyPressed(Keys.W)) {
			tiledMap = new TmxMapLoader().load("Maps/room.tmx");
			ortMapRender = new OrthogonalTiledMapRenderer(tiledMap);
		}
		// SWITCH MAP [exit.tmx]
		if (Gdx.input.isKeyPressed(Keys.E)) {
			tiledMap = new TmxMapLoader().load("Maps/exit.tmx");
			ortMapRender = new OrthogonalTiledMapRenderer(tiledMap);
		}
		camera.update();
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		ortMapRender.setView((OrthographicCamera) viewport.getCamera());
		ortMapRender.render();
	}

	@Override
	public void dispose() {
	}
}
