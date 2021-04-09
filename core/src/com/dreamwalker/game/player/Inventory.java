package com.dreamwalker.game.player;

/* TODO
    - Попытаться реализовать сортировку
    - Реализовать доступ по имени
 */

public class Inventory {
    private Item[] inventory;
    private int itemsCount = 0;

    public Inventory(int size){
        this.inventory = new Item[size];
    }

    /**
     * Метод получения доступа к предмету в инвентаре, без его удаления
     * Может понадобится для вывода на панель быстрого доступа
     *
     * @param index - индекс предмета
     * @return - ссылка на предмет
     */
    public Item getItem(int index){
        Item returnable = null;
        if(index >= this.inventory.length){
            System.out.println("There is no such item");
        }
        else{
            returnable = this.inventory[index];
        }
        return returnable;
    }

    /**
     * Метод добавления предмета в инвентарь
     * Если такой предмет уже есть в инвентаре - будет увеличен счетчик предмета
     * и пустой слот занят не будет
     * Иначе же займет первую пустую ячейку
     *
     * @param item - вкладываемый предмет
     */
    public void putItem(Item item){
        if(this.itemsCount + 1 == this.inventory.length){
            System.out.println("Inventory is full!");
        }
        else{
            for(int i = 0; i < this.inventory.length; i++){
                if(this.inventory[i].getName().equals(item.getName())){
                    this.inventory[i].setCount(this.inventory[i].getCount() + 1);
                }
            }
            for(int i = 0; i < this.inventory.length; i++){
                if(this.inventory[i] == null){
                    this.inventory[i] = item;
                    break;
                }
            }
            this.itemsCount++;
        }
    }

    /**
     * Метод удаления элемента из списка с получением его ссылки
     * (Возможно стоит переделать, чтоб ссылку не возвращал)
     *
     * @param index - индекс искомого предмета
     * @return - ссылка на предмет
     */
    public Item popItem(int index){
        Item returnable = null;
        if(index >= this.inventory.length || index < 0){
            System.out.println("There is no such cell");
        }
        else{
            if(this.inventory[index] != null){
                returnable = this.inventory[index];
                this.inventory[index] = null;
                this.itemsCount--;
            }
            else{
                System.out.println("This cell is empty");
            }
        }
        return returnable;
    }

    /**
     * Метод замены предметов
     * Может быть полезен для реализации перетаскивания предметов в инвентаре
     * при его графической реализации.
     * если обе ячейки не пустые - предметы поменяются местами
     * если одна из них пустая - премдет просто переместится
     *
     * @param index1 - индекс первой ячейки
     * @param index2 - индекс второй ячейки
     */
    public void replaceItems(int index1, int index2){
        if(index1 >= this.inventory.length || index2 >= this.inventory.length || index1 < 0 || index2 < 0){
            System.out.println("There is no such cell");
        }
        else{
            if(this.inventory[index1] != null && this.inventory[index2] != null){
                Item tempItem = this.inventory[index1];
                this.inventory[index1] = this.inventory[index2];
                this.inventory[index2] = tempItem;
            }
            if(this.inventory[index1] != null && this.inventory[index2] == null){
                this.inventory[index2] = this.inventory[index1];
                this.inventory[index1] = null;
            }
            if(this.inventory[index1] == null && this.inventory[index2] != null){
                this.inventory[index1] = this.inventory[index2];
                this.inventory[index2] = null;
            }
        }
    }

    public int getSize(){
        return this.inventory.length;
    }
}
