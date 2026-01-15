package com.skishop.web.action.admin;

import com.skishop.domain.product.Product;
import com.skishop.service.catalog.ProductService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AdminProductListAction extends Action {
  private static final int MAX_ADMIN_PRODUCTS = 200;
  private final ProductService productService = new ProductService();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    List<Product> products = productService.search(null, null, 0, MAX_ADMIN_PRODUCTS);
    request.setAttribute("products", products);
    return mapping.getInputForward();
  }
}
