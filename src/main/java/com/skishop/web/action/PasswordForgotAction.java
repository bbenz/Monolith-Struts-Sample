package com.skishop.web.action;

import com.skishop.dao.user.PasswordResetTokenDao;
import com.skishop.dao.user.PasswordResetTokenDaoImpl;
import com.skishop.domain.user.PasswordResetToken;
import com.skishop.domain.user.User;
import com.skishop.service.mail.MailService;
import com.skishop.service.user.UserService;
import com.skishop.web.form.PasswordResetRequestForm;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PasswordForgotAction extends Action {
  private final UserService userService = new UserService();
  private final PasswordResetTokenDao tokenDao = new PasswordResetTokenDaoImpl();
  private final MailService mailService = new MailService();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    if (!"POST".equalsIgnoreCase(request.getMethod())) {
      return mapping.getInputForward();
    }
    PasswordResetRequestForm resetForm = (PasswordResetRequestForm) form;
    User user = userService.findByEmail(resetForm.getEmail());
    if (user != null) {
      PasswordResetToken token = new PasswordResetToken();
      token.setId(UUID.randomUUID().toString());
      token.setUserId(user.getId());
      token.setToken(UUID.randomUUID().toString());
      token.setExpiresAt(addHours(new Date(), 1));
      token.setUsedAt(null);
      tokenDao.insert(token);
      request.setAttribute("resetToken", token.getToken());
      mailService.enqueuePasswordReset(user.getEmail(), token.getToken());
    }
    return mapping.findForward("success");
  }

  private Date addHours(Date base, int hours) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(base);
    cal.add(Calendar.HOUR_OF_DAY, hours);
    return cal.getTime();
  }
}
