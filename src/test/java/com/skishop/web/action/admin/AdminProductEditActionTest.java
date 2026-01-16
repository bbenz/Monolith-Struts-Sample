package com.skishop.web.action.admin;

import com.skishop.dao.product.ProductDao;
import com.skishop.dao.product.ProductDaoImpl;
import com.skishop.domain.product.Product;
import com.skishop.web.action.StrutsActionTestBase;

public class AdminProductEditActionTest extends StrutsActionTestBase {
  public void testCreateProduct() throws Exception {
    setLoginUser("admin-1", "ADMIN");
    setRequestPathInfo("/admin/product/edit");
    setPostRequest();
    addRequestParameter("id", "P100");
    addRequestParameter("name", "Admin Ski");
    addRequestParameter("brand", "AdminBrand");
    addRequestParameter("description", "New product");
    addRequestParameter("categoryId", "c-1");
    addRequestParameter("price", "12345");
    addRequestParameter("status", "ACTIVE");
    addRequestParameter("inventoryQty", "5");
    actionPerform();
    verifyForward("success");

    ProductDao productDao = new ProductDaoImpl();
    Product product = productDao.findById("P100");
    assertNotNull(product);
    assertEquals("Admin Ski", product.getName());
  }
}
