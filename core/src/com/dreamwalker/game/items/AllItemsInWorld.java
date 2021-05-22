package com.dreamwalker.game.items;

import java.util.ArrayList;

public class AllItemsInWorld {
    private static ArrayList items;

    public static void addItem(ItemInWorld item) {
        if (items == null) {
            items = new ArrayList<ItemInWorld>();
        }
        items.add(item);
    }

    public static void clearItmes() {
        if (items != null) {
            items = new ArrayList<ItemInWorld>();
        }
    }

    public static ArrayList getItemList() {
        return items;
    }

    public static void removeItem(ItemInWorld item) {
        items.remove(item);
    }

}
