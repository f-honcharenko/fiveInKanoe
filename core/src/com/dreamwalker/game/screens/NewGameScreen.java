package com.dreamwalker.game.screens;

import com.badlogic.gdx.Gdx;
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

public class NewGameScreen implements Screen, Disposable {
    private final DreamWalker game;
    private Image nextButton;
    private final Image scroll;
    private final Stage stage;
    private final Viewport viewport;

    private EventListener nextEvent;

    private final float buttonsWidth;
    private final float buttonsHeight;
    private final float paddingBottom;

    private final Sprite background;

    public NewGameScreen(DreamWalker game, Texture bg) {
        this.background = new Sprite(bg);
        this.background.setColor(50 / 225f, 33 / 225f, 37 / 225f, 0.2f);
        this.buttonsWidth = 400f;
        this.buttonsHeight = 150f;
        this.paddingBottom = 20f;
        this.game = game;

        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        this.stage = new Stage(this.viewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);


        this.nextButton = new Image(new Sprite(new Texture("buttons/ContinueBtn_default.png")));
        this.scroll = new Image(new Sprite(new Texture("Scroll.png")));


        this.nextEvent = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenSwitcher.toGame();
                ScreenSwitcher.disposeNewGameMenu();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleNextButton(new Image(new Texture("buttons/ContinueBtn_hover.png")));
                updateTable();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleNextButton(new Image(new Texture("buttons/ContinueBtn_default.png")));
                updateTable();
            }
        };

        this.updateTable();
    }

    public void updateTable() {

        this.nextButton.addListener(this.nextEvent);

        Table table = new Table();

        table.setFillParent(true);

        table.center().bottom();
        table.add(this.scroll).width(((1536 * Gdx.graphics.getWidth()) / 1920))
                .height(((864 * Gdx.graphics.getHeight()) / 1080));
        table.row();
        table.add(this.nextButton).padBottom(((this.paddingBottom * Gdx.graphics.getHeight()) / 1080))
                .width(((this.buttonsWidth * Gdx.graphics.getWidth()) / 1920))
                .height(((this.buttonsHeight * Gdx.graphics.getHeight()) / 1080));
        table.row();

        table.setBackground(new SpriteDrawable(this.background));

        this.stage.addActor(table);
    }

    public DreamWalker getGame() {
        return this.game;
    }

    public void toggleNextButton(Image newImage) {
        this.nextButton = newImage;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
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
        this.nextButton = null;
        this.nextEvent = null;
        this.stage.dispose();
    }

}