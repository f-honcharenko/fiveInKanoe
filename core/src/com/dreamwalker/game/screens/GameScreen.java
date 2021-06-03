package com.dreamwalker.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.generator.LevelGraph;
import com.dreamwalker.game.items.AllItemsInWorld;
import com.dreamwalker.game.items.ItemInWorld;
import com.dreamwalker.game.items.PotionHP;
import com.dreamwalker.game.items.PotionMP;
import com.dreamwalker.game.location.Location;
import com.dreamwalker.game.entities.player.Player;
import com.dreamwalker.game.scenes.Hud;
import com.dreamwalker.game.tools.Destroyer;
import com.dreamwalker.game.tools.MapChanger;
import com.dreamwalker.game.tools.ScreenSwitcher;

public class GameScreen implements Screen {

    private DreamWalker game;

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

    public Destroyer dstr;

    /**
     * Конструктор экрана игры
     *
     * @param game - экземпляр основного класса игры
     */
    public GameScreen(DreamWalker game) {
        MapChanger.setLevelGraph(new LevelGraph("Maps/", 6));
        MapChanger.getLevelGraph().print();
        MapChanger.setCurrentVertex(MapChanger.getLevelGraph().getStart());
        this.game = game;
        // Загрузка карты и создание коллизий
        this.location = MapChanger.getCurrentVertex().getLocation(); // !!!
        this.debugRenderer = new Box2DDebugRenderer();

        this.player = new Player(location.getWorld(), this.location.getSpawnPoint());

        this.hud = new Hud(this.game.getBatch(), this.player);

        this.camera = new OrthographicCamera();
        // Прогрузка карты
        this.ortMapRender = new OrthogonalTiledMapRenderer(null, 1 / DreamWalker.PPM);
        // Установка Ортоганальная проекция камеры, центрированная по вьюпорту
        // (первый параметр отвечает за направление оси у)
        this.camera.setToOrtho(false, Gdx.graphics.getWidth() / DreamWalker.PPM,
                Gdx.graphics.getHeight() / DreamWalker.PPM);

        // Задаём масштабируемый вьюпорт, с сохранением соотношения сторон
        this.viewport = new ScreenViewport(this.camera);
        this.camera.zoom = 0.6f;

        // this.dstr = new Destroyer(this.location.getWorld());
        // this.items = new ItemInWorld[10];
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
        if (!this.player.isAlive()) {
            ScreenSwitcher.toMainMenu();
        }
        // Реализация "времени" в игровом мире
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
            // this.pause();
            ScreenSwitcher.toInventory(player.getInventory());
            this.player.getInventory().getInfoInConsole();
            // this.screenSwitcher.ToGameMenu();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            // this.pause();
            PotionHP tempItem = new PotionHP(1);
            ItemInWorld tempItmInWorld = new ItemInWorld(this.player.getX(), this.player.getY(), tempItem,
                    this.player.getWorld());
            // this.items[this.items.length] = tempItmInWorld;
            // this.items.add(tempItmInWorld);
            // this.items.add(tempItmInWorld);
            AllItemsInWorld.addItem(tempItmInWorld);
            // this.player.getInventory().putItem(new PotionHP(1));
            // this.screenSwitcher.ToGameMenu();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            PotionMP tempItem = new PotionMP(1);
            ItemInWorld tempItmInWorld = new ItemInWorld(this.player.getX(), this.player.getY(), tempItem,
                    this.player.getWorld());
            // this.items[this.items.length] = tempItmInWorld;
            // this.items.add(tempItmInWorld);
            AllItemsInWorld.addItem(tempItmInWorld);

            // this.player.getInventory().putItem(new PotionMP(1));

        }

        // dstr.destroyBody2D();
    }

    @Override
    public void render(float delta) {
        this.location = MapChanger.getCurrentVertex().getLocation();
        this.player.setWorld(this.location.getWorld());

        update(delta);

        // Цвет окна и фикс мерцания экрана при изменении
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.ortMapRender.setMap(this.location.getMap());
        this.ortMapRender.render();

        // Получение привычных координат мыши (начало в левом НИЖНЕМ углу)
        // Координаты мыши в пространстве игрового мира

        // Камера должна следовать за игроком
        this.camera.position.x = this.player.getX();
        this.camera.position.y = this.player.getY();

        // рендер игрока
        this.game.getBatch().setProjectionMatrix(this.camera.combined);
        this.game.getBatch().begin();
        this.player.render(this.game.getBatch());
        // рендер вещей
        if (AllItemsInWorld.getItemList() != null) {
            for (int i = 0; i < AllItemsInWorld.getItemList().size(); i++) {
                if (AllItemsInWorld.getItemList().get(i) != null) {
                    ((ItemInWorld) AllItemsInWorld.getItemList().get(i)).draw(this.game.getBatch());
                }
            }
        }

        // рендер npc
        this.location.enemiesRender(this.game.getBatch());

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

    public void nextFloor() {
        MapChanger.setLevelGraph(new LevelGraph("Maps/", 6));
        MapChanger.getLevelGraph().print();
        MapChanger.setCurrentVertex(MapChanger.getLevelGraph().getStart());
        this.location = MapChanger.getCurrentVertex().getLocation(); // !!!
        this.player.setSpawnPoint(this.location.getSpawnPoint());
        System.out.println();
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

    public Player getPlayer() {
        return this.player;
    }

    /**
     * Очистка памяти от ресурсов, используемых игрок
     */
    @Override
    public void dispose() {
        MapChanger.getLevelGraph().dispose();
        AllItemsInWorld.clearItmes();
        this.player.dispose();
        this.ortMapRender.dispose();
        this.debugRenderer.dispose();
    }

    /*
     * dispose() работает, как free в С и delete в С++ Диспоузить надо все ресурсы,
     * которые используются в игре
     *
     */

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}