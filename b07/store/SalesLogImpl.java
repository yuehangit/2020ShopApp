package com.b07.store;

import com.b07.inventory.Item;
import java.util.ArrayList;
import java.util.List;

public class SalesLogImpl implements SalesLog {

  private List<Sale> allSales = new ArrayList<>();

  @Override
  public List<Sale> getSales() {
    return this.allSales;
  }

  @Override
  public void setSales(List<Sale> allSales) {
    this.allSales = allSales;
  }

  @Override
  public void addSale(Sale sale) {
    this.allSales.add(sale);
  }

  @Override
  public void updateSale(Sale sale, Item item, Integer quantity) {
    int i = 0;
    while (i < this.allSales.size()) {
      if (this.allSales.get(i).getId() == sale.getId()) {
        this.allSales.get(i).updateMap(item, quantity);
        return;
      }
      i++;
    }
  }
}
