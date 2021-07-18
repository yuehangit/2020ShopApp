package com.b07.users;

public enum Roles {
  ADMIN("ADMIN"),
  EMPLOYEE("EMPLOYEE"),
  CUSTOMER("CUSTOMER");

  private String role;

  Roles(String role) {
    this.role = role;
  }

  public static boolean contains(String role) {
    try {
      Roles.valueOf(role);
    } catch (IllegalArgumentException e1) {
      return false;
    } catch (NullPointerException e2) {
      return false;
    }
    return true;
  }

  public String getRole() {
    return this.role;
  }
}