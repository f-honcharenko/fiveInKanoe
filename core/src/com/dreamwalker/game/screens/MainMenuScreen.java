package com.dreamwalker.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.screens.GameScreen;

public class MainMenuScreen implements Screen {

    private static final int EXIT_BUTTON_WIDTH = 250;
    private static final int EXIT_BUTTON_HEIGHT = 120;
    private static final int PLAY_BUTTON_WIDTH = 300;
    private static final int PLAY_BUTTON_HEIGHT = 120;
    private static final int EXIT_BUTTON_X_PADDING = 50;
    private static final int EXIT_BUTTON_Y = 100;
    private static final int PLAY_BUTTON_Y = 230;

    DreamWalker game;
    OrthographicCamera camera;
    ScreenViewport viewport;

    Texture playButtonActive;
    Texture playButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;

    Sprite playButtonActiveSprite;
    Sprite playButtonInactiveSprite;
    Sprite exitButtonInactiveSprite;
    Sprite exitButtonActiveSprite;

    int globalWidth;

    public MainMenuScreen(DreamWalker game) {
        this.game = game;
        // Текстуры
        playButtonActive = new Texture("play_button_active.png");
        playButtonInactive = new Texture("play_button_inactive.png");
        exitButtonInactive = new Texture("exit_button_inactive.png");
        exitButtonActive = new Texture("exit_button_active.png");

        // Спрайты
        playButtonActiveSprite = new Sprite(playButtonActive);
        playButtonInactiveSprite = new Sprite(playButtonInactive);
        exitButtonInactiveSprite = new Sprite(exitButtonInactive);
        exitButtonActiveSprite = new Sprite(exitButtonActive);
        // Ширина єкрана
        globalWidth = Gdx.graphics.getWidth();

        // Camera
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.game.getBatch().setProjectionMatrix(camera.combined);
        this.viewport = new FitViewport(800, 480, camera);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        int x = globalWidth / 2 - (EXIT_BUTTON_WIDTH / 2);

        if (Gdx.input.getX() < x + EXIT_BUTTON_WIDTH && Gdx.input.getX() > x
                && Gdx.graphics.getHeight() - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT
                && Gdx.graphics.getHeight() - Gdx.input.getY() > EXIT_BUTTON_Y) {
            game.getBatch().draw(exitButtonActiveSprite, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
            if (Gdx.input.isTouched()) {
                System.out.println("EXIT");
                Gdx.app.exit();
            }
        } else {
            game.getBatch().draw(exitButtonInactiveSprite, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        }

        x = globalWidth / 2 - (PLAY_BUTTON_WIDTH / 2);
        System.out.println(Gdx.input.getX());
        if ((Gdx.input.getX() < x + PLAY_BUTTON_WIDTH) && (Gdx.input.getX() > x)
                && (Gdx.graphics.getHeight() - Gdx.input.getY() < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT)
                && (Gdx.graphics.getHeight() - Gdx.input.getY() > PLAY_BUTTON_Y)) {
            // game.getBatch().draw(playButtonActive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH,
            // PLAY_BUTTON_HEIGHT);
            game.getBatch().draw(playButtonActiveSprite, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
            if (Gdx.input.isTouched()) {
                System.out.println("Play");
                this.game.setScreen(new GameScreen(game));
                // Gdx.app.exit();
            }
        } else {
            game.getBatch().draw(playButtonInactiveSprite, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
            // game.getBatch().draw(playButtonInactive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH,
            // PLAY_BUTTON_HEIGHT);
        }

        game.getBatch().end();
    }

    public void update(float deltaTime) {
        this.camera.update();
        this.viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void resize(int width, int height) {
        // globalWidth = width;
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
        Gdx.input.setInputProcessor(null);
    }

}