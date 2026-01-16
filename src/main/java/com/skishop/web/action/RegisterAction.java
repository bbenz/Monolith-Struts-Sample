package com.skishop.web.action;

import com.skishop.common.util.PasswordHasher;
import com.skishop.domain.user.User;
import com.skishop.service.user.UserService;
import com.skishop.web.form.RegisterForm;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class RegisterAction extends Action {
  private final UserService userService = new UserService();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    if (!"POST".equalsIgnoreCase(request.getMethod())) {
      return mapping.getInputForward();
    }
    RegisterForm registerForm = (RegisterForm) form;
    if (userService.findByEmail(registerForm.getEmail()) != null) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.register.duplicate"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }
    try {
      User user = new User();
      user.setEmail(registerForm.getEmail());
      user.setUsername(registerForm.getUsername());
      String salt = PasswordHasher.generateSalt();
      String hashed = PasswordHasher.hash(registerForm.getPassword(), salt);
      user.setSalt(salt);
      user.setPasswordHash(hashed);
      userService.register(user);
      return mapping.findForward("success");
    } catch (NoSuchAlgorithmException e) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.register.failed"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    } catch (UnsupportedEncodingException e) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.register.failed"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }
  }
}
