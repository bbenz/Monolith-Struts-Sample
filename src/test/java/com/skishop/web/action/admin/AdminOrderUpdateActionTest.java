package com.skishop.web.action.admin;

import com.skishop.dao.order.OrderDao;
import com.skishop.dao.order.OrderDaoImpl;
import com.skishop.domain.order.Order;
import com.skishop.web.action.StrutsActionTestBase;

public class AdminOrderUpdateActionTest extends StrutsActionTestBase {
  public void testUpdateOrderStatus() throws Exception {
    setLoginUser("admin-1", "ADMIN");
    setRequestPathInfo("/admin/order/update");
    setPostRequest();
    addRequestParameter("orderId", "order-1");
    addRequestParameter("status", "SHIPPED");
    addRequestParameter("paymentStatus", "CAPTURED");
    actionPerform();
    verifyForward("success");

    OrderDao orderDao = new OrderDaoImpl();
    Order order = orderDao.findById("order-1");
    assertNotNull(order);
    assertEquals("SHIPPED", order.getStatus());
    assertEquals("CAPTURED", order.getPaymentStatus());
  }
}
