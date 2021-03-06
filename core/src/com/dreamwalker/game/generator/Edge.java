package com.dreamwalker.game.generator;

import com.badlogic.gdx.physics.box2d.Body;

public class Edge {
    private final Vertex first;
    private final Body exitFirst;

    private final Vertex second;
    private final Body exitSecond;

    public Edge(Vertex first, Body exitFirst, Vertex second, Body exitSecond){
        this.first = first;
        this.exitFirst = exitFirst;
        this.second = second;
        this.exitSecond = exitSecond;
    }

    public Vertex getFirst() {
        return this.first;
    }

    public Body getExitFirst() {
        return this.exitFirst;
    }

    public Vertex getSecond() {
        return this.second;
    }

    public Body getExitSecond() {
        return this.exitSecond;
    }
}
