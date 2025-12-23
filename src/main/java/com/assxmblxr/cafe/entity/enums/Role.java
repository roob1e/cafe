package com.assxmblxr.cafe.entity.enums;

public enum Role {
  CLIENT,
  ADMIN;

  public static Role fromString(String string) {
    for (Role role : Role.values()) {
      if (role.name().equalsIgnoreCase(string)) {
        return role;
      }
    }
    return CLIENT;
  }
}
