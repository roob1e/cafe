package com.assxmblxr.cafe.service;

import com.assxmblxr.cafe.entity.Order;
import com.assxmblxr.cafe.entity.User;

import java.util.Optional;

public interface UserService {
  Optional<User> login(String username, String password);
  boolean register(User user);
  void placeOrder(User user, Order order);
  void processLoyalty(Order order);
}
