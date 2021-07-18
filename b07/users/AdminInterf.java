package com.b07.users;

import com.b07.exceptions.IdNotInDatabaseException;
import com.b07.exceptions.InvalidInputException;
import com.b07.inventory.ItemTypes;
import java.util.HashMap;

public interface AdminInterf extends UserInterface {

  /**
   * Returns true if successfully changed role of an Employee to the role of Admin
   *
   * @param employee Employee to be promoted
   * @return true if the Employee has successfully been promoted to an admin
   * @throws IdNotInDatabaseException Throws an exception if the role id is incorrect
   */
  boolean promoteEmployee(EmployeeInterf employee)
      throws IdNotInDatabaseException;

  /**
   * Returns a table of the items currently in the inventory and their quantity
   *
   * @return a hashmap of item types and their quantity
   */
  HashMap<ItemTypes, Integer> mapStockLevels();

  /**
   * A print function that allows the admin to see the details of sales
   */
  String Viewbook();

  /**
   * Returns true if successfully updated an item by a specific price
   *
   * @param newPrice Price that wants to be used in the database
   * @param itemId Item to be updated
   * @return true if it has successfully changed the price of the item, else false
   * @throws IdNotInDatabaseException Throws an exception if the given id does not exist in the
   * database
   * @throws InvalidInputException Throws an exception if the new price being inputed is invalid
   */
  boolean changePrices(long newPrice, int itemId)
      throws IdNotInDatabaseException, InvalidInputException;

  String getHistoricAccounts(int userId)
      throws IdNotInDatabaseException;

  String getActiveAccounts(int userId)
      throws IdNotInDatabaseException;
}
