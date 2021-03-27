package com.mygdx.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
//import com.mygdx.game.MyGdxGame;

public class Hud {
    public Stage stage;
    private Viewport viewport;

    private Integer score;
    private Integer healthPoints;
    private Integer manaPoints;

    Label healthPointsLable;
    Label manaPointsLable;
    Label scoreLable;

    public Hud(SpriteBatch sb) {
        healthPoints = 101;
        manaPoints = 102;
        score = 0;
        //viewport = new FitViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        healthPointsLable = new Label(String.format("%03d", healthPoints),
                new Label.LabelStyle(new BitmapFont(), Color.CYAN));
        manaPointsLable = new Label(String.format("%03d", manaPoints),
                new Label.LabelStyle(new BitmapFont(), Color.CYAN));
        scoreLable = new Label(String.format("%01d", score), new Label.LabelStyle(new BitmapFont(), Color.CYAN));
        table.add(healthPointsLable).expandX().padTop(30);
        table.add(scoreLable).expandX().padTop(10);
        table.add(manaPointsLable).expandX().padTop(10);
        table.row();
        stage.addActor(table);
    }
}
