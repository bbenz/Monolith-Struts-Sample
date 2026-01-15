package com.skishop.web.action;

import com.skishop.domain.user.User;
import com.skishop.service.order.OrderFacade;
import com.skishop.service.order.OrderFacadeImpl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class OrderCancelAction extends Action {
  private final OrderFacade orderFacade = new OrderFacadeImpl();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String orderId = request.getParameter("orderId");
    HttpSession session = request.getSession(false);
    User user = session != null ? (User) session.getAttribute("loginUser") : null;
    if (orderId == null || orderId.length() == 0) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.order.notfound"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }
    try {
      orderFacade.cancelOrder(orderId, user != null ? user.getId() : null);
      return mapping.findForward("success");
    } catch (RuntimeException e) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.order.cancel.failed"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }
  }
}
