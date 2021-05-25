package com.dreamwalker.game.screens;

import java.nio.ByteBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.tools.ScreenSwitcher;

public class InventoryScreen implements Screen, Disposable {
    private DreamWalker game;

    private Image inventoreyPattern;
    private Image inventoreyActiveItemPattern;
    private Image inventoreyDefaultItemPattern;

    // 2д сцена, на которой распологаются элементы интерфейса
    private Stage stage;
    private Viewport viewport;
    private Table table;

    private EventListener resumeEvent;
    private EventListener ExitEvent;
    private EventListener startEvent;

    public ScreenSwitcher screenSwitcher;
    private Sprite background;

    public InventoryScreen(DreamWalker game, Texture bg) {
        this.background = new Sprite(bg);
        this.background.setColor(50 / 225f, 33 / 225f, 37 / 225f, 0.2f);
        this.game = game;
        // Задаём масштабируемый вьюпорт, с сохранением соотношения сторон
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        this.stage = new Stage(this.viewport, game.getBatch());

        // Штука для отслеживания нажатий
        Gdx.input.setInputProcessor(stage);

        // Текстуры
        this.inventoreyPattern = new Image(new Sprite(new Texture("InventoryFrame.png")));
        this.inventoreyActiveItemPattern = new Image(new Sprite(new Texture("ItemPanel_default.png")));
        this.inventoreyDefaultItemPattern = new Image(new Sprite(new Texture("ItemPanel_active.png")));

        // Действия для кнопок
        this.resumeEvent = new ClickListener() {
            DreamWalker game = getGame();
            private ScreenSwitcher screenSwitcher;

            @Override
            public void clicked(InputEvent event, float x, float y) {
                // this.game.getScreen().dispose();
                ScreenSwitcher.toGame();
                ScreenSwitcher.disposeGameMenu();
                // this.game.setScreen(new GameScreen(this.game));

            };

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleresumeButton(new Image(new Texture("buttons/button_resume_active.png")));
                updateTable();
            };

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleresumeButton(new Image(new Texture("buttons/button_resume_unactive.png")));
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
                toggleExitButton(new Image(new Texture("buttons/button_exit_active.png")));
                updateTable();
            };

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleExitButton(new Image(new Texture("buttons/button_exit_unactive.png")));
                updateTable();
            };
        };
        this.startEvent = new ClickListener() {

            DreamWalker game = getGame();
            private ScreenSwitcher screenSwitcher;

            @Override
            public void clicked(InputEvent event, float x, float y) {

                ScreenSwitcher.disposeGame();
                ScreenSwitcher.toGame();
                ScreenSwitcher.disposeGameMenu();
            };

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleStartButton(new Image(new Texture("buttons/button_play_active.png")));
                updateTable();
            };

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleStartButton(new Image(new Texture("buttons/button_play_unactive.png")));
                updateTable();
            };
        };
        // Установка взаимодействий
        this.updateTable();

        System.out.println("w" + Gdx.graphics.getWidth());
        System.out.println("h" + Gdx.graphics.getHeight());

    }

    public void updateTable() {

        // Установка взаимодействий
        // this.resumeButton.addListener(this.resumeEvent);
        // this.exitButton.addListener(this.ExitEvent);
        // this.startButton.addListener(this.startEvent);

        // Установка таблицы
        this.table = new Table();

        // Включить масштабирование под таблицу
        this.table.setFillParent(true);

        this.table.center();
        // this.table.bottom();
        // this.table.left();
        this.table.add(this.inventoreyPattern).width(1200f).height(800f);
        // this.table.add(this.inventoreyPattern).padLeft(50).width(310f).height(144f);
        // this.table.row();
        // this.table.add(this.startButton).padLeft(50).width(310f).height(144f);
        // this.table.row();
        // this.table.add(this.exitButton).padLeft(50).width(310f).height(144f);
        // BG
        // this.table.Background(new SpriteDrawable(new Sprite()));
        this.table.setBackground(new SpriteDrawable(this.background));
        // Отладка таблицы
        // this.table.debugAll();

        // Добавить таблцу на "сцену"
        this.stage.addActor(this.table);
    }

    public void update(float deltaTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.screenSwitcher.toGame();
        }
    }

    public DreamWalker getGame() {
        return this.game;
    }

    public void toggleresumeButton(Image newImage) {
        // this.resumeButton = newImage;
    }

    public void toggleExitButton(Image newImage) {
        // this.exitButton = newImage;
    }

    public void toggleStartButton(Image newImage) {
        // this.startButton = newImage;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        update(delta);
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
        this.resumeEvent = null;
        this.ExitEvent = null;
        this.startEvent = null;
        this.stage.dispose();
    }

}