package com.dreamwalker.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.location.Location;
import com.dreamwalker.game.player.Player;
import com.dreamwalker.game.scenes.Hud;

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

    /**
     * Конструктор экрана игры
     * 
     * @param game - экземпляр основного класса игры
     */
    public GameScreen(DreamWalker game) {

        this.game = game;
        this.mapLoader = new TmxMapLoader();

        // Загрузка карты и создание коллизий
        this.location = new Location(this.mapLoader.load("Maps/StartFixed.tmx"));
        this.location.initCollisions();

        this.debugRenderer = new Box2DDebugRenderer();
        this.hud = new Hud(this.game.getBatch());

        this.player = new Player(location.getWorld(), location.getSpawnPoint());

        this.camera = new OrthographicCamera();
        // Прогрузка карты
        float unitScale = 1f;
        this.ortMapRender = new OrthogonalTiledMapRenderer(location.getMap(), unitScale);
        // Установка Ортоганальная проекция камеры, центрированная по вьюпорту
        // (первый параметр отвечает за направление оси у)
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Задаём масштабируемый вьюпорт, с сохранением соотношения сторон
        this.viewport = new ScreenViewport(this.camera);
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
        this.camera.update();
        this.ortMapRender.setView(this.camera);
        this.viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        System.out.println(this.camera.position.x + "       " + this.camera.position.y);
    }

    @Override
    public void render(float delta) {
        update(delta);

        // Реализация "времени" в игровом мире
        this.location.getWorld().step(1 / 60f, 6, 2);

        // Цвет окна и фикс мерцания экрана при изменении
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Получение привычных координат мыши (начало в левом НИЖНЕМ углу)
        // Координаты мыши в пространстве игрового мира
        Vector3 mousePosition = this.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        this.player.playerControl(new Vector2(mousePosition.x, mousePosition.y));

        // Камера должна следовать за игроком
        this.camera.position.x = this.player.getX();
        this.camera.position.y = this.player.getY();

        // Рендер карты
        this.ortMapRender.render();

        // рендер игрока
        game.getBatch().begin();
        player.draw((new Vector3(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0)), game.getBatch());
        game.getBatch().end();

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
        // this.player.dispose();
        this.ortMapRender.dispose();
        this.debugRenderer.dispose();
    }

    /*
     * dispose() работает, как free в С и delete в С++ Диспоузить надо все ресурсы,
     * которые используются в игре
     *
     */
}
