package com.dreamwalker.game.generator;

import com.badlogic.gdx.math.Vector2;

public class Edge {
    private Vertex first;
    private Vector2 exitFirst;

    private Vertex second;
    private Vector2 exitSecond;

    public Edge(Vertex first, Vector2 exitFirst, Vertex second, Vector2 exitSecond){
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
