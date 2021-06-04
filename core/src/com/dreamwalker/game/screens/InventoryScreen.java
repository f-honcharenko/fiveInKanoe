package com.dreamwalker.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.entities.player.Inventory;
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
    private int selectedItemList;
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
        //
        this.selectedItemList = 0;
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
        Stack inventoryContainer = new Stack();
        Table inventoryPattern = new Table();
        Table inventoryList = new Table();
        Table invenotryLogo = new Table();

        // Включить масштабирование под таблицу
        this.table.setFillParent(true);
        inventoryPattern.setFillParent(true);
        inventoryList.setFillParent(true);

        this.table.center();
        inventoryPattern.add(new Image(this.inventoreyPattern)).width(((1520 * Gdx.graphics.getWidth()) / 1920))
                .height(((1000 * Gdx.graphics.getHeight()) / 1080));

        if (inventory.getTypesSize() > 1) {
            for (int i = 0; i < inventory.getTypesSize(); i++) {
                final int index = i;
                Stack itemContainer = new Stack();
                Table itemPattern = new Table();
                Table itemIcon = new Table();
                itemPattern.setFillParent(true);
                itemIcon.setFillParent(true);
                itemPattern.top();
                itemIcon.top();
                Image frame = (i == this.selectedItemList) ? (new Image(this.inventoreyDefaultItemPattern))
                        : (new Image(this.inventoreyActiveItemPattern));
                frame.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        setSelectedItemList(index);
                        updateTable();
                    };
                });
                itemPattern.add(frame).width((600 * Gdx.graphics.getWidth()) / 1920)
                        .height(((123 * Gdx.graphics.getHeight()) / 1080))
                        .padBottom((10 * Gdx.graphics.getHeight()) / 1080)
                        .padRight((520 * Gdx.graphics.getWidth()) / 1920);
                itemPattern.row();
                itemIcon.add(new Image(inventory.getItem(i).getTexture())).width((70 * Gdx.graphics.getWidth()) / 1920)
                        .height(((70 * Gdx.graphics.getHeight()) / 1080))
                        .padBottom((10 * Gdx.graphics.getHeight()) / 1080)
                        .padRight((1010 * Gdx.graphics.getWidth()) / 1920)
                        .padTop((30 * Gdx.graphics.getHeight()) / 1080).align(Align.left);
                itemIcon.row();
                itemIcon.add(new Label(inventory.getItem(i).getName(), this.labelStyle))
                        .padBottom((10 * Gdx.graphics.getHeight()) / 1080)
                        .padLeft((100 * Gdx.graphics.getWidth()) / 1920).padTop((-90 * Gdx.graphics.getHeight()) / 1080)
                        .align(Align.left);
                itemIcon.row();
                itemIcon.add(new Label("x" + inventory.getItem(i).getCount(), this.labelStyle))
                        .padBottom((10 * Gdx.graphics.getHeight()) / 1080)
                        .padLeft((500 * Gdx.graphics.getWidth()) / 1920).padTop((-90 * Gdx.graphics.getHeight()) / 1080)
                        .align(Align.left);
                itemIcon.row();

                itemContainer.add(itemPattern);
                itemContainer.add(itemIcon);

                inventoryList.add(itemContainer);
                inventoryList.row();
            }
        } else {
            if (inventory.getTypesSize() > 0) {
                Stack itemContainer = new Stack();
                Table itemPattern = new Table();
                Table itemIcon = new Table();
                Image frame = new Image(this.inventoreyActiveItemPattern);

                itemPattern.add(frame).width((600 * Gdx.graphics.getWidth()) / 1920)
                        .padTop((-350 * Gdx.graphics.getHeight()) / 1080)
                        .height(((123 * Gdx.graphics.getHeight()) / 1080))
                        .padBottom((230 * Gdx.graphics.getHeight()) / 1080)
                        .padRight((520 * Gdx.graphics.getWidth()) / 1920).align(Align.left).top();
                itemIcon.add(new Image(inventory.getItem(0).getTexture())).width((70 * Gdx.graphics.getWidth()) / 1920)
                        .padTop((-350 * Gdx.graphics.getHeight()) / 1080)
                        .height(((70 * Gdx.graphics.getHeight()) / 1080))
                        .padBottom((230 * Gdx.graphics.getHeight()) / 1080)
                        .padRight((1006 * Gdx.graphics.getWidth()) / 1920).align(Align.left);
                itemIcon.row();
                itemIcon.add(new Label(inventory.getItem(0).getName(), this.labelStyle))
                        .width((70 * Gdx.graphics.getWidth()) / 1920).padTop((-350 * Gdx.graphics.getHeight()) / 1080)
                        .height(((70 * Gdx.graphics.getHeight()) / 1080))
                        .padBottom((230 * Gdx.graphics.getHeight()) / 1080).align(Align.left)
                        .padLeft((100 * Gdx.graphics.getWidth()) / 1920);
                itemIcon.row();
                itemIcon.add(new Label("x" + inventory.getItem(0).getCount(), this.labelStyle))
                        .width((70 * Gdx.graphics.getWidth()) / 1920).padTop((-350 * Gdx.graphics.getHeight()) / 1080)
                        .height(((70 * Gdx.graphics.getHeight()) / 1080))
                        .padBottom((230 * Gdx.graphics.getHeight()) / 1080).align(Align.right)
                        .padRight((500 * Gdx.graphics.getWidth()) / 1920);

                itemContainer.add(itemPattern);
                itemContainer.add(itemIcon);

                inventoryList.add(itemContainer);
                inventoryList.row();
            }
        }
        if (inventory.getTypesSize() > 0) {
            invenotryLogo.add(new Image(inventory.getItem(this.selectedItemList).getTexture()))
                    .width(((400 * Gdx.graphics.getWidth()) / 1920)).height(((400 * Gdx.graphics.getHeight()) / 1080))
                    .padLeft((750 * Gdx.graphics.getWidth()) / 1920).padTop((-220 * Gdx.graphics.getHeight()) / 1080);
            invenotryLogo.row();
            invenotryLogo.add(new Label(inventory.getItem(this.selectedItemList).getName(), this.labelStyle))
                    .padLeft((740 * Gdx.graphics.getWidth()) / 1920).padTop((-50 * Gdx.graphics.getHeight()) / 1080);
            invenotryLogo.row();
            invenotryLogo.add(new Label(inventory.getItem(this.selectedItemList).getDescription(), this.labelStyle))
                    .padLeft((740 * Gdx.graphics.getWidth()) / 1920).padTop((150 * Gdx.graphics.getHeight()) / 1080);
        }
        this.table.setBackground(new SpriteDrawable(this.background));

        inventoryContainer.add(inventoryPattern);
        inventoryContainer.add(invenotryLogo);
        inventoryContainer.add(inventoryList);

        this.table.add(inventoryContainer);

        // Отладка таблицы
        // this.table.debugAll();

        // Добавить таблцу на "сцену"
        this.stage.addActor(this.table);
    }

    public void update(float deltaTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Input.Keys.I)) {
            this.screenSwitcher.toGame();
            ScreenSwitcher.disposeInventory();
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

    public void setSelectedItemList(int num) {
        this.selectedItemList = num;
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