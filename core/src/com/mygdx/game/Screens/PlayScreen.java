package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scenes.Hud;

public class PlayScreen implements Screen {
    private MyGdxGame game;

    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    public PlayScreen(MyGdxGame game) {
        this.game = game;
        gameCam = new OrthographicCamera();
        // gamePort = new StretchViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT,
        // gameCam);
        // gamePort = new ScreenViewport(gameCam);
        gamePort = new FitViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, gameCam);
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Maps/room.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        gameCam.position.set(MyGdxGame.V_WIDTH / 2, MyGdxGame.V_HEIGHT / 2, 0);
        System.out.println(gameCam.position.x);
        System.out.println(gameCam.position.y);
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    public void handleInput(float deltaTime) {
        if (Gdx.input.isKeyPressed(Keys.UP)) {
            gameCam.position.y += 100 * deltaTime;
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            gameCam.position.y -= 100 * deltaTime;
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            gameCam.position.x -= 100 * deltaTime;
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            gameCam.position.x += 100 * deltaTime;
        }

    }

    public void update(float deltaTime) {

        handleInput(deltaTime);
        gameCam.update();
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {

        update(delta);

        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}
