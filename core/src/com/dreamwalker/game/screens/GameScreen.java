package com.dreamwalker.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.entities.player.Player;
import com.dreamwalker.game.generator.LevelGraph;
import com.dreamwalker.game.location.Location;
import com.dreamwalker.game.scenes.Hud;
import com.dreamwalker.game.tools.MapChanger;
import com.dreamwalker.game.tools.ScreenSwitcher;

public class GameScreen implements Screen {

    private DreamWalker game;

    private OrthogonalTiledMapRenderer ortMapRender;
    private OrthographicCamera camera;
    private ScreenViewport viewport;

    private Hud hud;
    private Player player;

    private Location location;

    public GameScreen(DreamWalker game) {
        MapChanger.setLevelGraph(new LevelGraph("Maps/", 6));
        MapChanger.getLevelGraph().print();
        MapChanger.setCurrentVertex(MapChanger.getLevelGraph().getStart());
        this.game = game;

        this.location = MapChanger.getCurrentVertex().getLocation();

        this.player = new Player(location.getWorld(), this.location.getSpawnPoint());

        this.hud = new Hud(this.game.getBatch(), this.player);

        this.camera = new OrthographicCamera();
        this.ortMapRender = new OrthogonalTiledMapRenderer(null, 1 / DreamWalker.PPM);

        this.camera.setToOrtho(false, Gdx.graphics.getWidth() / DreamWalker.PPM,
                Gdx.graphics.getHeight() / DreamWalker.PPM);

        this.viewport = new ScreenViewport(this.camera);
        this.camera.zoom = 0.6f;

    }

    @Override
    public void show() {

    }

    public void update(float deltaTime) {
        if (!this.player.isAlive()) {
            ScreenSwitcher.toMainMenu();
        }

        this.location.getWorld().step(1 / 60f, 6, 2);
        Vector3 mousePosition = this.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        this.player.update(deltaTime, new Vector2(mousePosition.x, mousePosition.y));// !
        this.camera.update();
        this.ortMapRender.setView(this.camera);
        this.viewport.update((int) (Gdx.graphics.getWidth() / DreamWalker.PPM),
                (int) (Gdx.graphics.getHeight() / DreamWalker.PPM));

        this.hud.update(deltaTime);
        this.location.enemiesUpdate(deltaTime, this.player);
        this.location.enemiesResp();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.pause();
            ScreenSwitcher.toGameMenu();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            ScreenSwitcher.toInventory(player.getInventory());
        }
    }

    @Override
    public void render(float delta) {
        this.location = MapChanger.getCurrentVertex().getLocation();
        this.player.setWorld(this.location.getWorld());

        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.ortMapRender.setMap(this.location.getMap());
        this.ortMapRender.render();

        this.camera.position.x = this.player.getX();
        this.camera.position.y = this.player.getY();

        this.game.getBatch().setProjectionMatrix(this.camera.combined);
        this.game.getBatch().begin();

        this.player.render(this.game.getBatch());
        this.location.enemiesRender(this.game.getBatch());
        this.location.itemsRender(this.game.getBatch());

        this.game.getBatch().end();

        int[] foregroundLayer2 = { location.getMap().getLayers().size() - 1 };
        this.ortMapRender.render(foregroundLayer2);

        this.game.getBatch().setProjectionMatrix(this.hud.getStage().getCamera().combined);

        this.hud.getStage().draw();
    }

    public void nextFloor() {
        MapChanger.setLevelGraph(new LevelGraph("Maps/", 6));
        MapChanger.getLevelGraph().print();
        MapChanger.setCurrentVertex(MapChanger.getLevelGraph().getStart());
        this.location = MapChanger.getCurrentVertex().getLocation(); // !!!
        this.player.setSpawnPoint(this.location.getSpawnPoint());
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
    }

    @Override
    public void pause() {
        System.out.println("pause");
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void dispose() {
        MapChanger.getLevelGraph().dispose();
        this.player.dispose();
        this.ortMapRender.dispose();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}