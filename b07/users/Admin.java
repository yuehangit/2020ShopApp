package com.b07.users;

import android.content.Context;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.exceptions.IdNotInDatabaseException;
import com.b07.exceptions.InvalidInputException;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.inventory.ItemTypes;
import com.b07.store.Sale;
import com.b07.store.SaleHelper;
import com.b07.store.SalesLog;
import com.b07.translate.LanguageSingleton;
import com.b07.translate.Translator;
import com.google.api.services.translate.Translate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Admin extends User implements AdminInterf {

  private transient Context context;

  public Admin(int id, String name, int age, String address, Context context) {
    this.id = id;
    this.name = name;
    this.age = age;
    this.address = address;
    this.context = context;
    DatabaseInsertHelper inserter = new DatabaseInsertHelper(context);
    this.roleId = inserter.insertRoleHelper(Roles.ADMIN.getRole());
  }

  public Admin(int id, String name, int age, String address, boolean authenticated,
      Context context) {
    this.id = id;
    this.name = name;
    this.age = age;
    this.address = address;
    this.authenticated = authenticated;
    this.context = context;
    DatabaseInsertHelper inserter = new DatabaseInsertHelper(context);
    this.roleId = inserter.insertRoleHelper(Roles.ADMIN.getRole());
  }

  public void setContext(Context context) {
    this.context = context;
  }

  @Override
  public boolean promoteEmployee(EmployeeInterf employee) throws
      IdNotInDatabaseException {
    DatabaseUpdateHelper updator = new DatabaseUpdateHelper(context);
    if (updator.updateUserRoleHelper(this.roleId, employee.getId())) {
      employee = null;
      return true;
    }
    return false;
  }

  @Override
  public HashMap<ItemTypes, Integer> mapStockLevels() {
    DatabaseSelectHelper selector = new DatabaseSelectHelper(context);
    Inventory inventory = selector.getInventoryHelper();
    HashMap<Item, Integer> inventoryMap = inventory.getItemMap();
    HashMap<ItemTypes, Integer> stockMap = new HashMap<>();
    for (Item item : inventoryMap.keySet()) {
      ItemTypes enumItem = item.getName();
      int quantity = inventoryMap.get(item);
      stockMap.put(enumItem, quantity);
    }
    return stockMap;
  }

  @Override
  public String Viewbook() {
    DatabaseSelectHelper selector = new DatabaseSelectHelper(context);
    SalesLog salesLog = selector.getItemizedSalesHelper();
    Translator translator = new Translator(context);
    String translated = "";
    int i = 0;
    HashMap<Item, Integer> allItemsSold = new HashMap<Item, Integer>();
    BigDecimal fullTotal = new BigDecimal(0);
    String allLogs = "";
    while (i < salesLog.getSales().size()) {
      Sale sale = salesLog.getSales().get(i);
      String name = sale.getUser().getName();
      int saleId = sale.getId();
      BigDecimal total = sale.getTotalPrice();
      fullTotal = fullTotal.add(total);
      translated  = translated + translator.translate("Customer: " + name, LanguageSingleton.
              getInstance().getValue()) + "\n";
      translated  = translated + translator.translate("PUrchase Number: " + saleId, LanguageSingleton.
              getInstance().getValue()) + "\n";
      translated  = translated + translator.translate("Purchase Price: " + total, LanguageSingleton.
              getInstance().getValue()) + "\n";
      translated  = translated + translator.translate("Itemized Breakdown: ", LanguageSingleton.
              getInstance().getValue()) + "\n";
      HashMap<Item, Integer> itemSold = sale.getItemMap();
      for (Map.Entry<Item, Integer> entry : itemSold.entrySet()) {
        Item key = entry.getKey();
        Integer value = entry.getValue();
        SaleHelper.updateMap(key, value, allItemsSold);
        allLogs = "" + key.getName() + ": " + value;
        translated = translated + translator.translate(allLogs, LanguageSingleton.
                getInstance().getValue()) + "\n";
      }
      translated = translated  + "\n";
      i++;
    }
    for (Map.Entry<Item, Integer> entry : allItemsSold.entrySet()) {
      Item key = entry.getKey();
      Integer value = entry.getValue();
      allLogs = "Number " + key.getName() + " Sold: " + value;
      translated = translated + translator.translate(allLogs, LanguageSingleton.
              getInstance().getValue()) + "\n";
    }
    allLogs = "TOTAL SALES: " + fullTotal;
    translated = translated + translator.translate(allLogs, LanguageSingleton.
            getInstance().getValue()) + "\n";
    return translated;
  }

  @Override
  public boolean changePrices(long newPrice, int itemId) throws
      IdNotInDatabaseException, InvalidInputException {
    DatabaseUpdateHelper updater = new DatabaseUpdateHelper(context);
    return updater.updateItemPriceHelper(BigDecimal.valueOf(newPrice), itemId);
    // TODO Auto-generated method stub

  }

  public String getHistoricAccounts(int userId) throws IdNotInDatabaseException {
    DatabaseSelectHelper selector = new DatabaseSelectHelper(context);
    Translator translator = new Translator(context);
    String translated = "";
    List<Integer> accounts = selector.getUserInactiveAccountsHelper(userId);
    if (selector.getUserRoleId(userId) != selector.getRoleId(Roles.CUSTOMER)) {
      throw new IdNotInDatabaseException();
    }
    String historicAccounts =
        "Here is the list of account ids that are inactive for user " + userId;
    translated = translated + translator.translate(historicAccounts, LanguageSingleton.
            getInstance().getValue()) + "\n";
    for (int i = 0; i < accounts.size(); i++) {
      historicAccounts = "" + accounts.get(i);
      translated = translated + translator.translate(historicAccounts, LanguageSingleton.
              getInstance().getValue()) + "\n";
    }
    return translated;
  }

  public String getActiveAccounts(int userId) throws IdNotInDatabaseException {
    DatabaseSelectHelper selector = new DatabaseSelectHelper(context);
    Translator translator = new Translator(context);
    String translated = "";
    List<Integer> accounts = selector.getUserActiveAccountsHelper(userId);
    if (selector.getUserRoleId(userId) != selector.getRoleId(Roles.CUSTOMER)) {
      throw new IdNotInDatabaseException();
    }
    String activeAccounts =
        "Here is the list of account ids that are active for user " + userId;
    translated = translated + translator.translate(activeAccounts, LanguageSingleton.
            getInstance().getValue()) + "\n";
    for (int i = 0; i < accounts.size(); i++) {
      activeAccounts = "" + accounts.get(i);
      translated = translated + translator.translate(activeAccounts, LanguageSingleton.
              getInstance().getValue()) + "\n";
    }
    return translated;
  }

}
