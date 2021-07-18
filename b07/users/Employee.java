package com.b07.users;

import android.content.Context;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.exceptions.DatabaseInsertException;

public class Employee extends User implements EmployeeInterf {

  private transient Context context;

  public Employee(int id, String name, int age, String address, Context context)
      throws DatabaseInsertException {
    this.id = id;
    this.name = name;
    this.age = age;
    this.address = address;
    this.context = context;
    DatabaseInsertHelper inserter = new DatabaseInsertHelper(context);
    this.roleId = inserter.insertRoleHelper(Roles.EMPLOYEE.getRole());
  }

  public Employee(int id, String name, int age, String address, boolean authenticated,
      Context context)
      throws DatabaseInsertException {
    this.id = id;
    this.name = name;
    this.age = age;
    this.address = address;
    this.authenticated = authenticated;
    this.context = context;
    DatabaseInsertHelper inserter = new DatabaseInsertHelper(context);
    this.roleId = inserter.insertRoleHelper(Roles.EMPLOYEE.getRole());
  }

}
