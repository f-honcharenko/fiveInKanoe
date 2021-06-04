package com.dreamwalker.game.entities.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dreamwalker.game.entities.Entity;
import com.dreamwalker.game.items.*;

/* TODO
    - Попытаться реализовать сортировку
    - Реализовать доступ по имени
 */

public class Inventory {
    private Item[] inventory;
    private int itemsCount = 0;

    public Inventory(int size) {
        this.inventory = new Item[size];
    }

    /**
     * Метод получения доступа к предмету в инвентаре, без его удаления Может
     * понадобится для вывода на панель быстрого доступа
     *
     * @param index - индекс предмета
     * @return - ссылка на предмет
     */
    public Item getItem(int index) {
        Item returnable = null;
        if (index >= this.inventory.length) {
            System.out.println("There is no such item");
        } else {
            returnable = this.inventory[index];
        }
        return returnable;
    }

    /**
     * Метод добавления предмета в инвентарь Если такой предмет уже есть в инвентаре
     * - будет увеличен счетчик предмета и пустой слот занят не будет Иначе же
     * займет первую пустую ячейку
     *
     * @param item - вкладываемый предмет
     */
    public Boolean putItem(Item item) {
        if (this.itemsCount == this.inventory.length) {
            System.out.println("Inventory is full!");
            return false;
        } else {
            boolean isAdded = false;
            for (int i = 0; i < this.inventory.length; i++) {
                if (this.inventory[i] != null) {
                    if (this.inventory[i].getId() == item.getId()) {
                        isAdded = true;
                        this.itemsCount++;
                        System.out.println(this.inventory[i].getCount() + "+" + item.getCount() + "="
                                + (this.inventory[i].getCount() + item.getCount()));
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
                System.out.println(item);
                returnable = item;
            }
        }
        return returnable;
    }

    /**
     * Метод удаления элемента из списка с получением его ссылки (Возможно стоит
     * переделать, чтоб ссылку не возвращал)
     *
     * @param index - индекс искомого предмета
     * @return - ссылка на предмет
     */
    public Item popItem(int index) {
        Item returnable = null;
        if (index >= this.inventory.length || index < 0) {
            System.out.println("There is no such cell");
        } else {
            if (this.inventory[index] != null) {
                returnable = this.inventory[index];
                this.inventory[index] = null;
                this.itemsCount--;
            } else {
                System.out.println("This cell is empty");
            }
        }
        return returnable;
    }

    /**
     * Метод замены предметов Может быть полезен для реализации перетаскивания
     * предметов в инвентаре при его графической реализации. если обе ячейки не
     * пустые - предметы поменяются местами если одна из них пустая - премдет просто
     * переместится
     *
     * @param index1 - индекс первой ячейки
     * @param index2 - индекс второй ячейки
     */
    public void replaceItems(int index1, int index2) {
        if (index1 >= this.inventory.length || index2 >= this.inventory.length || index1 < 0 || index2 < 0) {
            System.out.println("There is no such cell");
        } else {
            if (this.inventory[index1] != null && this.inventory[index2] != null) {
                Item tempItem = this.inventory[index1];
                this.inventory[index1] = this.inventory[index2];
                this.inventory[index2] = tempItem;
            }
            if (this.inventory[index1] != null && this.inventory[index2] == null) {
                this.inventory[index2] = this.inventory[index1];
                this.inventory[index1] = null;
            }
            if (this.inventory[index1] == null && this.inventory[index2] != null) {
                this.inventory[index1] = this.inventory[index2];
                this.inventory[index2] = null;
            }
        }
    }

    public int getCorrectSize() {
        int sum = 0;
        for (Item item : inventory) {
            if (item != null) {
                sum += item.getCount();
            }
        }
        return sum;
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
                System.out.println(item.getName() + "[" + item.getId() + "] (" + item.getCount() + ")");
            } else {
                System.out.println("-");

            }
        }
        System.out.println("=========");
    }

    public void update(Entity entity){
        for(Item item : this.inventory){
            if(item != null){
                item.usage(entity);
            }
        }
    }

    public void renderHedgehogs(SpriteBatch spriteBatch){
        for(Item item : this.inventory){
            if(item != null){
                if(item instanceof Hedgehog){
                   ((Hedgehog)item).render(spriteBatch);
                }
            }
        }
    }
}
