package com.dreamwalker.game.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Hud {
    // 2д сцена, на которой распологаются элементы интерфейса
    private Stage stage;
    private Viewport viewport;

    private Integer score;
    private Integer healthPoints;
    private Integer manaPoints;

    private Label healthPointsLabel;
    private Label manaPointsLabel;
    private Label scoreLabel;

    /**
     * Конструктор
     * @param sb - пакет спрайтов (инициализировано в основном классе)
     */
    public Hud(SpriteBatch sb) {
        this.healthPoints = 101;
        this.manaPoints = 102;
        this.score = 0;

        // Задаём масштабируемый вьюпорт, с сохранением соотношения сторон
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        this.stage = new Stage(this.viewport, sb);

        Table table = new Table();
        // Установить таблицу сверху
        table.top();
        // Включить масштабирование под таблицу
        table.setFillParent(true);

        // Свойства надписей
        this.healthPointsLabel = new Label(String.format("%03d", this.healthPoints),
                                    new Label.LabelStyle(new BitmapFont(), Color.CYAN));

        this.manaPointsLabel = new Label(String.format("%03d", this.manaPoints),
                                new Label.LabelStyle(new BitmapFont(), Color.CYAN));

        this.scoreLabel = new Label(String.format("%01d", this.score),
                            new Label.LabelStyle(new BitmapFont(), Color.CYAN));

        // Добавляем надписи в таблицу
        table.add(this.healthPointsLabel).expandX().padTop(30);
        table.add(this.scoreLabel).expandX().padTop(10);
        table.add(this.manaPointsLabel).expandX().padTop(10);
        // Перейти на новую строку
        table.row();
        // Добавить таблцу на "сцену"
        this.stage.addActor(table);
    }

    public Stage getStage() {
        return this.stage;
    }
}
