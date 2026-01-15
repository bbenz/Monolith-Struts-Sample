package com.skishop.web.action;

import com.skishop.domain.product.Product;
import com.skishop.service.catalog.ProductService;
import com.skishop.web.form.ProductSearchForm;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ProductListAction extends Action {
  private static final int DEFAULT_SIZE = 10;
  private final ProductService productService = new ProductService();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    ProductSearchForm searchForm = (ProductSearchForm) form;
    int page = searchForm != null ? searchForm.getPage() : 1;
    int size = searchForm != null ? searchForm.getSize() : DEFAULT_SIZE;
    if (page <= 0) {
      page = 1;
    }
    if (size <= 0) {
      size = DEFAULT_SIZE;
    }
    String keyword = searchForm != null ? searchForm.getKeyword() : null;
    String categoryId = searchForm != null ? searchForm.getCategoryId() : null;
    int offset = (page - 1) * size;
    List<Product> products = productService.search(keyword, categoryId, offset, size);
    request.setAttribute("productList", products);
    request.setAttribute("page", Integer.valueOf(page));
    request.setAttribute("size", Integer.valueOf(size));
    return mapping.getInputForward();
  }
}
