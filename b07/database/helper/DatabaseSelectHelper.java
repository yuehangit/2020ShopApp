package com.b07.database.helper;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.b07.database.DatabaseDriverAndroid;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.IdNotInDatabaseException;
import com.b07.exceptions.InvalidInputException;
import com.b07.inventory.Inventory;
import com.b07.inventory.InventoryImpl;
import com.b07.inventory.Item;
import com.b07.inventory.ItemImpl;
import com.b07.inventory.ItemTypes;
import com.b07.store.Sale;
import com.b07.store.SaleImpl;
import com.b07.store.SalesLog;
import com.b07.store.SalesLogImpl;
import com.b07.users.Roles;
import com.b07.users.User;
import com.b07.users.UserFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DatabaseSelectHelper extends DatabaseDriverAndroid {

  private transient Context appContext;

  public DatabaseSelectHelper(Context context) {
    super(context);
    appContext = context;
  }

  /**
   * Gets the list of role IDs stored in the database.
   *
   * @return A list of roleIds or an empty list if there are no roles inserted in the database
   */

  public List<Integer> getRoleIds() {
    Cursor cursor = super.getRoles();
    List<Integer> ids = new ArrayList<>();
    while (cursor.moveToNext()) {
      ids.add(cursor.getInt(cursor.getColumnIndex("ID")));
    }
    cursor.close();
    return ids;
  }

  /**
   * Get the name of a given role Id.
   *
   * @param roleId the ID of the role
   * @return the name of the role
   * @throws IdNotInDatabaseException if roleId could not be found in the database
   */
  public String getRoleName(int roleId) {
    return super.getRole(roleId);
  }

  /**
   * Get role id given a role name.
   *
   * @param role the enumerator of the role
   * @return roleId the ID of the role
   */
  public int getRoleId(Roles role) throws IdNotInDatabaseException {
    Cursor cursor = super.getRoles();
    while (cursor.moveToNext()) {
      if (cursor.getString(cursor.getColumnIndex("NAME")).equals(role.toString())) {
        int returnInteger = cursor.getInt(cursor.getColumnIndex("ID"));
        cursor.close();
        return returnInteger;
      }
    }
    throw new IdNotInDatabaseException();
  }

  /**
   * Get the role id for a given user. Returns -1 due to database errors.
   *
   * @return roleId
   */
  public int getUserRoleId(int userId) {
    return super.getUserRole(userId);
  }

  /**
   * Get a list of users of the same role.
   *
   * @return userIds
   */
  public List<Integer> getUsersByRoleHelper(int roleId) {
    Cursor cursor = super.getUsersByRole(roleId);
    List<Integer> userIds = new ArrayList<>();
    while (cursor.moveToNext()) {
      userIds.add(cursor.getInt(cursor.getColumnIndex("ID")));
    }
    cursor.close();
    return userIds;
  }

  /**
   * Get a list of all users and their details
   *
   * @return users
   */
  public List<User> getUsersDetailsHelper() {
    List<Integer> allUserIds = getUsersIds();
    List<User> allUsers = new ArrayList<>();
    for (Integer id : allUserIds) {
      allUsers.add(getUserDetailsHelper(id));
    }
    return allUsers;
  }

  /**
   * Get a list of user ids
   *
   * @return usersIds
   */
  public List<Integer> getUsersIds() {
    Cursor cursor = super.getUsersDetails();
    List<Integer> usersIds = new ArrayList<>();
    while (cursor.moveToNext()) {
      usersIds.add(cursor.getInt(cursor.getColumnIndex("ID")));
    }
    cursor.close();
    return usersIds;
  }

  /**
   * Get details of a given user id
   *
   * @return user
   */
  public User getUserDetailsHelper(int userId) {
    Cursor cursor = getUserDetails(userId);
    if (cursor == null || !DataInfoValidator.checkUserExists(userId, appContext)) {
      return null;
    }
    User user = null;
    while (cursor.moveToNext()) {
      String userName = cursor.getString(cursor.getColumnIndex("NAME"));
      int userAge = cursor.getInt(cursor.getColumnIndex("AGE"));
      String userAddress = cursor.getString(cursor.getColumnIndex("ADDRESS"));
      int roleId = getUserRoleId(userId);
      try {
        user = (User) UserFactory
            .createUser(userId, userName, userAge, userAddress, roleId, appContext);
      } catch (DatabaseInsertException e) {
        user = null;
      }
      break;
    }
    cursor.close();
    return user;
  }

  /**
   * Get the password for a given userId
   *
   * @return password
   */
  public String getPassword(int userId) {
    return super.getPassword(userId);
  }

  /**
   * Get a list of all the items added to the database
   *
   * @return items
   */
  public List<Item> getAllItemsHelper() {
    List<Item> items = new ArrayList<>();
    Cursor cursor = super.getAllItems();
    while (cursor.moveToNext()) {
      ItemTypes item =
          ItemTypes.getEnum(cursor.getString(cursor.getColumnIndex("NAME")));
      BigDecimal price =
          new BigDecimal(cursor.getString(cursor.getColumnIndex("PRICE")));

      items.add(new ItemImpl(cursor.getInt(cursor.getColumnIndex("ID")), item, price));
    }
    cursor.close();
    return items;
  }

  /**
   * Get details of an item based on the given item id
   *
   * @return item
   */
  public Item getItemHelper(int itemId) throws IdNotInDatabaseException {
    if (DataInfoValidator.checkItemExists(itemId, appContext) == false) {
      Log.d("GET ITEM HELPER", "CHECKING FOR IF " + itemId + " EXISTS");
      throw new IdNotInDatabaseException();
    }
    List<Item> items = getAllItemsHelper();
    int i = 0;
    while (i < items.size()) {
      if (items.get(i).getId() == itemId) {
        return items.get(i);
      }
      i++;
    }
    return null;
  }

  /**
   * Get a list of the details of the current inventory in the shop
   *
   * @return inventory
   */
  public Inventory getInventoryHelper() {
    Cursor cursor = super.getInventory();
    Inventory inventory = new InventoryImpl();
    while (cursor.moveToNext()) {
      Integer quantity = cursor.getInt(cursor.getColumnIndex("QUANTITY"));
      try {
        Item itemId = getItemHelper(cursor.getInt(cursor.getColumnIndex("ITEMID")));
        inventory.updateMap(itemId, quantity);
        inventory.setTotalItems(inventory.getTotalItems() + quantity);
      } catch (IdNotInDatabaseException e) {
        cursor.close();
        return null;
      }
    }
    cursor.close();
    return inventory;
  }

  /**
   * Get the quantity of a specific item in the store
   *
   * @return quantity
   */
  public int getInventoryQuantity(int itemId) {
    if (DataInfoValidator.checkItemExists(itemId, appContext) == false
        || !DataInfoValidator.checkIfItemInInventory(itemId, appContext)) {
      return -1;
    }
    return super.getInventoryQuantity(itemId);
  }

  /**
   * Get a list of sales of the shop
   *
   * @return allSales
   */
  public SalesLog getSalesHelper() {
    Cursor cursor = super.getSales();
    SalesLog allSales = new SalesLogImpl();
    while (cursor.moveToNext()) {
      int id = cursor.getInt(cursor.getColumnIndex("ID"));
      User user = getUserDetailsHelper(cursor.getInt(cursor.getColumnIndex("USERID")));
      Sale sale = new SaleImpl(id, user,
          new BigDecimal(cursor.getString(cursor.getColumnIndex("TOTALPRICE"))),
          appContext);
      allSales.addSale(sale);
    }
    cursor.close();
    return allSales;
  }

  /**
   * Get details of a sale for a given sale id
   *
   * @return sale
   */
  public Sale getSaleByIdHelper(int saleId) throws IdNotInDatabaseException {
    if (DataInfoValidator.checkSaleExists(saleId, appContext) == false) {
      Log.d("GET SALE BY ID ", "sale id does not exist");
      throw new IdNotInDatabaseException();
    }
    Cursor cursor = super.getSaleById(saleId);
    cursor.moveToFirst();
    int id = saleId;
    User user = getUserDetailsHelper(cursor.getInt(cursor.getColumnIndex("USERID")));
    BigDecimal price =
        new BigDecimal(cursor.getString(cursor.getColumnIndex("TOTALPRICE")));
    cursor.close();
    return new SaleImpl(id, user, price, appContext);
  }


  /**
   * Get a list of sale by a given user
   *
   * @return sales
   */
  public List<Sale> getSalesToUserHelper(int userId)
      throws IdNotInDatabaseException {
    if (DataInfoValidator.checkUserExists(userId, appContext) == false || userId == -1) {
      throw new IdNotInDatabaseException();
    }
    Cursor cursor = super.getSalesToUser(userId);
    List<Sale> sales = new ArrayList<>();
    while (cursor.moveToNext()) {
      if (cursor.getInt(cursor.getColumnIndex(("USERID"))) == userId) {
        Sale sale = getSaleByIdHelper(cursor.getInt(cursor.getColumnIndex("ID")));
        sales.add(sale);
      }
    }
    cursor.close();
    return sales;
  }


  /**
   * Get the itemized sales of a given sale
   *
   * @return sale
   */
  public Sale getItemizedSaleByIdHelper(int saleId) throws IdNotInDatabaseException {
    if (DataInfoValidator.checkSaleExists(saleId, appContext) == false) {
      throw new IdNotInDatabaseException();
    }
    Cursor cursor = super.getItemizedSaleById(saleId);
    Sale sale = getSaleByIdHelper(saleId);
    while (cursor.moveToNext()) {
      if (cursor.getInt(cursor.getColumnIndex(("SALEID"))) == saleId) {
        sale.updateMap(getItemHelper(cursor.getInt(cursor.getColumnIndex("ITEMID"))),
            cursor.getInt(cursor.getColumnIndex("QUANTITY")));
      }
    }
    cursor.close();
    return sale;
  }

  /**
   * Get a log of all the itemized sales made
   *
   * @return allSales
   */
  public SalesLog getItemizedSalesHelper() {
    Cursor cursor = super.getItemizedSales();
    SalesLog allSales = getSalesHelper();
    while (cursor.moveToNext()) {
      try {
        allSales.updateSale(getSaleByIdHelper(
            cursor.getInt(cursor.getColumnIndex("SALEID"))),
            getItemHelper(cursor.getInt(cursor.getColumnIndex("ITEMID"))),
            cursor.getInt(cursor.getColumnIndex("QUANTITY")));
      } catch (IdNotInDatabaseException e) {
        cursor.close();
        return null;
      }
    }
    cursor.close();
    return allSales;
  }

  /**
   * Get the accounts assigned to a given user.
   *
   * @param userId the id of the user.
   * @return a list containing the id's of the accounts.
   * @throws IdNotInDatabaseException if id is not in database
   * @throws InvalidInputException if user is not a customer
   */
  public List<Integer> getUserAccountsHelper(int userId)
      throws IdNotInDatabaseException, InvalidInputException {
    if (!DataInfoValidator.checkUserExists(userId, appContext)) {
      throw new IdNotInDatabaseException();
    }
    if (getUserRoleId(userId) != getRoleId(Roles.CUSTOMER)) {
      throw new InvalidInputException();
    }
    Cursor cursor = super.getUserAccounts(userId);
    List<Integer> accounts = new ArrayList<Integer>();
    while (cursor.moveToNext()) {
      accounts.add(cursor.getInt(cursor.getColumnIndex("ID")));
    }
    cursor.close();
    return accounts;
  }

  /**
   * Get the details of a given account.
   *
   * @param accountId the ID of the account.
   * @return the details associated to the given account
   */
  public HashMap<Item, Integer> getAccountDetailsHelper(int accountId) {
    Cursor cursor = super.getAccountDetails(accountId);
    HashMap<Item, Integer> previousCart = new HashMap<Item, Integer>();
    while (cursor.moveToNext()) {
      Integer quantity = cursor.getInt(cursor.getColumnIndex("QUANTITY"));
      Item itemId = null;
      try {
        itemId = getItemHelper(cursor.getInt(cursor.getColumnIndex("ITEMID")));
      } catch (IdNotInDatabaseException e) {
        previousCart = new HashMap<Item, Integer>();
        break;
      }
      previousCart.put(itemId, quantity);
    }
    cursor.close();
    return previousCart;
  }

  /**
   * gets a list of active accounts for a user
   *
   * @param userId the id of the user
   * @return a list of active account ids for the user
   * @throws IdNotInDatabaseException if user id is not in database
   */
  public List<Integer> getUserActiveAccountsHelper(int userId) throws IdNotInDatabaseException {
    Cursor cursor = super.getUserActiveAccounts(userId);
    List<Integer> accounts = new ArrayList<Integer>();
    int accId;
    if (!DataInfoValidator.checkUserExists(userId, appContext)) {
      throw new IdNotInDatabaseException();
    }
    while (cursor.moveToNext()) {
      accId = cursor.getInt(cursor.getColumnIndex("ID"));
      accounts.add(accId);
    }
    cursor.close();
    return accounts;
  }

  /**
   * gets a list of inactive accounts for a user
   *
   * @param userId the id of the user
   * @return a list of inactive account ids for the user
   * @throws IdNotInDatabaseException if user id is not in database
   */
  public List<Integer> getUserInactiveAccountsHelper(int userId) throws IdNotInDatabaseException {
    List<Integer> accounts = new ArrayList<Integer>();
    int accId;
    if (!DataInfoValidator.checkUserExists(userId, appContext)) {
      throw new IdNotInDatabaseException();
    }
    Cursor cursor = super.getUserInactiveAccounts(userId);
    while (cursor.moveToNext()) {
      accId = cursor.getInt(cursor.getColumnIndex("ID"));
      accounts.add(accId);
    }
    cursor.close();
    return accounts;
  }
}
  
  
