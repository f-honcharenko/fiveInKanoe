package com.dreamwalker.game.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader.Uniform;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dreamwalker.game.player.Player;

import org.w3c.dom.css.Rect;

public class Hud {
        // 2д сцена, на которой распологаются элементы интерфейса
        private Stage stage;
        private Viewport viewport;
        private Player player;

        private Image BarHP;
        private Texture TBarHP;
        private Image BarMP;
        private Texture TBarMP;

        private Table table;

        private Texture BarTexture;
        private Image BarImage;

        private int BarsHeight;
        private int BarsWidth;

        private SpriteBatch sb;

        /**
         * Конструктор
         * 
         * @param sb     - пакет спрайтов (инициализировано в основном классе)
         * @param player
         */
        public Hud(SpriteBatch sb, Player player) {
                this.player = player;
                this.sb = sb;

                this.BarsHeight = 40;
                this.BarsWidth = 246;

                this.defineHud();
        }

        public void update(float deltaTime) {
                this.initBars();
                this.createTable();
        }

        public void defineHud() {
                // Инициализация Бара
                this.BarTexture = new Texture("Bar.png");
                this.BarImage = new Image(this.BarTexture);
                this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                                new OrthographicCamera());

                this.stage = new Stage(this.viewport, sb);
                this.initBars();
                this.createTable();

        }

        public void createTable() {
                double tempPercentHP = (this.player.getCurrentHealth() / this.player.getMaxHealth()) * this.BarsWidth;
                double tempPercentMP = (this.player.getCurrentMana() / this.player.getMaxMana()) * this.BarsWidth;

                this.table = new Table();
                this.stage = new Stage(this.viewport, sb);
                this.table.setFillParent(true);

                this.table.top();

                Stack bar = new Stack();
                Table containerHPMP = new Table();
                Table containerBorder = new Table();

                containerHPMP.setFillParent(true);
                containerBorder.setFillParent(true);

                containerHPMP.top();
                containerBorder.top();

                containerHPMP.add(this.BarHP).padBottom(200).height((int) this.BarsHeight).width((int) tempPercentHP)
                                .maxWidth((int) this.BarsWidth).align(Align.right)
                                .padLeft((int) (this.BarsWidth - tempPercentHP));
                containerHPMP.add(this.BarMP).padBottom(200).height((int) this.BarsHeight).width((int) tempPercentMP)
                                .maxWidth((int) this.BarsWidth).align(Align.left)
                                .padRight((int) (this.BarsWidth - tempPercentMP));

                containerBorder.add(this.BarImage).expandX().padTop(0).height(100).width(600).colspan(2);

                bar.add(containerHPMP);
                bar.add(containerBorder);

                this.table.add(bar).expandX().padTop(0).colspan(2);
                this.table.pack();
                this.stage.addActor(this.table);
        }

        public void initBars() {
                System.out.println(this.BarsWidth);
                Pixmap pixmap = createProceduralPixmap((int) this.BarsWidth, (int) this.BarsHeight, 1, 0, 0);
                Pixmap pixmap2 = createProceduralPixmap((int) this.BarsWidth, (int) this.BarsHeight, 0, 0, 1);
                this.TBarHP = new Texture(pixmap);
                this.TBarMP = new Texture(pixmap2);
                this.BarHP = new Image(this.TBarHP);
                this.BarMP = new Image(this.TBarMP);
                pixmap.dispose();
                pixmap2.dispose();
        }

        public Stage getStage() {
                return this.stage;
        }

        private Pixmap createProceduralPixmap(int width, int height, int r, int g, int b) {
                Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

                pixmap.setColor(r, g, b, 1);
                pixmap.fillRectangle(0, 0, width, height);

                return pixmap;
        }

        public void dispose() {
                this.getStage().dispose();
        }
}
