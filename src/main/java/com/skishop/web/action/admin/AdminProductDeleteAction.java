package com.skishop.web.action.admin;

import com.skishop.domain.product.Product;
import com.skishop.service.catalog.ProductService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class AdminProductDeleteAction extends Action {
  private final ProductService productService = new ProductService();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String productId = request.getParameter("id");
    if (productId == null || productId.length() == 0) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.product.id"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }
    Product product = productService.findById(productId);
    if (product == null) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.product.notfound"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }
    productService.deactivateProduct(product.getId());
    return mapping.findForward("success");
  }
}
