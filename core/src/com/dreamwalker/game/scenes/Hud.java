package com.dreamwalker.game.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dreamwalker.game.entities.player.Player;
import com.dreamwalker.game.items.*;

public class Hud {
        // 2д сцена, на которой распологаются элементы интерфейса
        private Stage stage;
        private Viewport viewport;
        private Player player;

        private Image BarHP;
        private Texture TBarHP;
        private Image BarMP;
        private Texture TBarMP;

        private Table tableHP;
        private Table tableItems;
        private Table tableSkills;

        private Texture BarTexture;
        private Texture ItemAreaTexture;
        private Texture SkillBarTexture;
        private Texture HPPTexture;
        private Texture MPPTexture;
        private Texture HeadgeHogTexture;
        private Texture SkillRageTexture;
        private Texture SkillTeleportTexture;
        private Texture SkillFlyingSwordTexture;
        private Texture KDTexture;
        private Texture ManaWarningTexture;

        private Image BarImage;
        private Image ItemAreaImage;
        private Image SkillBarImage;
        private Image SkillRageImage;

        private float BarsHeight;
        private float BarsWidth;

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

                this.BarsHeight = 60f;
                this.BarsWidth = 370f;

                this.defineHud();
        }

        public void update(float deltaTime) {
                this.initBars();
                this.updateTables();
        }

        public void defineHud() {
                this.BarTexture = new Texture("Bar.png");
                this.ItemAreaTexture = new Texture("ItemArea.png");
                this.SkillBarTexture = new Texture("SkillBar.png");
                this.HPPTexture = new Texture("HPpot_icon.png");
                this.MPPTexture = new Texture("MPpot_icon.png");
                this.HeadgeHogTexture = new Texture("hedgehog_icon.png");
                this.SkillRageTexture = new Texture("skill_rage_icon.png");
                this.SkillTeleportTexture = new Texture("skill_teleport_icon.png");
                this.SkillFlyingSwordTexture = new Texture("skill_flyingSword_icon.png");

                this.KDTexture = new Texture("skill_icon_mask.png");
                this.ManaWarningTexture = new Texture("skill_icon_mask_red.png");

                this.BarImage = new Image(this.BarTexture);
                this.ItemAreaImage = new Image(this.ItemAreaTexture);
                this.SkillBarImage = new Image(this.SkillBarTexture);
                new Image(this.HPPTexture);
                new Image(this.MPPTexture);
                this.SkillRageImage = new Image(this.SkillRageTexture);

                this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                                new OrthographicCamera());

                this.stage = new Stage(this.viewport, sb);
                this.initBars();
                this.updateTables();
        }

        public void updateTables() {
                this.stage = new Stage(this.viewport, sb);
                this.createTableHP();
                this.createTableItems();
                this.createTableSkils();

        }

        public void createTableHP() {
                float tempPercentHP = ((float) this.player.getCurrentHealth()) / ((float) this.player.getMaxHealth())
                                * this.BarsWidth;
                float tempPercentMP = ((float) this.player.getCurrentMana()) / ((float) this.player.getMaxMana())
                                * this.BarsWidth;

                this.tableHP = new Table();
                this.tableHP.setFillParent(true);

                this.tableHP.center().top();

                Stack bar = new Stack();
                Table containerHPMP = new Table();
                Table containerBorder = new Table();

                containerHPMP.setFillParent(true);
                containerBorder.setFillParent(true);

                containerHPMP.center().top();
                containerBorder.center().top();

                containerHPMP.add(this.BarHP).width((tempPercentHP * ((float) Gdx.graphics.getWidth()) / 1920f))
                                .maxWidth((tempPercentHP * ((float) Gdx.graphics.getWidth()) / 1920f))
                                .height(((this.BarsHeight * Gdx.graphics.getHeight()) / 1080)).align(Align.right)
                                .padLeft(((float) (this.BarsWidth - tempPercentHP) * ((float) Gdx.graphics.getWidth())
                                                / 1920f));
                containerHPMP.add(this.BarMP).width((tempPercentMP * ((float) Gdx.graphics.getWidth()) / 1920f))
                                .maxWidth((tempPercentMP * ((float) Gdx.graphics.getWidth()) / 1920f))
                                .height(((this.BarsHeight * Gdx.graphics.getHeight()) / 1080)).align(Align.left)
                                .padRight(((float) (this.BarsWidth - tempPercentMP) * ((float) Gdx.graphics.getWidth())
                                                / 1920f));

                containerBorder.add(this.BarImage).expandX().padTop(0)
                                .maxWidth(((tempPercentHP * Gdx.graphics.getWidth()) / 1920))
                                .width(((900 * Gdx.graphics.getWidth()) / 1920))
                                .height(((150 * Gdx.graphics.getHeight()) / 1080)).colspan(2);

                bar.add(containerHPMP);
                bar.add(containerBorder);
                this.tableHP.add(bar).expandX().padTop(0).colspan(2);

                this.tableHP.pack();

                this.stage.addActor(this.tableHP);
        }

        public void createTableItems() {
                this.tableItems = new Table();
                this.tableItems.setFillParent(true);

                this.tableItems.bottom().left();

                Stack itemsContainer = new Stack();
                Table containerPotion = new Table();
                Table borderPotion = new Table();

                containerPotion.setFillParent(true);
                borderPotion.setFillParent(true);

                containerPotion.top().left();
                borderPotion.top().left();

                Image tempHP = new Image(this.HPPTexture);
                Image tempMP = new Image(this.MPPTexture);
                Image tempHedgehog = new Image(this.HeadgeHogTexture);
                if (this.player.getInventory().getItem(PotionHP.class) == null) {
                        tempHP.setColor(new Color(1, 1, 1, 0.2f));
                }
                if (this.player.getInventory().getItem(PotionMP.class) == null) {
                        tempMP.setColor(new Color(1, 1, 1, 0.2f));
                }
                if (this.player.getInventory().getItem(Hedgehog.class) == null) {
                        tempHedgehog.setColor(new Color(1, 1, 1, 0.2f));
                }

                containerPotion.add(tempHP).width(((100 * Gdx.graphics.getWidth()) / 1920))
                                .height((100 * Gdx.graphics.getHeight()) / 1080)
                                .padLeft((105 * Gdx.graphics.getWidth()) / 1920)
                                .padTop((15 * Gdx.graphics.getHeight()) / 1080);
                containerPotion.row();
                containerPotion.add(tempHedgehog).width(((50 * Gdx.graphics.getWidth()) / 1920))
                                .height(((50 * Gdx.graphics.getHeight()) / 1080))
                                .padLeft((-105 * Gdx.graphics.getWidth()) / 1920)
                                .padTop((-15 * Gdx.graphics.getHeight()) / 1080);
                containerPotion.row();
                containerPotion.add(tempMP).width(((70 * Gdx.graphics.getWidth()) / 1920))
                                .height(((70 * Gdx.graphics.getHeight()) / 1080))
                                .padLeft((65 * Gdx.graphics.getWidth()) / 1920)
                                .padTop((5 * Gdx.graphics.getHeight()) / 1080);

                borderPotion.add(this.ItemAreaImage).expandX().padTop(0).width(((250 * Gdx.graphics.getWidth()) / 1920))
                                .height(((250 * Gdx.graphics.getHeight()) / 1080));

                itemsContainer.add(borderPotion);
                itemsContainer.add(containerPotion);

                this.tableItems.row();
                this.tableItems.add(itemsContainer);
                this.tableItems.pack();

                this.stage.addActor(this.tableItems);
        }

        public void createTableSkils() {

                float MaxCoolDown1Skill = this.player.getSkillPanel().get(0).getCoolDown();
                float CurrentCoolDown1Skill = MaxCoolDown1Skill
                                - this.player.getSkillPanel().get(0).getCurrentCoolDown();

                this.tableSkills = new Table();
                this.tableSkills.setFillParent(true);

                this.tableSkills.center().bottom();

                Stack skillsContainer = new Stack();
                Table containerSkills = new Table();
                Table containerKDs = new Table();
                Table containerManaWarning = new Table();
                Table borderSkills = new Table();

                containerSkills.setFillParent(true);
                containerKDs.setFillParent(true);
                containerManaWarning.setFillParent(true);
                borderSkills.setFillParent(true);

                containerSkills.top().left();
                containerKDs.top().left();
                containerManaWarning.top().left();
                borderSkills.top().left();

                containerSkills.add(new Image(this.SkillFlyingSwordTexture))
                                .padTop((10 * Gdx.graphics.getWidth()) / 1920)
                                .padLeft((110 * Gdx.graphics.getWidth()) / 1920)
                                .width(((263 / 2f * Gdx.graphics.getWidth()) / 1920))
                                .height(((148 / 2f * Gdx.graphics.getHeight()) / 1080));

                containerSkills.add(new Image(this.SkillRageTexture)).padTop((10 * Gdx.graphics.getWidth()) / 1920)
                                .padLeft((17.5f * Gdx.graphics.getWidth()) / 1920)
                                .width(((263 / 2f * Gdx.graphics.getWidth()) / 1920))
                                .height(((148 / 2f * Gdx.graphics.getHeight()) / 1080));

                containerSkills.add(new Image(this.SkillTeleportTexture)).padTop((10 * Gdx.graphics.getWidth()) / 1920)
                                .padLeft((17.5f * Gdx.graphics.getWidth()) / 1920)
                                .width(((263 / 2f * Gdx.graphics.getWidth()) / 1920))
                                .height(((148 / 2f * Gdx.graphics.getHeight()) / 1080));

                Image Mask1ManaWarning = new Image(this.ManaWarningTexture);
                Mask1ManaWarning.setColor(1, 0, 0,
                                (this.player.getCurrentMana() < this.player.getSkillPanel().get(0).getManaCost())
                                                ? (0.5f)
                                                : (0f));
                containerManaWarning.add(Mask1ManaWarning).padTop((10 * Gdx.graphics.getWidth()) / 1920)
                                .padLeft((110 * Gdx.graphics.getWidth()) / 1920)
                                .width(((263 / 2f * Gdx.graphics.getWidth()) / 1920))
                                .height(((148 / 2f * Gdx.graphics.getHeight()) / 1080));

                Image Mask1KD = new Image(this.KDTexture);
                Image Mask21 = new Image(this.KDTexture);
                Image Mask22 = new Image(this.KDTexture);
                Mask1KD.setColor(1, 0, 0,
                                ((CurrentCoolDown1Skill / MaxCoolDown1Skill) == 1f) ? (0f)
                                                : (this.SkillRageImage.getColor().a
                                                                - (1f - (CurrentCoolDown1Skill / MaxCoolDown1Skill))));
                Mask21.setColor(1, 1, 1, 0.8f);
                Mask22.setColor(1, 1, 1, 0.8f);
                containerKDs.add(Mask1KD).padTop((10 * Gdx.graphics.getWidth()) / 1920)
                                .padLeft((110 * Gdx.graphics.getWidth()) / 1920)
                                .width(((263 / 2f * Gdx.graphics.getWidth()) / 1920))
                                .height(((148 / 2f * Gdx.graphics.getHeight()) / 1080));
                containerKDs.add(Mask21).padTop((10 * Gdx.graphics.getWidth()) / 1920)
                                .padLeft((17.5f * Gdx.graphics.getWidth()) / 1920)
                                .width(((263 / 2f * Gdx.graphics.getWidth()) / 1920))
                                .height(((148 / 2f * Gdx.graphics.getHeight()) / 1080));
                containerKDs.add(Mask22).padTop((10 * Gdx.graphics.getWidth()) / 1920)
                                .padLeft((17.5f * Gdx.graphics.getWidth()) / 1920)
                                .width(((263 / 2f * Gdx.graphics.getWidth()) / 1920))
                                .height(((148 / 2f * Gdx.graphics.getHeight()) / 1080));

                borderSkills.add(this.SkillBarImage).expandX().padTop(0).width(((800 * Gdx.graphics.getWidth()) / 1920))
                                .height(((100 * Gdx.graphics.getHeight()) / 1080));

                skillsContainer.add(containerSkills);
                skillsContainer.add(containerKDs);
                skillsContainer.add(containerManaWarning);
                skillsContainer.add(borderSkills);

                this.tableSkills.row();
                this.tableSkills.add(skillsContainer);
                this.tableSkills.pack();

                this.stage.addActor(this.tableSkills);
        }

        public void initBars() {
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
