package com.dreamwalker.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dreamwalker.game.DreamWalker;

public class MainMenuScreen implements Screen, Disposable {
    private DreamWalker game;
    private Image startButton;
    private Image exitButton;
    // 2д сцена, на которой распологаются элементы интерфейса
    private Stage stage;
    private Viewport viewport;
    private Table table;

    private EventListener StartEvent;
    private EventListener ExitEvent;

    public DreamWalker getGame() {

        return this.game;
    }

    public void toggleStartButton(Image newImage) {
        this.startButton = newImage;
    }

    public void toggleExitButton(Image newImage) {
        this.exitButton = newImage;
    }

    public void updateTable() {
        // Установка взаимодействий
        this.startButton.addListener(this.StartEvent);
        this.exitButton.addListener(this.ExitEvent);

        // Установка таблицы
        this.table = new Table();

        // Включить масштабирование под таблицу
        this.table.setFillParent(true);

        this.table.bottom();
        this.table.left();
        this.table.add(this.startButton).padLeft(50).width(300f).height(120f);
        this.table.row();
        this.table.add(this.exitButton).padLeft(50).width(250f).height(120f);

        // Отладка таблицы
        this.table.debugAll();

        // Добавить таблцу на "сцену"
        this.stage.addActor(this.table);
    }

    public MainMenuScreen(DreamWalker game) {
        this.game = game;
        // Задаём масштабируемый вьюпорт, с сохранением соотношения сторон
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        this.stage = new Stage(this.viewport, game.getBatch());

        // Штука для отслеживания нажатий
        Gdx.input.setInputProcessor(stage);

        // Текстуры
        this.startButton = new Image(new Sprite(new Texture("play_button_inactive.png")));
        this.exitButton = new Image(new Sprite(new Texture("exit_button_inactive.png")));

        // Действия для кнопок
        this.StartEvent = new ClickListener() {
            DreamWalker game = getGame();

            @Override
            public void clicked(InputEvent event, float x, float y) {
                this.game.getScreen().dispose();
                this.game.setScreen(new GameScreen(this.game));
            };

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleStartButton(new Image(new Texture("play_button_active.png")));
                updateTable();
            };

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleStartButton(new Image(new Texture("play_button_inactive.png")));
                updateTable();
            };
        };
        this.ExitEvent = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            };

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleExitButton(new Image(new Texture("exit_button_active.png")));
                updateTable();
            };

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleExitButton(new Image(new Texture("exit_button_inactive.png")));
                updateTable();
            };
        };

        // Установка взаимодействий
        this.startButton.addListener(StartEvent);
        this.exitButton.addListener(ExitEvent);

        // Установка таблицы
        this.table = new Table();

        // Включить масштабирование под таблицу
        this.table.setFillParent(true);

        this.table.bottom();
        this.table.left();
        this.table.add(this.startButton).padLeft(50).width(300f).height(120f);
        this.table.row();
        this.table.add(this.exitButton).padLeft(50).width(250f).height(120f);

        // Отладка таблицы
        this.table.debugAll();

        // Добавить таблцу на "сцену"
        this.stage.addActor(this.table);

    }

    public void update(float deltaTime) {
        // this.hud.update(deltaTime);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        update(delta);

        // Цвет окна и фикс мерцания экрана при изменении
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Отрисовка UI
        this.stage.draw();
        this.stage.act();
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

    @Override
    public void dispose() {
    }

}