package com.skishop.dao;

import com.skishop.dao.shipping.ShippingMethodDao;
import com.skishop.dao.shipping.ShippingMethodDaoImpl;
import com.skishop.domain.shipping.ShippingMethod;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Assert;

public class ShippingMethodDaoTest extends DaoTestBase {
  private ShippingMethodDao shippingMethodDao;

  protected void setUp() throws Exception {
    super.setUp();
    resetDatabase();
    shippingMethodDao = new ShippingMethodDaoImpl();
  }

  public void testListActiveAndFind() {
    List<ShippingMethod> methods = shippingMethodDao.listActive();
    Assert.assertFalse(methods.isEmpty());

    ShippingMethod method = shippingMethodDao.findByCode("STANDARD");
    Assert.assertNotNull(method);
  }

  public void testInsert() {
    ShippingMethod method = new ShippingMethod();
    method.setId("ship-exp");
    method.setCode("EXPRESS");
    method.setName("Express");
    method.setFee(new BigDecimal("1200"));
    method.setActive(true);
    method.setSortOrder(2);
    shippingMethodDao.insert(method);

    ShippingMethod loaded = shippingMethodDao.findByCode("EXPRESS");
    Assert.assertNotNull(loaded);
  }

  public void testListAllAndUpdate() {
    List<ShippingMethod> methods = shippingMethodDao.listAll();
    Assert.assertFalse(methods.isEmpty());

    ShippingMethod method = shippingMethodDao.findByCode("STANDARD");
    method.setName("Standard Updated");
    method.setFee(new BigDecimal("900"));
    shippingMethodDao.update(method);

    ShippingMethod updated = shippingMethodDao.findByCode("STANDARD");
    Assert.assertEquals("Standard Updated", updated.getName());
  }
}
