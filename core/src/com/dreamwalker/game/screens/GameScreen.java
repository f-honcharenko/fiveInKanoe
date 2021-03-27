package com.dreamwalker.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.location.Location;
import com.dreamwalker.game.player.Player;

public class GameScreen implements Screen {
    private DreamWalker game;

    private TmxMapLoader mapLoader;
    private OrthogonalTiledMapRenderer ortMapRender;
    private OrthographicCamera camera;
    private FitViewport viewport;
    //int[] map;

    private Player player;
    private Location location;
    private Box2DDebugRenderer debugRenderer;

    public GameScreen(DreamWalker game){
        this.mapLoader = new TmxMapLoader();

        this.location = new Location(this.mapLoader.load("Maps/Start.tmx"));

        this.debugRenderer = new Box2DDebugRenderer();

        this.player = new Player(location.getWorld(), this.location.getSpawnPoint());
        this.game = game;


        this.camera = new OrthographicCamera();
        this.ortMapRender = new OrthogonalTiledMapRenderer(this.location.getMap());
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.translate(256, 0);
        this.camera.update();
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), this.camera);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT); // фикс мерцания экрана при изменении

        this.location.getWorld().step(1/60f, 6, 2);

        //Получение привычных координат мыши (начало в левом НИЖНЕМ углу)
        Vector3 mousePosition = this.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        this.player.move(new Vector2(mousePosition.x, mousePosition.y));

        this.camera.position.x = this.player.getX();
        this.camera.position.y = this.player.getY();

        // размеров окна
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.camera.position.y++;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.camera.position.y--;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.camera.position.x--;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.camera.position.x++;
        }
        // SWITCH MAP [start.tmx]
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            this.location.setMap(this.mapLoader.load("Maps/Start.tmx"));
            this.ortMapRender = new OrthogonalTiledMapRenderer(this.location.getMap());
        }
        // SWITCH MAP [room.tmx]
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            this.location.setMap(this.mapLoader.load("Maps/room.tmx"));
            this.ortMapRender = new OrthogonalTiledMapRenderer(this.location.getMap());
        }
        // SWITCH MAP [exit.tmx]
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            this.location.setMap(this.mapLoader.load("Maps/Exit.tmx"));
            this.ortMapRender = new OrthogonalTiledMapRenderer(this.location.getMap());
        }
        this.camera.update();
        this.viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.ortMapRender.setView((OrthographicCamera) this.viewport.getCamera());
        this.ortMapRender.render();

        debugRenderer.render(this.location.getWorld(), this.camera.combined);

    }

    @Override
    public void resize(int width, int height) {

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
    * dispose() работает, как free в С и delete в С++
    * Диспоузить надо все ресурсы, которые используются в игре
    *
    * */

    @Override
    public void dispose() {
        this.location.dispose();
        //this.player.dispose();
        this.ortMapRender.dispose();
        this.debugRenderer.dispose();
    }
}
