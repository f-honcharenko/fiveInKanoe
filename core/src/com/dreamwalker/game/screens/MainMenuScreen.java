package com.dreamwalker.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.viewport.FitViewport;
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

    public void updateTable() {
        // Установка взаимодействий
        this.startButton.addListener(this.StartEvent);
        this.continueButton.addListener(this.ContinueEvent);
        this.optionsButton.addListener(this.OptionEvent);
        this.exitButton.addListener(this.ExitEvent);

        // Установка таблицы
        this.table = new Table();

        // Включить масштабирование под таблицу
        this.table.setFillParent(true);

        this.table.bottom();
        this.table.right();
        this.table.add(this.gameIcon).padRight(this.paddingRight).padBottom(35).width(90 * 2.8f).height(100 * 2.8f);
        this.table.row();
        this.table.add(this.startButton).padRight(this.paddingRight).padBottom(25).width(this.buttonsWidth)
                .height(this.buttonsHeight);
        this.table.row();
        this.table.add(this.continueButton).padRight(this.paddingRight).padBottom(25).width(this.buttonsWidth)
                .height(this.buttonsHeight);
        this.table.row();
        this.table.add(this.optionsButton).padRight(this.paddingRight).padBottom(25).width(this.buttonsWidth)
                .height(this.buttonsHeight);
        this.table.row();
        this.table.add(this.exitButton).padRight(this.paddingRight).padBottom(100).width(this.buttonsWidth)
                .height(this.buttonsHeight);
        // БГ
        this.table.setBackground(new SpriteDrawable(this.background));
        // Отладка таблицы
        // this.table.debugAll();

        // Добавить таблцу на "сцену"
        this.stage.addActor(this.table);
    }

    public MainMenuScreen(DreamWalker game) {
        this.buttonsWidth = 400f;
        this.buttonsHeight = 150f;
        this.paddingRight = 150f;
        this.game = game;
        this.background = new Sprite(new Texture("./Art.png"));
        this.gameIcon = new Image(new Sprite(new Texture("./game_logo.png")));
        // this.background.setColor(50 / 225f, 33 / 225f, 37 / 225f, 0.2f);

        // Задаём масштабируемый вьюпорт, с сохранением соотношения сторон
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        this.stage = new Stage(this.viewport, game.getBatch());

        // Штука для отслеживания нажатий
        Gdx.input.setInputProcessor(stage);

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
            DreamWalker game = getGame();

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
                // toggleButtonImage(startButton, "./buttons/NewGameBtn_default.png");
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
        // this.hud.update(deltaTime);
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
        this.viewport.update(width, height);
    }

    @Override
    public void dispose() {
        this.StartEvent = null;
        this.ExitEvent = null;
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