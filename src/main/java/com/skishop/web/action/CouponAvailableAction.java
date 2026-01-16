package com.skishop.web.action;

import com.skishop.domain.coupon.Coupon;
import com.skishop.service.coupon.CouponService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CouponAvailableAction extends Action {
  private final CouponService couponService = new CouponService();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    List<Coupon> coupons = couponService.listActiveCoupons();
    request.setAttribute("coupons", coupons);
    return mapping.findForward("success");
  }
}
