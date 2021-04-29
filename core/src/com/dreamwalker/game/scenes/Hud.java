package com.dreamwalker.game.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dreamwalker.game.player.Player;

import org.w3c.dom.css.Rect;

public class Hud {
        // 2д сцена, на которой распологаются элементы интерфейса
        private Stage stage;
        private Viewport viewport;
        private Player player;

        private Label healthPointsLabel;
        private Label manaPointsLabel;
        private Label posX;
        private Label posY;

        private Image BarHP;
        private Image BarMP;

        private Table table;
        private Pixmap pixmap;

        /**
         * Конструктор
         * 
         * @param sb     - пакет спрайтов (инициализировано в основном классе)
         * @param player
         */
        public Hud(SpriteBatch sb, Player player) {
                this.player = player;

                // Задаём масштабируемый вьюпорт, с сохранением соотношения сторон
                this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                                new OrthographicCamera());
                this.stage = new Stage(this.viewport, sb);

                this.pixmap = new Pixmap(100, 10, Pixmap.Format.RGBA8888);
                this.pixmap.setColor(Color.BLUE);
                this.pixmap.fillRectangle(0, 0, 1, 10);
                this.BarMP = new Image(new Texture(this.pixmap));
                // this.pixmap.dispose();

                this.table = new Table();
                // Установить таблицу сверху
                this.table.top();
                // Включить масштабирование под таблицу
                this.table.setFillParent(true);

                // Свойства надписей
                this.healthPointsLabel = new Label(String.format("%.3f", 0.0),
                                new Label.LabelStyle(new BitmapFont(), Color.CYAN));

                this.manaPointsLabel = new Label(String.format("%.3f", 0.0),
                                new Label.LabelStyle(new BitmapFont(), Color.CYAN));

                this.posX = new Label(String.format("%.1f", 0.0), new Label.LabelStyle(new BitmapFont(), Color.CYAN));
                this.posY = new Label(String.format("%.1f", 0.0), new Label.LabelStyle(new BitmapFont(), Color.CYAN));

                // Добавляем надписи в таблицу
                this.table.add(this.BarMP).expandX().padTop(30);
                // Перейти на новую строку
                this.table.row();
                // table.add(this.scoreLabel).expandX().padTop(10);
                this.table.add(this.manaPointsLabel).expandX().padTop(10);
                this.table.add(this.healthPointsLabel).expandX().padTop(10);
                this.table.row();
                this.table.row();
                this.table.add(this.posX).expandX().padTop(10);
                this.table.row();
                this.table.add(this.posY).expandX().padTop(10);

                // Добавить таблцу на "сцену"
                this.stage.addActor(table);
        }

        public void update(float deltaTime) {
                // Форматирование
                String HPtext = String.format("%.3f", this.player.getCurrentHealth()) + "/"
                                + String.format("%.3f", this.player.getMaxHealth());
                String MPtext = String.format("%.3f", this.player.getCurrentMana()) + "/"
                                + String.format("%.3f", this.player.getMaxMana());
                String xText = String.format("%.0f", ((double) this.player.getX()));
                String yText = String.format("%.0f", ((double) this.player.getY()));
                this.healthPointsLabel.setText(HPtext);
                this.manaPointsLabel.setText(MPtext);
                this.posX.setText(xText);
                this.posY.setText(yText);

                int percentMP = (int) ((int) this.player.getCurrentMana() / this.player.getMaxMana()) * 100;
                System.out.println(percentMP);
                Pixmap pixmap = new Pixmap(percentMP, 10, Pixmap.Format.RGBA8888);
                pixmap.setColor(Color.BLUE);
                pixmap.fillRectangle(0, 0, percentMP, 10);
                this.BarMP = new Image(new Texture(pixmap));
                createTable();
                pixmap.dispose();

        }

        public void createTable() {
                this.table = new Table();
                this.table.setFillParent(true);
                this.table.top();
                // Добавляем надписи в таблицу
                this.table.add(this.BarMP).expandX().padTop(30);
                this.table.add(this.BarHP).expandX().padTop(30);
                // Перейти на новую строку
                this.table.row();
                // table.add(this.scoreLabel).expandX().padTop(10);
                this.table.add(this.manaPointsLabel).expandX().padTop(10);
                this.table.add(this.healthPointsLabel).expandX().padTop(10);
                this.table.row();
                this.table.row();
                this.table.add(this.posX).expandX().padTop(10);
                this.table.row();
                this.table.add(this.posY).expandX().padTop(10);

                // Добавить таблцу на "сцену"
                this.stage.addActor(table);

        }

        public Stage getStage() {
                return this.stage;
        }
}
