package com.dreamwalker.game.generator;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Edge {
    private Vertex first;
    private Rectangle exitFirst;

    private Vertex second;
    private Rectangle exitSecond;

    public Edge(Vertex first, Rectangle exitFirst, Vertex second, Rectangle exitSecond){
        this.first = first;
        this.exitFirst = exitFirst;
        this.second = second;
        this.exitSecond = exitSecond;
    }

    public Vertex getFirst() {
        return this.first;
    }

    public Vertex getSecond() {
        return this.second;
    }
}
