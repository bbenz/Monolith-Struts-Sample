package com.skishop.web.form;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

public final class FormValidationUtils {
  private FormValidationUtils() {
  }

  public static void addPasswordMismatch(ActionErrors errors, String property, String password, String passwordConfirm) {
    if (password != null && passwordConfirm != null && password.length() > 0 && passwordConfirm.length() > 0) {
      if (!password.equals(passwordConfirm)) {
        errors.add(property, new ActionMessage("error.password.mismatch"));
      }
    }
  }
}
