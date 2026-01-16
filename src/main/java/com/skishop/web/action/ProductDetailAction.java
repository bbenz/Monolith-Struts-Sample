package com.skishop.web.action;

import com.skishop.domain.product.Product;
import com.skishop.service.catalog.ProductService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ProductDetailAction extends Action {
  private final ProductService productService = new ProductService();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String productId = request.getParameter("id");
    if (productId == null || productId.length() == 0) {
      return mapping.findForward("notfound");
    }
    Product product = productService.findById(productId);
    if (product == null) {
      return mapping.findForward("notfound");
    }
    request.setAttribute("product", product);
    return mapping.findForward("success");
  }
}
