package com.b07.database.helper;

import android.content.Context;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.store.Sale;
import com.b07.store.SalesLog;
import java.math.BigDecimal;
import java.util.List;

/**
 * This class is used by the DatabaseHelper methods to check if the values being passed as
 * parameters conform to the set restrictions
 *
 * @author vladislavtrukhin
 */
public final class DataInfoValidator {

  private DataInfoValidator() {

  }

  public static boolean checkStringValid(String input) {
    return input != null && !input.contentEquals("");
  }

  public static boolean checkAddressValid(String input) {
    return checkStringValid(input) && input.length() <= 100;
  }

  public static boolean checkItemExists(int id, Context context) {
    DatabaseSelectHelper selector = new DatabaseSelectHelper(context);
    List<Item> list = selector.getAllItemsHelper();
    for (Item item : list) {
      if (item.getId() == id) {
        return true;
      }
    }
    return false;
  }

  public static boolean checkSaleExists(int id, Context context) {
    DatabaseSelectHelper selector = new DatabaseSelectHelper(context);
    SalesLog log = selector.getSalesHelper();
    List<Sale> list = log.getSales();
    for (Sale sale : list) {
      if (sale.getId() == id) {
        return true;
      }
    }
    return false;
  }

  public static boolean checkUserExists(int id, Context context) {
    DatabaseSelectHelper selector = new DatabaseSelectHelper(context);
    List<Integer> list = selector.getUsersIds();
    for (Integer users : list) {
      //Toast.makeText(context, users + ":" + id, Toast.LENGTH_LONG).show();
      if (users == id) {
        return true;
      }
    }
    return false;
  }

  public static boolean checkRoleExists(int id, Context context) {
    DatabaseSelectHelper selector = new DatabaseSelectHelper(context);
    List<Integer> allRoles = selector.getRoleIds();
    int i = 0;
    while (i < allRoles.size()) {
      if (allRoles.get(i) == id) {
        return true;
      }
      i++;
    }
    return false;
  }

  public static boolean checkIfItemInInventory(int id, Context context) {
    DatabaseSelectHelper selector = new DatabaseSelectHelper(context);
    Inventory inventory = selector.getInventoryHelper();
    for (Item item : inventory.getItemMap().keySet()) {
      if (item.getId() == (id)) {
        return true;
      }
    }
    return false;
  }

  public static boolean checkBigDecimalValid(BigDecimal number) {
    return number.signum() > 0;
  }
}
  

