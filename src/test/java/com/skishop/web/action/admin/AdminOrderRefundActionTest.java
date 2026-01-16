package com.skishop.web.action.admin;

import com.skishop.dao.order.OrderDao;
import com.skishop.dao.order.OrderDaoImpl;
import com.skishop.domain.order.Order;
import com.skishop.web.action.StrutsActionTestBase;

public class AdminOrderRefundActionTest extends StrutsActionTestBase {
  public void testRefundOrder() throws Exception {
    setLoginUser("admin-1", "ADMIN");
    OrderDao orderDao = new OrderDaoImpl();
    orderDao.updateStatus("order-1", "DELIVERED");
    orderDao.updatePaymentStatus("order-1", "CAPTURED");

    setRequestPathInfo("/admin/order/refund");
    setPostRequest();
    addRequestParameter("orderId", "order-1");
    actionPerform();
    verifyForward("success");

    Order order = orderDao.findById("order-1");
    assertNotNull(order);
    assertEquals("RETURNED", order.getStatus());
    assertEquals("REFUNDED", order.getPaymentStatus());
  }
}
