package com.dreamwalker.game.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.dreamwalker.game.generator.LevelGraph;
import com.dreamwalker.game.generator.Vertex;

public class MapChanger {
    private static Vertex currentVertex;
    private static LevelGraph levelGraph;
    private static LevelGraph oldLevelGraph;

    public static void setLevelGraph(LevelGraph levelGraph) {
        if(MapChanger.levelGraph != null){
            MapChanger.oldLevelGraph = MapChanger.levelGraph;
        }
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
        }
        else{
            spawnAreaBody = currentVertex.getEdges().get(index).getExitFirst();
            currentVertex = currentVertex.getEdges().get(index).getFirst();
        }
        Vector2 spawnPoint = spawnAreaBody.getPosition();
        ScreenSwitcher.getGameScreen().setLocation(currentVertex.getLocation());
        ScreenSwitcher.getGameScreen().getPlayer().setSpawnPoint(spawnPoint);
    }

    public static void disposeOldLevelGraph(){
        MapChanger.oldLevelGraph.dispose();
    }
}
