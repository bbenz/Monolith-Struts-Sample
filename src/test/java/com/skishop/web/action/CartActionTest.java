package com.skishop.web.action;

import com.skishop.domain.cart.CartItem;
import java.util.List;

public class CartActionTest extends StrutsActionTestBase {
  public void testCartAddItemCreatesCart() throws Exception {
    setLoginUser("u-1", "USER");
    setRequestPathInfo("/cart");
    setPostRequest();
    addRequestParameter("productId", "P001");
    addRequestParameter("quantity", "2");
    actionPerform();
    verifyForward("success");
    List<CartItem> items = (List<CartItem>) getRequest().getAttribute("cartItems");
    assertNotNull(items);
    assertEquals(1, items.size());
    CartItem item = items.get(0);
    assertEquals("P001", item.getProductId());
    assertEquals(2, item.getQuantity());
    assertNotNull(getRequest().getAttribute("cartId"));
  }
}
