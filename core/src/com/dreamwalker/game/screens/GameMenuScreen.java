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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
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

public class GameMenuScreen implements Screen, Disposable {
    private DreamWalker game;

    private Image resumeButton;
    private Image helpButton;
    private Image optionButton;
    private Image mainMenuButton;
    // 2д сцена, на которой распологаются элементы интерфейса
    private Stage stage;
    private Viewport viewport;
    private Table table;

    private EventListener resumeEvent;
    private EventListener helpEvent;
    private EventListener optionEvent;
    private EventListener mainMenuEvent;

    private float buttonsWidth;
    private float buttonsHeight;
    private float paddingRight;
    private float paddingBottom;

    public ScreenSwitcher screenSwitcher;
    private Sprite background;

    public GameMenuScreen(DreamWalker game, Texture bg) {
        this.background = new Sprite(bg);
        this.background.setColor(50 / 225f, 33 / 225f, 37 / 225f, 0.2f);
        this.buttonsWidth = 400f;
        this.buttonsHeight = 150f;
        this.paddingRight = 150f;
        this.paddingBottom = 20f;
        this.game = game;
        // Задаём масштабируемый вьюпорт, с сохранением соотношения сторон
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        this.stage = new Stage(this.viewport, game.getBatch());
        // Штука для отслеживания нажатий
        Gdx.input.setInputProcessor(stage);

        // Текстуры
        this.resumeButton = new Image(new Sprite(new Texture("buttons/ContinueBtn_default.png")));
        this.helpButton = new Image(new Sprite(new Texture("buttons/HelpBtn_default.png")));
        this.optionButton = new Image(new Sprite(new Texture("buttons/OptionsBtn_default.png")));
        this.mainMenuButton = new Image(new Sprite(new Texture("buttons/MainMenuBtn_default.png")));

        // Действия для кнопок
        this.resumeEvent = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // this.game.getScreen().dispose();
                ScreenSwitcher.toGame();
                ScreenSwitcher.disposeGameMenu();
                // this.game.setScreen(new GameScreen(this.game));

            };

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleresumeButton(new Image(new Texture("buttons/ContinueBtn_hover.png")));
                updateTable();
            };

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleresumeButton(new Image(new Texture("buttons/ContinueBtn_default.png")));
                updateTable();
            };
        };
        this.helpEvent = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // this.game.getScreen().dispose();
                // ScreenSwitcher.toGame();
                // ScreenSwitcher.disposeGameMenu();
                // this.game.setScreen(new GameScreen(this.game));

            };

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleHelpButton(new Image(new Texture("buttons/HelpBtn_hover.png")));
                updateTable();
            };

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleHelpButton(new Image(new Texture("buttons/HelpBtn_default.png")));
                updateTable();
            };
        };
        this.optionEvent = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // this.game.getScreen().dispose();
                // ScreenSwitcher.toGame();
                // ScreenSwitcher.disposeGameMenu();
                // this.game.setScreen(new GameScreen(this.game));

            };

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleOptionButton(new Image(new Texture("buttons/OptionsBtn_hover.png")));
                updateTable();
            };

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleOptionButton(new Image(new Texture("buttons/OptionsBtn_default.png")));
                updateTable();
            };
        };
        this.mainMenuEvent = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Gdx.app.exit();
                ScreenSwitcher.disposeGame();
                ScreenSwitcher.toMainMenu();
            };

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleMainMenuButton(new Image(new Texture("buttons/MainMenuBtn_hover.png")));
                updateTable();
            };

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleMainMenuButton(new Image(new Texture("buttons/MainMenuBtn_default.png")));
                updateTable();
            };
        };
        // Установка взаимодействий
        this.updateTable();

    }

    public void update(float deltaTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ScreenSwitcher.toGame();
        }
    }

    public void updateTable() {

        // Установка взаимодействий
        this.resumeButton.addListener(this.resumeEvent);
        this.helpButton.addListener(this.helpEvent);
        this.optionButton.addListener(this.optionEvent);
        this.mainMenuButton.addListener(this.mainMenuEvent);

        // Установка таблицы
        this.table = new Table();

        // Включить масштабирование под таблицу
        this.table.setFillParent(true);

        this.table.bottom();
        this.table.left();
        this.table.add(this.resumeButton).padLeft(((this.paddingRight * Gdx.graphics.getHeight()) / 1920))
                .padBottom(((this.paddingBottom * Gdx.graphics.getHeight()) / 1080))
                .width(((this.buttonsWidth * Gdx.graphics.getWidth()) / 1920))
                .height(((this.buttonsHeight * Gdx.graphics.getHeight()) / 1080));
        this.table.row();
        this.table.add(this.helpButton).padLeft(((this.paddingRight * Gdx.graphics.getHeight()) / 1920))
                .padBottom(((this.paddingBottom * Gdx.graphics.getHeight()) / 1080))
                .width(((this.buttonsWidth * Gdx.graphics.getWidth()) / 1920))
                .height(((this.buttonsHeight * Gdx.graphics.getHeight()) / 1080));
        this.table.row();
        this.table.add(this.optionButton).padLeft(((this.paddingRight * Gdx.graphics.getHeight()) / 1920))
                .padBottom(((this.paddingBottom * Gdx.graphics.getHeight()) / 1080))
                .width(((this.buttonsWidth * Gdx.graphics.getWidth()) / 1920))
                .height(((this.buttonsHeight * Gdx.graphics.getHeight()) / 1080));
        this.table.row();
        this.table.add(this.mainMenuButton).padLeft(((this.paddingRight * Gdx.graphics.getHeight()) / 1920))
                .padBottom(((this.paddingBottom * Gdx.graphics.getHeight()) / 1080))
                .width(((this.buttonsWidth * Gdx.graphics.getWidth()) / 1920))
                .height(((this.buttonsHeight * Gdx.graphics.getHeight()) / 1080));
        this.table.row();
        // BG
        // this.table.Background(new SpriteDrawable(new Sprite()));
        this.table.setBackground(new SpriteDrawable(this.background));
        // Отладка таблицы
        // this.table.debugAll();

        // Добавить таблцу на "сцену"
        this.stage.addActor(this.table);
    }

    public DreamWalker getGame() {
        return this.game;
    }

    public void toggleresumeButton(Image newImage) {
        this.resumeButton = newImage;
    }

    public void toggleHelpButton(Image newImage) {
        this.helpButton = newImage;
    }

    public void toggleOptionButton(Image newImage) {
        this.optionButton = newImage;
    }

    public void toggleMainMenuButton(Image newImage) {
        this.mainMenuButton = newImage;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        update(delta);
        // Цвет окна и фикс мерцания экрана при изменении
        // Gdx.gl.glClearColor(0.5f, 0, 0.2f, 0.6f);
        // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        this.resumeEvent = null;
        this.mainMenuEvent = null;
        this.helpEvent = null;
        this.optionEvent = null;
        this.stage.dispose();
    }

}