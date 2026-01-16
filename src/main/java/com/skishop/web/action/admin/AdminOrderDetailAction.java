package com.skishop.web.action.admin;

import com.skishop.domain.order.Order;
import com.skishop.domain.order.OrderItem;
import com.skishop.service.order.OrderService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AdminOrderDetailAction extends Action {
  private final OrderService orderService = new OrderService();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String orderId = request.getParameter("orderId");
    if (orderId != null && orderId.trim().length() > 0) {
      Order order = orderService.findById(orderId);
      if (order != null) {
        List<OrderItem> items = orderService.listItems(orderId);
        request.setAttribute("order", order);
        request.setAttribute("orderItems", items);
      }
    }
    return mapping.findForward("success");
  }
}
