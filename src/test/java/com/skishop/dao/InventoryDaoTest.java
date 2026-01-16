package com.skishop.dao;

import com.skishop.dao.inventory.InventoryDao;
import com.skishop.dao.inventory.InventoryDaoImpl;
import com.skishop.domain.inventory.Inventory;
import org.junit.Assert;

public class InventoryDaoTest extends DaoTestBase {
  private InventoryDao inventoryDao;

  protected void setUp() throws Exception {
    super.setUp();
    resetDatabase();
    inventoryDao = new InventoryDaoImpl();
  }

  public void testReserve() {
    boolean reserved = inventoryDao.reserve("P001", 2);
    Assert.assertTrue(reserved);
    Inventory inventory = inventoryDao.findByProductId("P001");
    Assert.assertEquals(2, inventory.getReservedQuantity());
  }
}
