package com.assxmblxr.cafe.entity;

import com.assxmblxr.cafe.type.OrderStatus;
import com.assxmblxr.cafe.type.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
  private Long orderId;
  private Long userId;
  private BigDecimal totalPrice;
  private LocalDateTime pickupTime;
  private PaymentMethod paymentMethod;
  private OrderStatus status;
  private LocalDateTime createdAt;
  private List<OrderItem> items;

  public Order() {}

  public Order(Long userId, List<OrderItem> items, LocalDateTime pickupTime, PaymentMethod paymentMethod) {
    this.userId = userId;
    this.items = items;
    this.pickupTime = pickupTime;
    this.paymentMethod = paymentMethod;
    this.status = OrderStatus.NEW;
    this.createdAt = LocalDateTime.now();
    this.totalPrice = items.stream()
            .map(item -> item.getMenuItem().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private Order(Builder builder) {
    this.orderId = builder.orderId;
    this.userId = builder.userId;
    this.totalPrice = builder.totalPrice;
    this.pickupTime = builder.pickupTime;
    this.paymentMethod = builder.paymentMethod;
    this.status = builder.status;
    this.createdAt = builder.createdAt;
    this.items = builder.items;
  }

  public Long getOrderId() { return orderId; }
  public void setOrderId(Long orderId) { this.orderId = orderId; }

  public Long getUserId() { return userId; }
  public void setUserId(Long userId) { this.userId = userId; }

  public BigDecimal getTotalPrice() { return totalPrice; }
  public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

  public LocalDateTime getPickupTime() { return pickupTime; }
  public void setPickupTime(LocalDateTime pickupTime) { this.pickupTime = pickupTime; }

  public OrderStatus getStatus() { return status; }
  public void setStatus(OrderStatus status) { this.status = status; }

  public PaymentMethod getPaymentMethod() { return paymentMethod; }
  public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

  public List<OrderItem> getItems() { return items; }
  public void setItems(List<OrderItem> items) { this.items = items; }

  public static Builder builder() { return new Builder(); }

  public static class Builder {
    private Long orderId;
    private Long userId;
    private BigDecimal totalPrice;
    private LocalDateTime pickupTime;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private LocalDateTime createdAt;
    private List<OrderItem> items;

    public Builder orderId(Long orderId) { this.orderId = orderId; return this; }
    public Builder userId(Long userId) { this.userId = userId; return this; }
    public Builder totalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; return this; }
    public Builder pickupTime(LocalDateTime pickupTime) { this.pickupTime = pickupTime; return this; }
    public Builder status(OrderStatus status) { this.status = status; return this; }
    public Builder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
    public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
    public Builder items(List<OrderItem> items) { this.items = items; return this; }

    public Order build() { return new Order(this); }
  }
}