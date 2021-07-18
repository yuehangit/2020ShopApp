package com.b07.users;

import android.content.Context;
import android.widget.Toast;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.exceptions.IdNotInDatabaseException;
import com.b07.exceptions.InvalidInputException;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.translate.LanguageSingleton;
import com.b07.translate.Translator;
import java.io.Serializable;

public class EmployeeInterface implements EmployeeInterfaceInterface, Serializable {

  private EmployeeInterf currentEmployee = null;
  private transient Context context;
  private Inventory inventory;
  private transient Translator translator;

  public EmployeeInterface(EmployeeInterf employee, Inventory inventory, Context context) {

    if (employee.getAuthenticate()) {
      this.currentEmployee = employee;
    }
    this.inventory = inventory;
    this.context = context;
    translator = new Translator(context);
  }

  public EmployeeInterface(Inventory inventory, Context context) {
    this.inventory = inventory;
    this.context = context;
    translator = new Translator(context);
  }

  public Context getContext() {
    return this.context;
  }

  public void setContext(Context context) {
    this.context = context;
    translator = new Translator(context);
  }

  @Override
  public void upgradeAccount(int id) {
    DatabaseInsertHelper mydbInsert = new DatabaseInsertHelper(this.context);
    if (mydbInsert.insertAccountHelper(id) != -1) {
      Toast.makeText(context,
          translator.translate("Account created", LanguageSingleton.getInstance().getValue()),
          Toast.LENGTH_SHORT)
          .show();
    } else {
      Toast.makeText(context, translator
              .translate("Account creation failure", LanguageSingleton.getInstance().getValue()),
          Toast.LENGTH_SHORT)
          .show();
    }
  }

  @Override
  public void setCurrentEmployee(EmployeeInterf employee) {
    if (employee.getAuthenticate()) {
      currentEmployee = employee;
    }
  }

  @Override
  public boolean hasCurrentEmployee() {
    return currentEmployee != null;
  }

  @Override
  public boolean restockInventory(Item item, int quantity) {
    if (item == null) {
      return false;
    }
    DatabaseSelectHelper mydbSelect = new DatabaseSelectHelper(context);
    DatabaseUpdateHelper mydbUpdate = new DatabaseUpdateHelper(context);
    int updateOrInsert = mydbSelect.getInventoryQuantity(item.getId());
    DatabaseInsertHelper mydbInsert = new DatabaseInsertHelper(context);
    boolean success;
    try {
      if (updateOrInsert == -1) {
        mydbInsert.insertInventoryHelper(item.getId(), quantity);
        success = true;
      } else {
        success = mydbUpdate.updateInventoryQuantityHelper(quantity, item.getId());
      }
    } catch (InvalidInputException | IdNotInDatabaseException e) {
      Toast.makeText(context,
          translator.translate("Invalid input", LanguageSingleton.getInstance().getValue()),
          Toast.LENGTH_SHORT)
          .show();
      return false;
    }
    inventory.updateMap(item, quantity);
    if (success) {
      Toast.makeText(context,
          translator.translate("Inventory restocked", LanguageSingleton.getInstance().getValue()),
          Toast.LENGTH_SHORT)
          .show();
      return true;
    } else {
      Toast.makeText(context,
          translator.translate("Restock failed", LanguageSingleton.getInstance().getValue()),
          Toast.LENGTH_SHORT)
          .show();
      return false;
    }
  }

  @Override
  public int createCustomer(String name, int age, String address, String password)
      throws InvalidInputException {
    DatabaseInsertHelper mydb = new DatabaseInsertHelper(context);
    int customer = mydb.insertNewUserHelper(name, age, address, password);
    int customerRole = mydb.insertRoleHelper(Roles.CUSTOMER.toString());
    return mydb.insertUserRoleHelper(customer, customerRole);
  }

  @Override
  public int createEmployee(String name, int age, String address, String password)
      throws InvalidInputException {
    DatabaseInsertHelper mydb = new DatabaseInsertHelper(context);
    int customer = mydb.insertNewUserHelper(name, age, address, password);
    int customerRole = mydb.insertRoleHelper(Roles.EMPLOYEE.toString());
    return mydb.insertUserRoleHelper(customer, customerRole);
  }
}
