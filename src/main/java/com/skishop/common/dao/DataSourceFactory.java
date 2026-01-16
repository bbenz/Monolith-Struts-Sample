package com.skishop.common.dao;

import com.skishop.common.config.AppConfig;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

public final class DataSourceFactory {
  private static final String DEFAULT_DRIVER = "org.postgresql.Driver";
  private static final int DEFAULT_MAX_ACTIVE = 20;
  private static final int DEFAULT_MAX_IDLE = 5;
  private static final int DEFAULT_MAX_WAIT = 10000;

  private DataSourceFactory() {
  }

  public static DataSource createFromConfig() {
    AppConfig config = AppConfig.getInstance();
    String url = config.getString("db.url");
    if (url == null) {
      throw new IllegalStateException("db.url is required");
    }
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setUrl(url);
    dataSource.setUsername(config.getString("db.username"));
    dataSource.setPassword(config.getString("db.password"));
    String driver = config.getString("db.driver");
    if (driver == null) {
      driver = DEFAULT_DRIVER;
    }
    dataSource.setDriverClassName(driver);
    dataSource.setMaxActive(config.getInt("db.pool.maxActive", DEFAULT_MAX_ACTIVE));
    dataSource.setMaxIdle(config.getInt("db.pool.maxIdle", DEFAULT_MAX_IDLE));
    dataSource.setMaxWait(config.getInt("db.pool.maxWait", DEFAULT_MAX_WAIT));
    return dataSource;
  }
}
