package com.skishop.common.dao;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DataSourceLocator {
  private static DataSourceLocator instance;
  private final DataSource dataSource;

  public DataSourceLocator(DataSource dataSource) {
    this.dataSource = dataSource;
    DataSourceLocator.instance = this;
  }

  public static DataSourceLocator getInstance() {
    if (instance == null) {
      throw new IllegalStateException("DataSourceLocator not initialized by Spring");
    }
    return instance;
  }

  public DataSource getDataSource() {
    return dataSource;
  }
}
