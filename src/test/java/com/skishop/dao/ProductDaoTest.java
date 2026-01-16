package com.skishop.dao;

import com.skishop.dao.product.ProductDao;
import com.skishop.dao.product.ProductDaoImpl;
import com.skishop.domain.product.Product;
import java.util.Date;
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

  public void testInsert() {
    Product product = new Product();
    product.setId("P999");
    product.setName("Ski Pro");
    product.setBrand("BrandZ");
    product.setDescription("Pro ski");
    product.setCategoryId("c-1");
    product.setSku("SKU-999");
    product.setStatus("ACTIVE");
    product.setCreatedAt(new Date());
    product.setUpdatedAt(new Date());
    productDao.insert(product);

    Product loaded = productDao.findById("P999");
    Assert.assertNotNull(loaded);
    Assert.assertEquals("Ski Pro", loaded.getName());
  }
}
