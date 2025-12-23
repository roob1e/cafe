package com.assxmblxr.cafe.service.impl;

import com.assxmblxr.cafe.dao.OrderDao;
import com.assxmblxr.cafe.dao.UserDao;
import com.assxmblxr.cafe.entity.*;
import com.assxmblxr.cafe.entity.enums.PaymentMethod;
import com.assxmblxr.cafe.entity.enums.Role;
import com.assxmblxr.cafe.exception.CafeException;
import com.assxmblxr.cafe.service.UserService;
import org.mindrot.jbcrypt.BCrypt;

import java.math.BigDecimal;
import java.util.Optional;

public class UserServiceImpl implements UserService {
  private final UserDao userDao;
  private final OrderDao orderDao;

  public UserServiceImpl(UserDao userDao, OrderDao orderDao) {
    this.userDao = userDao;
    this.orderDao = orderDao;
  }

  @Override
  public Optional<User> login(String email, String password) {
    Optional<User> user = userDao.findByEmail(email);
    if (user.isPresent() && BCrypt.checkpw(password, user.get().getPassword())) {
      return user;
    }
    return Optional.empty();
  }

  @Override
  public boolean register(User user) {
    if (userDao.findByEmail(user.getEmail()).isPresent()) {
      return false;
    }

    String rawPassword = user.getPassword();
    String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    user.setPassword(hashedPassword);
    user.setLoyaltyPoints(BigDecimal.valueOf(5));
    userDao.create(user);
    return true;
  }

  @Override
  public void placeOrder(User user, Order order) {
    if (user.getRole() == Role.ADMIN) {
      throw new CafeException("Admins are not allowed to place orders");
    }

    if (order.getPaymentMethod() == PaymentMethod.ACCOUNT) {
      BigDecimal balance = user.getAccountBalance();
      if (balance.compareTo(order.getTotalPrice()) < 0) {
        throw new CafeException("Not enough money on balance");
      }
      user.setAccountBalance(balance.subtract(order.getTotalPrice()));
      userDao.update(user);
    }
    orderDao.create(order);
  }
}