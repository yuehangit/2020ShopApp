package com.b07.store;

import com.b07.inventory.Item;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides commonly used methods that are utilized by the numerous classes in store
 * package
 *
 * @author vladislavtrukhin
 */
public final class SaleHelper {

  private SaleHelper() {

  }

  /**
   * Update a specific item by a specific given value, and will remove the item from the hashmap if
   * the quantity is equal or less than 0.
   *
   * @param item Item to be updated
   * @param value Value to be added to the quantity, can be negative or positive
   * @param items The map where the items and quantity is stored
   */
  public static void updateMap(Item item, Integer value, HashMap<Item, Integer> items) {
    int currentQuantity = 0;
    for (Map.Entry<Item, Integer> entry : items.entrySet()) {
      Item keyItem = entry.getKey();
      Integer keyValue = entry.getValue();
      if (keyItem.getId() == item.getId()) {
        currentQuantity = keyValue;
        items.remove(keyItem);
        break;
      }
    }
    if (currentQuantity + value <= 0) {
      items.remove(item);
    } else {
      items.put(item, currentQuantity + value);
    }
  }

  /**
   * Returns the total price in a given hashmap,
   *
   * @param items The list of items and quantity
   * @return a price of all the items in the given hashmap
   */
  public static BigDecimal generateTotal(HashMap<Item, Integer> items) {
    BigDecimal changeInPrice = new BigDecimal(0);
    for (Map.Entry<Item, Integer> entry : items.entrySet()) {
      Item keyItem = entry.getKey();
      Integer keyValue = entry.getValue();
      changeInPrice =
          changeInPrice.add(keyItem.getPrice().multiply(new BigDecimal(keyValue)));
    }
    return changeInPrice;
  }
}
