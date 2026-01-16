package com.skishop.dao;

import com.skishop.dao.mail.EmailQueueDao;
import com.skishop.dao.mail.EmailQueueDaoImpl;
import com.skishop.domain.mail.EmailQueue;
import java.util.Date;
import java.util.List;
import org.junit.Assert;

public class EmailQueueDaoTest extends DaoTestBase {
  private EmailQueueDao emailQueueDao;

  protected void setUp() throws Exception {
    super.setUp();
    resetDatabase();
    emailQueueDao = new EmailQueueDaoImpl();
  }

  public void testFindByStatusAndUpdate() {
    List<EmailQueue> pending = emailQueueDao.findByStatus("PENDING");
    Assert.assertEquals(1, pending.size());

    emailQueueDao.updateStatus("mail-1", "SENT", 1, null, new Date(), new Date());
    List<EmailQueue> updated = emailQueueDao.findByStatus("SENT");
    Assert.assertEquals(1, updated.size());
  }

  public void testEnqueue() {
    EmailQueue mail = new EmailQueue();
    mail.setId("mail-2");
    mail.setToAddr("admin@example.com");
    mail.setSubject("Alert");
    mail.setBody("Body");
    mail.setStatus("PENDING");
    mail.setRetryCount(0);
    mail.setLastError(null);
    mail.setScheduledAt(new Date());
    mail.setSentAt(null);
    emailQueueDao.enqueue(mail);

    List<EmailQueue> pending = emailQueueDao.findByStatus("PENDING");
    Assert.assertTrue(pending.size() >= 2);
  }
}
