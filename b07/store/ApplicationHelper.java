package com.b07.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.InvalidInputException;
import com.b07.inventory.ItemTypes;
import com.b07.translate.LanguageSingleton;
import com.b07.translate.Translator;
import com.b07.users.Roles;
import com.b07.users.UserInterface;
import java.math.BigDecimal;

/**
 * This class is intended to support SalesApplication with methods to run the different
 * functionalities selected by the user.
 *
 * @author vladislavtrukhin
 */
public final class ApplicationHelper extends AppCompatActivity {

  Intent intent;

  private ApplicationHelper() {

  }

  /**
   * Insert items into the database according to the enumerators with prices that increments by 10
   * each time. Used for testing purposes.
   */
  public static void addItems(Context context) {
    BigDecimal price = new BigDecimal(10);
    for (ItemTypes item : ItemTypes.values()) {
      try {
        DatabaseInsertHelper inserter = new DatabaseInsertHelper(context);
        inserter.insertItemHelper(item.getItemType(), price);
      } catch (InvalidInputException e) {
        Translator translator = new Translator(context);
        Toast.makeText(context,
            translator.translate("Failed to add items", LanguageSingleton.getInstance().getValue()),
            Toast.LENGTH_SHORT)
            .show();
      }
      price = price.add(price);
    }
  }

  /**
   * Handles the login interfaces for users (Admin, Employee or Customer)
   *
   * @param role Role of the user that will be using the interface
   * @return Returns the user object that have been authenticated and successfully logged in
   */
  public static UserInterface logIn(Roles role, int id, String password, Context context) {
    UserInterface user = null;
    DatabaseSelectHelper select = new DatabaseSelectHelper(context);
    if (id != -1) {
      user = select.getUserDetailsHelper(id);
    }
    if (user == null) {
      return null;
    }
    if (!select.getRoleName(user.getRoleId()).equals(role.getRole())) {
      return null;
    }
    if (user.authenticate(password, context)) {
      return user;
    }
    return null;
  }

  @Override
  protected void onCreate(Bundle savedInstantState) {
    super.onCreate(savedInstantState);
    intent = getIntent();

  }

}