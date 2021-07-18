package com.b07.inventory;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public interface Item extends Serializable {

  int getId();

  void setId(int id);

  ItemTypes getName();

  void setName(ItemTypes name);

  BigDecimal getPrice();

  void setPrice(BigDecimal price);

}
