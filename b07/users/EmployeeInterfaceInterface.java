package com.b07.users;

import android.content.Context;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.IdNotInDatabaseException;
import com.b07.exceptions.InvalidInputException;
import com.b07.inventory.Item;
import java.io.Serializable;

public interface EmployeeInterfaceInterface extends Serializable {

  void setCurrentEmployee(EmployeeInterf employee);

  boolean hasCurrentEmployee();

  void upgradeAccount(int id);

  Context getContext();

  void setContext(Context context);

  /**
   * Returns true if the given the item and its quantity has been inserted into inventory, false
   * otherwise
   *
   * @param item Item object wanting to be restocked
   * @param quantity Quantity of that object wanted to be restocked
   * @return true if successfully restocked
   * @throws IdNotInDatabaseException Throw if item object not in database
   * @throws InvalidInputException Throw if given invalid quantity
   */
  boolean restockInventory(Item item, int quantity)
      throws IdNotInDatabaseException, InvalidInputException;

  /**
   * Returns the id of the newly created customer with the given info
   *
   * @return id of the new customer
   * @throws InvalidInputException If one of the parameters is not in correct format
   * @throws DatabaseInsertException If failed to insert into database
   */
  int createCustomer(String name, int age, String address, String password)
      throws InvalidInputException, DatabaseInsertException;

  /**
   * Returns the id of the newly created employee with the given info
   *
   * @return id of the new employee
   * @throws InvalidInputException If one of the parameters is not in correct format
   */
  int createEmployee(String name, int age, String address, String password)
      throws InvalidInputException, DatabaseInsertException;

}
