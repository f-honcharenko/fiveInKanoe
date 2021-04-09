package com.dreamwalker.game.player;

/* TODO
    -Возможно стоит сделать интерфейсом
 */
public class Item {
    private int count;
    private String name;

    public Item(String name){
        this.count = 1;
        this.name = name;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return this.name;
    }
}
