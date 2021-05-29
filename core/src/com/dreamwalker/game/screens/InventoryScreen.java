package com.dreamwalker.game.screens;

import java.nio.ByteBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.player.Inventory;
import com.dreamwalker.game.tools.ScreenSwitcher;

public class InventoryScreen implements Screen, Disposable {
    private DreamWalker game;

    private Sprite inventoreyPattern;
    private Sprite inventoreyActiveItemPattern;
    private Sprite inventoreyDefaultItemPattern;

    // 2д сцена, на которой распологаются элементы интерфейса
    private Stage stage;
    private Viewport viewport;
    private Table table;

    private EventListener resumeEvent;
    private EventListener ExitEvent;
    private EventListener startEvent;

    public ScreenSwitcher screenSwitcher;
    private Sprite background;

    private Inventory inventory;

    private Label.LabelStyle labelStyle;

    public InventoryScreen(DreamWalker game, Texture bg, Inventory inv) {
        this.labelStyle = new Label.LabelStyle();
        // BitmapFont myFont = new
        // BitmapFont(Gdx.files.internal("./arcade/skin/arcade-ui.json"));
        BitmapFont myFont = new BitmapFont();
        this.labelStyle.font = myFont;
        this.labelStyle.fontColor = Color.YELLOW;

        this.inventory = inv;
        this.background = new Sprite(bg);
        this.background.setColor(50 / 225f, 33 / 225f, 37 / 225f, 0.2f);
        this.game = game;
        // Задаём масштабируемый вьюпорт, с сохранением соотношения сторон
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        this.stage = new Stage(this.viewport, game.getBatch());

        // Штука для отслеживания нажатий
        Gdx.input.setInputProcessor(stage);

        // Текстуры
        this.inventoreyPattern = new Sprite(new Texture("InventoryFrame.png"));
        this.inventoreyActiveItemPattern = new Sprite(new Texture("ItemPanel_default.png"));
        this.inventoreyDefaultItemPattern = new Sprite(new Texture("ItemPanel_active.png"));

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
                toggleresumeButton(new Image(new Texture("buttons/button_resume_active.png")));
                updateTable();
            };

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                toggleresumeButton(new Image(new Texture("buttons/button_resume_unactive.png")));
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
        // this.table.add(this.inventoreyPattern).width(1200f).height(800f);
        for (int i = 0; i < inventory.getTypesSize(); i++) {
            this.table.add(new Image(this.inventoreyDefaultItemPattern)).width(300f).height(100f).padBottom(10f);
            this.table.add(new Image(inventory.getItem(i).getTexture())).width(50).height(50).padBottom(10f);
            this.table.add(new Label(inventory.getItem(i).getName(), this.labelStyle)).width(50).height(50)
                    .padBottom(10f).padRight(100f);
            ;
            this.table.add(new Label("(" + inventory.getItem(i).getCount() + ")", this.labelStyle)).width(50).height(50)
                    .padBottom(10f);
            this.table.row();
        }
        // this.table.add(this.inventoreyDefaultItemPattern).width(300f).height(100f).padBottom(50f).padTop(50f);
        // this.table.add(this.inventoreyDefaultItemPattern).width(300f).height(100f).padBottom(50f).padTop(50f);
        // this.table.add(this.inventoreyDefaultItemPattern).width(300f).height(100f).padBottom(50f).padTop(50f);

        // this.table.add(this.inventoreyPattern).width(1200f).height(800f);

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