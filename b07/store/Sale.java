package com.b07.store;

import com.b07.inventory.Item;
import com.b07.users.UserInterface;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

@SuppressWarnings("serial")
public interface Sale extends Serializable {

  int getId();

  void setId(int id);

  UserInterface getUser();

  void setUser(UserInterface user);

  BigDecimal getTotalPrice();

  void setTotalPrice(BigDecimal price);

  HashMap<Item, Integer> getItemMap();

  void setItemMap(HashMap<Item, Integer> itemMap);

  /**
   * Update a specific item by a specific value. Will remove the item if the item is being updated
   * to have a quantity of 0 or less.
   *
   * @param item Item that needs to be updated
   * @param value Value to be added to item quantity. Can be positive or negative
   */
  void updateMap(Item item, Integer value);

  /**
   * Get a formated statement about the sale
   *
   * @return String containing id, total and items of sale
   */
  String saleInfo();
}

