package com.b07.inventory;

public enum ItemTypes {
  FISHING_ROD("FISHING_ROD"), HOCKEY_STICK("HOCKEY_STICK"), SKATES("SKATES"),
  RUNNING_SHOES("RUNNING_SHOES"), PROTEIN_BAR("PROTEIN_BAR");

  private String itemType;

  ItemTypes(String itemType) {
    this.itemType = itemType;
  }

  /**
   * Returns the correct item type given a string name of the item
   *
   * @param name Name of the item
   * @return item Returns the item type that matches the given name
   */
  public static ItemTypes getEnum(String name) {
    for (ItemTypes item : ItemTypes.values()) {
      if (item.getItemType().equals(name)) {
        return item;
      }
    }
    return null;
  }

  /**
   * Checks if the given itemType exists, returns true if it exists
   *
   * @param itemType The item to be checked if it is valid and exists
   * @return Returns true if the item exists, else false
   */
  public static boolean contains(String itemType) {
    try {
      ItemTypes.valueOf(itemType);
    } catch (IllegalArgumentException e1) {
      return false;
    } catch (NullPointerException e2) {
      return false;
    }
    return true;
  }

  public String getItemType() {
    return this.itemType;
  }
}
