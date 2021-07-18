package com.b07.users;

import android.content.Context;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.exceptions.DatabaseInsertException;

/**
 * This class instantiates a User object depending on the provided roleId
 *
 * @author vladislavtrukhin
 */
public final class UserFactory {

  private UserFactory() {

  }

  /**
   * Returns a user object with a specific role given by the role id
   *
   * @param userId Id of the user
   * @param userName Name of the user
   * @param userAge Age of the user
   * @param userAddress Address of the user
   * @return UserInterface of a newly instantiated User object
   * @throws DatabaseInsertException Throws an exception when the inputs ( userId, userName, age,
   * address) have an error
   */
  public static UserInterface createUser(int userId, String userName, int userAge,
      String userAddress, int roleId, Context context) throws DatabaseInsertException {
    DatabaseInsertHelper inserter = new DatabaseInsertHelper(context);
    if (inserter.insertRoleHelper(Roles.CUSTOMER.getRole()) == roleId) {
      return new Customer(userId, userName, userAge, userAddress, context);
    } else if (inserter.insertRoleHelper(Roles.EMPLOYEE.getRole()) == roleId) {
      return new Employee(userId, userName, userAge, userAddress, context);
    } else if (inserter.insertRoleHelper(Roles.ADMIN.getRole()) == roleId) {
      return new Admin(userId, userName, userAge, userAddress, context);
    } else {
      return null;
    }
  }

}
