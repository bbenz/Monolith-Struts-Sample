package com.skishop.dao;

import com.skishop.dao.user.PasswordResetTokenDao;
import com.skishop.dao.user.PasswordResetTokenDaoImpl;
import com.skishop.domain.user.PasswordResetToken;
import java.util.Date;
import org.junit.Assert;

public class PasswordResetTokenDaoTest extends DaoTestBase {
  private PasswordResetTokenDao tokenDao;

  protected void setUp() throws Exception {
    super.setUp();
    resetDatabase();
    tokenDao = new PasswordResetTokenDaoImpl();
  }

  public void testFindAndMarkUsed() {
    PasswordResetToken token = tokenDao.findByToken("token-1");
    Assert.assertNotNull(token);

    tokenDao.markUsed(token.getId());
    PasswordResetToken updated = tokenDao.findByToken("token-1");
    Assert.assertNotNull(updated.getUsedAt());
  }

  public void testInsert() {
    PasswordResetToken token = new PasswordResetToken();
    token.setId("prt-2");
    token.setUserId("u-1");
    token.setToken("token-2");
    token.setExpiresAt(new Date());
    tokenDao.insert(token);

    PasswordResetToken loaded = tokenDao.findByToken("token-2");
    Assert.assertNotNull(loaded);
  }
}
