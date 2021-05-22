package com.dreamwalker.game.tools;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.generator.LevelGraph;
import com.dreamwalker.game.generator.Vertex;
import com.dreamwalker.game.screens.GameScreen;

public class MapChanger {
    private static Vertex currentVertex;
    private static LevelGraph levelGraph;

    public static void setLevelGraph(LevelGraph levelGraph) {
        MapChanger.levelGraph = levelGraph;
    }

    public static void setCurrentVertex(Vertex currentVertex) {
        MapChanger.currentVertex = currentVertex;
    }

    public static LevelGraph getLevelGraph() {
        return levelGraph;
    }

    public static Vertex getCurrentVertex() {
        return currentVertex;
    }

    public static void changeLocation(int index){
        Body spawnAreaBody;
        if(currentVertex == currentVertex.getEdges().get(index).getFirst()){
            spawnAreaBody = currentVertex.getEdges().get(index).getExitSecond();
            currentVertex = currentVertex.getEdges().get(index).getSecond();
            System.out.println("CASE1");
        }
        else{
            spawnAreaBody = currentVertex.getEdges().get(index).getExitFirst();
            currentVertex = currentVertex.getEdges().get(index).getFirst();
            System.out.println("CASE2");
        }
        Vector2 spawnPoint = spawnAreaBody.getPosition();
        System.out.println("THIS IS MAP " + currentVertex.getLocation().getMap());
        System.out.println("THIS IS LOCATION " + ScreenSwitcher.getGameScreen().getLocation());
        ScreenSwitcher.getGameScreen().setLocation(currentVertex.getLocation());
        ScreenSwitcher.getGameScreen().getPlayer().setSpawnPoint(spawnPoint);
    }
}
