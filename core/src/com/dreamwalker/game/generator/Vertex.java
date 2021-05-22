package com.dreamwalker.game.generator;
import com.badlogic.gdx.math.Rectangle;
import com.dreamwalker.game.handler.ContactHandler;
import com.dreamwalker.game.location.Location;

import java.util.ArrayList;
import java.util.Random;

public class Vertex {
    public String name;

    private Location location;
    private ArrayList<Edge> edges;
    private ArrayList<Rectangle> exits;
    private int maxPower;
    private int currentPower;

    public Vertex(Location location, String name, ContactHandler contactHandler){
        this.name = name;
        this.location = location;
        this.location.getWorld().setContactListener(contactHandler);
        this.edges = new ArrayList<>();
        this.exits = this.location.getExits();
        //Переделать из вертекс в боди
        this.maxPower = this.exits.size();
        this.currentPower = 0;
    }

    public Rectangle getExit(){
        Rectangle exitRect = this.exits.get(currentPower);
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

    public Location getLocation() {
        return this.location;
    }
}
