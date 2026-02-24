package com.skishop.web.controller.admin;

import com.skishop.dao.inventory.InventoryDao;
import com.skishop.dao.product.PriceDao;
import com.skishop.dao.product.ProductDao;
import com.skishop.domain.inventory.Inventory;
import com.skishop.domain.product.Price;
import com.skishop.domain.product.Product;
import com.skishop.service.catalog.ProductService;
import com.skishop.web.dto.admin.AdminProductRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("/admin/product/edit")
public class AdminProductEditController {
    private static final int PRODUCT_ID_RANDOM_LENGTH = 12;
    private final ProductService productService;
    private final ProductDao productDao;
    private final PriceDao priceDao;
    private final InventoryDao inventoryDao;

    public AdminProductEditController(ProductService productService,
                                     ProductDao productDao,
                                     PriceDao priceDao,
                                     InventoryDao inventoryDao) {
        this.productService = productService;
        this.productDao = productDao;
        this.priceDao = priceDao;
        this.inventoryDao = inventoryDao;
    }

    @GetMapping
    public String showEditForm(@RequestParam(required = false) String id, Model model) {
        AdminProductRequest productRequest = new AdminProductRequest();
        
        if (id != null && !id.isEmpty()) {
            Product product = productService.findById(id);
            if (product != null) {
                productRequest.setId(product.getId());
                productRequest.setName(product.getName());
                productRequest.setBrand(product.getBrand());
                productRequest.setDescription(product.getDescription());
                productRequest.setCategoryId(product.getCategoryId());
                productRequest.setStatus(product.getStatus());
                
                Price price = priceDao.findByProductId(id);
                if (price != null && price.getRegularPrice() != null) {
                    productRequest.setPrice(price.getRegularPrice().toString());
                }
                
                Inventory inventory = inventoryDao.findByProductId(id);
                if (inventory != null) {
                    productRequest.setInventoryQty(inventory.getQuantity());
                }
            }
        } else {
            productRequest.setId(generateProductId());
            productRequest.setStatus("ACTIVE");
            productRequest.setPrice("0");
            productRequest.setInventoryQty(0);
        }
        
        model.addAttribute("adminProductRequest", productRequest);
        return "admin/product-edit";
    }

    @PostMapping
    public String saveProduct(@Valid @ModelAttribute AdminProductRequest productRequest,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            return "admin/product-edit";
        }

        if (productRequest.getId() == null || productRequest.getId().isEmpty()) {
            model.addAttribute("error", "Product ID is required");
            return "admin/product-edit-failure";
        }

        BigDecimal priceValue;
        try {
            priceValue = new BigDecimal(productRequest.getPrice());
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Invalid price format");
            return "admin/product-edit-failure";
        }

        Product existing = productService.findById(productRequest.getId());

        Product product = new Product();
        product.setId(productRequest.getId());
        product.setName(productRequest.getName());
        product.setBrand(productRequest.getBrand());
        product.setDescription(productRequest.getDescription());
        product.setCategoryId(productRequest.getCategoryId());
        product.setStatus(productRequest.getStatus());
        
        if (existing == null) {
            product.setSku("SKU-" + productRequest.getId());
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
        price.setProductId(productRequest.getId());
        price.setRegularPrice(priceValue);
        price.setSalePrice(null);
        price.setCurrencyCode("JPY");
        price.setSaleStartDate(null);
        price.setSaleEndDate(null);
        priceDao.saveOrUpdate(price);

        Inventory inventory = inventoryDao.findByProductId(productRequest.getId());
        int quantity = productRequest.getInventoryQty();
        String status = quantity > 0 ? "AVAILABLE" : "OUT_OF_STOCK";
        if (inventory == null) {
            inventory = new Inventory();
            inventory.setId(UUID.randomUUID().toString());
            inventory.setProductId(productRequest.getId());
            inventory.setQuantity(quantity);
            inventory.setReservedQuantity(0);
            inventory.setStatus(status);
            inventoryDao.insert(inventory);
        } else {
            inventoryDao.updateQuantity(productRequest.getId(), quantity, status);
        }

        model.addAttribute("updatedAt", new Date());
        return "redirect:/admin/products";
    }

    private String generateProductId() {
        String raw = UUID.randomUUID().toString().replace("-", "");
        return "P" + raw.substring(0, PRODUCT_ID_RANDOM_LENGTH);
    }
}
