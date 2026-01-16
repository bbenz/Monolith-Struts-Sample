package com.skishop.web.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class AddCartForm extends ValidatorForm {
  private String productId;
  private int quantity = 1;

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    if (!"POST".equalsIgnoreCase(request.getMethod())) {
      return new ActionErrors();
    }
    return super.validate(mapping, request);
  }
}
