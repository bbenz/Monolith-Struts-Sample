package com.skishop.common.dao;

import com.skishop.dao.address.UserAddressDao;
import com.skishop.dao.address.UserAddressDaoImpl;

public final class DaoFactory {
  private DaoFactory() {
  }

  public static UserAddressDao getUserAddressDao() {
    return new UserAddressDaoImpl();
  }
}
