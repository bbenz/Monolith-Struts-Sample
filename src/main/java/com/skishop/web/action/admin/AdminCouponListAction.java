package com.skishop.web.action.admin;

import com.skishop.dao.coupon.CouponDao;
import com.skishop.dao.coupon.CouponDaoImpl;
import com.skishop.domain.coupon.Coupon;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AdminCouponListAction extends Action {
  private final CouponDao couponDao = new CouponDaoImpl();

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    List<Coupon> coupons = couponDao.listAll();
    request.setAttribute("coupons", coupons);
    return mapping.getInputForward();
  }
}
