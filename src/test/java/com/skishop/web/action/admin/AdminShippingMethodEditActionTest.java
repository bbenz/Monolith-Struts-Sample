package com.skishop.web.action.admin;

import com.skishop.dao.shipping.ShippingMethodDao;
import com.skishop.dao.shipping.ShippingMethodDaoImpl;
import com.skishop.domain.shipping.ShippingMethod;
import com.skishop.web.action.StrutsActionTestBase;
import java.math.BigDecimal;

public class AdminShippingMethodEditActionTest extends StrutsActionTestBase {
  public void testCreateShippingMethod() throws Exception {
    setLoginUser("admin-1", "ADMIN");
    setRequestPathInfo("/admin/shipping/edit");
    setPostRequest();
    addRequestParameter("id", "ship-new");
    addRequestParameter("code", "EXPRESS");
    addRequestParameter("name", "Express");
    addRequestParameter("fee", "1200");
    addRequestParameter("active", "on");
    addRequestParameter("sortOrder", "2");
    actionPerform();
    verifyForward("success");

    ShippingMethodDao shippingMethodDao = new ShippingMethodDaoImpl();
    ShippingMethod method = shippingMethodDao.findByCode("EXPRESS");
    assertNotNull(method);
    assertEquals("Express", method.getName());
    assertEquals(0, method.getFee().compareTo(new BigDecimal("1200")));
  }
}
