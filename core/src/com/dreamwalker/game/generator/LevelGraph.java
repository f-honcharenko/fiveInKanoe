package com.dreamwalker.game.generator;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Queue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class LevelGraph {
    private String path;
    private int roomsCount;
    private final Random random;
    private final TmxMapLoader mapLoader;
    private ArrayList<TiledMap> mapPool;
    private ArrayList<Vertex> vertices;
    private ArrayList<Edge> edges;

    public LevelGraph(String path, int roomsCount){
        this.mapLoader = new TmxMapLoader();
        this.random = new Random();
        this.mapPool = new ArrayList<>();
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.roomsCount = roomsCount;
        this.path = path;
        this.generate();
    }

    public void addEdge(Vertex first, Vertex second){
        Rectangle exitFirst = first.getExit(this.random);
        Rectangle exitSecond = second.getExit(this.random);
        Edge edge = new Edge(first, exitFirst, second, exitSecond);
        first.addEdge(edge);
        second.addEdge(edge);
        this.edges.add(edge);
    }

    public void generate(){
        for(int i = 1; i <= this.roomsCount; i++){
            this.mapPool.add(this.mapLoader.load(this.path + i + ".tmx"));
        }
        this.shuffle();
        this.vertices.add(new Vertex(this.mapLoader.load(this.path + "start.tmx"), "start"));
        for(int i = 0 ; i < this.roomsCount; i++){
            this.vertices.add(new Vertex(this.mapPool.get(0), Integer.toString(i)));
            this.mapPool.remove(0);
        }
        this.vertices.add(new Vertex(this.mapLoader.load(this.path + "exit.tmx"), "exit"));
        for(int i = 0; i < this.vertices.size(); i++){
            for(int j = i + 1; j < this.vertices.size(); j++){
                if(this.vertices.get(i).getCurrentPower() == this.vertices.get(i).getMaxPower()){
                    break;
                }
                if(this.vertices.get(j).getCurrentPower() == this.vertices.get(j).getMaxPower()){
                    continue;
                }
                if(!this.vertices.get(i).isConnected(this.vertices.get(j))){
                    System.out.println(this.vertices.get(i).name + " - " + this.vertices.get(j).name);
                    this.addEdge(this.vertices.get(i), this.vertices.get(j));
                }
            }
        }
        if(this.vertices.get(this.vertices.size() - 1).getEdges().size() == 0){
            System.out.println("Reconstruct!");
            this.vertices.clear();
            this.edges.clear();
            this.generate();
        }
    }

    //del
    public void print(){
        for(Edge edge : this.edges){
            System.out.printf("(%s)-------(%s)\n", edge.getFirst().name, edge.getSecond().name);
        }
        System.out.println("--------------------------------------");
    }

    private void shuffle(){
        TiledMap[] mapPoolArr = this.mapPool.toArray(new TiledMap[0]);
        int j;
        for(int i = mapPoolArr.length - 1; i > 0; i--){
            j = (int)Math.floor(random.nextDouble() * (i + 1));
            TiledMap tempMap = mapPoolArr[j];
            mapPoolArr[j] = mapPoolArr[i];
            mapPoolArr[i] = tempMap;
        }
        this.mapPool = new ArrayList<>(Arrays.asList(mapPoolArr));
    }

}
