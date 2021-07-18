package com.b07.store;

import com.b07.inventory.Item;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public interface SalesLog extends Serializable {

  List<Sale> getSales();

  void setSales(List<Sale> allSales);

  void addSale(Sale sale);

  /**
   * Updates an item's quantity in sale in the sales log
   *
   * @param sale The sale record where the item's quantity needs to be updated
   * @param item The item where the quantity needs to be updated
   * @param quanity The quantity to be added to the item's quantity. Can be positive or negative
   * (negative to remove)
   */
  void updateSale(Sale sale, Item item, Integer quanity);
}
