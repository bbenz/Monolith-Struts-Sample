package com.skishop.web.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import com.skishop.web.form.FormValidationUtils;

public class RegisterForm extends ValidatorForm {
  private String email;
  private String password;
  private String passwordConfirm;
  private String username;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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
