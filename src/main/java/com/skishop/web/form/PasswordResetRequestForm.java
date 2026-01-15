package com.skishop.web.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class PasswordResetRequestForm extends ValidatorForm {
  private String email;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    if (!"POST".equalsIgnoreCase(request.getMethod())) {
      return new ActionErrors();
    }
    return super.validate(mapping, request);
  }
}
