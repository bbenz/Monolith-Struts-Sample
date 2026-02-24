# Product Image Generation and Integration Plan

**Project**: Duke's Ski Chalet  
**Target Application**: `appmod-migrated-spring-boot-2nd-challenge`  
**Date**: February 23, 2026  
**Status**: Planning Phase  

---

## Executive Summary

This document outlines the plan to generate, store, and integrate product images for all 30 products in Duke's Ski Chalet. Each image will feature:
- AI-generated or programmatically created ski/snowboard equipment imagery
- Product name overlaid as styled graphic text
- Consistent branding matching Duke's Ski Chalet color scheme (navy #1a1a2e, red #c0392b, white)

---

## Current State Analysis

### Database Schema
- **Table**: `products` (30 products total)
- **Current fields**: `id`, `name`, `brand`, `description`, `category_id`, `sku`, `status`, `created_at`, `updated_at`
- **Missing**: No image storage field currently exists

### Product Categories
Based on `categories` table and sample data:
- Snowboards (Board A, Board B, etc.)
- Boots
- Bindings
- Goggles
- Helmets
- Gloves
- Jackets
- Pants
- Accessories

### Current UI Implementation
- **Templates**: `templates/products/list.html`, `templates/products/detail.html`, `templates/index.html`
- **Display**: Text-only product cards with name, brand, status tags
- **No image placeholder** currently displayed

---

## Goals and Requirements

### Functional Requirements
1. Generate 30 unique product images (one per product)
2. Each image must display the product name as readable graphic text
3. Images should reflect the product category (snowboard shape for boards, boot shape for boots, etc.)
4. Store image paths/URLs in the database
5. Integrate images into all product display templates
6. Support responsive display (mobile, tablet, desktop)

### Non-Functional Requirements
1. **Performance**: Images optimized for web (< 200KB per image)
2. **Quality**: Minimum 800x600px, PNG or WebP format
3. **Consistency**: Uniform style across all products
4. **Accessibility**: Proper alt text for screen readers
5. **Fallback**: Graceful handling if image missing (placeholder)

### Branding Requirements
- Background colors: Navy (#1a1a2e), white (#ffffff), or gradient
- Accent colors: Red (#c0392b), lighter red (#e74c3c)
- Typography: Bold, readable fonts matching website style
- Duke mascot integration: Optional small logo/watermark

---

## Technical Architecture

### Option A: Azure OpenAI DALL-E Image Generation (Recommended)

**Approach**: Use Azure OpenAI DALL-E 3 API to generate high-quality product images with text overlay.

#### Advantages
- High-quality, realistic product imagery
- Natural-looking text integration
- AI can generate category-appropriate equipment shapes
- Leverages existing Azure OpenAI integration
- Professional results with minimal manual effort

#### Implementation Components
1. **Service Layer**: `ProductImageGenerationService.java`
   - Interface with Azure OpenAI DALL-E 3 API
   - Construct prompts for each product category
   - Handle image generation requests
   - Download and store generated images

2. **Batch Generation Script**: `GenerateProductImages.java`
   - Command-line utility or REST endpoint
   - Iterate through all products
   - Generate images with prompts like:
     ```
     "A premium {category} product photo for '{product_name}' by {brand}, 
     studio lighting, white background, product photography style, 
     with bold red and navy text overlay showing '{product_name}', 
     ski resort equipment catalog style"
     ```

3. **Storage Options**:
   - **Option A1**: Azure Blob Storage (recommended for production)
     - Store images in blob container
     - CDN integration for fast delivery
     - Save blob URL in database
   
   - **Option A2**: Local filesystem (simpler for development)
     - Store in `src/main/resources/static/images/products/`
     - Save relative path in database (`/images/products/{id}.png`)

#### Estimated Costs
- DALL-E 3: ~$0.04-0.08 per image
- 30 products × $0.06 average = ~$1.80 one-time cost
- Azure Blob Storage: ~$0.02/GB/month (negligible for 30 images)

---

### Option B: Programmatic Image Generation (Java-based)

**Approach**: Use Java libraries to generate images programmatically with text overlay.

#### Advantages
- No external API costs
- Complete control over styling
- Reproducible and deterministic
- Can regenerate instantly
- No internet connection required

#### Implementation Components
1. **Service Layer**: `ProductImageGeneratorService.java`
   - Use Java BufferedImage and Graphics2D
   - Create template backgrounds (gradients, colors)
   - Render product name with custom fonts
   - Add category icons/shapes

2. **Library Dependencies** (add to pom.xml):
   ```xml
   <dependency>
       <groupId>com.github.jai-imageio</groupId>
       <artifactId>jai-imageio-core</artifactId>
       <version>1.4.0</version>
   </dependency>
   ```

3. **Template Assets**:
   - Category icon SVGs or PNGs (snowboard, boot, goggle icons)
   - Custom fonts (Apache licensed)
   - Background templates

#### Example Code Structure
```java
public BufferedImage generateProductImage(Product product) {
    BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = image.createGraphics();
    
    // Background
    g2d.setColor(new Color(0x1a1a2e)); // Navy
    g2d.fillRect(0, 0, 800, 600);
    
    // Product name text
    g2d.setColor(Color.WHITE);
    g2d.setFont(new Font("Arial", Font.BOLD, 48));
    
    // Add category icon (if available)
    // drawCategoryIcon(g2d, product.getCategoryId());
    
    // Center and draw product name
    drawCenteredString(g2d, product.getName(), 800, 300);
    
    // Brand name (smaller)
    g2d.setFont(new Font("Arial", Font.PLAIN, 24));
    drawCenteredString(g2d, product.getBrand(), 800, 360);
    
    g2d.dispose();
    return image;
}
```

---

### Option C: Hybrid Approach (Recommended for MVP)

**Approach**: Use programmatic generation for initial implementation, with option to upgrade to DALL-E later.

#### Phase 1: Programmatic Generation
- Quick implementation
- Zero ongoing costs
- Validates database schema changes
- Tests UI integration

#### Phase 2: AI Enhancement (Optional)
- Generate 3-5 sample products with DALL-E
- Compare quality vs programmatic
- Decide on full migration based on results

---

## Database Schema Changes

### Add Image Field to Products Table

#### Migration SQL (`03-add-product-images.sql`)
```sql
-- Add image_url column to products table
ALTER TABLE products 
ADD COLUMN image_url VARCHAR(500);

-- Add image_alt_text for accessibility
ALTER TABLE products 
ADD COLUMN image_alt_text VARCHAR(255);

-- Create index for faster queries
CREATE INDEX idx_products_image ON products(image_url);

-- Optional: Add image metadata
ALTER TABLE products 
ADD COLUMN image_generated_at TIMESTAMP;

COMMENT ON COLUMN products.image_url IS 'Relative path or full URL to product image';
COMMENT ON COLUMN products.image_alt_text IS 'Accessibility description for screen readers';
```

#### Update Data Script
```sql
-- Populate alt text for existing products (will be generated by script)
UPDATE products 
SET image_alt_text = CONCAT(name, ' by ', brand, ' - ', description)
WHERE image_alt_text IS NULL;
```

---

## Entity and Repository Updates

### Update Product Entity

**File**: `src/main/java/com/skishop/model/entity/Product.java`

```java
@Entity
@Table(name = "products")
public class Product {
    // ... existing fields ...
    
    @Size(max = 500)
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    
    @Size(max = 255)
    @Column(name = "image_alt_text", length = 255)
    private String imageAltText;
    
    @Column(name = "image_generated_at")
    private LocalDateTime imageGeneratedAt;
    
    // Getters and setters
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getImageAltText() {
        return imageAltText;
    }
    
    public void setImageAltText(String imageAltText) {
        this.imageAltText = imageAltText;
    }
    
    public LocalDateTime getImageGeneratedAt() {
        return imageGeneratedAt;
    }
    
    public void setImageGeneratedAt(LocalDateTime imageGeneratedAt) {
        this.imageGeneratedAt = imageGeneratedAt;
    }
    
    // Utility method
    public String getImageUrlOrDefault() {
        return (imageUrl != null && !imageUrl.isEmpty()) 
            ? imageUrl 
            : "/images/product-placeholder.png";
    }
}
```

### Update DTOs

**File**: `src/main/java/com/skishop/dto/ProductResponse.java`

Add image fields to all ProductResponse DTOs:
```java
public class ProductResponse {
    private String id;
    private String name;
    private String brand;
    private String description;
    private String imageUrl;
    private String imageAltText;
    // ... other fields ...
}
```

---

## Service Layer Implementation

### Product Image Generation Service

**File**: `src/main/java/com/skishop/service/ProductImageGenerationService.java`

```java
package com.skishop.service;

import com.skishop.model.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class ProductImageGenerationService {
    
    @Value("${product.images.directory:src/main/resources/static/images/products}")
    private String imageDirectory;
    
    private static final int IMAGE_WIDTH = 800;
    private static final int IMAGE_HEIGHT = 600;
    private static final Color NAVY = new Color(0x1a1a2e);
    private static final Color RED_ACCENT = new Color(0xc0392b);
    private static final Color WHITE = Color.WHITE;
    
    /**
     * Generate image for a single product
     */
    public String generateProductImage(Product product) throws IOException {
        BufferedImage image = createProductImage(product);
        String filename = String.format("%s.png", product.getId());
        String filepath = imageDirectory + File.separator + filename;
        
        // Ensure directory exists
        new File(imageDirectory).mkdirs();
        
        // Save image
        ImageIO.write(image, "PNG", new File(filepath));
        
        return "/images/products/" + filename;
    }
    
    /**
     * Create the actual image using Graphics2D
     */
    private BufferedImage createProductImage(Product product) {
        BufferedImage image = new BufferedImage(
            IMAGE_WIDTH, 
            IMAGE_HEIGHT, 
            BufferedImage.TYPE_INT_RGB
        );
        Graphics2D g2d = image.createGraphics();
        
        // Enable antialiasing for smooth text
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2d.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );
        
        // Background gradient (navy to lighter navy)
        GradientPaint gradient = new GradientPaint(
            0, 0, NAVY,
            0, IMAGE_HEIGHT, new Color(0x2a2a3e)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        
        // Red accent bar at top
        g2d.setColor(RED_ACCENT);
        g2d.fillRect(0, 0, IMAGE_WIDTH, 10);
        
        // Product name (large, white, bold)
        g2d.setColor(WHITE);
        Font productFont = new Font("Arial", Font.BOLD, 64);
        g2d.setFont(productFont);
        
        String productName = product.getName();
        drawCenteredText(g2d, productName, IMAGE_HEIGHT / 2 - 40);
        
        // Brand name (smaller, red accent)
        g2d.setColor(RED_ACCENT);
        Font brandFont = new Font("Arial", Font.PLAIN, 32);
        g2d.setFont(brandFont);
        drawCenteredText(g2d, product.getBrand(), IMAGE_HEIGHT / 2 + 30);
        
        // Category badge (optional, bottom right)
        // drawCategoryBadge(g2d, product.getCategoryId());
        
        g2d.dispose();
        return image;
    }
    
    /**
     * Draw text centered horizontally
     */
    private void drawCenteredText(Graphics2D g2d, String text, int y) {
        FontMetrics metrics = g2d.getFontMetrics();
        int x = (IMAGE_WIDTH - metrics.stringWidth(text)) / 2;
        g2d.drawString(text, x, y);
    }
    
    /**
     * Generate alt text for accessibility
     */
    public String generateAltText(Product product) {
        return String.format("%s by %s - %s", 
            product.getName(), 
            product.getBrand(),
            product.getDescription()
        );
    }
}
```

### Batch Generation Command

**File**: `src/main/java/com/skishop/command/GenerateAllProductImagesCommand.java`

```java
package com.skishop.command;

import com.skishop.model.entity.Product;
import com.skishop.repository.ProductRepository;
import com.skishop.service.ProductImageGenerationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

@SpringBootApplication
public class GenerateAllProductImagesCommand {
    
    private static final Logger logger = LoggerFactory.getLogger(
        GenerateAllProductImagesCommand.class
    );
    
    public static void main(String[] args) {
        SpringApplication.run(GenerateAllProductImagesCommand.class, args);
    }
    
    @Bean
    public CommandLineRunner generateImages(
        ProductRepository productRepository,
        ProductImageGenerationService imageService
    ) {
        return args -> {
            logger.info("Starting product image generation...");
            
            var products = productRepository.findAll();
            int successCount = 0;
            int errorCount = 0;
            
            for (Product product : products) {
                try {
                    String imageUrl = imageService.generateProductImage(product);
                    String altText = imageService.generateAltText(product);
                    
                    product.setImageUrl(imageUrl);
                    product.setImageAltText(altText);
                    product.setImageGeneratedAt(LocalDateTime.now());
                    
                    productRepository.save(product);
                    
                    logger.info("Generated image for product: {} -> {}", 
                        product.getName(), imageUrl);
                    successCount++;
                    
                } catch (Exception e) {
                    logger.error("Failed to generate image for product: {}", 
                        product.getName(), e);
                    errorCount++;
                }
            }
            
            logger.info("Image generation complete. Success: {}, Errors: {}", 
                successCount, errorCount);
        };
    }
}
```

---

## UI Integration

### Update Thymeleaf Templates

#### Product List Template

**File**: `src/main/resources/templates/products/list.html`

```html
<div class="product-card" th:each="p : ${products}">
    <!-- Add product image -->
    <div class="product-image">
        <img th:src="@{${p.imageUrlOrDefault}}" 
             th:alt="${p.imageAltText}"
             loading="lazy" />
    </div>
    
    <div class="product-info">
        <div class="name">
            <a th:href="@{|/ui/products/${p.id}|}" 
               th:text="${p.name}">Product Name</a>
        </div>
        <div class="brand" th:text="${p.brand}">Brand</div>
        <div class="tags">
            <span class="tag" th:text="${p.status}">ACTIVE</span>
        </div>
        <div>
            <a th:href="@{|/ui/products/${p.id}|}" 
               class="btn btn-secondary">View Details</a>
        </div>
    </div>
</div>
```

#### Product Detail Template

**File**: `src/main/resources/templates/products/detail.html`

```html
<div class="product-detail">
    <div class="product-image-large">
        <img th:src="@{${product.imageUrlOrDefault}}" 
             th:alt="${product.imageAltText}"
             class="product-hero-image" />
    </div>
    
    <div class="product-details">
        <h1 th:text="${product.name}">Product Name</h1>
        <p class="brand" th:text="${product.brand}">Brand</p>
        <p class="description" th:text="${product.description}">Description</p>
        <!-- ... rest of details ... -->
    </div>
</div>
```

#### Home Page (Index) Template

**File**: `src/main/resources/templates/index.html`

```html
<div class="products-grid" th:if="${products}">
    <div class="product-card" th:each="p : ${products}">
        <!-- Add product image -->
        <div class="product-image-thumb">
            <a th:href="@{|/ui/products/${p.id}|}">
                <img th:src="@{${p.imageUrlOrDefault}}" 
                     th:alt="${p.imageAltText}"
                     loading="lazy" />
            </a>
        </div>
        
        <div class="name">
            <a th:href="@{|/ui/products/${p.id}|}" 
               th:text="${p.name}">name</a>
        </div>
        <!-- ... rest of card ... -->
    </div>
</div>
```

---

## CSS Styling

### Add Product Image Styles

**File**: `src/main/resources/static/css/main.css`

```css
/* Product image containers */
.product-image {
    width: 100%;
    height: 200px;
    overflow: hidden;
    background: #f8f9fa;
    border-radius: 8px 8px 0 0;
    display: flex;
    align-items: center;
    justify-content: center;
}

.product-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s ease;
}

.product-card:hover .product-image img {
    transform: scale(1.05);
}

/* Product detail page large image */
.product-image-large {
    width: 100%;
    max-width: 600px;
    margin: 0 auto 2rem;
}

.product-hero-image {
    width: 100%;
    height: auto;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

/* Thumbnail for home page */
.product-image-thumb {
    width: 100%;
    height: 180px;
    overflow: hidden;
    border-radius: 8px;
    margin-bottom: 1rem;
}

.product-image-thumb img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

/* Responsive adjustments */
@media (max-width: 768px) {
    .product-image {
        height: 150px;
    }
    
    .product-image-thumb {
        height: 140px;
    }
}

/* Loading state */
.product-image img[loading="lazy"] {
    background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
    background-size: 200% 100%;
    animation: loading 1.5s infinite;
}

@keyframes loading {
    0% { background-position: 200% 0; }
    100% { background-position: -200% 0; }
}
```

---

## Placeholder Image

Create a default placeholder image for products without generated images.

**File**: `src/main/resources/static/images/product-placeholder.png`

- Simple navy background with Duke mascot silhouette
- "Image Coming Soon" text
- 800x600px PNG
- Can be created programmatically or use existing Duke ski image with overlay

---

## Testing Strategy

### Unit Tests

**File**: `src/test/java/com/skishop/service/ProductImageGenerationServiceTest.java`

```java
@SpringBootTest
class ProductImageGenerationServiceTest {
    
    @Autowired
    private ProductImageGenerationService imageService;
    
    @Test
    void testGenerateProductImage() throws IOException {
        Product product = new Product();
        product.setId("test-1");
        product.setName("Test Snowboard");
        product.setBrand("TestBrand");
        
        String imageUrl = imageService.generateProductImage(product);
        
        assertNotNull(imageUrl);
        assertTrue(imageUrl.startsWith("/images/products/"));
        
        // Verify file exists
        File imageFile = new File("src/main/resources/static" + imageUrl);
        assertTrue(imageFile.exists());
        assertTrue(imageFile.length() > 0);
    }
    
    @Test
    void testGenerateAltText() {
        Product product = new Product();
        product.setName("Test Board");
        product.setBrand("TestBrand");
        product.setDescription("A test product");
        
        String altText = imageService.generateAltText(product);
        
        assertNotNull(altText);
        assertTrue(altText.contains("Test Board"));
        assertTrue(altText.contains("TestBrand"));
    }
}
```

### Integration Tests

1. **Database persistence test**: Verify image URL saved correctly
2. **Template rendering test**: Verify images display in templates
3. **404 handling test**: Verify fallback to placeholder works
4. **Batch generation test**: Verify all products get images

### Manual Testing Checklist

- [ ] Product list page displays images
- [ ] Product detail page displays large image
- [ ] Home page featured products show images
- [ ] Images responsive on mobile/tablet
- [ ] Alt text present for accessibility
- [ ] Placeholder shows for missing images
- [ ] Image hover effects work
- [ ] Lazy loading works (check network tab)

---

## Deployment Plan

### Development Environment

1. **Generate images locally**:
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.main-class=com.skishop.command.GenerateAllProductImagesCommand
   ```

2. **Commit generated images to git**:
   ```bash
   git add src/main/resources/static/images/products/
   git commit -m "Add generated product images"
   ```

3. **Test in local browser**: http://localhost:8080

### Docker Environment

Update Dockerfile to ensure images are included:
```dockerfile
# Images are already in src/main/resources/static/, 
# so they'll be packaged in the JAR automatically
# No additional COPY needed
```

### Production Deployment (Azure)

**Option 1: Package with JAR** (Recommended for MVP)
- Images included in JAR file
- No external dependencies
- Simple deployment

**Option 2: Azure Blob Storage** (Recommended for scale)
- Upload images to blob storage
- Update image URLs in database to blob URLs
- Enable CDN for fast delivery
- Reduces JAR size

---

## Performance Optimization

### Image Optimization

1. **Compression**: Use PNG optimization tools (pngquant, optipng)
   - Target: < 100KB per image
   
2. **WebP format**: Convert to WebP for modern browsers
   - 25-35% smaller than PNG
   - Fallback to PNG for older browsers

3. **Lazy loading**: Already implemented with `loading="lazy"` attribute

4. **CDN**: Serve from Azure CDN in production

### Caching Strategy

Add cache headers for static images in `application.yml`:
```yaml
spring:
  web:
    resources:
      cache:
        cachecontrol:
          max-age: 604800  # 7 days
```

---

## Implementation Timeline

### Phase 1: Database and Entity Updates (1-2 hours)
- [ ] Create migration SQL script
- [ ] Update Product entity
- [ ] Update DTOs and mappers
- [ ] Run migrations

### Phase 2: Image Generation Service (3-4 hours)
- [ ] Implement ProductImageGenerationService
- [ ] Implement batch generation command
- [ ] Create placeholder image
- [ ] Test image generation locally

### Phase 3: Generate All Product Images (30 minutes)
- [ ] Run batch generation command
- [ ] Review generated images
- [ ] Adjust styling if needed
- [ ] Regenerate if necessary

### Phase 4: UI Integration (2-3 hours)
- [ ] Update product list template
- [ ] Update product detail template
- [ ] Update home page template
- [ ] Update cart/order templates (if needed)
- [ ] Add CSS styles
- [ ] Test responsive display

### Phase 5: Testing (1-2 hours)
- [ ] Write unit tests
- [ ] Write integration tests
- [ ] Manual testing on all pages
- [ ] Cross-browser testing
- [ ] Mobile testing

### Phase 6: Documentation and Cleanup (1 hour)
- [ ] Update README with image generation instructions
- [ ] Document image format/sizing standards
- [ ] Clean up test images
- [ ] Git commit and push

**Total Estimated Time**: 8-12 hours

---

## Future Enhancements

### Phase 2 (Optional)
1. **AI-Generated Images**: Upgrade to DALL-E 3 for photorealistic images
2. **Multiple Images**: Support 2-4 images per product (different angles)
3. **Image Upload**: Admin interface to manually upload/replace images
4. **Image Variants**: Generate thumbnails, medium, large sizes
5. **Dynamic Text**: Render product name dynamically on hover
6. **Duke Integration**: Add Duke mascot wearing/using each product

### Phase 3 (Advanced)
1. **Azure Computer Vision**: Auto-tag images with AI-detected features
2. **A/B Testing**: Test different image styles for conversion
3. **User-Generated Content**: Allow customers to upload product photos
4. **3D Product Views**: Interactive 360° product rotation
5. **AR Preview**: "Try before you buy" with AR on mobile

---

## Risk Assessment

### Risks and Mitigation

| Risk | Likelihood | Impact | Mitigation |
|------|-----------|--------|-----------|
| Generated images look unprofessional | Medium | High | Start with programmatic generation, use AI for final version |
| Image generation takes too long | Low | Medium | Pre-generate all images, not on-demand |
| Large JAR file size (30 images × 100KB = 3MB) | Low | Low | Acceptable for 30 products; use blob storage if scaling |
| Missing product names in images | Low | Medium | Thorough testing before deployment |
| Accessibility issues | Low | High | Generate proper alt text, test with screen readers |
| Docker build fails with large images | Low | Medium | Optimize images before committing, test Docker build |

---

## Success Metrics

### Key Performance Indicators

1. **Technical Success**:
   - All 30 products have images
   - 100% test coverage for image service
   - Zero broken image links
   - Page load time < 2 seconds

2. **User Experience**:
   - Images visible on all product pages
   - Readable product names on images
   - Responsive display on all devices
   - Accessibility score 100% (Lighthouse)

3. **Business Impact** (post-deployment):
   - Increased product page engagement
   - Higher add-to-cart conversion rate
   - Reduced bounce rate on product pages

---

## Appendix

### Sample Product Names (for testing)
From `02-data.sql`:
- Board A (SnowPro)
- Board B (RideEasy)
- Boots (various brands)
- Goggles (various models)
- Helmets, Gloves, Jackets, Pants

### Dependencies to Add

**pom.xml additions**:
```xml
<!-- For advanced image processing (optional) -->
<dependency>
    <groupId>com.github.jai-imageio</groupId>
    <artifactId>jai-imageio-core</artifactId>
    <version>1.4.0</version>
</dependency>

<!-- For WebP conversion (optional) -->
<dependency>
    <groupId>org.sejda.imageio</groupId>
    <artifactId>webp-imageio</artifactId>
    <version>0.1.6</version>
</dependency>

<!-- For Azure Blob Storage (if using) -->
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-storage-blob</artifactId>
    <version>12.25.0</version>
</dependency>

<!-- For Azure OpenAI DALL-E (if using) -->
<!-- Already included via openai-java 4.21.0 -->
```

### Configuration Properties

**application.yml additions**:
```yaml
product:
  images:
    directory: src/main/resources/static/images/products
    width: 800
    height: 600
    format: PNG
    quality: 85
    
# If using Azure Blob Storage
azure:
  storage:
    connection-string: ${AZURE_STORAGE_CONNECTION_STRING}
    container-name: product-images
    
# If using Azure OpenAI DALL-E
openai:
  dalle:
    enabled: false  # Set to true to enable
    model: dall-e-3
    quality: standard
    size: 1024x1024
```

---

## Conclusion

This plan provides a comprehensive approach to adding product images to Duke's Ski Chalet. The recommended approach is:

1. **MVP (Immediate)**: Programmatic image generation
   - Fast implementation (8-12 hours)
   - Zero runtime costs
   - Validates technical approach

2. **Production (Optional)**: AI-enhanced images
   - Upgrade 5-10 key products with DALL-E
   - Compare conversion metrics
   - Full rollout if results justify cost

The programmatic approach gives us professional-looking product images with branded text overlays, improves the user experience dramatically, and can be implemented quickly without external dependencies.

**Next Steps**: Await approval to proceed with implementation.
