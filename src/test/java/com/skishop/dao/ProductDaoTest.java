package com.skishop.dao;

import com.skishop.dao.product.ProductDao;
import com.skishop.dao.product.ProductDaoImpl;
import com.skishop.domain.product.Product;
import java.util.List;
import org.junit.Assert;

public class ProductDaoTest extends DaoTestBase {
  private ProductDao productDao;

  protected void setUp() throws Exception {
    super.setUp();
    resetDatabase();
    productDao = new ProductDaoImpl();
  }

  public void testFindById() {
    Product product = productDao.findById("P001");
    Assert.assertNotNull(product);
    Assert.assertEquals("Ski A", product.getName());
  }

  public void testFindPaged() {
    List<Product> products = productDao.findPaged("Ski", "c-1", 0, 10);
    Assert.assertFalse(products.isEmpty());
  }
}
