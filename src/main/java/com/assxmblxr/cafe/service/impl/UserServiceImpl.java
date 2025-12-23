package com.assxmblxr.cafe.service.impl;

import com.assxmblxr.cafe.dao.OrderDao;
import com.assxmblxr.cafe.dao.UserDao;
import com.assxmblxr.cafe.entity.*;
import com.assxmblxr.cafe.entity.enums.OrderStatus;
import com.assxmblxr.cafe.entity.enums.PaymentMethod;
import com.assxmblxr.cafe.entity.enums.Role;
import com.assxmblxr.cafe.exception.CafeException;
import com.assxmblxr.cafe.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UserServiceImpl implements UserService {
  private final UserDao userDao;
  private final OrderDao orderDao;

  public UserServiceImpl(UserDao userDao, OrderDao orderDao) {
    this.userDao = userDao;
    this.orderDao = orderDao;
  }

  @Override
  public Optional<User> login(String email, String password) {
    User user = userDao.findByEmail(email)
            .orElseThrow(() -> new CafeException("User not found"));

    boolean passwordMatches = BCrypt.checkpw(password, user.getPassword());
    if (!passwordMatches) {
      return Optional.empty();
    }
    return Optional.of(user);
  }

  @Override
  public boolean register(User user) {
    if (userDao.findByEmail(user.getEmail()).isPresent()) return false;

    user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
    user.setLoyaltyPoints(new BigDecimal("5.00"));
    user.setRole(Role.CLIENT);
    userDao.create(user);
    return true;
  }

  @Override
  public void placeOrder(User user, Order order) {
    if (user.isBlocked()) throw new CafeException("User is blocked");
    if (user.getRole() == Role.ADMIN) throw new CafeException("Admins are not allowed to place orders");

    if (order.getPaymentMethod() == PaymentMethod.ACCOUNT) {
      if (user.getAccountBalance().compareTo(order.getTotalPrice()) < 0) {
        throw new CafeException("Insufficient funds");
      }
      user.setAccountBalance(user.getAccountBalance().subtract(order.getTotalPrice()));
    }
    log.info("User {} placed order {}", user.getEmail(), order.getOrderId());
    orderDao.create(order);
    userDao.update(user);
  }

  @Override
  public void blockUser(long userId) {
    userDao.updateBlockedStatus(userId, true);
    log.info("User blocked ID: {}", userId);
  }

  @Override
  public void unblockUser(long userId) {
    userDao.updateBlockedStatus(userId, false);
    log.info("User unblocked ID: {}", userId);
  }

  @Override
  public void changeLoyaltyPoints(long userId, BigDecimal newPoints) {
    if (newPoints.compareTo(BigDecimal.ZERO) < 0) {
      userDao.updateBlockedStatus(userId, true);
    }
    userDao.updateLoyaltyPoints(userId, newPoints);
    log.info("Points changed for user {} to {}", userId, newPoints);
  }

  @Override
  public void processOrderFinalization(long orderId, boolean isSuccess) {
    Order order = orderDao.findById(orderId)
            .orElseThrow(() -> new CafeException("Order not found"));

    if (isSuccess) {
      order.setStatus(OrderStatus.COMPLETED);
    } else {
      order.setStatus(OrderStatus.CANCELLED);
    }
    orderDao.update(order);
  }

  @Override
  public void grantAdminRole(long userId) {
    User user = userDao.findById(userId)
            .orElseThrow(() -> new CafeException("User not found"));

    if (user.getRole() == Role.ADMIN) {
      throw new CafeException("User is already admin");
    }
    user.setRole(Role.ADMIN);
    userDao.update(user);
  }

  @Override
  public List<User> findAllUsers() {
    return userDao.findAll();
  }

  @Override
  public Optional<User> findById(long id) {
    return userDao.findById(id);
  }
}