package com.b07.users;

import android.content.Context;
import java.io.Serializable;

@SuppressWarnings("serial")
public interface UserInterface extends Serializable {

  boolean authenticate(String password, Context context);

  int getRoleId();

  String getAddress();

  void setAddress(String address);

  int getAge();

  void setAge(int age);

  String getName();

  void setName(String name);

  int getId();

  void setId(int id);

  boolean getAuthenticate();

}
