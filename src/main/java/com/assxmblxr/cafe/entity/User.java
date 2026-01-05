package com.assxmblxr.cafe.entity;

import com.assxmblxr.cafe.type.Role;

import java.math.BigDecimal;

/**
 * Сущность, представляющая пользователя системы кафе.
 * Содержит учетные данные, финансовое состояние и статус в программе лояльности.
 *
 * <p>Для гибкого создания объектов используйте статический метод {@link #builder()}.</p>
 *
 * @author assxmblxr
 * @version 1.0
 */
public class User {
  /** Уникальный идентификатор пользователя в базе данных. */
  private Long id;

  /** Отображаемое имя или логин пользователя. */
  private String name;

  /** Адрес электронной почты, используемый для входа и уведомлений. */
  private String email;

  /** Хешированный пароль пользователя. */
  private String password;

  /** Текущий денежный баланс пользователя для оплаты заказов. */
  private BigDecimal accountBalance;

  /** Количество накопленных баллов в системе лояльности кафе. */
  private BigDecimal loyaltyPoints;

  /** Флаг, указывающий на ограничение доступа пользователя к системе. */
  private boolean blocked;

  /** Права доступа пользователя (например, {@link Role#CLIENT} или {@link Role#ADMIN}). */
  private Role role;

  /**
   * Конструктор без параметров. Требуется для работы JPA и механизмов рефлексии.
   */
  public User() {}

  /**
   * Конструктор для первичной регистрации нового пользователя.
   * Автоматически устанавливает баланс в ноль и роль клиента.
   *
   * @param name     имя пользователя.
   * @param email    электронная почта.
   * @param password пароль (рекомендуется передавать уже захешированным).
   */
  public User(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.accountBalance = BigDecimal.ZERO;
    this.loyaltyPoints = BigDecimal.valueOf(5);
    this.blocked = false;
    this.role = Role.CLIENT;
  }

  /**
   * Приватный конструктор для создания объекта через строитель.
   *
   * @param builder экземпляр {@link Builder}, содержащий данные.
   */
  private User(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.email = builder.email;
    this.password = builder.password;
    this.accountBalance = builder.accountBalance;
    this.loyaltyPoints = builder.loyaltyPoints;
    this.blocked = builder.blocked;
    this.role = builder.role;
  }

  /** @return уникальный ID пользователя. */
  public Long getId() { return id; }

  /** @param id уникальный ID пользователя. */
  public void setId(Long id) { this.id = id; }

  /** @return имя пользователя. */
  public String getName() { return name; }

  /** @param name имя пользователя. */
  public void setName(String name) { this.name = name; }

  /** @return электронная почта. */
  public String getEmail() { return email; }

  /** @param email электронная почта. */
  public void setEmail(String email) { this.email = email; }

  /** @return хешированный пароль. */
  public String getPassword() { return password; }

  /** @param password хешированный пароль. */
  public void setPassword(String password) { this.password = password; }

  /** @return текущий баланс счета. */
  public BigDecimal getAccountBalance() { return accountBalance; }

  /** @param accountBalance сумма для установки на баланс. */
  public void setAccountBalance(BigDecimal accountBalance) { this.accountBalance = accountBalance; }

  /** @return количество баллов лояльности. */
  public BigDecimal getLoyaltyPoints() { return loyaltyPoints; }

  /** @param loyaltyPoints количество баллов для установки. */
  public void setLoyaltyPoints(BigDecimal loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }

  /** @return роль пользователя. */
  public Role getRole() { return role; }

  /** @param role роль пользователя. */
  public void setRole(Role role) { this.role = role; }

  /** @return true, если пользователь заблокирован. */
  public boolean isBlocked() { return blocked; }

  /** @param blocked статус блокировки. */
  public void setBlocked(boolean blocked) { this.blocked = blocked; }

  /**
   * Создает новый экземпляр строителя.
   * @return {@link Builder}
   */
  public static Builder builder() {
    return new Builder();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("User{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", email='").append(email).append('\'');
    sb.append(", password='").append(password).append('\'');
    sb.append(", accountBalance=").append(accountBalance);
    sb.append(", loyaltyPoints=").append(loyaltyPoints);
    sb.append(", blocked=").append(blocked);
    sb.append(", role=").append(role);
    sb.append('}');
    return sb.toString();
  }

  /**
   * Класс-строитель (Builder) для создания объектов {@link User}.
   */
  public static class Builder {
    private Long id;
    private String name;
    private String email;
    private String password;
    private BigDecimal accountBalance;
    private BigDecimal loyaltyPoints;
    private boolean blocked;
    private Role role;

    /**
     *  @param id ID пользователя.
     *  @return объект {@link Builder}.
     */
    public Builder id(Long id) { this.id = id; return this; }

    /**
     *  @param name имя пользователя.
     *  @return объект {@link Builder}.
     */
    public Builder name(String name) { this.name = name; return this; }

    /**
     *  @param email почта пользователя.
     *  @return объект {@link Builder}.
     */
    public Builder email(String email) { this.email = email; return this; }

    /**
     *  @param password хэш пароля.
     *  @return объект {@link Builder}.
     */
    public Builder password(String password) { this.password = password; return this; }

    /**
     * @param accountBalance баланс
     * @return объект {@link Builder}.
     */
    public Builder accountBalance(BigDecimal accountBalance) { this.accountBalance = accountBalance; return this; }

    /**
     * @param loyaltyPoints баллы лояльности.
     * @return объект {@link Builder}. */
    public Builder loyaltyPoints(BigDecimal loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; return this; }

    /**
     * @param blocked флаг блокировки.
     * @return объект {@link Builder}.
     */
    public Builder blocked(boolean blocked) { this.blocked = blocked; return this; }

    /**
     * @param role роль.
     * @return объект {@link Builder}.
     */
    public Builder role(Role role) { this.role = role; return this; }

    /**
     * Собирает и возвращает объект {@link User}.
     * @return новый экземпляр {@link User}.
     */
    public User build() {
      return new User(this);
    }
  }
}