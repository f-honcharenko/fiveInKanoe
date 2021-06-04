package com.dreamwalker.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.tools.ScreenSwitcher;

public class MainMenuScreen implements Screen, Disposable {
    private final DreamWalker game;

    private Image startButton;
    private Image exitButton;
    private final Image continueButton;
    private final Image optionsButton;

    private final Image gameIcon;

    private final Image newGame_default;
    private final Image newGame_hover;
    private final Image continue_default;

    private final Image exitGame_default;
    private final Image exitGame_hover;
    private final Image options_default;

    private final Stage stage;

    private final EventListener StartEvent;
    private final EventListener ExitEvent;

    private final Sprite background;

    private final float buttonsWidth;
    private final float buttonsHeight;
    private final float paddingRight;
    private final float paddingBottom;
    private final float iconWidth;
    private final float iconHeight;

    private final Texture newGameDefTexture;
    private final Texture newGameHovTexture;

    private final Texture contDefTexture;
    private final Texture optDefTexture;

    private final Texture exitDefTexture;
    private final Texture exitHovTexture;

    private final Texture artTexture;
    private final Texture logoTexture;

    public MainMenuScreen(DreamWalker game) {
        this.newGameDefTexture = new Texture("./buttons/NewGameBtn_default.png");
        this.newGameHovTexture = new Texture("./buttons/NewGameBtn_hover.png");
        this.contDefTexture = new Texture("./buttons/ContinueBtn_default.png");
        this.optDefTexture = new Texture("./buttons/OptionsBtn_default.png");
        this.exitDefTexture = new Texture("./buttons/ExitBtn_default.png");
        this.exitHovTexture = new Texture("./buttons/ExitBtn_hover.png");

        this.buttonsWidth = 400f;
        this.buttonsHeight = 150f;
        this.paddingRight = 150f;
        this.paddingBottom = 20f;
        this.iconHeight = 400f;
        this.iconWidth = 400f;
        this.game = game;

        this.artTexture = new Texture("./Art.png");
        this.logoTexture = new Texture("./game_logo.png");

        this.background = new Sprite(this.artTexture);
        this.gameIcon = new Image(new Sprite(this.logoTexture));

        Viewport viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.stage = new Stage(viewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);

        Gdx.graphics.setResizable(false);

        this.newGame_default = new Image(new Sprite(this.newGameDefTexture));
        this.newGame_hover = new Image(new Sprite(this.newGameHovTexture));

        this.continue_default = new Image(new Sprite(this.contDefTexture));
        this.continue_default.setColor(new Color(1, 1, 1, 0.4f));

        this.options_default = new Image(new Sprite(this.optDefTexture));
        this.options_default.setColor(new Color(1, 1, 1, 0.4f));

        this.exitGame_default = new Image(new Sprite(exitDefTexture));
        this.exitGame_hover = new Image(new Sprite(exitHovTexture));

        this.startButton = this.newGame_default;
        this.exitButton = this.exitGame_default;
        this.continueButton = this.continue_default;
        this.optionsButton = this.options_default;

        this.StartEvent = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startButton = newGame_hover;
                updateTable();
                ScreenSwitcher.toNewGameMenu();
                ScreenSwitcher.disposeMainMenu();

            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                startButton = newGame_hover;
                updateTable();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                startButton = newGame_default;
                updateTable();
            }
        };
        this.ExitEvent = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitButton = exitGame_hover;
                Gdx.app.exit();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                exitButton = exitGame_hover;
                updateTable();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                exitButton = exitGame_default;
                updateTable();
            }
        };

        this.startButton.addListener(StartEvent);
        this.exitButton.addListener(ExitEvent);
        this.updateTable();
    }

    public void updateTable() {
        this.startButton.addListener(this.StartEvent);
        this.exitButton.addListener(this.ExitEvent);

        Table table = new Table();

        table.setBackground(new SpriteDrawable(this.background));

        table.setFillParent(true);

        table.bottom();
        table.right();
        table.add(this.gameIcon).padRight(((this.paddingRight * Gdx.graphics.getHeight()) / 1920))
                .padBottom(((this.paddingBottom * Gdx.graphics.getHeight()) / 1080))
                .width(((this.iconWidth * Gdx.graphics.getWidth()) / 1920))
                .height(((this.iconHeight * Gdx.graphics.getHeight()) / 1080));
        table.row();
        table.add(this.startButton).padRight(((this.paddingRight * Gdx.graphics.getHeight()) / 1920))
                .padBottom(((this.paddingBottom * Gdx.graphics.getHeight()) / 1080))
                .width(((this.buttonsWidth * Gdx.graphics.getWidth()) / 1920))
                .height(((this.buttonsHeight * Gdx.graphics.getHeight()) / 1080));
        table.row();
        table.add(this.continueButton).padRight(((this.paddingRight * Gdx.graphics.getHeight()) / 1920))
                .padBottom(((this.paddingBottom * Gdx.graphics.getHeight()) / 1080))
                .width(((this.buttonsWidth * Gdx.graphics.getWidth()) / 1920))
                .height(((this.buttonsHeight * Gdx.graphics.getHeight()) / 1080));
        table.row();
        table.add(this.optionsButton).padRight(((this.paddingRight * Gdx.graphics.getHeight()) / 1920))
                .padBottom(((this.paddingBottom * Gdx.graphics.getHeight()) / 1080))
                .width(((this.buttonsWidth * Gdx.graphics.getWidth()) / 1920))
                .height(((this.buttonsHeight * Gdx.graphics.getHeight()) / 1080));
        table.row();
        table.add(this.exitButton).padRight(((this.paddingRight * Gdx.graphics.getHeight()) / 1920))
                .padBottom(((this.paddingBottom * Gdx.graphics.getHeight()) / 1080))
                .width(((this.buttonsWidth * Gdx.graphics.getWidth()) / 1920))
                .height(((this.buttonsHeight * Gdx.graphics.getHeight()) / 1080));
        table.pack();
        this.stage.addActor(table);

    }

    public DreamWalker getGame() {
        return this.game;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.stage.draw();
        this.stage.act();
    }

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height);
        this.updateTable();

    }

    @Override
    public void dispose() {
        this.newGameDefTexture.dispose();
        this.newGameHovTexture.dispose();
        this.contDefTexture.dispose();
        this.optDefTexture.dispose();
        this.exitDefTexture.dispose();
        this.exitHovTexture.dispose();
        this.artTexture.dispose();
        this.logoTexture.dispose();
        this.background.getTexture().dispose();
        this.stage.dispose();
    }

    @Override
    public void show() {

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

}