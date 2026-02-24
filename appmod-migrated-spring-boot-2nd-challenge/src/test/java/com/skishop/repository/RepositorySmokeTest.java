package com.skishop.repository;

import com.skishop.model.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RepositorySmokeTest {

    @Autowired CampaignRepository campaignRepository;
    @Autowired CartRepository cartRepository;
    @Autowired CartItemRepository cartItemRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired CouponRepository couponRepository;
    @Autowired CouponUsageRepository couponUsageRepository;
    @Autowired EmailQueueRepository emailQueueRepository;
    @Autowired InventoryRepository inventoryRepository;
    @Autowired OrderRepository orderRepository;
    @Autowired OrderItemRepository orderItemRepository;
    @Autowired OrderShippingRepository orderShippingRepository;
    @Autowired PaymentRepository paymentRepository;
    @Autowired PointAccountRepository pointAccountRepository;
    @Autowired PointTransactionRepository pointTransactionRepository;
    @Autowired PriceRepository priceRepository;
    @Autowired ProductRepository productRepository;
    @Autowired ReturnRequestRepository returnRequestRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired SecurityLogRepository securityLogRepository;
    @Autowired ShipmentRepository shipmentRepository;
    @Autowired ShippingMethodRepository shippingMethodRepository;
    @Autowired UserRepository userRepository;
    @Autowired UserAddressRepository userAddressRepository;
    @Autowired PasswordResetTokenRepository passwordResetTokenRepository;

    private static String uuid() {
        return UUID.randomUUID().toString();
    }

    private static String productId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 20);
    }

    @Test
    void saveAllEntitiesSmoke() {
        // Product
        Product product = new Product();
        product.setId(productId());
        product.setName("Ski");
        product.setStatus("ACTIVE");
        product = productRepository.save(product);
        assertThat(product.getId()).isNotNull();

        // Price
        Price price = new Price();
        price.setId(uuid());
        price.setProductId(product.getId());
        price.setRegularPrice(new BigDecimal("100.00"));
        price.setCurrencyCode("USD");
        price = priceRepository.save(price);
        assertThat(price.getId()).isNotNull();

        // Category
        Category category = new Category();
        category.setId(uuid());
        category.setName("Category");
        category = categoryRepository.save(category);
        assertThat(category.getId()).isNotNull();

        // Inventory
        Inventory inventory = new Inventory();
        inventory.setId(uuid());
        inventory.setProductId(product.getId());
        inventory.setQuantity(10);
        inventory.setReservedQuantity(0);
        inventory.setStatus("AVAILABLE");
        inventory = inventoryRepository.save(inventory);
        assertThat(inventory.getId()).isNotNull();

        // Cart
        Cart cart = new Cart();
        cart.setId(uuid());
        cart.setStatus("OPEN");
        cart = cartRepository.save(cart);
        assertThat(cart.getId()).isNotNull();

        // CartItem
        CartItem cartItem = new CartItem();
        cartItem.setId(uuid());
        cartItem.setCartId(cart.getId());
        cartItem.setProductId(product.getId());
        cartItem.setQuantity(1);
        cartItem.setUnitPrice(new BigDecimal("10.00"));
        cartItem = cartItemRepository.save(cartItem);
        assertThat(cartItem.getId()).isNotNull();

        // Campaign
        Campaign campaign = new Campaign();
        campaign.setId(uuid());
        campaign.setName("Camp");
        campaign.setIsActive(true);
        campaign = campaignRepository.save(campaign);
        assertThat(campaign.getId()).isNotNull();

        // Coupon
        Coupon coupon = new Coupon();
        coupon.setId(uuid());
        coupon.setCode("CODE1");
        coupon.setDiscountValue(new BigDecimal("5.00"));
        coupon.setDiscountType("AMOUNT");
        coupon.setCouponType("GENERAL");
        coupon.setIsActive(true);
        coupon.setUsageLimit(10);
        coupon.setUsedCount(0);
        coupon = couponRepository.save(coupon);
        assertThat(coupon.getId()).isNotNull();

        // CouponUsage
        CouponUsage cu = new CouponUsage();
        cu.setId(uuid());
        cu.setCouponId(coupon.getId());
        cu.setUserId(uuid());
        cu.setOrderId(uuid());
        cu.setDiscountApplied(new BigDecimal("5.00"));
        cu.setUsedAt(LocalDateTime.now());
        cu = couponUsageRepository.save(cu);
        assertThat(cu.getId()).isNotNull();

        // ShippingMethod
        ShippingMethod sm = new ShippingMethod();
        sm.setId(uuid());
        sm.setCode("STD");
        sm.setName("Standard");
        sm.setFee(new BigDecimal("10.00"));
        sm.setIsActive(true);
        sm.setSortOrder(1);
        sm = shippingMethodRepository.save(sm);
        assertThat(sm.getId()).isNotNull();

        // EmailQueue
        EmailQueue email = new EmailQueue();
        email.setId(uuid());
        email.setToAddr("to@example.com");
        email.setSubject("sub");
        email.setBody("body");
        email.setStatus("PENDING");
        email.setRetryCount(0);
        email.setScheduledAt(LocalDateTime.now());
        email = emailQueueRepository.save(email);
        assertThat(email.getId()).isNotNull();

        // Role
        Role role = new Role();
        role.setId(uuid());
        role.setName("USER");
        role = roleRepository.save(role);
        assertThat(role.getId()).isNotNull();

        // User
        User user = new User();
        user.setId(uuid());
        user.setEmail("u@example.com");
        user.setUsername("user1");
        user.setPasswordHash("hash");
        user.setSalt("salt");
        user.setStatus("ACTIVE");
        user.setRole("USER");
        user = userRepository.save(user);
        assertThat(user.getId()).isNotNull();

        // UserAddress
        UserAddress ua = new UserAddress();
        ua.setId(uuid());
        ua.setUserId(user.getId());
        ua.setLabel("home");
        ua.setRecipientName("John");
        ua.setPostalCode("80435");
        ua.setPrefecture("Colorado");
        ua.setAddress1("1-2-3");
        ua.setIsDefault(Boolean.TRUE);
        ua = userAddressRepository.save(ua);
        assertThat(ua.getId()).isNotNull();

        // Order
        Order order = new Order();
        order.setId(uuid());
        order.setOrderNumber("ORD1");
        order.setStatus("NEW");
        order.setPaymentStatus("PENDING");
        order.setSubtotal(BigDecimal.ZERO);
        order.setTax(BigDecimal.ZERO);
        order.setShippingFee(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setUsedPoints(0);
        order = orderRepository.save(order);
        assertThat(order.getId()).isNotNull();

        // OrderItem
        OrderItem oi = new OrderItem();
        oi.setId(uuid());
        oi.setOrderId(order.getId());
        oi.setProductId(product.getId());
        oi.setQuantity(1);
        oi.setUnitPrice(new BigDecimal("10.00"));
        oi.setSubtotal(new BigDecimal("10.00"));
        oi.setProductName("Ski");
        oi = orderItemRepository.save(oi);
        assertThat(oi.getId()).isNotNull();

        // OrderShipping
        OrderShipping os = new OrderShipping();
        os.setId(uuid());
        os.setOrderId(order.getId());
        os.setShippingFee(new BigDecimal("5.00"));
        os.setPostalCode("80435");
        os.setPrefecture("Colorado");
        os.setRecipientName("John");
        os.setAddress1("1-2-3");
        os = orderShippingRepository.save(os);
        assertThat(os.getId()).isNotNull();

        // Payment
        Payment payment = new Payment();
        payment.setId(uuid());
        payment.setAmount(new BigDecimal("10.00"));
        payment.setCurrency("USD");
        payment.setStatus("PENDING");
        payment = paymentRepository.save(payment);
        assertThat(payment.getId()).isNotNull();

        // PointAccount
        PointAccount pa = new PointAccount();
        pa.setId(uuid());
        pa.setUserId(user.getId());
        pa.setBalance(0);
        pa.setLifetimeEarned(0);
        pa.setLifetimeRedeemed(0);
        pa = pointAccountRepository.save(pa);
        assertThat(pa.getId()).isNotNull();

        // PointTransaction
        PointTransaction pt = new PointTransaction();
        pt.setId(uuid());
        pt.setUserId(user.getId());
        pt.setType("EARN");
        pt.setAmount(10);
        pt = pointTransactionRepository.save(pt);
        assertThat(pt.getId()).isNotNull();

        // PasswordResetToken
        PasswordResetToken prt = new PasswordResetToken();
        prt.setId(uuid());
        prt.setUserId(user.getId());
        prt.setToken("TOKEN-123");
        prt.setExpiresAt(LocalDateTime.now().plusDays(1));
        prt = passwordResetTokenRepository.save(prt);
        assertThat(prt.getId()).isNotNull();

        // SecurityLog
        SecurityLog sl = new SecurityLog();
        sl.setId(uuid());
        sl.setEventType("LOGIN");
        sl = securityLogRepository.save(sl);
        assertThat(sl.getId()).isNotNull();

        // Shipment
        Shipment sh = new Shipment();
        sh.setId(uuid());
        sh.setOrderId(order.getId());
        sh = shipmentRepository.save(sh);
        assertThat(sh.getId()).isNotNull();

        // ReturnRequest
        ReturnRequest rr = new ReturnRequest();
        rr.setId(uuid());
        rr.setOrderId(order.getId());
        rr.setOrderItemId(oi.getId());
        rr.setQuantity(1);
        rr.setRefundAmount(new BigDecimal("10.00"));
        rr = returnRequestRepository.save(rr);
        assertThat(rr.getId()).isNotNull();
    }
}
