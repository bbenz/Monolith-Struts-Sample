package com.skishop.web.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class LoginForm extends ValidatorForm {
  private String email;
  private String password;

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

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    if (!"POST".equalsIgnoreCase(request.getMethod())) {
      return new ActionErrors();
    }
    return super.validate(mapping, request);
  }
}
