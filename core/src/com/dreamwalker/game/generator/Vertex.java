package com.dreamwalker.game.generator;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dreamwalker.game.DreamWalker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Vertex {
    public String name;

    private TiledMap map;
    private ArrayList<Edge> edges;
    private ArrayList<RectangleMapObject> exits;
    private int maxPower;
    private int currentPower;

    public Vertex(TiledMap map, String name){
        this.name = name;

        this.map = map;
        this.edges = new ArrayList<>();
        this.exits = new ArrayList<>(Arrays.asList(
                this.map.getLayers().
                        get("exits").
                        getObjects().
                        getByType(RectangleMapObject.class).
                        toArray()
        ));
        //Переделать из вертекс в боди
        this.maxPower = this.exits.size();
        this.currentPower = 0;
    }

    public Rectangle getExit(Random rand){
        int exitIndex = (int)(rand.nextDouble() * (this.maxPower - this.currentPower));
        Rectangle exitRect = this.exits.get(exitIndex).getRectangle();
        this.exits.remove(exitIndex);
        return exitRect;
    }

    public boolean isConnected(Vertex other){
        boolean result = false;
        for(Edge edge : this.edges){
            if(edge.getFirst() == this){
                result = edge.getSecond() == other;
            }
            else{
                result = edge.getFirst() == other;
            }
            if(result){
                break;
            }
        }
        return result;
    }

    public void addEdge(Edge edge){
        this.edges.add(edge);
        this.currentPower++;
    }

    public int getMaxPower() {
        return this.maxPower;
    }

    public int getCurrentPower() {
        return this.currentPower;
    }

    public ArrayList<Edge> getEdges() {
        return this.edges;
    }

    public TiledMap getMap() {
        return this.map;
    }
}
