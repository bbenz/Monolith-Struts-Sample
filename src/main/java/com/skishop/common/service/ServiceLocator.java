package com.skishop.common.service;

import com.skishop.common.dao.DaoFactory;
import com.skishop.service.auth.AuthService;
import com.skishop.service.cart.CartService;
import com.skishop.service.catalog.ProductService;
import com.skishop.service.coupon.CouponService;
import com.skishop.service.inventory.InventoryService;
import com.skishop.service.mail.MailService;
import com.skishop.service.order.OrderFacade;
import com.skishop.service.order.OrderFacadeImpl;
import com.skishop.service.order.OrderService;
import com.skishop.service.payment.PaymentService;
import com.skishop.service.point.PointService;
import com.skishop.service.shipping.ShippingService;
import com.skishop.service.tax.TaxService;
import com.skishop.service.user.UserService;

public final class ServiceLocator {
  private static final AuthService AUTH_SERVICE = new AuthService();
  private static final CartService CART_SERVICE = new CartService();
  private static final ProductService PRODUCT_SERVICE = new ProductService();
  private static final CouponService COUPON_SERVICE = new CouponService();
  private static final InventoryService INVENTORY_SERVICE = new InventoryService();
  private static final PaymentService PAYMENT_SERVICE = new PaymentService();
  private static final OrderService ORDER_SERVICE = new OrderService();
  private static final PointService POINT_SERVICE = new PointService();
  private static final ShippingService SHIPPING_SERVICE = new ShippingService();
  private static final TaxService TAX_SERVICE = new TaxService();
  private static final MailService MAIL_SERVICE = new MailService();
  private static final UserService USER_SERVICE = new UserService();
  private static final OrderFacade ORDER_FACADE = new OrderFacadeImpl(
      CART_SERVICE,
      COUPON_SERVICE,
      INVENTORY_SERVICE,
      PAYMENT_SERVICE,
      ORDER_SERVICE,
      POINT_SERVICE,
      SHIPPING_SERVICE,
      TAX_SERVICE,
      PRODUCT_SERVICE,
      DaoFactory.getUserAddressDao(),
      MAIL_SERVICE,
      USER_SERVICE);

  private ServiceLocator() {
  }

  public static AuthService getAuthService() {
    return AUTH_SERVICE;
  }

  public static CartService getCartService() {
    return CART_SERVICE;
  }

  public static ProductService getProductService() {
    return PRODUCT_SERVICE;
  }

  public static CouponService getCouponService() {
    return COUPON_SERVICE;
  }

  public static InventoryService getInventoryService() {
    return INVENTORY_SERVICE;
  }

  public static PaymentService getPaymentService() {
    return PAYMENT_SERVICE;
  }

  public static OrderService getOrderService() {
    return ORDER_SERVICE;
  }

  public static OrderFacade getOrderFacade() {
    return ORDER_FACADE;
  }

  public static PointService getPointService() {
    return POINT_SERVICE;
  }

  public static ShippingService getShippingService() {
    return SHIPPING_SERVICE;
  }

  public static TaxService getTaxService() {
    return TAX_SERVICE;
  }

  public static MailService getMailService() {
    return MAIL_SERVICE;
  }

  public static UserService getUserService() {
    return USER_SERVICE;
  }
}
