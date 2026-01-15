package com.skishop.dao;

import com.skishop.dao.point.PointAccountDao;
import com.skishop.dao.point.PointAccountDaoImpl;
import com.skishop.domain.point.PointAccount;
import org.junit.Assert;

public class PointAccountDaoTest extends DaoTestBase {
  private PointAccountDao pointAccountDao;

  protected void setUp() throws Exception {
    super.setUp();
    resetDatabase();
    pointAccountDao = new PointAccountDaoImpl();
  }

  public void testFindAndIncrement() {
    PointAccount account = pointAccountDao.findByUserId("u-1");
    Assert.assertNotNull(account);
    Assert.assertEquals(100, account.getBalance());

    pointAccountDao.increment("u-1", 50);
    PointAccount updated = pointAccountDao.findByUserId("u-1");
    Assert.assertEquals(150, updated.getBalance());
  }
}
