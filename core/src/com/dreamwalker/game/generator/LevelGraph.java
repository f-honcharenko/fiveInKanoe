package com.dreamwalker.game.generator;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.handler.ContactHandler;
import com.dreamwalker.game.location.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class LevelGraph implements Disposable {
    private String path;
    private int roomsCount;
    private final Random random;
    private final TmxMapLoader mapLoader;
    private ArrayList<TiledMap> mapPool;
    private ArrayList<Vertex> vertices;
    private ArrayList<Edge> edges;
    private ContactHandler contactHandler;

    public LevelGraph(String path, int roomsCount){
        this.contactHandler = new ContactHandler();
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
        Body exitFirst = first.getExit();
        Body exitSecond = second.getExit();
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
        this.vertices.add(new Vertex(
                new Location(this.mapLoader.load(this.path + "start.tmx")),
                "start",
                this.contactHandler
                )
        );
        for(int i = 0 ; i < this.roomsCount; i++){
            this.vertices.add(new Vertex(
                    new Location(this.mapPool.get(0)),
                    Integer.toString(i),
                    this.contactHandler
                    )
            );
            this.mapPool.remove(0);
        }
        this.vertices.add(new Vertex(
                new Location(this.mapLoader.load(this.path + "exit.tmx")),
                "exit",
                this.contactHandler
                )
        );
        for(int i = 1; i < this.vertices.size(); i++){
            this.addEdge(this.vertices.get(i-1), this.vertices.get(i));
        }
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

    public Vertex getStart(){
        return this.vertices.get(0);
    }

    @Override
    public void dispose() {
        for(TiledMap map : this.mapPool){
            map.dispose();
        }
        for(Vertex vertex : vertices){
            vertex.getLocation().dispose();
        }
    }
}
