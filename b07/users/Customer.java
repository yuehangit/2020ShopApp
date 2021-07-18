package com.b07.users;

import android.content.Context;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.IdNotInDatabaseException;
import com.b07.store.Sale;
import com.b07.translate.LanguageSingleton;
import com.b07.translate.Translator;
import java.util.List;

public class Customer extends User implements CustomerInterf {

  private transient Context context;

  public Customer(int id, String name, int age, String address, Context context)
      throws DatabaseInsertException {
    this.id = id;
    this.name = name;
    this.age = age;
    this.address = address;
    this.context = context;
    DatabaseInsertHelper inserter = new DatabaseInsertHelper(context);
    this.roleId = inserter.insertRoleHelper(Roles.CUSTOMER.getRole());
  }

  public Customer(int id, String name, int age, String address, boolean authenticated,
      Context context)
      throws DatabaseInsertException {
    this.id = id;
    this.name = name;
    this.age = age;
    this.address = address;
    this.authenticated = authenticated;
    this.context = context;
    DatabaseInsertHelper inserter = new DatabaseInsertHelper(context);
    this.roleId = inserter.insertRoleHelper(Roles.CUSTOMER.getRole());
  }

  public Context getContext() {
    return this.context;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  public String viewPurchases() throws IdNotInDatabaseException {
    String purchases = "";
    String dashline = "--------------------------------------------------------------------------\n";
    DatabaseSelectHelper selector = new DatabaseSelectHelper(context);
    List<Sale> sales = selector.getSalesToUserHelper(getId());
    if (sales.isEmpty()) {
      return purchases;
    } else {
      for (Sale sale : sales) {
        purchases = purchases.concat(
            sale.saleInfo() + "\n"
                + dashline);
      }
    }
    return purchases;
  }
}
