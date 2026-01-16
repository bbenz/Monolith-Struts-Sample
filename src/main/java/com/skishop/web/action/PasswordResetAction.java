package com.skishop.web.action;

import com.skishop.common.util.PasswordHasher;
import com.skishop.dao.user.PasswordResetTokenDao;
import com.skishop.dao.user.PasswordResetTokenDaoImpl;
import com.skishop.domain.user.PasswordResetToken;
import com.skishop.domain.user.User;
import com.skishop.service.user.UserService;
import com.skishop.web.form.PasswordResetForm;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class PasswordResetAction extends Action {
  private final PasswordResetTokenDao tokenDao = new PasswordResetTokenDaoImpl();
  private final UserService userService = new UserService();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    if (!"POST".equalsIgnoreCase(request.getMethod())) {
      return mapping.getInputForward();
    }
    PasswordResetForm resetForm = (PasswordResetForm) form;
    PasswordResetToken token = tokenDao.findByToken(resetForm.getToken());
    if (token == null || token.getUsedAt() != null || isExpired(token)) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.password.reset.invalid"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }
    User user = userService.findById(token.getUserId());
    if (user == null) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.password.reset.invalid"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }
    try {
      String salt = PasswordHasher.generateSalt();
      String hashed = PasswordHasher.hash(resetForm.getPassword(), salt);
      userService.updatePassword(user.getId(), hashed, salt);
      tokenDao.markUsed(token.getId());
      return mapping.findForward("success");
    } catch (NoSuchAlgorithmException e) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.password.reset.failed"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    } catch (UnsupportedEncodingException e) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.password.reset.failed"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }
  }

  private boolean isExpired(PasswordResetToken token) {
    if (token.getExpiresAt() == null) {
      return true;
    }
    return token.getExpiresAt().before(new Date());
  }
}
