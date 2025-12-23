package com.assxmblxr.cafe.dao;

import com.assxmblxr.cafe.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserDao {
  void create(User entity);
  Optional<User> findById(long id);
  Optional<User> findByEmail(String email);
  List<User> findAll();
  void update(User entity);
  void delete(User entity);
  void updateBlockedStatus(long userId, boolean blocked);
  void updateLoyaltyPoints(long userId, BigDecimal points);
}
