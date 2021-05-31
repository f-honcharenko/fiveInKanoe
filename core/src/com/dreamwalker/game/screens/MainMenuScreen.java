package com.dreamwalker.game.screens;

import java.io.Console;

import javax.management.monitor.Monitor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.tools.ScreenSwitcher;

public class MainMenuScreen implements Screen, Disposable {
    private DreamWalker game;
    // Buttons
    private Image startButton;
    private Image exitButton;
    private Image continueButton;
    private Image optionsButton;
    // Icon
    private Image gameIcon;

    private Image newGame_default;
    private Image newGame_hover;
    private Image newGame_pressed;
    private Image continue_default;
    private Image continue_hover;
    private Image continue_pressed;
    private Image exitGame_default;
    private Image exitGame_hover;
    private Image exitGame_pressed;
    private Image options_default;
    private Image options_hover;
    private Image options_pressed;

    // 2д сцена, на которой распологаются элементы интерфейса
    private Stage stage;
    private Viewport viewport;
    private Table table;

    // Events
    private EventListener StartEvent;
    private EventListener ExitEvent;
    private EventListener OptionEvent;
    private EventListener ContinueEvent;
    // BG
    private Sprite background;
    // Button settings
    private float buttonsWidth;
    private float buttonsHeight;
    private float paddingRight;
    private float paddingBottom;
    private float iconWidth;
    private float iconHeight;

    public void updateTable() {
        // Установка взаимодействий
        this.startButton.addListener(this.StartEvent);
        this.continueButton.addListener(this.ContinueEvent);
        this.optionsButton.addListener(this.OptionEvent);
        this.exitButton.addListener(this.ExitEvent);

        // Установка таблицы
        this.table = new Table();

        // БГ
        this.table.setBackground(new SpriteDrawable(this.background));

        // Включить масштабирование
        this.table.setFillParent(true);

        this.table.bottom();
        this.table.right();
        this.table.add(this.gameIcon).padRight(((this.paddingRight * Gdx.graphics.getHeight()) / 1920))
                .padBottom(((this.paddingBottom * Gdx.graphics.getHeight()) / 1080))
                .width(((this.iconWidth * Gdx.graphics.getWidth()) / 1920))
                .height(((this.iconHeight * Gdx.graphics.getHeight()) / 1080));
        this.table.row();
        this.table.add(this.startButton).padRight(((this.paddingRight * Gdx.graphics.getHeight()) / 1920))
                .padBottom(((this.paddingBottom * Gdx.graphics.getHeight()) / 1080))
                .width(((this.buttonsWidth * Gdx.graphics.getWidth()) / 1920))
                .height(((this.buttonsHeight * Gdx.graphics.getHeight()) / 1080));
        this.table.row();
        this.table.add(this.continueButton).padRight(((this.paddingRight * Gdx.graphics.getHeight()) / 1920))
                .padBottom(((this.paddingBottom * Gdx.graphics.getHeight()) / 1080))
                .width(((this.buttonsWidth * Gdx.graphics.getWidth()) / 1920))
                .height(((this.buttonsHeight * Gdx.graphics.getHeight()) / 1080));
        this.table.row();
        this.table.add(this.optionsButton).padRight(((this.paddingRight * Gdx.graphics.getHeight()) / 1920))
                .padBottom(((this.paddingBottom * Gdx.graphics.getHeight()) / 1080))
                .width(((this.buttonsWidth * Gdx.graphics.getWidth()) / 1920))
                .height(((this.buttonsHeight * Gdx.graphics.getHeight()) / 1080));
        this.table.row();
        this.table.add(this.exitButton).padRight(((this.paddingRight * Gdx.graphics.getHeight()) / 1920))
                .padBottom(((this.paddingBottom * Gdx.graphics.getHeight()) / 1080))
                .width(((this.buttonsWidth * Gdx.graphics.getWidth()) / 1920))
                .height(((this.buttonsHeight * Gdx.graphics.getHeight()) / 1080));

        // Отладка таблицы
        // this.table.debug();

        // Упаковака таблицы
        this.table.pack();

        // Добавить таблцу на "сцену"
        this.stage.addActor(this.table);

    }

    public MainMenuScreen(DreamWalker game) {
        this.buttonsWidth = 400f;
        this.buttonsHeight = 150f;
        this.paddingRight = 150f;
        this.paddingBottom = 20f;
        this.iconHeight = 400f;
        this.iconWidth = 400f;
        this.game = game;
        this.background = new Sprite(new Texture("./Art.png"));
        this.gameIcon = new Image(new Sprite(new Texture("./game_logo.png")));

        this.viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.stage = new Stage(this.viewport, game.getBatch());

        // Штука для отслеживания нажатий
        Gdx.input.setInputProcessor(stage);

        // Отключить Масштабирование окна
        Gdx.graphics.setResizable(false);

        // Текстуры
        this.newGame_default = new Image(new Sprite(new Texture("./buttons/NewGameBtn_default.png")));
        this.newGame_pressed = new Image(new Sprite(new Texture("./buttons/NewGameBtn_pressed.png")));
        this.newGame_hover = new Image(new Sprite(new Texture("./buttons/NewGameBtn_hover.png")));

        this.continue_default = new Image(new Sprite(new Texture("./buttons/ContinueBtn_default.png")));
        this.continue_pressed = new Image(new Sprite(new Texture("./buttons/ContinueBtn_pressed.png")));
        this.continue_hover = new Image(new Sprite(new Texture("./buttons/ContinueBtn_hover.png")));

        this.options_default = new Image(new Sprite(new Texture("./buttons/OptionsBtn_default.png")));
        this.options_pressed = new Image(new Sprite(new Texture("./buttons/OptionsBtn_pressed.png")));
        this.options_hover = new Image(new Sprite(new Texture("./buttons/OptionsBtn_hover.png")));

        this.exitGame_default = new Image(new Sprite(new Texture("./buttons/ExitBtn_default.png")));
        this.exitGame_pressed = new Image(new Sprite(new Texture("./buttons/ExitBtn_pressed.png")));
        this.exitGame_hover = new Image(new Sprite(new Texture("./buttons/ExitBtn_hover.png")));

        this.startButton = this.newGame_default;
        this.exitButton = this.exitGame_default;
        this.continueButton = this.continue_default;
        this.optionsButton = this.options_default;

        // Действия для кнопок
        this.StartEvent = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startButton = newGame_pressed;
                updateTable();
                ScreenSwitcher.toGame();
                ScreenSwitcher.disposeMainMenu();

            };

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                startButton = newGame_hover;
                updateTable();
            };

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                startButton = newGame_default;
                updateTable();
            };
        };
        this.ExitEvent = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitButton = exitGame_pressed;
                Gdx.app.exit();
            };

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                exitButton = exitGame_hover;
                updateTable();
            };

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                exitButton = exitGame_default;
                updateTable();
            };
        };
        this.OptionEvent = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionsButton = options_pressed;
                Gdx.app.exit();
                updateTable();
            };

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                optionsButton = options_hover;
                updateTable();
            };

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                optionsButton = options_default;
                updateTable();
            };
        };
        this.ContinueEvent = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                continueButton = continue_pressed;
                Gdx.app.exit();
                updateTable();
            };

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                continueButton = continue_hover;
                updateTable();
            };

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                continueButton = continue_default;
                updateTable();
            };
        };

        // Установка взаимодействий
        this.startButton.addListener(StartEvent);
        this.continueButton.addListener(ContinueEvent);
        this.optionsButton.addListener(OptionEvent);
        this.exitButton.addListener(ExitEvent);

        this.updateTable();
    }

    public void toggleStartButton(Image newImage) {
        this.startButton = newImage;
    }

    public void toggleExitButton(Image newImage) {
        this.exitButton = newImage;
    }

    public void update(float deltaTime) {

    }

    public DreamWalker getGame() {
        return this.game;
    }

    @Override
    public void render(float delta) {
        update(delta);

        // Цвет окна и фикс мерцания экрана при изменении
        // Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Отрисовка UI
        this.stage.draw();
        this.stage.act();
    }

    @Override
    public void resize(int width, int height) {
        // Обновление вьюпорта при изменении размеров окна
        // this.viewport.update(width, height);
        this.stage.getViewport().update(width, height);
        this.updateTable();

    }

    @Override
    public void dispose() {
        this.StartEvent = null;
        this.ExitEvent = null;
        this.OptionEvent = null;
        this.ContinueEvent = null;
        this.stage.dispose();
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

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

}