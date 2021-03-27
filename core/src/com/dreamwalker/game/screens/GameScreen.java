package com.dreamwalker.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.location.Location;
import com.dreamwalker.game.player.Player;
import com.dreamwalker.game.scenes.Hud;

public class GameScreen implements Screen {
    private DreamWalker game;

    private TmxMapLoader mapLoader;
    private OrthogonalTiledMapRenderer ortMapRender;
    private OrthographicCamera camera;
    private FitViewport viewport;

    private Hud hud;
    private Player player;
    private Location location;
    private Box2DDebugRenderer debugRenderer;

    public GameScreen(DreamWalker game) {
        this.game = game;
        this.mapLoader = new TmxMapLoader();

        location = new Location(this.mapLoader.load("Maps/Start.tmx"));
        location.initColission();

        debugRenderer = new Box2DDebugRenderer();
        hud = new Hud(game.batch);

        player = new Player(location.getWorld(), location.getSpawnPoint());

        camera = new OrthographicCamera();
        ortMapRender = new OrthogonalTiledMapRenderer(location.getMap());
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), this.camera);

    }

    @Override
    public void show() {

    }

    public void update(float deltaTime) {

        camera.update();
        ortMapRender.setView(camera);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        update(delta);

        location.getWorld().step(1 / 60f, 6, 2);
        player.move();

        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // фикс мерцания экрана при изменении

        camera.position.x = player.getX();
        camera.position.y = player.getY();

        ortMapRender.setView((OrthographicCamera) viewport.getCamera());
        ortMapRender.render();

        debugRenderer.render(location.getWorld(), camera.combined);
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);

        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    /*
     * dispose() работает, как free в С и delete в С++ Диспоузить надо все ресурсы,
     * которые используются в игре
     *
     */

    @Override
    public void dispose() {
        this.location.dispose();
        // this.player.dispose();
        this.ortMapRender.dispose();
        this.debugRenderer.dispose();
    }
}
