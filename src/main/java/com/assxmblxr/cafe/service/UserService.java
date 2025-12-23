package com.assxmblxr.cafe.service;

import com.assxmblxr.cafe.entity.Order;
import com.assxmblxr.cafe.entity.User;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserService {
  Optional<User> login(String email, String password);
  boolean register(User user);

  void placeOrder(User user, Order order);

  void blockUser(long userId);
  void unblockUser(long userId);
  void changeLoyaltyPoints(long userId, BigDecimal newPoints);
  void processOrderFinalization(long orderId, boolean isSuccess);
  void grantAdminRole(long userId);

  List<User> findAllUsers();
  Optional<User> findById(long id);
}