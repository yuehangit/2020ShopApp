package com.b07.store;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.b07.database.helper.DataInfoValidator;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.exceptions.IdNotInDatabaseException;
import com.b07.exceptions.InvalidInputException;
import com.b07.exceptions.OutOfStockException;
import com.b07.inventory.Item;
import com.b07.translate.LanguageSingleton;
import com.b07.translate.Translator;
import com.b07.users.CustomerInterf;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("serial")
public class ShoppingCart implements ShoppingCartInterface, Serializable {

  private final static BigDecimal TAXRATE = new BigDecimal(1.13);
  private HashMap<Item, Integer> items = new HashMap<Item, Integer>();
  private CustomerInterf customer = null;
  private int accountId = -1;
  private BigDecimal total = new BigDecimal(0);
  private boolean isRestored = false;
  private transient Translator trans;
  private transient Context context;

  public ShoppingCart(CustomerInterf customer, Context context) {
    if (customer != null) {
      this.customer = customer;
      this.context = context;
      DatabaseSelectHelper selector = new DatabaseSelectHelper(context);
    } else {
      Toast.makeText(context, "Cannot make cart", Toast.LENGTH_SHORT).show();
    }
  }

  public Context getContext() {
    return this.context;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  @Override
  public boolean hasAccount() {
    DatabaseSelectHelper selector = new DatabaseSelectHelper(context);
    try {
      return selector.getUserActiveAccountsHelper(customer.getId()).isEmpty() == false;
    } catch (IdNotInDatabaseException e) {
      return false;
    }
  }

  private boolean compareCarts(HashMap<Item, Integer> current, HashMap<Item, Integer> saved) {
    Log.d("COMAPRE CART", "CALLED");
    if (current.size() != saved.size() && saved.size() != 0) {
      Log.d("COMAPRE CART", "SIZE IS DIFFERENT: currentcart: " + current.size() + " savedcart: " + saved.size());
      return false;
    } else {
      boolean same;
      for (Item item : current.keySet()) {
        same = false;
        checkloop:
        for (Item item2 : saved.keySet()) {
          Log.d("COMPARE CART", " Comarping " + item.getName() + " and " + item2.getName());
          Log.d("COMPARE CART", "ITEM FROM CURRENT CART ID = " + item.getId() + " ITEM FROM SAVED CART ID = " + item2.getId());
          Log.d("COMPARE CART", "QUANTITY: cur: " + current.get(item) + " saved: " + saved.get(item2));
          if (item.getId() == item2.getId()) {
            Log.d("COMPARE CART", "ITEM ID SAME");
          } else {
            Log.d("COMPARE CART", "ITEM ID NOT SAME");
          }
          if (current.get(item).intValue() == saved.get(item2).intValue()) {
            Log.d("COMPARE CART", "QUANTITY SAME");
          } else {
            Log.d("COMPARE CART", "QUANTITY NOT SAME");
          }
          if ( item.getId() == item2.getId() && current.get(item).intValue() == saved.get(item2).intValue()) {
            Log.d("COMPARE CART", "same");
            same = true;
            break checkloop;
          }
        }
        if (same == false) {
          Log.d("COMPARE CART", "CART DIFFERENT");
          return false;
        }
      }
      Log.d("COMPARE CART", "CART SAME");
      return true;
    }
  }

  @Override
  public HashMap<Integer, HashMap<Item, Integer>> getPrevCart() {
    HashMap<Integer, HashMap<Item, Integer>> prevCarts =
        new HashMap<Integer, HashMap<Item, Integer>>();
    List<Integer> accountIds;
    try {
      DatabaseSelectHelper selector = new DatabaseSelectHelper(context);
      accountIds = selector.getUserActiveAccountsHelper(customer.getId());
    } catch (IdNotInDatabaseException e) {
      return prevCarts;
    }
    DatabaseSelectHelper selector = new DatabaseSelectHelper(context);
    for (Integer acc : accountIds) {
      HashMap<Item, Integer> details = selector.getAccountDetailsHelper(acc);
      if (details.isEmpty() == false) {
        prevCarts.put(acc, details);
      }
    }
    return prevCarts;
  }

  @Override
  public int getQuantityInCart(int itemId) {
    for (Map.Entry<Item, Integer> entry : items.entrySet()) {
      Item keyItem = entry.getKey();
      Integer keyValue = entry.getValue();
      if (keyItem.getId() == itemId) {
        return keyValue;
      }
    }
    return 0;
  }

  @Override
  public void addItem(Item item, int quantity) throws InvalidInputException {
    if (item == null || DataInfoValidator.checkItemExists(item.getId(), context) == false) {
      throw new InvalidInputException();
    } else {
      SaleHelper.updateMap(item, quantity, items);
      total = SaleHelper.generateTotal(items);
    }
  }

  @Override
  public void removeItem(Item item, int quantity) throws InvalidInputException {
    if (item == null || DataInfoValidator.checkItemExists(item.getId(), context) == false) {
      throw new InvalidInputException();
    } else {
      SaleHelper.updateMap(item, -quantity, items);
      total = SaleHelper.generateTotal(items);
    }
  }

  @Override
  public List<Item> getItems() {
    Set<Item> itemKeys = items.keySet();
    List<Item> allItems = new ArrayList<Item>(itemKeys);
    return allItems;
  }

  @Override
  public CustomerInterf getCustomer() {
    return customer;
  }

  @Override
  public BigDecimal getTotal() {
    total = SaleHelper.generateTotal(items);
    return this.total;
  }

  @Override
  public BigDecimal getTaxRate() {
    return TAXRATE;
  }

  @Override
  public boolean checkOut()
      throws InvalidInputException, IdNotInDatabaseException, OutOfStockException {
    if (customer == null) {
      return false;
    }
    total = SaleHelper.generateTotal(items);
    if (total == new BigDecimal(0)) {
      trans = new Translator(context);
      Toast.makeText(context, trans
              .translate("Empty cart, nothing was bought", LanguageSingleton.getInstance().getValue()),
          Toast.LENGTH_SHORT).show();
      return false;
    }
    List<Item> itemsPurchased = this.getItems();
    int i = 0;
    int itemId = -1;
    int quantity = 0;
    int quantityBought;
    DatabaseSelectHelper selector = new DatabaseSelectHelper(context);
    while (i < itemsPurchased.size()) {
      itemId = itemsPurchased.get(i).getId();
      quantity = selector.getInventoryQuantity(itemId);
      quantityBought = this.getQuantityInCart(itemId);
      if (quantity < quantityBought) {
        throw new OutOfStockException();
      }
      i++;
    }
    total = SaleHelper.generateTotal(items);
    BigDecimal taxTotal = total.multiply(TAXRATE).setScale(2, RoundingMode.CEILING);
    DatabaseInsertHelper inserter = new DatabaseInsertHelper(context);
    DatabaseUpdateHelper updator = new DatabaseUpdateHelper(context);
    int saleId = inserter.insertSaleHelper(customer.getId(), taxTotal);
    i = 0;
    while (i < itemsPurchased.size()) {
      itemId = itemsPurchased.get(i).getId();
      quantity = selector.getInventoryQuantity(itemId);
      quantityBought = this.getQuantityInCart(itemId);
      updator.updateInventoryQuantityHelper(quantity - quantityBought, itemId);
      int insertId = inserter.insertItemizedSaleHelper(saleId, itemId, quantityBought);
      if (insertId == -1) {
        throw new IdNotInDatabaseException();
      }
      i++;
    }
    this.clearCart();
    if (accountId != -1) {
      trans = new Translator(context);
      if (!updator.updateAccountStatusHelper(accountId, false)) {
        Toast.makeText(context, trans
            .translate("Failed to deactivate account. Please seek an employee for help.",
                LanguageSingleton.getInstance().getValue()), Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(context, trans.translate("Deactivated account!",
            LanguageSingleton.getInstance().getValue()), Toast.LENGTH_SHORT).show();
      }
    }
    return true;
  }


  @Override
  public boolean saveCart() throws InvalidInputException {
    boolean sameCart = false;
    DatabaseSelectHelper selector = new DatabaseSelectHelper(context);
    try {
      for (Integer acc : selector.getUserActiveAccountsHelper(customer.getId())) {
        Log.d("SAVE CART", "CHECKING USER " + customer.getId() + " ACCOUNT " + acc);

        for (Item item: selector.getAccountDetailsHelper(acc).keySet()) {
          Log.d("ACCOUNT DETAILS", "Item: " + item.getName());
        }

        if (selector.getAccountDetailsHelper(acc).size() == 0) {
          Log.d("SAVE CART", "THIS SAVED CART SIZE IS 0");
        } else {
          Log.d("SAVE CART", "THIS SAVED CART HAS SIZE OF " + selector.getAccountDetailsHelper(acc).size());
        }

        if (selector.getAccountDetailsHelper(acc).isEmpty()) {
          Log.d("SAVE CART", "THIS SAVED CART IS EMPTY");
        }

        if (selector.getAccountDetailsHelper(acc).size() != 0) {
          if (compareCarts(items, selector.getAccountDetailsHelper(acc)) == true) {
            sameCart = true;
            return true;
          }
        }
      }
    } catch (IdNotInDatabaseException e1) {
      Log.d("SAVE CART", "Id not in database");
    }
    if (((isRestored == true && sameCart == false) || isRestored == false)
        && items.isEmpty() == false) {

      if (isRestored) {
          Log.d("SAVE CART", "isRestored is true");
      }
      if (sameCart == false) {
          Log.d("SAVE CART", "Cart changed");
      }

      if (items.isEmpty() == false) {
        Log.d("SAVE CART", "Cart is not empty");
      }



      int id = -1;
      try {
        for (Integer acc : selector.getUserActiveAccountsHelper(customer.getId())) {
          if (selector.getAccountDetailsHelper(acc).isEmpty() == true) {
            id = acc;
            break;
          }
        }
      } catch (IdNotInDatabaseException e) {

      }
      if (id == -1) {
        return false;
      }
      DatabaseInsertHelper inserter = new DatabaseInsertHelper(context);
      for (Item item : items.keySet()) {
        inserter.insertAccountLineHelper(id, item.getId(), items.get(item));
      }
    }
    return true;
  }

  @Override
  public void restoreCart(int accountId) {
    this.accountId = accountId;
    DatabaseSelectHelper selector = new DatabaseSelectHelper(context);
    items = selector.getAccountDetailsHelper(this.accountId);
    isRestored = true;
  }

  public boolean isRestored() {
    return isRestored;
  }

  @Override
  public void clearCart() {
    items.clear();
  }
}
