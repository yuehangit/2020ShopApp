package com.b07.database.helper;

import android.content.Context;
import android.widget.Toast;
import com.b07.database.DatabaseDriverAndroid;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.IdNotInDatabaseException;
import com.b07.exceptions.InvalidInputException;
import com.b07.inventory.ItemTypes;
import com.b07.users.Roles;
import java.math.BigDecimal;

public class DatabaseInsertHelper extends DatabaseDriverAndroid {

  private Context appContext;

  public DatabaseInsertHelper(Context context) {
    super(context);
    appContext = context;
  }

  /**
   * Takes in a name for the role and inserts it into the database if it hasn't, if it was inserted
   * or the role exists, the function returns the roleId.
   *
   * @param name the name of the user
   * @return roleId if the role has already been inserted or if successfully inserted
   */
  public int insertRoleHelper(String name) {
    if (Roles.valueOf(name) == null) {
      return -1;
    }
    try {
      DatabaseSelectHelper dbSelect = new DatabaseSelectHelper(appContext);
      return dbSelect.getRoleId(Roles.valueOf(name));
    } catch (IdNotInDatabaseException e) {
      long roleId = super.insertRole(name);
      return Math.toIntExact(roleId);
    }
  }

  /**
   * Inserts a new user into the database. Returns the new user id if it was successfully inserted.
   * Otherwise -1 is returned due to database errors.
   *
   * @param name the name of the user
   * @param age the age of the user
   * @param address the user's address
   * @param password the user's password
   * @return userId if successful or -1 otherwise
   */
  public int insertNewUserHelper(String name, int age, String address,
      String password) throws InvalidInputException {
    long userId;
    if (DataInfoValidator.checkAddressValid(address)
        && DataInfoValidator.checkStringValid(name) && age > 0) {
      userId = super.insertNewUser(name, age, address, password);
      return Math.toIntExact(userId);
    } else {
      throw new InvalidInputException();
    }
  }

  /**
   * For a given user and role, assign the user with a specific roleId. If it was successful, the
   * roleId is returned. Otherwise, -1 is returned due to database errors.
   *
   * @param userId the ID number of the user
   * @param roleId the ID number of the role to be assigned
   * @return userRoleId if successful or -1 otherwise
   * @throws InvalidInputException if roleId or userId do not exist in the database
   */
  public int insertUserRoleHelper(int userId, int roleId)
      throws InvalidInputException {
    long userRoleId = -1;
    if (DataInfoValidator.checkRoleExists(roleId, appContext) == false ||
        DataInfoValidator.checkUserExists(userId, appContext) == false) {
      throw new InvalidInputException();
    }
    userRoleId = super.insertUserRole(userId, roleId);
    return Math.toIntExact(userRoleId);
  }

  /**
   * Inserts an item into the database and returns the itemId if successful. Otherwise, returns -1
   * due to database errors.
   *
   * @param name the name of the item
   * @param price the price of the item
   * @return itemId if successfully inserted or -1 otherwise
   * @throws InvalidInputException if length of name > 64, or price is 0/negative
   */
  public int insertItemHelper(String name, BigDecimal price)
      throws InvalidInputException {
    long itemId = -1;
    if (!(DataInfoValidator.checkStringValid(name)) ||
        ItemTypes.getEnum(name) == null ||
        !DataInfoValidator.checkBigDecimalValid(price) ||
        name.length() >= 64) {
      throw new InvalidInputException();
    }
    itemId = super.insertItem(name, price);
    return Math.toIntExact(itemId);
  }

  /**
   * Insert an item with a given quantity into the inventory, returns the inventory id if
   * successful. Otherwise, returns -1 due to database errors.
   *
   * @param itemId the id of the item to be inserted
   * @param quantity the amount of the item to insert
   * @return the id of the inventory or -1 otherwise
   * @throws InvalidInputException if quantity is negative or itemId could not be found in the
   * database
   */
  public int insertInventoryHelper(int itemId, int quantity)
      throws InvalidInputException {
    long inventoryId = -1;
    if (quantity < 0 || DataInfoValidator.checkItemExists(itemId, appContext) == false) {
      throw new InvalidInputException();
    }
    inventoryId = super.insertInventory(itemId, quantity);
    return Math.toIntExact(inventoryId);
  }

  /**
   * Inserts a new sale that records the user who bought it and the total price of it, returns
   * saleId if successful. Otherwise, returns -1 due to database errors.
   *
   * @param userId the id of the user making the sale
   * @param totalPrice the sub-total of the sale
   * @return the id of the sale
   * @throws InvalidInputException if price is negative or the userId could not be found in the
   * database
   */
  public int insertSaleHelper(int userId, BigDecimal totalPrice)
      throws InvalidInputException {
    long saleId = -1;
    if (DataInfoValidator.checkUserExists(userId, appContext) == false ||
        !DataInfoValidator.checkBigDecimalValid(totalPrice)) {
      throw new InvalidInputException();
    }
    saleId = super.insertSale(userId, totalPrice);
    return Math.toIntExact(saleId);
  }

  /**
   * Inserts an itemized sale. Returns itemizedId if successful, otherwise returns -1 due to
   * database errors.
   *
   * @param saleId the ID of the sale
   * @param itemId the ID of the item
   * @param quantity the amount purchased in the sale of the item
   * @return itemizedId if successful or -1 otherwise
   * @throws InvalidInputException if saleId/itemId could not be found in the database or if
   * quantity is 0/negative
   */
  public int insertItemizedSaleHelper(int saleId, int itemId, int quantity)
      throws InvalidInputException {
    long itemizedId = -1;
    if (DataInfoValidator.checkSaleExists(saleId, appContext) == false
        || DataInfoValidator.checkItemExists(itemId, appContext) == false || quantity <= 0) {
      throw new InvalidInputException();
    }
    itemizedId = super.insertItemizedSale(saleId, itemId, quantity);
    return Math.toIntExact(itemizedId);
  }

  /**
   * Inserts an account. Returns id of account if successful, otherwise returns -1
   *
   * @param userId the id of the user
   * @return account id if successful or -1 otherwise
   * @throws DatabaseInsertException if could not add account to database
   */
  public int insertAccountHelper(int userId) {
    long accountId = -1;
    if (!DataInfoValidator.checkUserExists(userId, appContext)) {
      return -1;
    }
    try {
      DatabaseSelectHelper dbSelect = new DatabaseSelectHelper(appContext);
      if (dbSelect.getUserRoleId(userId) !=
          dbSelect.getRoleId(Roles.CUSTOMER)) {
        Toast.makeText(appContext, "User is not a customer", Toast.LENGTH_SHORT).show();
        return -1;
      }
    } catch (IdNotInDatabaseException e1) {
      Toast.makeText(appContext, "Id not in database", Toast.LENGTH_SHORT).show();
      return -1;
    }
    accountId = super.insertAccount(userId, true);
    return Math.toIntExact(accountId);
  }

  /**
   * insert a single item into a given account for recovery next login.
   *
   * @param accountId the id of the account
   * @param itemId the item to be inserted
   * @param quantity the amount of that item
   * @return the id of the inserted record
   * @throws DatabaseInsertException if could not insert the item
   * @throws InvalidInputException if accountId/itemId is not in database or if quantity is not
   * positive
   */
  public int insertAccountLineHelper(int accountId, int itemId, int quantity)
      throws InvalidInputException {
    long recordId = -1;
    if (!DataInfoValidator.checkItemExists(itemId, appContext) || quantity < 0) {
      throw new InvalidInputException();
    }
    recordId = super.insertAccountLine(accountId, itemId, quantity);
    return Math.toIntExact(recordId);
  }
}
