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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.enemy.Robber;
import com.dreamwalker.game.generator.LevelGraph;
import com.dreamwalker.game.listeners.AttackListener;
import com.dreamwalker.game.location.Location;
import com.dreamwalker.game.player.Player;
import com.dreamwalker.game.scenes.Hud;
import com.dreamwalker.game.enemy.Goblin;
import com.dreamwalker.game.skills.FlyingSword;
import com.dreamwalker.game.skills.Sword;
import com.dreamwalker.game.tools.Destroyer;
import com.dreamwalker.game.tools.ScreenSwitcher;

import java.util.ArrayList;

public class GameScreen implements Screen {

    private DreamWalker game;

    // Загрузчик карт
    private TmxMapLoader mapLoader;
    // "Прогрузчик" тайловых карт
    private OrthogonalTiledMapRenderer ortMapRender;
    // Игровая камера
    private OrthographicCamera camera;
    // Вьюпорт (область просмотра игрока)
    private ScreenViewport viewport;

    // Интерфейс
    private Hud hud;
    private Player player;
    // Игровая локация
    private Location location;
    // Временный "прогрузчик" для отладки
    private Box2DDebugRenderer debugRenderer;

    // Тестовый голбин
    private Goblin testGoblin;
    private Goblin testGoblin2;
    private Goblin testGoblin3;

    // Тестовый разбойник
    private Robber testRobber;
    private Robber testRobber1;

    public Destroyer dstr;
    private ScreenSwitcher screenSwitcher;



    /**
     * Конструктор экрана игры
     *
     * @param game - экземпляр основного класса игры
     */
    public GameScreen(DreamWalker game) {


        this.game = game;
        this.mapLoader = new TmxMapLoader();

        this.screenSwitcher = new ScreenSwitcher(this.game);
        // Загрузка карты и создание коллизий
        this.location = new Location(this.mapLoader.load("Maps/StartFixed.tmx"));
        this.location.initCollisions();

        this.debugRenderer = new Box2DDebugRenderer();

        this.player = new Player(location.getWorld(), location.getSpawnPoint());
        this.testGoblin = new Goblin(this.player, location.getSpawnPoint().x + 200 / DreamWalker.PPM,
                location.getSpawnPoint().y + 200 / DreamWalker.PPM);
        this.testGoblin2 = new Goblin(this.player, location.getSpawnPoint().x - 200 / DreamWalker.PPM,
                location.getSpawnPoint().y + 200 / DreamWalker.PPM);
        this.testGoblin3 = new Goblin(this.player, location.getSpawnPoint().x + 200 / DreamWalker.PPM,
                location.getSpawnPoint().y - 200 / DreamWalker.PPM);
        this.testRobber = new Robber(this.player, location.getSpawnPoint().x + 80 / DreamWalker.PPM,
                location.getSpawnPoint().y + 100 / DreamWalker.PPM);
        this.testRobber1 = new Robber(this.player, location.getSpawnPoint().x + 150 / DreamWalker.PPM,
                location.getSpawnPoint().y - 80 / DreamWalker.PPM);
        this.location.getWorld().setContactListener(new AttackListener());

        this.hud = new Hud(this.game.getBatch(), this.player);

        this.camera = new OrthographicCamera();
        // Прогрузка карты
        this.ortMapRender = new OrthogonalTiledMapRenderer(location.getMap(), 1 / DreamWalker.PPM);
        // Установка Ортоганальная проекция камеры, центрированная по вьюпорту
        // (первый параметр отвечает за направление оси у)
        this.camera.setToOrtho(false, Gdx.graphics.getWidth() / DreamWalker.PPM,
                Gdx.graphics.getHeight() / DreamWalker.PPM);

        // Задаём масштабируемый вьюпорт, с сохранением соотношения сторон
        this.viewport = new ScreenViewport(this.camera);
        this.camera.zoom = 0.6f;

        // this.dstr = new Destroyer(this.location.getWorld());

    }

    @Override
    public void show() {

    }

    /**
     * Метод, отвечающий за обновление позиций камеры, установки для "прогрузчика"
     * гранц рендера, обновление вьюпорта
     *
     * @param deltaTime - время тика
     */
    public void update(float deltaTime) {
        // Реализация "времени" в игровом мире
        this.location.getWorld().step(1 / 60f, 6, 2);
        Vector3 mousePosition = this.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        this.player.update(deltaTime, new Vector2(mousePosition.x, mousePosition.y));
        this.camera.update();
        this.ortMapRender.setView(this.camera);
        this.viewport.update((int) (Gdx.graphics.getWidth() / DreamWalker.PPM),
                (int) (Gdx.graphics.getHeight() / DreamWalker.PPM));

        this.hud.update(deltaTime);
        this.testGoblin.update(deltaTime);
        this.testGoblin2.update(deltaTime);
        this.testGoblin3.update(deltaTime);

        this.testRobber.update(deltaTime);
        this.testRobber1.update(deltaTime);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.pause();

            this.screenSwitcher.ToGameMenu();

        }

        // dstr.destroyBody2D();
    }

    @Override
    public void render(float delta) {
        update(delta);

        // Цвет окна и фикс мерцания экрана при изменении
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Получение привычных координат мыши (начало в левом НИЖНЕМ углу)
        // Координаты мыши в пространстве игрового мира

        // Камера должна следовать за игроком
        this.camera.position.x = this.player.getX();
        this.camera.position.y = this.player.getY();

        // Рендер карты
        this.ortMapRender.render();

        // рендер игрока
        this.game.getBatch().setProjectionMatrix(this.camera.combined);
        this.game.getBatch().begin();
        this.player.render(this.game.getBatch());

        // рендер npc
        this.testGoblin.render(this.game.getBatch());
        this.testGoblin2.render(this.game.getBatch());
        this.testGoblin3.render(this.game.getBatch());

        this.testRobber.render(this.game.getBatch());
        this.testRobber1.render(this.game.getBatch());

        this.game.getBatch().end();

        // Рендер верхнего слоя
        // MapLayer foregroundLayer = location.getMap().getLayers().size();
        // пусть foreground -- всгда нулевой слой
        int[] foregroundLayer2 = { location.getMap().getLayers().size() - 1 };
        this.ortMapRender.render(foregroundLayer2);

        // Рендер элементов отладки
        this.debugRenderer.render(this.location.getWorld(), this.camera.combined);

        this.game.getBatch().setProjectionMatrix(this.hud.getStage().getCamera().combined);
        // Отрисовка интерфейса
        this.hud.getStage().draw();
    }

    @Override
    public void resize(int width, int height) {
        // Обновление вьюпорта при изменении размеров окна
        this.viewport.update(width, height);
    }

    @Override
    public void pause() {
        System.out.println("pause");
        // this.game.setScreen(new Sc);
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    /**
     * Очистка памяти от ресурсов, используемых игрок
     */
    @Override
    public void dispose() {
        this.location.dispose();
        this.player.dispose();
        this.ortMapRender.dispose();
        this.debugRenderer.dispose();
    }

    /*
     * dispose() работает, как free в С и delete в С++ Диспоузить надо все ресурсы,
     * которые используются в игре
     *
     */
}