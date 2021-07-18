package com.b07.serializer;

import android.content.Context;
import android.widget.Toast;
import com.b07.database.helper.DatabaseDeserializerInserter;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.exceptions.IdNotInDatabaseException;
import com.b07.exceptions.InvalidInputException;
import com.b07.inventory.Inventory;
import com.b07.inventory.Item;
import com.b07.store.Sale;
import com.b07.translate.LanguageSingleton;
import com.b07.translate.Translator;
import com.b07.users.UserInterface;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class DatabaseDeserializer {

  private Context context;
  private DatabaseSelectHelper selector;
  private Translator translator;

  public DatabaseDeserializer(Context context) {
    this.context = context;
    selector = new DatabaseSelectHelper(context);
    translator = new Translator(context);
  }

  public final void deserialize()
      throws IOException, ClassNotFoundException, InvalidInputException {

    FileInputStream file = context.openFileInput("database_copy.ser");
    ObjectInputStream in = new ObjectInputStream(file);

    //Deserialize roles
    Map<Integer, String> roles = (Map<Integer, String>) in.readObject();

    //Deserialize users
    ArrayList<UserInterface> usersDeserialized = (ArrayList<UserInterface>) in.readObject();

    //Deserialize passwords
    Map<Integer, String> passwords = (Map<Integer, String>) in.readObject();

    //Deserialize items
    ArrayList<Item> allItems = (ArrayList<Item>) in.readObject();

    //Deserialize sales
    ArrayList<Sale> allSales = (ArrayList<Sale>) in.readObject();

    //Deserialize inventory
    Inventory inventory = (Inventory) in.readObject();

    //Deserialize accounts
    ArrayList<AccountSerializer> accounts = (ArrayList<AccountSerializer>) in.readObject();

    DatabaseSerializer serializeBackup = new DatabaseSerializer(context);

    context.deleteFile("database_copy.ser");
    try {
      serializeBackup.serialize();
    } catch (IdNotInDatabaseException e) {
      context.deleteFile("database_copy.ser");
      Toast.makeText(context, translator.translate("Failed to create backup",
          LanguageSingleton.getInstance().getValue()), Toast.LENGTH_SHORT)
          .show();
      return;
    }
    Toast.makeText(context, translator.translate("Database backed up",
        LanguageSingleton.getInstance().getValue()), Toast.LENGTH_SHORT)
        .show();

    context.deleteDatabase("inventorymgmt.db");

    DatabaseInsertHelper mydb = new DatabaseInsertHelper(context);
    DatabaseUpdateHelper mydb2 = new DatabaseUpdateHelper(context);

    //Insert roles
    for (Map.Entry<Integer, String> entry : roles.entrySet()) {
      ((DatabaseInsertHelper) mydb).insertRoleHelper(entry.getValue());
    }
    //Insert users
    DatabaseDeserializerInserter inserter = new DatabaseDeserializerInserter(context);
    for (UserInterface user : usersDeserialized) {
      int userId = (int) inserter.insertPreviousUser(user.getName(),
          user.getAge(), user.getAddress(), passwords.get(user.getId()));
      ((DatabaseInsertHelper) mydb).insertUserRoleHelper(userId, user.getRoleId());
    }
    //Insert items
    for (Item item : allItems) {
      ((DatabaseInsertHelper) mydb).insertItemHelper(item.getName().toString(), item.getPrice());
    }
    //Insert sales
    for (Sale sale : allSales) {
      ((DatabaseInsertHelper) mydb).insertSaleHelper(sale.getUser().getId(), sale.getTotalPrice());
      HashMap<Item, Integer> itemMap = sale.getItemMap();
      for (Map.Entry<Item, Integer> entry : itemMap.entrySet()) {
        ((DatabaseInsertHelper) mydb).insertItemizedSaleHelper(sale.getId(),
            entry.getKey().getId(), entry.getValue());
      }
    }
    //Insert Inventory
    for (Map.Entry<Item, Integer> entry : inventory.getItemMap().entrySet()) {
      ((DatabaseInsertHelper) mydb).insertInventoryHelper(entry.getKey().getId(), entry.getValue());
    }

    //Insert Account
    for (AccountSerializer account : accounts) {
      ((DatabaseInsertHelper) mydb).insertAccountHelper(account.getUserId());
      ((DatabaseUpdateHelper) mydb2)
          .updateAccountStatusHelper(account.getAccountId(), account.isActive());
      for (Map.Entry<Item, Integer> entry : account.getItems().entrySet()) {
        ((DatabaseInsertHelper) mydb)
            .insertAccountLineHelper(account.getAccountId(), entry.getKey().getId(),
                entry.getValue());
      }
    }
    in.close();
    file.close();
    context.deleteFile("database_copy.ser");
    Toast.makeText(context, translator.translate("Database Deserialized",
        LanguageSingleton.getInstance().getValue()), Toast.LENGTH_SHORT)
        .show();
  }
}
