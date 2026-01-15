package com.skishop.web.action.admin;

import com.skishop.domain.order.Order;
import com.skishop.service.order.OrderService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AdminOrderListAction extends Action {
  private static final int MAX_ADMIN_ORDERS = 200;
  private final OrderService orderService = new OrderService();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    List<Order> orders = orderService.listAll(MAX_ADMIN_ORDERS);
    request.setAttribute("orders", orders);
    return mapping.getInputForward();
  }
}
