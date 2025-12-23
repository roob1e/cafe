package com.assxmblxr.cafe.entity;

import java.math.BigDecimal;

public class OrderItem {
  private MenuItem menuItem;
  private int quantity;

  public OrderItem(MenuItem menuItem, int quantity) {
    this.menuItem = menuItem;
    this.quantity = quantity;
  }

  public BigDecimal getSubTotal() {
    return menuItem.getPrice().multiply(new BigDecimal(quantity));
  }

  public MenuItem getMenuItem() {
    return menuItem;
  }

  public void setMenuItem(MenuItem menuItem) {
    this.menuItem = menuItem;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
