package com.b07.inventory;

import java.io.Serializable;
import java.util.HashMap;

@SuppressWarnings("serial")
public interface Inventory extends Serializable {

  HashMap<Item, Integer> getItemMap();

  /**
   * Updates the itemMap in the inventory with the input itemMap
   */
  void setItemMap(HashMap<Item, Integer> itemMap);

  /**
   * Updates the quantity of an time inside the inventory
   *
   * @param item Item that wished to have its quantity updated
   * @param value Value to be added to or removed from the quantity. Can be negative.
   */
  void updateMap(Item item, Integer value);

  int getTotalItems();

  void setTotalItems(int total);

  /**
   * Updates the total number of items in the inventory
   */
  void generateTotalItems();

}

