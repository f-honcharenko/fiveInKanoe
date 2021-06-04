package com.dreamwalker.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.tools.ScreenSwitcher;

public class GameMenuScreen implements Screen, Disposable {
    private DreamWalker game;

    private Image resumeButtonDef;
    private Image resumeButton;
    private Image resumeButtonHover;
    private Image mainMenuButtonDef;
    private Image mainMenuButton;
    private Image mainMenuButtonHover;

    private Stage stage;
    private Viewport viewport;
    private Table table;

    private EventListener resumeEvent;
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

        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        this.stage = new Stage(this.viewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);

        // Текстуры
        this.resumeButtonDef = new Image(new Sprite(new Texture("buttons/ContinueBtn_default.png")));
        this.resumeButtonHover = new Image(new Sprite(new Texture("buttons/ContinueBtn_hover.png")));
        this.resumeButton = this.resumeButtonDef;

        this.mainMenuButtonDef = new Image(new Sprite(new Texture("buttons/MainMenuBtn_default.png")));
        this.mainMenuButtonHover = new Image(new Sprite(new Texture("buttons/MainMenuBtn_hover.png")));
        this.mainMenuButton = this.mainMenuButtonDef;

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
                resumeButton = resumeButtonHover;
                updateTable();
            };

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                resumeButton = resumeButtonDef;
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
                mainMenuButton = mainMenuButtonHover;
                updateTable();
            };

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                mainMenuButton = mainMenuButtonDef;
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
        this.resumeButton.addListener(this.resumeEvent);
        this.mainMenuButton.addListener(this.mainMenuEvent);

        this.table = new Table();

        this.table.setFillParent(true);

        this.table.bottom();
        this.table.left();
        this.table.add(this.resumeButton).padLeft(((this.paddingRight * Gdx.graphics.getHeight()) / 1920))
                .padBottom(((this.paddingBottom * Gdx.graphics.getHeight()) / 1080))
                .width(((this.buttonsWidth * Gdx.graphics.getWidth()) / 1920))
                .height(((this.buttonsHeight * Gdx.graphics.getHeight()) / 1080));
        this.table.row();
        this.table.add(this.mainMenuButton).padLeft(((this.paddingRight * Gdx.graphics.getHeight()) / 1920))
                .padBottom(((this.paddingBottom * Gdx.graphics.getHeight()) / 1080))
                .width(((this.buttonsWidth * Gdx.graphics.getWidth()) / 1920))
                .height(((this.buttonsHeight * Gdx.graphics.getHeight()) / 1080));
        this.table.row();

        this.table.setBackground(new SpriteDrawable(this.background));

        this.stage.addActor(this.table);
    }

    public DreamWalker getGame() {
        return this.game;
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
        this.stage.dispose();
    }

}