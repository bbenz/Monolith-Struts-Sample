package com.skishop.service;

import com.skishop.common.util.PasswordHasher;
import com.skishop.dao.DaoTestBase;
import com.skishop.domain.cart.Cart;
import com.skishop.domain.cart.CartItem;
import com.skishop.domain.order.Order;
import com.skishop.domain.product.Product;
import com.skishop.domain.user.User;
import com.skishop.service.auth.AuthResult;
import com.skishop.service.auth.AuthService;
import com.skishop.service.cart.CartService;
import com.skishop.service.catalog.ProductService;
import com.skishop.service.order.OrderFacade;
import com.skishop.service.order.OrderFacadeImpl;
import com.skishop.service.order.OrderService;
import com.skishop.service.payment.PaymentInfo;
import com.skishop.service.user.UserService;
import java.util.Calendar;
import java.util.List;

public class ScenarioFlowTest extends DaoTestBase {
  private UserService userService;
  private AuthService authService;
  private ProductService productService;
  private CartService cartService;
  private OrderFacade orderFacade;
  private OrderService orderService;
  private String cardExpYear;

  protected void setUp() throws Exception {
    super.setUp();
    resetDatabase();
    userService = new UserService();
    authService = new AuthService();
    productService = new ProductService();
    cartService = new CartService();
    orderFacade = new OrderFacadeImpl();
    orderService = new OrderService();
    Calendar cal = Calendar.getInstance();
    cardExpYear = String.valueOf(cal.get(Calendar.YEAR) + 5);
  }

  public void testRegistrationLoginSearchCartCheckoutCancelReturn() throws Exception {
    User user = registerUser();
    AuthResult result = authService.authenticate(user.getEmail(), "password123", "127.0.0.1", "JUnit");
    assertTrue(result.isSuccess());
    String userId = result.getUser().getId();

    List<Product> products = productService.search("Ski", null, 0, 10);
    assertFalse(products.isEmpty());
    Product product = products.get(0);

    Cart cart = cartService.createCart(userId, "session-1");
    cartService.addItem(cart.getId(), product.getId(), 1);
    List<CartItem> items = cartService.getItems(cart.getId());
    assertEquals(1, items.size());

    Order order = orderFacade.placeOrder(cart.getId(), null, 0, createPaymentInfo(), userId);
    assertNotNull(order);
    Order cancelled = orderFacade.cancelOrder(order.getId(), userId);
    assertEquals("CANCELLED", cancelled.getStatus());

    Cart returnCart = cartService.createCart(userId, "session-2");
    cartService.addItem(returnCart.getId(), product.getId(), 1);
    Order returnOrder = orderFacade.placeOrder(returnCart.getId(), null, 0, createPaymentInfo(), userId);
    orderService.updateStatus(returnOrder.getId(), "DELIVERED");
    Order returned = orderFacade.returnOrder(returnOrder.getId(), userId);
    assertEquals("RETURNED", returned.getStatus());
  }

  private User registerUser() throws Exception {
    User user = new User();
    user.setEmail("scenario@example.com");
    user.setUsername("scenario");
    String salt = PasswordHasher.generateSalt();
    String hash = PasswordHasher.hash("password123", salt);
    user.setSalt(salt);
    user.setPasswordHash(hash);
    return userService.register(user);
  }

  private PaymentInfo createPaymentInfo() {
    PaymentInfo info = new PaymentInfo();
    info.setMethod("CARD");
    info.setCardNumber("4111111111111111");
    info.setCardExpMonth("12");
    info.setCardExpYear(cardExpYear);
    info.setCardCvv("123");
    info.setBillingZip("160-0022");
    return info;
  }
}
