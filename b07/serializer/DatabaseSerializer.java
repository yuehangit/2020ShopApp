package com.b07.serializer;

import android.content.Context;
import android.widget.Toast;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.IdNotInDatabaseException;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.store.Sale;
import com.b07.store.SalesLog;
import com.b07.translate.LanguageSingleton;
import com.b07.translate.Translator;
import com.b07.users.Roles;
import com.b07.users.UserInterface;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class DatabaseSerializer {

  private Context context;
  private DatabaseSelectHelper selector;
  private Translator translator;

  public DatabaseSerializer(Context context) {
    this.context = context;
    selector = new DatabaseSelectHelper(context);
    translator = new Translator(context);

  }

  private static void bubbleSortAccounts(ArrayList<AccountSerializer> accounts) {
    int n = accounts.size();
    for (int i = 0; i < n - 1; i++) {
      for (int j = 0; j < n - i - 1; j++) {
        if (accounts.get(j).getAccountId() > accounts.get(j + 1).getAccountId()) {
          AccountSerializer temp = accounts.get(j);
          accounts.add(j, accounts.get(j + 1));
          accounts.add(j + 1, temp);
        }
      }
    }
  }

  private static void bubbleSortUsers(ArrayList<UserInterface> users) {
    int n = users.size();
    for (int i = 0; i < n - 1; i++) {
      for (int j = 0; j < n - i - 1; j++) {
        if (users.get(j).getId() > users.get(j + 1).getId()) {
          UserInterface temp = users.get(j);
          users.add(j, users.get(j + 1));
          users.add(j + 1, temp);
        }
      }
    }
  }

  private static void bubbleSortItems(ArrayList<Item> items) {
    int n = items.size();
    for (int i = 0; i < n - 1; i++) {
      for (int j = 0; j < n - i - 1; j++) {
        if (items.get(j).getId() > items.get(j + 1).getId()) {
          Item temp = items.get(j);
          items.add(j, items.get(j + 1));
          items.add(j + 1, temp);
        }
      }
    }
  }

  private static void bubbleSortArraySales(ArrayList<Sale> sales) {
    int n = sales.size();
    for (int i = 0; i < n - 1; i++) {
      for (int j = 0; j < n - i - 1; j++) {
        if (sales.get(j).getId() > sales.get(j + 1).getId()) {
          Sale temp = sales.get(j);
          sales.add(j, sales.get(j + 1));
          sales.add(j + 1, temp);
        }
      }
    }
  }

  public final void serialize()
      throws IOException, IdNotInDatabaseException {
    FileOutputStream file = null;
    file = context.openFileOutput("database_copy.ser", Context.MODE_PRIVATE);
    ObjectOutputStream out = null;
    out = new ObjectOutputStream(file);

    //Serializing roles
    Map<Integer, String> roles = new TreeMap<>();
    ArrayList<Integer> roleIds = new ArrayList<Integer>(selector.getRoleIds());
    for (Integer role : roleIds) {
      roles.put(role, selector.getRoleName(role));
    }
    out.writeObject(roles);

    //Serialize users
    ArrayList<UserInterface> users = new ArrayList<UserInterface>(selector.getUsersDetailsHelper());
    bubbleSortUsers(users);
    out.writeObject(users);

    //Serialize passwords
    Map<Integer, String> passwords = new TreeMap<>();
    for (UserInterface user : users) {
      passwords.put(user.getId(), selector.getPassword(user.getId()));
    }
    out.writeObject(passwords);

    //Serialize items
    ArrayList<Item> allItems = new ArrayList<Item>(selector.getAllItemsHelper());
    bubbleSortItems(allItems);
    out.writeObject(allItems);

    //Serialize sales
    SalesLog allSales = selector.getItemizedSalesHelper();
    ArrayList<Sale> allSalesList = (ArrayList<Sale>) allSales.getSales();
    bubbleSortArraySales(allSalesList);
    out.writeObject(allSalesList);

    //Serialize inventory
    Inventory inventory = selector.getInventoryHelper();
    out.writeObject(inventory);

    //Serialize accounts
    ArrayList<AccountSerializer> accounts = new ArrayList<>();
    for (UserInterface user : users) {
      if (user.getRoleId() == selector.getRoleId(Roles.CUSTOMER)) {
        ArrayList<Integer> activeAccounts = new ArrayList<Integer>(
            selector.getUserActiveAccountsHelper(user.getId()));
        ArrayList<Integer> deactiveAccounts = new ArrayList<Integer>(
            selector.getUserInactiveAccountsHelper(user.getId()));
        for (Integer id : activeAccounts) {
          accounts.add(
              new AccountSerializer(user.getId(), id, selector.getAccountDetailsHelper(id), true));
        }
        for (Integer id : deactiveAccounts) {
          accounts.add(
              new AccountSerializer(user.getId(), id, selector.getAccountDetailsHelper(id), false));
        }
      }
    }
    bubbleSortAccounts(accounts);
    out.writeObject(accounts);

    out.close();
    file.close();
    Toast.makeText(context, translator.translate("Database Serialized",
        LanguageSingleton.getInstance().getValue()), Toast.LENGTH_SHORT)
        .show();
  }
}
