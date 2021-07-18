package com.b07.inventory;

import java.util.HashMap;

public class InventoryImpl implements Inventory {

  private HashMap<Item, Integer> itemMap = new HashMap<Item, Integer>();
  private transient int totalItems = 0;

  @Override
  public HashMap<Item, Integer> getItemMap() {
    return this.itemMap;
  }

  @Override
  public void setItemMap(HashMap<Item, Integer> itemMap) {
    this.itemMap = itemMap;
  }

  @Override
  public void updateMap(Item item, Integer value) {
    if (this.itemMap.get(item) != null) {
      this.itemMap.remove(item);
    }
    this.itemMap.put(item, value);
    this.generateTotalItems();
  }

  @Override
  public int getTotalItems() {
    return this.totalItems;
  }

  @Override
  public void setTotalItems(int total) {
    this.totalItems = total;
  }

  @Override
  public void generateTotalItems() {
    int sum = 0;
    for (Integer value : itemMap.values()) {
      sum = sum + value;
    }
    totalItems = sum;
  }
}
