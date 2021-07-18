package com.b07.serializer;

import com.b07.inventory.Item;
import java.io.Serializable;
import java.util.HashMap;

public class AccountSerializer implements Serializable {

  private static final long serialVersionUID = 1L;
  private int userId;
  private int accountId;
  private boolean active;
  private HashMap<Item, Integer> items;

  public AccountSerializer(int userId, int accountId, HashMap<Item, Integer> items,
      boolean active) {
    this.setUserId(userId);
    this.setAccountId(accountId);
    this.setActive(active);
    this.setItems(items);
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public int getAccountId() {
    return accountId;
  }

  public void setAccountId(int accountId) {
    this.accountId = accountId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public HashMap<Item, Integer> getItems() {
    return items;
  }

  public void setItems(HashMap<Item, Integer> items) {
    this.items = items;
  }
}
