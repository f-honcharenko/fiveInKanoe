package com.dreamwalker.game.tools;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
        //GameScreen prevScreen = ScreenSwitcher.getGameScreen();
        //ScreenSwitcher.setGameScreen(null);
        Rectangle spawnArea;
        if(currentVertex == currentVertex.getEdges().get(index).getFirst()){
            spawnArea = currentVertex.getEdges().get(index).getExitSecond();
            currentVertex = currentVertex.getEdges().get(index).getSecond();
            System.out.println("CASE1");
        }
        else{
            spawnArea = currentVertex.getEdges().get(index).getExitFirst();
            currentVertex = currentVertex.getEdges().get(index).getFirst();
            System.out.println("CASE2");
        }
        Vector2 spawnPoint = new Vector2(
                (spawnArea.getX() + spawnArea.getWidth() / 2) / DreamWalker.PPM,
                (spawnArea.getY() + spawnArea.getHeight() / 2) / DreamWalker.PPM
        );
        System.out.println("THIS IS MAP " + currentVertex.getLocation().getMap());
        System.out.println("THIS IS LOCATION " + ScreenSwitcher.getGameScreen().getLocation());
        ScreenSwitcher.getGameScreen().getLocation().setMap(currentVertex.getLocation().getMap());
        //screenSwitcher.toGame(currentVertex.getMap(), spawnPoint);
        //prevScreen.dispose();
    }
}
