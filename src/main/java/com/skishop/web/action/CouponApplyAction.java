package com.skishop.web.action;

import com.skishop.domain.cart.CartItem;
import com.skishop.domain.coupon.Coupon;
import com.skishop.service.cart.CartService;
import com.skishop.service.coupon.CouponService;
import com.skishop.web.form.CouponForm;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class CouponApplyAction extends Action {
  private final CouponService couponService = new CouponService();
  private final CartService cartService = new CartService();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    CouponForm couponForm = (CouponForm) form;
    HttpSession session = request.getSession(false);
    String cartId = session != null ? (String) session.getAttribute("cartId") : null;
    if (cartId == null) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.cart.notfound"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }
    List<CartItem> items = cartService.getItems(cartId);
    BigDecimal subtotal = cartService.calculateSubtotal(items);
    request.setAttribute("cartItems", items);
    request.setAttribute("cartSubtotal", subtotal);
    try {
      Coupon coupon = couponService.validateCoupon(couponForm.getCode(), subtotal);
      BigDecimal discount = couponService.calculateDiscount(coupon, subtotal);
      request.setAttribute("coupon", coupon);
      request.setAttribute("discountAmount", discount);
      return mapping.findForward("success");
    } catch (IllegalArgumentException e) {
      ActionMessages errors = new ActionMessages();
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.coupon.invalid"));
      saveErrors(request, errors);
      return mapping.findForward("failure");
    }
  }
}
