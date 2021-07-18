package com.b07.store;

import android.content.Context;
import android.util.Log;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.IdNotInDatabaseException;
import com.b07.inventory.Item;
import com.b07.translate.LanguageSingleton;
import com.b07.translate.Translator;
import com.b07.users.UserInterface;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SaleImpl<context> implements Sale {

  private int id;
  private UserInterface user;
  private BigDecimal totalPrice;
  private HashMap<Item, Integer> itemMap = new HashMap<Item, Integer>();
  private transient Context context;

  public SaleImpl(int id, UserInterface user, BigDecimal totalPrice, Context context) {
    this.id = id;
    this.user = user;
    this.totalPrice = totalPrice;
    this.context = context;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public UserInterface getUser() {
    return this.user;
  }

  @Override
  public void setUser(UserInterface user) {
    this.user = user;
  }

  @Override
  public BigDecimal getTotalPrice() {
    return this.totalPrice;
  }

  @Override
  public void setTotalPrice(BigDecimal price) {
    this.totalPrice = price;
  }

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
      if (this.itemMap.get(item) + value > 0) {
        this.itemMap.put(item, this.itemMap.get(item) + value);
      } else if (this.itemMap.get(item) + value <= 0) {
        this.itemMap.remove(item);
      }
    } else {
      this.itemMap.put(item, value);
    }
  }

  public String saleInfo() {
    Item key;
    Integer value;
    Translator trans = new Translator(context);
    String saleInfo =
        trans.translate("Purchase Number: ", LanguageSingleton.getInstance().getValue()) + this.id
            + "\n" + trans
            .translate("Total Purchase Price: ", LanguageSingleton.getInstance().getValue())
            + this.totalPrice + "\n" + trans
            .translate("Itemized Breakdown: ", LanguageSingleton.getInstance().getValue()) + "\n";

    try {
      DatabaseSelectHelper selector = new DatabaseSelectHelper(context);
      HashMap<Item, Integer> itemMaps = selector.getItemizedSaleByIdHelper(id).getItemMap();
      for (Map.Entry<Item, Integer> itemEntry : itemMaps.entrySet()) {
        key = itemEntry.getKey();
        value = itemEntry.getValue();
        saleInfo = saleInfo + "                    " + key.getName() + ": " + value + "\n";
      }
    } catch (IdNotInDatabaseException e) {
      Log.d("ID ERROR:", "ID NOT IN DATABASE EXCEPTION");
    }
    return saleInfo;
  }
}
