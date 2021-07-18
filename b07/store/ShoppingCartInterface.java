package com.b07.store;

import com.b07.exceptions.DatabaseInsertException;
import com.b07.exceptions.IdNotInDatabaseException;
import com.b07.exceptions.InvalidInputException;
import com.b07.exceptions.OutOfStockException;
import com.b07.inventory.Item;
import com.b07.users.CustomerInterf;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface ShoppingCartInterface {

  /**
   * Returns true if the customer that have logged in has an account
   *
   * @return true if the customer has an account, else false
   */
  boolean hasAccount();

  /**
   * Returns the quantity of a specific item that is in the cart
   *
   * @param itemId Item where the quantity stored in the cart wished to be checked
   * @return the quantity that is stored in the cart
   */
  int getQuantityInCart(int itemId);

  /**
   * Add an item into the cart with a specified quantity
   *
   * @param item Item to be added into the shopping cart
   * @param quantity Quantity to be added to the shopping cart
   * @throws InvalidInputException Throws an exception if the item given does not exists or is null
   */
  void addItem(Item item, int quantity) throws InvalidInputException;

  /**
   * Removes an item from the cart by a specific quantity, the item will be removed from the
   * shopping cart if the final quantity is 0 or less
   *
   * @param item Item to be removed from the shopping cart
   * @param quantity Quantity to be removed from the shopping cart
   * @throws InvalidInputException Throws an exception if the item given does not exists
   */
  void removeItem(Item item, int quantity) throws InvalidInputException;

  /**
   * @return Returns a list of items in the shopping cart
   */
  List<Item> getItems();

  CustomerInterf getCustomer();

  BigDecimal getTotal();

  BigDecimal getTaxRate();

  /**
   * If there is enough stock in the inventory for the customer to make the purchase, it calculates
   * the price of all the items saved in the shopping cart and calculates the price after tax, and
   * updates the inventory by removing the bought items, and adding a sale record, then clears the
   * shopping cart. Returns true if the check out was successful, else false. If checkout was
   * successful, the used account will be deactivated.
   *
   * @return true if the check out was successful, else false
   * @throws InvalidInputException Throws an exception if there is an invalid quantity passed into
   * the helper functions
   * @throws IdNotInDatabaseException Throws an exception if the item id or sale id needed do not
   * exist in the database
   * @throws OutOfStockException Throws an exception if there is not enough stock in the inventory
   * for the customer to make the purchase
   */
  boolean checkOut()
      throws InvalidInputException, IdNotInDatabaseException, OutOfStockException;

  /**
   * Saves the item in the current shopping cart to the customer's account details for restoration
   * next time they log back in
   *
   * @throws DatabaseInsertException Throws an exception if there is an error inserting into the
   * database
   * @throws InvalidInputException Throws an exception if there is an error with item quantity
   */
  boolean saveCart() throws DatabaseInsertException, InvalidInputException;

  void restoreCart(int accountId);

  void clearCart();

  /**
   * Checks if the user has a previous shopping cart record from the last time they logged in and
   * used the shopping cart
   *
   * @return true if user has previous cart, false otherwise.
   */
  HashMap<Integer, HashMap<Item, Integer>> getPrevCart();

}
