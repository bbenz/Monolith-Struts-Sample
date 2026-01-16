package com.skishop.web.action;

import com.skishop.domain.product.Product;
import java.util.List;

public class ProductListActionTest extends StrutsActionTestBase {
  public void testProductListReturnsProducts() throws Exception {
    setRequestPathInfo("/products");
    setGetRequest();
    addRequestParameter("keyword", "Ski");
    actionPerform();
    verifyInputForward();
    List<Product> products = (List<Product>) getRequest().getAttribute("productList");
    assertNotNull(products);
    assertFalse(products.isEmpty());
    Product product = products.get(0);
    assertEquals("P001", product.getId());
  }
}
