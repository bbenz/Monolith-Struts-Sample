package com.skishop.web.action.admin;

import com.skishop.domain.order.Order;
import com.skishop.service.order.OrderService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class AdminOrderUpdateAction extends Action {
  private final OrderService orderService = new OrderService();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String orderId = request.getParameter("orderId");
    String status = request.getParameter("status");
    String paymentStatus = request.getParameter("paymentStatus");
    if (orderId == null || orderId.length() == 0) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.order.notfound"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }
    Order order = orderService.findById(orderId);
    if (order == null) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.order.notfound"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }
    if (status != null && status.length() > 0) {
      orderService.updateStatus(orderId, status);
    }
    if (paymentStatus != null && paymentStatus.length() > 0) {
      orderService.updatePaymentStatus(orderId, paymentStatus);
    }
    return mapping.findForward("success");
  }
}
