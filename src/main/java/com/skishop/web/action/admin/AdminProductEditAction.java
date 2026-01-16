package com.skishop.web.action.admin;

import com.skishop.dao.inventory.InventoryDao;
import com.skishop.dao.inventory.InventoryDaoImpl;
import com.skishop.dao.product.PriceDao;
import com.skishop.dao.product.PriceDaoImpl;
import com.skishop.dao.product.ProductDao;
import com.skishop.dao.product.ProductDaoImpl;
import com.skishop.domain.inventory.Inventory;
import com.skishop.domain.product.Price;
import com.skishop.domain.product.Product;
import com.skishop.service.catalog.ProductService;
import com.skishop.web.form.admin.AdminProductForm;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class AdminProductEditAction extends Action {
  private static final int PRODUCT_ID_RANDOM_LENGTH = 12;
  private final ProductService productService = new ProductService();
  private final ProductDao productDao = new ProductDaoImpl();
  private final PriceDao priceDao = new PriceDaoImpl();
  private final InventoryDao inventoryDao = new InventoryDaoImpl();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    AdminProductForm productForm = (AdminProductForm) form;
    if (!"POST".equalsIgnoreCase(request.getMethod())) {
      String productId = request.getParameter("id");
      if (productId != null && productId.length() > 0) {
        Product product = productService.findById(productId);
        if (product != null) {
          productForm.setId(product.getId());
          productForm.setName(product.getName());
          productForm.setBrand(product.getBrand());
          productForm.setDescription(product.getDescription());
          productForm.setCategoryId(product.getCategoryId());
          productForm.setStatus(product.getStatus());
          Price price = priceDao.findByProductId(productId);
          if (price != null && price.getRegularPrice() != null) {
            productForm.setPrice(price.getRegularPrice().toString());
          }
          Inventory inventory = inventoryDao.findByProductId(productId);
          if (inventory != null) {
            productForm.setInventoryQty(inventory.getQuantity());
          }
        }
      } else {
        productForm.setId(generateProductId());
        productForm.setStatus("ACTIVE");
        productForm.setPrice("0");
        productForm.setInventoryQty(0);
      }
      return mapping.getInputForward();
    }

    if (productForm.getId() == null || productForm.getId().length() == 0) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.product.id"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }

    BigDecimal priceValue;
    try {
      priceValue = new BigDecimal(productForm.getPrice());
    } catch (NumberFormatException e) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.admin.product.price"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }

    Product existing = productService.findById(productForm.getId());

    Product product = new Product();
    product.setId(productForm.getId());
    product.setName(productForm.getName());
    product.setBrand(productForm.getBrand());
    product.setDescription(productForm.getDescription());
    product.setCategoryId(productForm.getCategoryId());
    product.setStatus(productForm.getStatus());
    if (existing == null) {
      product.setSku("SKU-" + productForm.getId());
      Date now = new Date();
      product.setCreatedAt(now);
      product.setUpdatedAt(now);
      productDao.insert(product);
    } else {
      product.setSku(existing.getSku());
      productDao.update(product);
    }

    Price price = new Price();
    price.setId(UUID.randomUUID().toString());
    price.setProductId(productForm.getId());
    price.setRegularPrice(priceValue);
    price.setSalePrice(null);
    price.setCurrencyCode("JPY");
    price.setSaleStartDate(null);
    price.setSaleEndDate(null);
    priceDao.saveOrUpdate(price);

    Inventory inventory = inventoryDao.findByProductId(productForm.getId());
    int quantity = productForm.getInventoryQty();
    String status = quantity > 0 ? "AVAILABLE" : "OUT_OF_STOCK";
    if (inventory == null) {
      inventory = new Inventory();
      inventory.setId(UUID.randomUUID().toString());
      inventory.setProductId(productForm.getId());
      inventory.setQuantity(quantity);
      inventory.setReservedQuantity(0);
      inventory.setStatus(status);
      inventoryDao.insert(inventory);
    } else {
      inventoryDao.updateQuantity(productForm.getId(), quantity, status);
    }

    request.setAttribute("updatedAt", new Date());
    return mapping.findForward("success");
  }

  private String generateProductId() {
    String raw = UUID.randomUUID().toString().replace("-", "");
    return "P" + raw.substring(0, PRODUCT_ID_RANDOM_LENGTH);
  }
}
