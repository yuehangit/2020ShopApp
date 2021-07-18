package com.b07.users;

import android.content.Context;
import com.b07.exceptions.IdNotInDatabaseException;

public interface CustomerInterf extends UserInterface {

  Context getContext();

  void setContext(Context context);

  /**
   * print the customer's sales
   */
  String viewPurchases() throws IdNotInDatabaseException;

}
