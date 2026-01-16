package com.skishop.web.form;

import org.apache.struts.validator.ValidatorForm;

public class CouponForm extends ValidatorForm {
  private String code;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
