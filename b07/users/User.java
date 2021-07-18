package com.b07.users;

import android.content.Context;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.security.PasswordHelpers;
import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class User implements UserInterface, Serializable {

  protected int id;
  protected String name;
  protected int age;
  protected String address;
  protected int roleId = -1;
  protected boolean authenticated = false;

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int getAge() {
    return this.age;
  }

  @Override
  public void setAge(int age) {
    this.age = age;
  }

  @Override
  public String getAddress() {
    return this.address;
  }

  @Override
  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public int getRoleId() {
    return this.roleId;
  }

  @Override
  public final boolean authenticate(String password, Context context) {
    String dbPassword = null;
    DatabaseSelectHelper selector = new DatabaseSelectHelper(context);
    dbPassword = selector.getPassword(this.id);
    authenticated = PasswordHelpers.comparePassword(dbPassword, password);

    return authenticated;
  }

  @Override
  public boolean getAuthenticate() {
    return authenticated;
  }
}
