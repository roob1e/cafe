package com.assxmblxr.cafe.entity;

import java.math.BigDecimal;

/**
 * Сущность, представляющая позицию в меню кафе.
 * Содержит информацию о названии блюда, его описании, цене и доступности.
 *
 * <p>Для гибкого создания объектов используйте статический метод {@link #builder()}.</p>
 *
 * @author assxmblxr
 * @version 1.0
 */
public class MenuItem {

  /** Уникальный идентификатор позиции меню в базе данных. */
  private Long menuItemId;

  /** Название блюда или напитка. */
  private String name;

  /** Подробное описание ингредиентов или способа приготовления. */
  private String description;

  /** Стоимость позиции. */
  private BigDecimal price;

  /** Флаг, указывающий, доступно ли блюдо для заказа в данный момент. */
  private boolean isAvailable;

  /**
   * Конструктор без параметров. Требуется для работы механизмов рефлексии.
   */
  public MenuItem() {}

  /**
   * Конструктор для создания новой позиции меню.
   *
   * @param name        название блюда.
   * @param description описание блюда.
   * @param price       цена.
   * @param isAvailable статус доступности.
   */
  public MenuItem(String name, String description, BigDecimal price, boolean isAvailable) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.isAvailable = isAvailable;
  }

  /**
   * Приватный конструктор для создания объекта через строитель.
   *
   * @param builder экземпляр {@link Builder}, содержащий данные.
   */
  private MenuItem(Builder builder) {
    this.menuItemId = builder.menuItemId;
    this.name = builder.name;
    this.description = builder.description;
    this.price = builder.price;
    this.isAvailable = builder.isAvailable;
  }

  /** @return уникальный ID позиции меню. */
  public Long getMenuItemId() { return menuItemId; }

  /** @param menuItemId уникальный ID позиции меню. */
  public void setMenuItemId(Long menuItemId) { this.menuItemId = menuItemId; }

  /** @return название блюда. */
  public String getName() { return name; }

  /** @param name название блюда. */
  public void setName(String name) { this.name = name; }

  /** @return описание блюда. */
  public String getDescription() { return description; }

  /** @param description описание блюда. */
  public void setDescription(String description) { this.description = description; }

  /** @return цена блюда. */
  public BigDecimal getPrice() { return price; }

  /** @param price цена блюда. */
  public void setPrice(BigDecimal price) { this.price = price; }

  /** @return true, если блюдо доступно для заказа. */
  public boolean isAvailable() { return isAvailable; }

  /** @param available статус доступности. */
  public void setAvailable(boolean available) { isAvailable = available; }

  /**
   * Создает новый экземпляр строителя.
   * @return {@link Builder}
   */
  public static Builder builder() { return new Builder(); }

  /**
   * Класс-строитель (Builder) для создания объектов {@link MenuItem}.
   */
  public static class Builder {
    private Long menuItemId;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean isAvailable;

    /** @param menuItemId ID позиции меню. @return объект {@link Builder}. */
    public Builder id(Long menuItemId) { this.menuItemId = menuItemId; return this; }

    /** @param name название блюда. @return объект {@link Builder}. */
    public Builder name(String name) { this.name = name; return this; }

    /** @param description описание блюда. @return объект {@link Builder}. */
    public Builder description(String description) { this.description = description; return this; }

    /** @param price цена. @return объект {@link Builder}. */
    public Builder price(BigDecimal price) { this.price = price; return this; }

    /** @param isAvailable статус доступности. @return объект {@link Builder}. */
    public Builder isAvailable(boolean isAvailable) { this.isAvailable = isAvailable; return this; }

    /**
     * Собирает и возвращает объект {@link MenuItem}.
     * @return новый экземпляр {@link MenuItem}.
     */
    public MenuItem build() { return new MenuItem(this); }
  }
}