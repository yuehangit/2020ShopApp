package com.b07.database.helper;

import android.content.Context;
import com.b07.database.DatabaseDriverAndroid;
import com.b07.exceptions.IdNotInDatabaseException;
import com.b07.exceptions.InvalidInputException;
import com.b07.users.Roles;
import java.math.BigDecimal;

public class DatabaseUpdateHelper extends DatabaseDriverAndroid {

  private Context appContext;

  public DatabaseUpdateHelper(Context context) {
    super(context);
    appContext = context;
  }

  /**
   * Updates the role name of a role by its given id. Returns true if successful.
   *
   * @return true if successfully updated role name in database
   * @throws IdNotInDatabaseException If name is not part of enumerators Roles or id not in
   * database
   */
  public boolean updateRoleNameHelper(String name, int id) throws IdNotInDatabaseException {
    if (DataInfoValidator.checkRoleExists(id, appContext) == false
        || Roles.contains(name) == false) {
      throw new IdNotInDatabaseException();
    }
    boolean complete = super.updateRoleName(name, id);
    return complete;
  }

  /**
   * Updates the name for a given user id. Returns true if successful
   *
   * @return true if updated name of user id in database
   */
  public boolean updateUserNameHelper(String name, int userId)
      throws InvalidInputException, IdNotInDatabaseException {
    if (DataInfoValidator.checkStringValid(name) == false) {
      throw new InvalidInputException();
    } else if (DataInfoValidator.checkUserExists(userId, appContext) == false) {
      throw new IdNotInDatabaseException();
    }
    boolean complete = super.updateUserName(name, userId);
    return complete;
  }

  /**
   * Updates the age for a given user id. Returns true if successful
   *
   * @return true if updated age of given user id in database
   */
  public boolean updateUserAgeHelper(int age, int userId)
      throws IdNotInDatabaseException, InvalidInputException {
    if (age < 0) {
      throw new InvalidInputException();
    } else if (DataInfoValidator.checkUserExists(userId, appContext) == false) {
      throw new IdNotInDatabaseException();
    }
    boolean complete = super.updateUserAge(age, userId);
    return complete;
  }

  /**
   * Update the address for a given user id. Returns true if successful
   *
   * @return true if updated address of a given user id in database
   */
  public boolean updateUserAddressHelper(String address, int userId)
      throws IdNotInDatabaseException, InvalidInputException {
    if (DataInfoValidator.checkUserExists(userId, appContext) == false) {
      throw new IdNotInDatabaseException();
    }
    if (DataInfoValidator.checkAddressValid(address) == false) {
      throw new InvalidInputException();
    }
    boolean complete = super.updateUserAddress(address, userId);
    return complete;

  }

  /**
   * Update the role of given user id. Returns true if successful
   *
   * @return true if updated role of user id in database
   */
  public boolean updateUserRoleHelper(int roleId, int userId) throws IdNotInDatabaseException {
    if (DataInfoValidator.checkRoleExists(roleId, appContext) == false
        || DataInfoValidator.checkUserExists(userId, appContext) == false) {
      throw new IdNotInDatabaseException();
    }
    boolean complete = super.updateUserRole(roleId, userId);
    return complete;

  }

  /**
   * Update the item name for a given item. Returns true if successful
   *
   * @return true if updated item name of the item in database
   */
  public boolean updateItemNameHelper(String name, int itemId)
      throws InvalidInputException, IdNotInDatabaseException {
    if (name == null || name.length() >= 64) {
      throw new InvalidInputException();
    }
    if (DataInfoValidator.checkItemExists(itemId, appContext) == false) {
      throw new IdNotInDatabaseException();
    }
    boolean complete = super.updateItemName(name, itemId);
    return complete;

  }

  /**
   * Update the price of a given item. Returns true if successful
   *
   * @return true if price of item was changed in database
   */
  public boolean updateItemPriceHelper(BigDecimal price, int itemId)
      throws IdNotInDatabaseException, InvalidInputException {
    if (DataInfoValidator.checkItemExists(itemId, appContext) == false) {
      throw new IdNotInDatabaseException();
    }
    if (price.compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidInputException();
    }
    boolean complete = super.updateItemPrice(price, itemId);
    return complete;
  }

  /**
   * Update the quantity of a given item in the inventory. Returns true if successful
   *
   * @return true if quantity of item was changed in inventory in database
   */
  public boolean updateInventoryQuantityHelper(int quantity, int itemId)
      throws IdNotInDatabaseException {
    if (DataInfoValidator.checkItemExists(itemId, appContext) == false) {
      throw new IdNotInDatabaseException();
    } else if (quantity < 0) {
      quantity = 0;
    }
    boolean complete = super.updateInventoryQuantity(quantity, itemId);
    return complete;
  }

  public boolean updateAccountStatusHelper(int accountId, boolean active) {
    boolean complete = super.updateAccountStatus(accountId, active);
    return complete;
  }
}
