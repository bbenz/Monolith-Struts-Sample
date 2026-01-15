package com.skishop.web.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import com.skishop.web.form.FormValidationUtils;

public class PasswordResetForm extends ValidatorForm {
  private String token;
  private String password;
  private String passwordConfirm;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPasswordConfirm() {
    return passwordConfirm;
  }

  public void setPasswordConfirm(String passwordConfirm) {
    this.passwordConfirm = passwordConfirm;
  }

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    if (!"POST".equalsIgnoreCase(request.getMethod())) {
      return new ActionErrors();
    }
    ActionErrors errors = super.validate(mapping, request);
    FormValidationUtils.addPasswordMismatch(errors, "passwordConfirm", password, passwordConfirm);
    return errors;
  }
}
