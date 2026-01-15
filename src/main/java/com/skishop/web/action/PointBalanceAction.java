package com.skishop.web.action;

import com.skishop.domain.point.PointAccount;
import com.skishop.domain.user.User;
import com.skishop.service.point.PointService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PointBalanceAction extends Action {
  private final PointService pointService = new PointService();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    HttpSession session = request.getSession(false);
    User user = session != null ? (User) session.getAttribute("loginUser") : null;
    if (user == null) {
      return mapping.findForward("login");
    }
    PointAccount account = pointService.getAccount(user.getId());
    request.setAttribute("pointBalance", account);
    return mapping.getInputForward();
  }
}
