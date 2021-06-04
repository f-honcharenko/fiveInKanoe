package com.dreamwalker.game.entities.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dreamwalker.game.items.Hedgehog;
import com.dreamwalker.game.items.HedgehogAmmo;
import com.dreamwalker.game.items.Item;

public class Inventory {
    private final Item[] inventory;
    private int itemsCount = 0;

    public Inventory(int size) {
        this.inventory = new Item[size];
    }

    public Item getItem(int index) {
        Item returnable = null;
        if (index < this.inventory.length) {
            returnable = this.inventory[index];
        }
        return returnable;
    }

    public Boolean putItem(Item item) {
        if (this.itemsCount == this.inventory.length) {
            return false;
        } else {
            boolean isAdded = false;
            for (int i = 0; i < this.inventory.length; i++) {
                if (this.inventory[i] != null) {
                    if (this.inventory[i].getId() == item.getId()) {
                        isAdded = true;
                        this.itemsCount++;
                        this.inventory[i].setCount(this.inventory[i].getCount() + item.getCount());
                    }
                }
            }
            int index = 0;
            while ((!isAdded) && (index < inventory.length)) {
                if (inventory[index] == null) {
                    inventory[index] = item;
                    isAdded = true;
                }
                index++;
            }
            return isAdded;
        }
    }

    public Item getItem(Class<?> myClass) {
        Item returnable = null;
        for (Item item : inventory) {
            if (myClass.isInstance(item)) {
                returnable = item;
            }
        }
        return returnable;
    }

    public int getTypesSize() {
        int sum = 0;
        for (Item item : inventory) {
            if (item != null) {
                sum += 1;
            }
        }
        return sum;
    }

    public int getSize() {
        return this.inventory.length;
    }

    public void getInfoInConsole() {
        for (Item item : inventory) {
            if (item != null) {
                System.out.println("*" + item.getName() + "[" + item.getId() + "] (" + item.getCount() + ")");
            } else {
                System.out.println("-");

            }
        }
        System.out.println("=========");
    }

    public void update(Player player) {
        int globalSum = 0;
        for (int i = 0; i < this.inventory.length; i++) {

            if (this.inventory[i] != null) {
                globalSum += this.inventory[i].getCount();
                this.inventory[i].usage(player);
                if (this.inventory[i].getCount() < 1) {
                    if (this.inventory[i] instanceof Hedgehog) {
                        int count = ((Hedgehog) this.inventory[i]).getAmmoCount();
                        if (count == 0) {
                            this.inventory[i] = null;
                        }
                    } else {
                        this.inventory[i] = null;
                    }
                }
            }

        }
        // for (Item item : this.inventory) {
        // if (item != null) {
        // globalSum += item.getCount();
        // item.usage(player);
        // }
        // }
        this.itemsCount = globalSum;
    }

    public void renderHedgehogs(SpriteBatch spriteBatch) {
        for (Item item : this.inventory) {
            if (item != null) {
                if (item instanceof Hedgehog) {
                    ((Hedgehog) item).render(spriteBatch);
                }
            }
        }
    }
}
